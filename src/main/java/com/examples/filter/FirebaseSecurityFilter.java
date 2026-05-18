package com.examples.filter;

import com.examples.dao.UserDAO;
import com.examples.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/clinic/*")
public class FirebaseSecurityFilter implements Filter {

    private UserDAO userDAO = new UserDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String idToken = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("firebaseToken".equals(cookie.getName())) {
                    idToken = cookie.getValue();
                    break;
                }
            }
        }

        if (idToken == null || idToken.isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();

            User loggedUser = userDAO.getUserByEmail(email);

            if (loggedUser == null) {
                System.out.println("--> Користувача немає в БД. Створюємо автоматично...");

                String name = decodedToken.getName();
                if (name == null || name.isEmpty()) {
                    name = email.split("@")[0];
                }

                loggedUser = new User(name, email, User.Role.PATIENT);
                userDAO.createUser(loggedUser);
                System.out.println("Успішно збережено в MySQL: " + email);
            }

            req.setAttribute("loggedUser", loggedUser);
            chain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("Помилка у фільтрі: " + e.getMessage());
            e.printStackTrace();

            Cookie clearCookie = new Cookie("firebaseToken", "");
            clearCookie.setMaxAge(0);
            clearCookie.setPath("/");
            res.addCookie(clearCookie);
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }
}