package com.examples.controller;

import com.examples.dao.UserDAO;
import com.examples.model.User;
import com.examples.util.TemplateEngine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/clinic/admin/user")
public class AdminUserServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = (User) req.getAttribute("loggedUser");

        if (loggedUser == null || loggedUser.getRole() != User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/clinic/dashboard");
            return;
        }

        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            User userToEdit = userDAO.getUserById(id);

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", req.getContextPath());
            data.put("userToEdit", userToEdit);

            TemplateEngine.render(req, resp, "edit-user.ftl", data);
        } else {
            resp.sendRedirect(req.getContextPath() + "/clinic/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = (User) req.getAttribute("loggedUser");

        if (loggedUser == null || loggedUser.getRole() != User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/clinic/dashboard");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));

                if (id != loggedUser.getId()) {
                    User userToDelete = userDAO.getUserById(id);

                    if (userToDelete != null) {
                        try {
                            UserRecord firebaseUser = FirebaseAuth.getInstance().getUserByEmail(userToDelete.getEmail());
                            FirebaseAuth.getInstance().deleteUser(firebaseUser.getUid());
                            System.out.println("Користувача видалено з Google Firebase: " + userToDelete.getEmail());
                        } catch (Exception e) {
                            System.out.println("У Firebase цього юзера немає, пропускаємо.");
                        }

                        userDAO.deleteUser(id);
                        System.out.println("Користувача видалено з MySQL: " + userToDelete.getEmail());
                    }
                }
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                User.Role role = User.Role.valueOf(req.getParameter("role"));

                User user = userDAO.getUserById(id);
                if (user != null) {
                    user.setName(name);
                    user.setEmail(email);
                    user.setRole(role);
                    userDAO.updateUser(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect(req.getContextPath() + "/clinic/dashboard");
    }
}