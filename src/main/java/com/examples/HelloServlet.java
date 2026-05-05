package com.examples;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/hello")
public class    HelloServlet extends HttpServlet {
    private Configuration cfg;



    @Override
    public void init() throws ServletException {
        // 1. Налаштування FreeMarker
        cfg = new Configuration(Configuration.VERSION_2_3_31);
        String realPath = getServletContext().getRealPath("/WEB-INF/templates/");
        try {
            if (realPath != null) {
                cfg.setDirectoryForTemplateLoading(new File(realPath));
            } else {
                cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            }
        } catch (IOException e) {
            throw new ServletException("Не вдалося налаштувати папку шаблонів", e);
        }
        cfg.setDefaultEncoding("UTF-8");

        // 2. Явне завантаження драйвера MySQL
        // Для версії 8.x клас називається com.mysql.cj.jdbc.Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found (перевірте наявність бібліотеки)", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Параметри, Сесії, Куки (без змін)
        String nameFromParam = req.getParameter("name");

        HttpSession session = req.getSession();
        if (nameFromParam != null && !nameFromParam.isEmpty()) {
            session.setAttribute("username", nameFromParam);
        }
        String sessionUser = (String) session.getAttribute("username");
        if (sessionUser == null) sessionUser = "Гість";

        String cookieValue = "Куки не знайдено";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("my_app_user".equals(c.getName())) cookieValue = c.getValue();
            }
        }
        if (nameFromParam != null && !nameFromParam.isEmpty()) {
            Cookie newCookie = new Cookie("my_app_user", nameFromParam);
            newCookie.setMaxAge(60 * 60 * 24);
            resp.addCookie(newCookie);
        }

        // --- БЛОК РОБОТИ З БД (Через DAO) ---
        List<String> dbData = new ArrayList<>();
        String dbStatus = "Спроба підключення...";

        try {
            // Ініціалізуємо DAO (він сам візьме з'єднання з пулу)
            UserDAO userDAO = new UserDAO();
            dbStatus = "Підключення через DataSource успішне!";

            // 1. Для тесту можемо створити нового користувача
            // Розкоментуй ці два рядки, якщо хочеш додати запис:
            // User newUser = new User("Тестовий Юзер", "test@test.com");
            // userDAO.createUser(newUser);

            // 2. Отримуємо користувача (наприклад, з id = 1)
            // Оскільки таблиця може бути порожньою, додамо перевірку
            User user = userDAO.getUserById(1);
            if (user != null) {
                dbData.add("Знайдено: " + user.getName() + " (" + user.getEmail() + ")");
            } else {
                dbData.add("Користувача з ID=1 не знайдено в базі.");
            }

        } catch (Exception e) { // Ловимо всі помилки, включно з NamingException та SQLException
            dbStatus = "Помилка роботи з БД: " + e.getMessage();
            e.printStackTrace();
        }

        // --- FreeMarker ---
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Java Servlet + MySQL");
        data.put("paramUser", nameFromParam == null ? "—" : nameFromParam);
        data.put("sessionUser", sessionUser);
        data.put("cookieUser", cookieValue);
        data.put("sessionId", session.getId());
        data.put("dbStatus", dbStatus);
        data.put("dbList", dbData);

        resp.setContentType("text/html; charset=UTF-8");
        try {
            Template temp = cfg.getTemplate("hello.ftl");
            temp.process(data, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("Template Error", e);
        }
    }
}