package sample.utilities;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    Canvas canvas_field;

    @FXML
    Label label_with_message;

    public static Label label_count;
    public static Canvas canvas_main;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label_count = label_with_message;
        canvas_main = canvas_field;
    }
}
