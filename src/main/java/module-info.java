module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.base;
    requires java.sql;
    requires com.google.gson;
    
    opens app to javafx.fxml;
    opens app.controller to javafx.fxml;
    opens app.model to javafx.base;
    
    exports app;
}
