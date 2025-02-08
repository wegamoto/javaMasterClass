module sample.scenebuilder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens sample.scenebuilder to javafx.fxml;
    exports sample.scenebuilder;
}