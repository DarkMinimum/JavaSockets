package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sample.stages.Editor;

public class Program extends Application {

    public static final Logger log = Logger.getLogger(Program.class);
    public static Editor editor;

    @Override
    public void start(Stage primaryStage) throws Exception{

        editor = new Editor(primaryStage);
        editor.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
