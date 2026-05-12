package com.examples;

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
        // Віддаємо пусту форму реєстрації
        TemplateEngine.render(req, resp, "register.ftl", null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String roleStr = req.getParameter("role");

        try {
            // Перетворюємо рядок на Enum і створюємо користувача
            User.Role role = User.Role.valueOf(roleStr);
            User newUser = new User(name, email, password, role);

            // Зберігаємо в базу (транзакція відпрацює автоматично)
            userDAO.createUser(newUser);

            // Після успішної реєстрації кидаємо на сторінку входу
            resp.sendRedirect(req.getContextPath() + "/login");

        } catch (Exception e) {
            // Якщо такий email вже є в базі, MySQL викине помилку (бо email UNIQUE)
            Map<String, Object> data = new HashMap<>();
            data.put("error", "Користувач з таким email вже існує або сталася помилка бази!");
            TemplateEngine.render(req, resp, "register.ftl", data);
        }
    }
}