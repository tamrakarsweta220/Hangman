// package client;

import server.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private ClientMessages clientinput;//the actions of the client
    private boolean running;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    
    private Socket clientSocket = null;

    //thread constructor
    ClientHandler(String host, int port) throws UnknownHostException, IOException {
        this.clientinput = clientinput;
        this.clientSocket = new Socket(host, port);
        
        if (this.clientSocket == null) {
            System.out.println("socket null");
        }
        this.out = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.in = new ObjectInputStream(this.clientSocket.getInputStream());
    }

    while (true) {

        System.out.println("Please enter the text that you want to send to the server \n");
        
        sentence = inFromUser.readLine();
        
        outToServer.writeBytes(sentence + "\n");
        
        modifiedSentence = inFromServer.readLine();
        
        System.out.println("RECEIVED FROM SERVER: " + modifiedSentence);
    }
}
