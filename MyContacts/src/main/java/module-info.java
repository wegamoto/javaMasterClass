module sample.mycontacts {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens sample.mycontacts to javafx.fxml;
    exports sample.mycontacts;
}