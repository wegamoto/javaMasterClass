module sample.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens sample.javafx to javafx.fxml;
    exports sample.javafx;
}