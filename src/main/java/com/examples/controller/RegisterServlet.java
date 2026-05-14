package com.examples.controller;

import com.examples.dao.UserDAO;
import com.examples.model.User;
import com.examples.util.TemplateEngine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath()); // ДОДАНО
        TemplateEngine.render(req, resp, "register.ftl", data);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String roleStr = req.getParameter("role");

        try {
            User.Role role = User.Role.valueOf(roleStr);
            User newUser = new User(name, email, password, role);

            userDAO.createUser(newUser);
            resp.sendRedirect(req.getContextPath() + "/login");

        } catch (Exception e) {
            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", req.getContextPath()); // ДОДАНО
            data.put("error", "Користувач з таким email вже існує або сталася помилка бази!");
            TemplateEngine.render(req, resp, "register.ftl", data);
        }
    }
}