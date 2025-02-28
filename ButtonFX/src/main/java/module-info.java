module sample.buttonfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens sample.buttonfx to javafx.fxml;
    exports sample.buttonfx;
}