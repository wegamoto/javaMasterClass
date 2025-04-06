module wewe.helloworldfx24 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens wewe.helloworldfx24 to javafx.fxml;
    exports wewe.helloworldfx24;
}