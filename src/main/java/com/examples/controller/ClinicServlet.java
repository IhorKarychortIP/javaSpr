package com.examples.controller;

import com.examples.dao.AppointmentDAO;
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

@WebServlet("/clinic/dashboard")
public class ClinicServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = (User) req.getAttribute("loggedUser");
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("loggedUser", loggedUser);

        try {
            if (loggedUser != null) {
                switch (loggedUser.getRole()) {
                    case ADMIN:
                        data.put("allUsers", userDAO.getAllUsers());
                        break;
                    case DOCTOR:
                        data.put("appointments", appointmentDAO.getAppointmentsByDoctor(loggedUser.getId()));
                        break;
                    case PATIENT:
                        data.put("myAppointments", appointmentDAO.getAppointmentsByPatient(loggedUser.getId()));
                        data.put("drHouse", userDAO.getUserById(2));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TemplateEngine.render(req, resp, "dashboard.ftl", data);
    }
}