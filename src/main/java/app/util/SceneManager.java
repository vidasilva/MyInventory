package app.util;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlFile, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/app/view/" + fxmlFile));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(minWidth);
        primaryStage.setMinHeight(minHeight);
        primaryStage.show();
    }

    public static void openWindow(String fxmlFile, String title, Boolean maximizeScreen, Boolean resizable) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/app/view/" + fxmlFile));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setMaximized(maximizeScreen);
        stage.setResizable(resizable);
        stage.show();

    }

    public static <T> T openWindowAndGetController(String fxmlFile, String title, boolean maximizeScreen, boolean resizable, Consumer<T> controllerSetup) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/app/view/" + fxmlFile));
        Parent root = loader.load();

        T controller = loader.getController();
        if (controllerSetup != null) {
            controllerSetup.accept(controller);
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setResizable(resizable);
        stage.show();

        return controller;
    }

    public static void closeWindow(Stage stage) throws IOException {
        stage.close();
    }

    public static void refreshStage() {
        // to do 
    }

}
