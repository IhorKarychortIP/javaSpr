package com.examples.controller;

import com.examples.util.TemplateEngine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        // ЧИТАЄМО ФАЙЛ CONFIG.PROPERTIES
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
                data.put("apiKey", props.getProperty("FIREBASE_API_KEY"));
                data.put("authDomain", props.getProperty("FIREBASE_AUTH_DOMAIN"));
                data.put("projectId", props.getProperty("FIREBASE_PROJECT_ID"));
                data.put("storageBucket", props.getProperty("FIREBASE_STORAGE_BUCKET"));
                data.put("messagingSenderId", props.getProperty("FIREBASE_MESSAGING_SENDER_ID"));
                data.put("appId", props.getProperty("FIREBASE_APP_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TemplateEngine.render(req, resp, "login.ftl", data);
    }
}