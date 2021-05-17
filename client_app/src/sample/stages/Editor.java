package sample.stages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.Program;
import sample.utilities.State;

import java.awt.*;
import java.io.IOException;


public class Editor extends BasicStage {

    public static State state = State.NONE;
    public static Point first = new Point();
    public static Point second = new Point();
    public static boolean isPerfect = false;

    public Editor(Stage stage) throws IOException {

        root = FXMLLoader.load(getClass().getResource("/sample/fxml/sampleDrawer.fxml"));
        this.stage = stage;
        stage.setTitle("Client");
        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);



    }


}
