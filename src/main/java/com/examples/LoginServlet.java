package com.examples;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Рендеримо логін без жодних даних
        TemplateEngine.render(req, resp, "login.ftl", null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userDAO.authenticate(email, password);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("loggedUser", user);
                resp.sendRedirect(req.getContextPath() + "/clinic/dashboard");
            } else {
                // Якщо помилка, передаємо повідомлення у шаблон
                Map<String, Object> data = new HashMap<>();
                data.put("error", "Невірний email або пароль!");
                TemplateEngine.render(req, resp, "login.ftl", data);
            }
        } catch (Exception e) {
            throw new ServletException("Помилка БД", e);
        }
    }
}