package server;

//import server.Textfile;
import java.net.*;
import java.io.*;

public class hangmanServer {

    static String filename = "words.txt";//consider passing it as parameter

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        int port = 6789;
        String host = "localhost";
        boolean listening = true;

        try {
            
            //create an IP address and the server's socket to this address and port 2222
            // InetAddress addr = InetAddress.getByName(host);
            // ServerSocket serversocket = new ServerSocket(port, 1000, addr);

            ServerSocket serverSocket = new ServerSocket(port);

            while (listening) {    // the main server's loop

                //listen and accept incoming connections
                System.out.println("Listening to connections...");
                Socket clientSocket = serverSocket.accept();

                //creates a thread in the connection socket that accepted the client
                Handler hangmanhandler = new Handler(clientSocket, filename);
                hangmanhandler.setPriority(hangmanhandler.getPriority() + 1);
                hangmanhandler.start();
            }
            serverSocket.close(); 
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }

    }
}