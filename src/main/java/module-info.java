module org.example.ejerciciod {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ejerciciod to javafx.fxml;
    exports org.example.ejerciciod;
}