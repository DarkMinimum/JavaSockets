package sample.utilities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.jdom2.Element;
import sample.Program;
import sample.stages.Editor;

import java.awt.*;

public class Drawer {

    public static void saveToXML(Shape shape, String type, Color color) {

        Element element = new Element(type);

        switch (type) {
            case ("Rectangle"): {

                Rectangle s = (Rectangle) shape;

                element.addContent(new Element("x").setText(String.valueOf(s.getX())));
                element.addContent(new Element("y").setText(String.valueOf(s.getY())));
                element.addContent((new Element("width").setText(String.valueOf(s.getWidth()))));
                element.addContent((new Element("height").setText(String.valueOf(s.getHeight()))));
                break;
            }
            case ("Line"): {
                Line s = (Line) shape;

                element.addContent(new Element("x1").setText(String.valueOf(s.getStartX())));
                element.addContent(new Element("y1").setText(String.valueOf(s.getStartY())));
                element.addContent(new Element("x2").setText(String.valueOf(s.getEndX())));
                element.addContent(new Element("y2").setText(String.valueOf(s.getEndY())));
                break;
            }
            case ("Ellipse"): {

                Ellipse s = (Ellipse) shape;

                element.addContent(new Element("x").setText(String.valueOf(s.getCenterX())));
                element.addContent(new Element("y").setText(String.valueOf(s.getCenterY())));
                element.addContent((new Element("radiusX").setText(String.valueOf(s.getRadiusX()))));
                element.addContent((new Element("radiusY").setText(String.valueOf(s.getRadiusY()))));
                break;
            }

        }


        element.addContent((new Element("color").setText(color.toString())));
        Controller.doc.getRootElement().addContent(element);
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

        saveToXML(rectangle, "Rectangle", color);
    }

    public static Rectangle computeBounds(Point p1, Point p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;

        int x = (dx < 0 ? p2.x : p1.x);
        int y = (dy < 0 ? p2.y : p1.y);
        int w = (int) Math.abs(dx);
        int h = (int) Math.abs(dy);

        if(Editor.isPerfect) {
            int average = Math.abs(w - h);
            return new Rectangle(x, y, average, average);
        }
        else {
            return new Rectangle(x, y, w, h);
        }
    }

    public static void drawCircle(GraphicsContext gc, Ellipse ellipse, Color color) {

        Program.log.info("Ellipse");
        Program.log.info("centerX " + ellipse.getCenterX());
        Program.log.info("centerY " + ellipse.getCenterY());
        Program.log.info("xRadius " + ellipse.getRadiusX());
        Program.log.info("yRadius " + ellipse.getRadiusY());


        gc.setFill(color);
        gc.fillOval(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());

        saveToXML(ellipse, "Ellipse", color);

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

        saveToXML(line, "Line", color);
    }
}
