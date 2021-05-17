package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;
import sample.stages.Viewer;

import sample.utilities.Server;

public class Program extends Application {

    public static final Logger log = Logger.getLogger(Program.class);
    public static Viewer viewer;


    @Override
    public void start(Stage primaryStage) throws Exception{

        viewer = new Viewer(primaryStage);

        new Thread(new Runnable(){
            public void run(){
                Server.run();
            }
        }).start();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        launch(args);

    }

}
