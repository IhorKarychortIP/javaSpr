package com.examples.controller;

import com.examples.dao.AppointmentDAO;
import com.examples.dao.UserDAO;
import com.examples.model.Appointment;
import com.examples.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/clinic/appointment")
public class AppointmentServlet extends HttpServlet {
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("book".equals(action)) {
                int patientId = Integer.parseInt(req.getParameter("patientId"));
                int doctorId = Integer.parseInt(req.getParameter("doctorId"));
                LocalDateTime date = LocalDateTime.parse(req.getParameter("date")); // Формат з HTML: YYYY-MM-DDThh:mm

                User patient = userDAO.getUserById(patientId);
                User doctor = userDAO.getUserById(doctorId);

                if (patient != null && doctor != null) {
                    Appointment appointment = new Appointment(patient, doctor, date);
                    appointmentDAO.createAppointment(appointment);
                }
            }
            else if ("updateStatus".equals(action)) {
                int appointmentId = Integer.parseInt(req.getParameter("appointmentId"));
                Appointment.Status newStatus = Appointment.Status.valueOf(req.getParameter("status"));

                appointmentDAO.updateStatus(appointmentId, newStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect(req.getContextPath() + "/clinic/dashboard");
    }
}