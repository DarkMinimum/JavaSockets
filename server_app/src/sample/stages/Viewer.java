package sample.stages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Viewer extends BasicStage {

    public Viewer(Stage stage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/sample/fxml/sampleViewer.fxml"));
        this.stage = stage;
        stage.setTitle("Server");
        stage.setScene(new Scene(root, 700, 400));

    }

}
