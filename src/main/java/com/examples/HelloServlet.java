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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/hello")
public class    HelloServlet extends HttpServlet {
    private Configuration cfg;

    // --- НАЛАШТУВАННЯ MYSQL ---
    // Важливо: перевірте ім'я бази даних (тут 'my_database')
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/myapp?serverTimezone=UTC";
    private static final String DB_USER = "java_user";   // Ваш логін MySQL
    private static final String DB_PASSWORD = "pass123";

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

        // --- БЛОК РОБОТИ З БД ---
        List<String> dbData = new ArrayList<>();
        String dbStatus = "Спроба підключення...";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            dbStatus = "Підключення до MySQL успішне!";

            // Змініть SQL запит під вашу таблицю! Наприклад: SELECT name FROM users
            // Якщо таблиці ще немає, цей код впаде з помилкою, тому огорніть в try/catch або створіть таблицю
            String sql = "SELECT 1"; // Простий тест-запит, щоб не ламалося, якщо немає таблиць

            try (PreparedStatement statement = conn.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dbData.add("Test Query Result: " + resultSet.getString(1));
                }
            }
        } catch (SQLException e) {
            dbStatus = "Помилка MySQL: " + e.getMessage();
            e.printStackTrace(); // Це піде в логи Tomcat
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