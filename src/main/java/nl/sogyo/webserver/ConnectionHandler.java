package nl.sogyo.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ConnectionHandler implements Runnable {
    private Socket socket;

    public ConnectionHandler(Socket toHandle) {
        this.socket = toHandle;
    }

    /// Handle the incoming connection. This method is called by the JVM when passing an
    /// instance of the connection handler class to a Thread.
    public void run() {
        try {
            ArrayList<String> readerList = new ArrayList<>();

            // Set up a reader that can conveniently read our incoming bytes as lines of text.
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            StringBuilder sB = new StringBuilder("");

            do {
                // Read the incoming message line by line and echo is to the system out.
                line = reader.readLine();
                readerList.add(line);
                if(readerList.get(0) == null) return;
            } while (line != null && !line.isEmpty());

            while(reader.ready()) {
                char c = ((char) reader.read());
                sB.append(c);
            }
            readerList.add(sB.toString());

            // Set up a writer that can write text to our binary output stream.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            RequestInformation reqInf = new RequestInformation(readerList);

            sB.setLength(0);

            sB.append("<meta content=\"text/html;charset=utf-8\" http-equiv=\"Content-Type\">\n" +
                    "<meta content=\"utf-8\" http-equiv=\"encoding\">\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "You did an HTTP " + reqInf.getHTTPMethod() + " request.</br>" +
                    "Requested resource: " + reqInf.getResourcePath() + "</br></br>The parameters of your request are:</br>");

            for(Map.Entry<String,String> entry : reqInf.getParameterInformation().entrySet()){
                sB.append(entry.getKey() + ": " + entry.getValue() + "</br>");
            }

            sB.append("\n</body>" +
                    "</html>");

            String content = sB.toString();
            ResponseInformation resInf = new ResponseInformation("OK", content);
            for(Map.Entry<String,String> entry : resInf.getCustomHeaders().entrySet()){
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
            writer.newLine();
            writer.write(resInf.getContent());
//
//            for(Map.Entry<String,String> entry : reqInf.getParameterInformation().entrySet()){
//                writer.write(entry.getKey() + " " + entry.getValue());
//                writer.newLine();
//            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // After handling the request, we can close our socket.
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String... args) {
        try {
            // A server socket opens a port on the local computer (in this program port 9090).
            // The computer now listens to connections that are made using the TCP/IP protocol.
            ServerSocket socket = new ServerSocket(9090);
            System.out.println("Application started. Listening at localhost:9090");

            // Start an infinite loop. This pattern is common for applications that run indefinitely
            // and react on system events (e.g. connection established). Inside the loop, we handle
            // the connection with our application logic. 
            while(true) {
                // Wait for someone to connect. This call is blocking; i.e. our program is halted
                // until someone connects to localhost:9090. A socket is a connection (a virtual
                // telephone line) between two endpoints - the client (browser) and the server (this).
                Socket newConnection = socket.accept();
                // We want to process our incoming call. Furthermore, we want to support multiple
                // connections. Therefore, we handle the processing on a background thread. Java
                // threads take a class that implements the Runnable interface as a constructor
                // parameter. Upon starting the thread, the run() method is called by the JVM.
                // As our handling is in a background thread, we can accept new connections on the
                // main thread (in the next iteration of the loop).
                // Starting the thread is so-called fire and forget. The main thread starts a second
                // thread and forgets about its existence. We receive no feedback on whether the
                // connection was handled gracefully.
                Thread t = new Thread(new ConnectionHandler(newConnection));
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
