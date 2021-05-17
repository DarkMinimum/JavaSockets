package sample.utilities;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import sample.Program;
import sample.stages.Editor;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.List;


public class Controller {

    FileChooser fc;
    @FXML
    Label label_current_tool;
    @FXML
    Canvas canvas_field;
    @FXML
    ColorPicker color_picker;
    public static Document doc = new Document().setRootElement(new Element("Objects"));

    @FXML
    private void setFirstPoint(MouseEvent event) {

        if(Editor.state.equals(State.ERASER)) {
            canvas_field.getGraphicsContext2D().clearRect(0, 0, canvas_field.getWidth(), canvas_field.getHeight());

        } else if(!Editor.state.equals(State.NONE)) {
            Editor.first.x = (int) event.getX();
            Editor.first.y = (int) event.getY();
        }
    }

    @FXML
    private void setSecondPoint(MouseEvent event) {
        if(!Editor.state.equals(State.NONE)) {
            Editor.second.x = (int) event.getX();
            Editor.second.y = (int) event.getY();

            switch(Editor.state) {

                case RECTANGLE:
                    Drawer.drawRectangle(canvas_field.getGraphicsContext2D(), Drawer.computeBounds(Editor.first, Editor.second), color_picker.getValue());
                    break;
                case LINE:

                    Line line = new Line();
                    line.setStartX(Editor.first.x);
                    line.setStartY(Editor.first.y);
                    line.setEndX(Editor.second.x);
                    line.setEndY(Editor.second.y);

                    Drawer.drawLine(canvas_field.getGraphicsContext2D(), line, color_picker.getValue());
                    break;

                case CIRCLE:
                    Ellipse ellipse = new Ellipse();

                    ellipse.setCenterX(Drawer.computeBounds(Editor.first, Editor.second).getX());
                    ellipse.setCenterY(Drawer.computeBounds(Editor.first, Editor.second).getY());
                    ellipse.setRadiusX(Drawer.computeBounds(Editor.first, Editor.second).getWidth());
                    ellipse.setRadiusY(Drawer.computeBounds(Editor.first, Editor.second).getHeight());

                    Drawer.drawCircle(canvas_field.getGraphicsContext2D(), ellipse, color_picker.getValue());
                    break;

                default:


            }
        }

    }

    @FXML
    private void keyPressed(KeyEvent event) {

        if(event.isShiftDown()) {
            Editor.isPerfect = true;
        }
    }

    @FXML
    private void unsetPerfect(KeyEvent event) {
        Editor.isPerfect = false;
    }

    @FXML
    private void rectangleButton(ActionEvent event) {
        Editor.state = State.RECTANGLE;
        Editor.root.setCursor(Cursor.CROSSHAIR);
        label_current_tool.setText("Current tool: Rectangle");
    }

    @FXML
    private void circleButton(ActionEvent event) {
        Editor.state = State.CIRCLE;
        Editor.root.setCursor(Cursor.CROSSHAIR);
        label_current_tool.setText("Current tool: Circle");
    }

    @FXML
    private void lineButton(ActionEvent event) {
        Editor.state = State.LINE;
        Editor.root.setCursor(Cursor.CROSSHAIR);
        label_current_tool.setText("Current tool: Line");
    }

    @FXML
    private void eraserButton(ActionEvent event) {
        Editor.state = State.ERASER;
        Editor.root.setCursor(Cursor.HAND);
        label_current_tool.setText("Current tool: Eraser");
        doc = new Document();
        doc.setRootElement(new Element("Objects"));
    }

    @FXML
    private void saveCanvas(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory( new File("C:\\"));
        fc.setTitle("Choose the location of the file");
        fc.setInitialFileName("picture");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image with transparent channel", "*.png"),
                new FileChooser.ExtensionFilter("Can handle 24bits colors", "*.jpeg"),
                new FileChooser.ExtensionFilter("Kinda old format", "*.bmp"),
                new FileChooser.ExtensionFilter("Editable format", "*.xml")
        );

        try {

            File file = fc.showSaveDialog(canvas_field.getScene().getWindow());
            String fileType = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);
            fc.setInitialDirectory(file.getParentFile());
            WritableImage writableImage = new WritableImage((int) canvas_field.getWidth(), (int) canvas_field.getHeight());

            switch (fileType) {
                case "png" -> {

                    SnapshotParameters params = new SnapshotParameters();
                    params.setFill(Color.TRANSPARENT);

                    canvas_field.snapshot(params, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);

                }
                case "bmp" -> {

                    canvas_field.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);


                }
                case "jpeg" -> {

                    canvas_field.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);


                }
                case "xml" -> {

                    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
                    xmlOutputter.output(doc, new FileOutputStream(file.getAbsolutePath()));

                }
            }

            SenderThread thread = new SenderThread(doc);
            thread.run();

        }
        catch(Exception ex) {
            Program.log.error(ex);
        }


    }

    @FXML
    private void openCanvas() {

        fc = new FileChooser();
        fc.setInitialDirectory( new File("C:\\"));
        fc.setTitle("Choose the location of the file");
        fc.setInitialFileName("picture");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Editable format", "*.xml"));

        try {

            File file = fc.showOpenDialog(canvas_field.getScene().getWindow());
            fc.setInitialDirectory(file.getParentFile());

            Document result;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = documentBuilder.parse(file.getAbsolutePath());
            result = new DOMBuilder().build(w3cDocument);

            if(result != null) {

                Element rootNode = result.getRootElement();
                List<Element> treeOfRects = rootNode.getChildren("Rectangle");

                for (Element element: treeOfRects) {

                    Rectangle rect = new Rectangle();

                    rect.setX(Double.parseDouble(element.getChildText("x")));
                    rect.setY(Double.parseDouble(element.getChildText("y")));

                    rect.setWidth(Double.parseDouble(element.getChildText("width")));
                    rect.setHeight(Double.parseDouble(element.getChildText("height")));
                    Color color = Color.web(element.getChildText("color"));

                    Drawer.drawRectangle(canvas_field.getGraphicsContext2D(), rect, color);

                }

                List<Element> treeOfLines = rootNode.getChildren("Line");

                for (Element element: treeOfLines) {

                    Line line = new Line();

                    line.setStartX(Double.parseDouble(element.getChildText("x1")));
                    line.setStartY(Double.parseDouble(element.getChildText("y1")));

                    line.setEndX(Double.parseDouble(element.getChildText("x2")));
                    line.setEndY(Double.parseDouble(element.getChildText("y2")));

                    Color color = Color.web(element.getChildText("color"));

                    Drawer.drawLine(canvas_field.getGraphicsContext2D(), line, color);

                }

                List<Element> treeOfEllipses = rootNode.getChildren("Ellipse");

                for (Element element: treeOfEllipses) {

                    Ellipse ellipse = new Ellipse();

                    ellipse.setCenterX(Double.parseDouble(element.getChildText("x")));
                    ellipse.setCenterY(Double.parseDouble(element.getChildText("y")));

                    ellipse.setRadiusX(Double.parseDouble(element.getChildText("radiusX")));
                    ellipse.setRadiusY(Double.parseDouble(element.getChildText("radiusY")));

                    Color color = Color.web(element.getChildText("color"));

                    Drawer.drawCircle(canvas_field.getGraphicsContext2D(), ellipse, color);

                }


            }
            else {
                throw new IllegalArgumentException();
            }

        }
        catch(Exception ex) {
            Program.log.error(ex);
        }

    }
}
