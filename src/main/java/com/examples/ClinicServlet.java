package com.examples;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/clinic/dashboard")
public class ClinicServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = (User) req.getSession().getAttribute("loggedUser");

        // Створюємо "коробку" з даними для шаблону
        Map<String, Object> data = new HashMap<>();
        data.put("loggedUser", loggedUser);

        try {
            if (loggedUser.getRole() == User.Role.DOCTOR) {
                data.put("allUsers", userDAO.getAllUsers());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Рендеримо дашборд
        TemplateEngine.render(req, resp, "dashboard.ftl", data);
    }
}