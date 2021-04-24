import java.io.*;
import java.net.*;

/**
 * 
 * @author Sweta Tamrakar & Burhan Muhammad
 * @version  version 1.0
 *
 *This class helps us build a TCP client object that is capable of processing
 multiple simultaneous service requests in parallel.
 */
class Client {
    public static void main(String argv[]) throws Exception {

        String userInput; //stores the input received from the user
        String serverResponse; //stores the response received from the server
        String hangmanWord; //the complete word (without dashes) that the guesser needs to guess
        boolean gameOn=true; //a flag that checks if the game is still going on or if it has ended

        String host = argv[0]; //local host ip address (usually 127.0.0.1)
        int port = (new Integer(argv[1])).intValue(); //the port number used to connect with the server

        // set up input stream to get messages from the user
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        // establish the socket for the client
        Socket clientSocket = new Socket(host, port);
        // get a reference to the socket's output stream
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        // set up input stream to get messages from the server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // sends a welcome message to the client and askes for their player status (creator or guesser)
        System.out.println("Welcome to the hangman game!");
        System.out.println("If you are the creator, please type in creator. If you are the guesser, please type in guesser. So, are you a creator or a guesser?\n");
        //gets the player status from the client
        String player = inFromUser.readLine();
        System.out.println("Great! You are a " + player+".");
        //sends the player status to the server
        outToServer.writeBytes(player + "\n");


        // if the player is a creator, this section maintains the communication with the server by
        // setting up the hangman word and dashed word and uses a loop to keep track of the guesses
        // made by the guesser to then send a response (correct/incorrect) to them accordingly.
        if (player.equals("creator")) {

            System.out.println("We are now waiting for another player to join the game. Once we have another player, you will receive a prompt for creating the hangman word and the game will start. Please wait until then.\n");
            
            serverResponse = inFromServer.readLine();
            System.out.println(serverResponse+"\n");

            hangmanWord = inFromUser.readLine();
            outToServer.writeBytes(hangmanWord + "\n");

            serverResponse = inFromServer.readLine();
            System.out.println(serverResponse+"\n");

            String dashedWord = inFromUser.readLine();
            System.out.println("We sent the dashedWord that you created to the guesser. Please wait for the guesser to make their first guess.\n");
           
            outToServer.writeBytes(dashedWord + "\n");
            
            // if the game is still going on, the creator recieves the guess made by the guesser via the server,
            // and responds with the progress the word to help the server tell the guesser if they made a correct or incorrect guess
            while (gameOn) {

                serverResponse = inFromServer.readLine();
                System.out.println(serverResponse);

                String[] serverResponseArray=serverResponse.split(" ");
                String responseToCheck=serverResponseArray[0]+" "+serverResponseArray[1]+" "+serverResponseArray[2];
                
                // compare and close the socket here, ends game if guesser lost
                if(responseToCheck.equals("The guesser lost")){
                    gameOn=false;
                    clientSocket.close();
                    break;
                }

                userInput = inFromUser.readLine();
                
                if(!userInput.equals(hangmanWord)){
                    System.out.println("Please wait for the guesser to make another guess.\n");
                }

                outToServer.writeBytes(userInput + "\n");

                // compare and close the socket here, ends game if guesser won
                if(userInput.equals(hangmanWord)){
                    System.out.println("Wow, the guesser made the right guess and won the game.");
                    gameOn=false;
                    clientSocket.close();
                }
            }
        }


        // if the player is a guesser, this section maintains the communication with the server by
        // first confirming that they are ready to play the game and then using a loop to send out their
        // guesses letter by letter or word by word to the server which will be sent to the creator eventually
        if (player.equals("guesser")) {

            System.out.println("The game is being set up. Please wait for a moment.\n");

            serverResponse = inFromServer.readLine();
            System.out.println(serverResponse+"\n");

            String ready = inFromUser.readLine();
            outToServer.writeBytes(ready + "\n");
            System.out.println("Great! The game has started.\n");

            serverResponse = inFromServer.readLine();
            System.out.println(serverResponse+"\n");

            // if the game is still going on, the guesser can send a new guess (letter or word) to the creator via server,
            // and get a response indicating if they made a correct or an incorrect guess. They are also notified
            // of the progress they've made so far and the number of attempts remaining through the response.
            while (gameOn) {

                userInput = inFromUser.readLine();
                outToServer.writeBytes(userInput + "\n");
                System.out.println("The creator is checking if you guessed the right letter/word.\n");

                serverResponse = inFromServer.readLine();
                System.out.println(serverResponse);

                String[] serverResponseArray=serverResponse.split(" ");
                String responseToCheck=serverResponseArray[0]+" "+serverResponseArray[1]+" "+serverResponseArray[2];
                // compare and close socket here, ends game if the guesser won
                if(responseToCheck.equals("Congratulations, you won!")){
                    gameOn=false;
                    clientSocket.close();
                }
                // compare and close socket here, ends game if the guesser lost
                if(responseToCheck.equals("You lost the")){
                    gameOn=false;
                    clientSocket.close();
                }
            }
        }
    }
}