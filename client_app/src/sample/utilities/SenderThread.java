package sample.utilities;

import javafx.scene.paint.Color;
import org.jdom2.Document;
import sample.Program;


import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SenderThread extends Thread {

    private final static int SERVER_PORT = 8071;
    private Document doc;

    public SenderThread(Document doc) {

        this.doc = doc;
    }

    public void run() {

        if (doc.getRootElement().getContentSize() == 0) {
            Program.log.info("No figures to send");
            return;
        }

        Program.log.info("Sending figures information to the server");

        try {
            send(doc);
        } catch (IOException e) {
            Program.log.error("Failed to send figures info");
        }

    }

    private void send(Document doc) throws IOException {

        Socket s = new Socket(InetAddress.getLocalHost(), SERVER_PORT);

        DataOutputStream ds = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
        int count = doc.getRootElement().getContentSize();

        Program.log.info("Count of object: " + count);
        ds.writeInt(count);

        try {
            for(int i = 0; i < count; ++i) {

                String fullName = doc.getRootElement().getContent(i).toString();
                char name = fullName.charAt(fullName.lastIndexOf("<") + 1);
                String[] values = doc.getRootElement().getContent(i).getValue().split("\\.0");
                Program.log.info(name + "\t" + doc.getRootElement().getContent(i).getValue());
                ds.writeChar(name);

                for (int j = 0; j < values.length - 1; ++j) {
                    Program.log.info("\t - " + values[j]);
                    ds.writeInt(Integer.parseInt(values[j]));

                }

                Color color = Color.web(values[values.length - 1]);
                ds.writeDouble(color.getRed());
                ds.writeDouble(color.getGreen());
                ds.writeDouble(color.getBlue());
            }


            ds.flush();
        }
        catch (Exception exception) {
            Program.log.error(exception.getMessage());
        }

    }




}
