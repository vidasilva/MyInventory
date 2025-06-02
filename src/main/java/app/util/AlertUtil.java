package app.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertUtil {

    public static void showError(String message) {
        show(AlertType.ERROR, "Error", null, message);
    }

    public static void showInfo(String message) {
        show(AlertType.INFORMATION, "Info", null, message);
    }

    public static void showConfirmation(String title, String message) {
        show(AlertType.CONFIRMATION, title, null, message);
    }

    public static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void show(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
