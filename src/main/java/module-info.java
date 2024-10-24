module org.example.ejerciciod {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ejerciciod to javafx.fxml;
    exports org.example.ejerciciod.model;
    opens org.example.ejerciciod.model to javafx.fxml;
    exports org.example.ejerciciod.app;
    opens org.example.ejerciciod.app to javafx.fxml;
}