package app.controller;

import app.dao.UserDAO;
import app.service.UserService;
import app.util.AlertUtil;
import app.util.SceneManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController implements Initializable {

    @FXML
    private Button signUpButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private CheckBox isAdmBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        signUpButton.setOnAction(
                (ActionEvent eh) -> {
                    // Gets user's input
                    String usr = usernameField.getText();
                    String pass = passwordField.getText();
                    String confirmPass = confirmPasswordField.getText();
                    String role = isAdmBox.isSelected() ? "admin" : "user";

                    try {
                        UserService userService;
                        userService = new UserService(new UserDAO());
                        userService.register(usr, pass, confirmPass, role);
                        AlertUtil.showInfo("Welcome to the crew!");
                        SceneManager.switchTo("login.fxml", 900, 600);

                    } catch (Exception ex) {
                        AlertUtil.showError(ex.getMessage());
                    }
                });

        cancelButton.setOnAction(
                (ActionEvent eh) -> {
                    try {
                        SceneManager.switchTo("login.fxml", 900, 600);
                    } catch (IOException ex) {
                        Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
    }
}
