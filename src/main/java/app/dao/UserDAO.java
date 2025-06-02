package app.dao;

import app.db.DBUtil;
import app.model.User;
import java.sql.*;

public class UserDAO {

    public boolean createUser(User user) {
        String sql = "INSERT INTO user (username, password_hash, role) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getGlobalConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("❌ Username already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";

        try (Connection conn = DBUtil.getGlobalConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("created_at"),
                        rs.getString("role")
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean authenticate(String username, String plainPassword) {
        User user = findByUsername(username);
        if (user == null) {
            return false;
        }

        String hash = app.util.HashUtil.sha256(plainPassword);
        return hash.equals(user.getPasswordHash());
    }
}
