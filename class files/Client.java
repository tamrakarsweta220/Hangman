import java.io.*;
import java.net.*;

class Client {

    public static void main(String argv[]) throws Exception{

        String sentence;
        String modifiedSentence;
        String host = argv[0];
        int port = (new Integer(argv[1])).intValue();
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket(host, port);


        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer =new BufferedReader(new InputStreamReader(
        clientSocket.getInputStream()));

        System.out.println("Welcome to the hangman game!");
        System.out.println("If you are the creator, please give the guesser a word. If you are the guesser, please type in guesser and wait for the word.");
        System.out.println("The word that is to be guessed can have as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n");
        if(true){
            String hangmanWord = inFromUser.readLine();
            System.out.println("got hangmanWord from user: "+hangmanWord);
            outToServer.writeBytes(hangmanWord + "\n");
        }
        while (true) {

            System.out.println("This is where the while loop for client starts");
            System.out.println("Please enter a letter/word \n");

            sentence = inFromUser.readLine();

            outToServer.writeBytes(sentence + "\n");

            modifiedSentence = inFromServer.readLine();

            System.out.println("RECEIVED FROM SERVER: " + modifiedSentence);
        }


    }
}