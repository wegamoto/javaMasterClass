module sample.events {
    requires javafx.controls;
    requires javafx.fxml;


    opens sample.events to javafx.fxml;
    exports sample.events;
}