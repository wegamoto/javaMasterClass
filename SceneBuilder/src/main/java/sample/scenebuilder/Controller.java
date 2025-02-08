package sample.scenebuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private Label label;

    @FXML
    public void handleAction() {
        label.setText("OK Button pressed");
    }
}