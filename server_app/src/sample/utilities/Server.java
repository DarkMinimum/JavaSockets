package sample.utilities;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import sample.Program;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int requestsCount = 0;
    private static final int SERVER_PORT = 8071;

    public static void run() {

        try {
            ServerSocket serv = new ServerSocket(SERVER_PORT);
            Program.log.info("Server is started");

            while (true) {
                Socket sock = serv.accept();
                processRequest(sock);
                sock.close();
            }

        } catch (IOException e) {
            Program.log.error("Error while running the server application:\t - " + e.getMessage());
        }
    }

    private static void processRequest(Socket sock) throws IOException {

        requestsCount++;
        Controller.canvas_main.getGraphicsContext2D().clearRect(0, 0, Controller.canvas_main.getWidth(), Controller.canvas_main.getHeight());

        Program.log.info("Received " + requestsCount + " client request");
        DataInputStream is = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
        int count = is.readInt();

        if(count > 0) {
            for (int i = 0; i < count; ++i) {

                switch (is.readChar()) {
                    case 'R' -> {
                        Rectangle shape = new Rectangle();

                        shape.setX(is.readInt());
                        shape.setY(is.readInt());
                        shape.setWidth(is.readInt());
                        shape.setHeight(is.readInt());

                        Color color = Color.color(is.readDouble(), is.readDouble(), is.readDouble());
                        drawRectangle(Controller.canvas_main.getGraphicsContext2D(), shape, color);

                    }
                    case 'E' -> {
                        Ellipse ellipse = new Ellipse();

                        ellipse.setCenterX(is.readInt());
                        ellipse.setCenterY(is.readInt());
                        ellipse.setRadiusX(is.readInt());
                        ellipse.setRadiusY(is.readInt());

                        Color color = Color.color(is.readDouble(), is.readDouble(), is.readDouble());
                        drawCircle(Controller.canvas_main.getGraphicsContext2D(), ellipse, color);

                    }
                    case 'L' -> {
                        Line line = new Line();

                        line.setStartX(is.readInt());
                        line.setStartY(is.readInt());
                        line.setEndX(is.readInt());
                        line.setEndY(is.readInt());

                        Color color = Color.color(is.readDouble(), is.readDouble(), is.readDouble());
                        drawLine(Controller.canvas_main.getGraphicsContext2D(), line, color);
                    }
                }
            }
        }

        sock.close();

        Platform.runLater(new Runnable(){
            public void run(){
                Program.viewer.show();
                Controller.label_count.setText("Request number: " + requestsCount + " | Figures number: " + count);
            }
        });


    }

    public static void drawRectangle(GraphicsContext gc, Rectangle rectangle, Color color) {

        Program.log.info("Rect");
        Program.log.info("x " + rectangle.getX());
        Program.log.info("y " + rectangle.getY());
        Program.log.info("width " + rectangle.getWidth());
        Program.log.info("height " + rectangle.getHeight());

        gc.setFill(color);
        gc.fillRect(rectangle.getX(),
                rectangle.getY(),
                rectangle.getWidth(),
                rectangle.getHeight());

    }

    public static void drawCircle(GraphicsContext gc, Ellipse ellipse, Color color) {

        Program.log.info("Ellipse");
        Program.log.info("centerX " + ellipse.getCenterX());
        Program.log.info("centerY " + ellipse.getCenterY());
        Program.log.info("xRadius " + ellipse.getRadiusX());
        Program.log.info("yRadius " + ellipse.getRadiusY());


        gc.setFill(color);
        gc.fillOval(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());

    }

    public static void drawLine(GraphicsContext gc, Line line, Color color) {

        Program.log.info("Line");
        Program.log.info("x1 " + line.getStartX());
        Program.log.info("y1 " + line.getStartY());
        Program.log.info("x2 " + line.getEndX());
        Program.log.info("y2 " + line.getEndY());

        gc.setStroke(color);
        gc.setLineWidth(2d);
        gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }


}
