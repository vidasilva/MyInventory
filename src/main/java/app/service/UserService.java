package app.service;

import app.dao.UserDAO;
import app.model.User;
import app.util.HashUtil;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void register(String username, String password, String confirmPassword, String role) throws Exception {
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            throw new IllegalArgumentException("All fields must be filled.");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords don't match.");
        }

        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        String hashed = HashUtil.sha256(password);
        if (!role.equalsIgnoreCase("admin")) {
            role = "user";
        }
        User user = new User(username, hashed, role);
        boolean created = userDAO.createUser(user);
        if (!created) {
            throw new RuntimeException("Failed to register user. Try again.");
        }
    }

    public User login(String username, String password) throws Exception {
        if (username.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        String hashedInput = HashUtil.sha256(password);
        if (!hashedInput.equals(user.getPasswordHash())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        return user;
    }
}
