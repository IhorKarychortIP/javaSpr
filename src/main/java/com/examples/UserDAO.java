package com.examples;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private DataSource dataSource;

    public UserDAO() {
        // Отримуємо пул з'єднань з context.xml (JNDI lookup)
        try {
            InitialContext initContext = new InitialContext();
            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/MyDB");
        } catch (NamingException e) {
            throw new RuntimeException("Помилка ініціалізації пулу з'єднань", e);
        }
    }

    // 1. CREATE (з використанням транзакції)
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection()) {
            // Початок транзакції
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.executeUpdate();

                // Якщо все добре, підтверджуємо зміни
                conn.commit();
            } catch (SQLException e) {
                // Якщо сталася помилка, відкочуємо всі зміни
                conn.rollback();
                throw e;
            } finally {
                // Повертаємо стандартну поведінку
                conn.setAutoCommit(true);
            }
        }
    }

    // 2. READ
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
                }
            }
        }
        return null;
    }

    // 3. UPDATE
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        }
    }

    // 4. DELETE
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}