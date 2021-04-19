package server;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.IOException;
import java.net.Socket;

public class Handler extends Thread implements Serializable {

    private Socket clientSocket;
    protected String chosenword;
    ServerMessages clientdata = new ServerMessages();//server's response to client
    protected String Filename;
    WordLibrary file;
    
    /**
     * initializing handler constructor
     * @param s the client socket
     * @param filename filename of the word library to choose word from it
     */
    Handler(Socket s, String filename) {

            file = new WordLibrary();
            Filename = filename;
            this.clientSocket = s;

            //initialize Server to Client messages/response values
            clientdata.score = 0;
            clientdata.FailAttempts = 10;
            clientdata.games = 0;
            clientdata.message = "";
            clientdata.info = "";

            //choose word from the word library
            try {
                chosenword = file.chooseword(filename, file.countlines(filename));
            } catch (IOException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Chosenword= "+chosenword);
            
            //Creates the dashed word to send to the Client
            clientdata.word = new StringBuffer(file.dashWord(chosenword));
            
    }

    @Override
    public void run() {
        try {
            boolean running = true;
            Object clientObj;
            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            
             //creates the output stream to send the object to the client
            try {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                System.out.println(e.toString());
            }

            //creates the input stream to read the object which the client sent
            try {
                input = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                System.out.println(e.toString());
                return;
            }
           
            while (running) {
                //read object that the clients have sent
                try {
                    clientObj = input.readObject();
                    //System.out.println("Received :" + ((ClientMessages) clientObj).clientword);
                } catch (ClassNotFoundException cnfe) {
                    System.out.println(cnfe.toString());
                    return;
                } catch (OptionalDataException ode) {
                    System.out.println(ode.toString());
                    return;
                } catch (IOException ioe) {
                    System.out.println(ioe.toString());
                    return;
                }

                //creates the hangmanCases class that implements the hangman cases
                HangmanCases hangmanCase = new HangmanCases();
                
                if (clientObj instanceof ClientMessages) {
                    if (((ClientMessages) clientObj).getaction() == 1) {//Send action
                        //Implement the server's response based on the client's actions on the game
                        hangmanCase.compute(chosenword, clientdata, (ClientMessages) clientObj);
                    }
                    else if (((ClientMessages) clientObj).getaction() == 2) {//New Game
                        //Initiallize failattempts and delete previously viewed messages
                        clientdata.FailAttempts = 10;
                        clientdata.message = "";
                        clientdata.info = "";
                        //Make a new choice
                        chosenword = file.chooseword(Filename, file.countlines(Filename));
                        System.out.println(chosenword);
                        clientdata.word = new StringBuffer(file.dashWord(chosenword));
                    }
                    else if (((ClientMessages) clientObj).getaction() == 3) {//stop the client's running thread in the server side
                        running=false;
                    }
                }
                
                //send the servers response to the client
                try {
                    // output.reset();
                    output.writeObject(clientdata);
                    output.flush();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }

            }
            
            //closes the streams if the server wants to stop running
            try {
                output.close();
                input.close();

            } catch (IOException ioe) {
                System.out.println(ioe.toString());
            }

            System.out.println("Connection Closed");
            clientSocket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
