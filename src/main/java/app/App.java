package app;

import app.db.DBSetup;
import app.util.SceneManager;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Run once at startup
        DBSetup.initGlobalDatabase(); // always run

        SceneManager.setStage(stage);
        SceneManager.switchTo("login.fxml", 900, 600);
    }

    public static void main(String[] args) {
        launch();
    }

}
