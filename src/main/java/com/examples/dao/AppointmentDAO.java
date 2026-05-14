package com.examples.dao;

import com.examples.model.Appointment;
import com.examples.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AppointmentDAO {

    public void createAppointment(Appointment appointment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(appointment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Отримати всі записи конкретного пацієнта
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery("FROM Appointment a WHERE a.patient.id = :patientId", Appointment.class);
            query.setParameter("patientId", patientId);
            return query.list();
        }
    }

    // Отримати всі записи до конкретного лікаря
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery("FROM Appointment a WHERE a.doctor.id = :doctorId", Appointment.class);
            query.setParameter("doctorId", doctorId);
            return query.list();
        }
    }

    // Оновлення статусу
    public void updateStatus(int appointmentId, Appointment.Status newStatus) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment != null) {
                appointment.setStatus(newStatus);
                session.merge(appointment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}