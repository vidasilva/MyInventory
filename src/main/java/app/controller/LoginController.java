package app.controller;

import app.dao.UserDAO;
import app.db.DBSetup;
import app.db.DBUtil;
import app.model.User;
import app.service.UserService;
import app.util.AlertUtil;
import app.util.SceneManager;
import app.util.Session;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signupLink;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserService userService = new UserService(new UserDAO());

        loginButton.setOnAction(
                (ActionEvent eh) -> {
                    try {
                        User user = userService.login(usernameField.getText(), passwordField.getText());
                        Session.set(user); // ✅ Save user globally
                        DBUtil.setUser(user.getUsername());
                        DBSetup.initUserDatabase();
                        SceneManager.switchTo("product-dashboard.fxml", 1280, 800);
                    } catch (IllegalArgumentException ex) {
                        AlertUtil.showError(ex.getMessage());
                    } catch (Exception ex) {
                        AlertUtil.showError("Login failed: " + ex.getMessage());
                        ex.printStackTrace();
                    }

                });

        signupLink.setOnAction(
                (ActionEvent eh) -> {
                    try {
                        SceneManager.switchTo("signup.fxml", 900, 600);
                    } catch (IOException ex) {
                        AlertUtil.showError("Something went wrong.");
                    }
                });
    }

}
