module sample.layouts {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens sample.layouts to javafx.fxml;
    exports sample.layouts;
}