import java.io.*;
import java.net.*;

class Client {

    public static void main(String argv[]) throws Exception {

        String userInput;
        String serverResponse;
        String host = argv[0];
        int port = (new Integer(argv[1])).intValue();
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket(host, port);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("Welcome to the hangman game!");
        System.out.println("If you are the creator, please type in creator. If you are the guesser, please type in guesser. So, are you a creator or a guesser?");
        String player = inFromUser.readLine();
        System.out.println("The player is a: " + player+".");
        outToServer.writeBytes(player + "\n");
        System.out.println("Client sent the player info to the server. Please wait to get a response from server.");

        if (player.equals("creator")) {
            serverResponse = inFromServer.readLine();
            System.out.println("Creator got the prompt for writing hangmanWord to the server.\n");
            System.out.println(serverResponse);

            String hangmanWord = inFromUser.readLine();
            System.out.println("Creator got the hangmanWord from the user: " + hangmanWord);
            outToServer.writeBytes(hangmanWord + "\n");
            System.out.println("Creator sent "+hangmanWord+" to the server.\n");

            serverResponse = inFromServer.readLine();
            System.out.println("Creator got the prompt for writing dashedWord to the server.");
            System.out.println(serverResponse);

            String dashedWord = inFromUser.readLine();
            System.out.println("Creator got the dashedWord from user: " + dashedWord+".\n");
            outToServer.writeBytes(dashedWord + "\n");
            System.out.println("Creator sent the dashedWord to the server.\n");

            while (true) {

                System.out.println("This is where the while loop for creator starts. So, we are now waiting to get something from the server first.");

                serverResponse = inFromServer.readLine();
                System.out.println("Creator got the response from server.\n");
                System.out.println(serverResponse);

                System.out.println("Creator is waiting to get the modified progress word from user.");
                userInput = inFromUser.readLine();
                System.out.println("Creator got input from user and is now sending that to the server.");

                outToServer.writeBytes(userInput + "\n");
                System.out.println("Creator sent the input to the server.\n");
            }
        }
        if (player.equals("guesser")) {
            serverResponse = inFromServer.readLine();
            System.out.println("Guesser got the prompt for writing if they are ready to the server.\n");
            System.out.println(serverResponse);

            String ready = inFromUser.readLine();
            System.out.println("Guesser received the ready confirmation from user: " + ready);
            outToServer.writeBytes(ready + "\n");
            System.out.println("Guesser sent the ready confirmation to the server.\n");

            serverResponse = inFromServer.readLine();
            System.out.println("Guesser got the dashedWord from the server. Now guesser can start playing by guessing individual letters.");
            System.out.println(serverResponse);
            while (true) {

                System.out.println("This is where the while loop for guesser starts. Now, please start guessign by typing in a letter.\n");

                System.out.println("Guesser is waiting to get the letter from user.");
                userInput = inFromUser.readLine();
                System.out.println("Guesser got the letter from user and is now sending that to the server.");

                outToServer.writeBytes(userInput + "\n");
                System.out.println("Guesser sent the letter to the server. Now, it is waiting to hear if their guess was correct or incorrect.\n");

                serverResponse = inFromServer.readLine();
                System.out.println("Guesser got the response (correct or incorrect) from the creator.\n");
                System.out.println(serverResponse);
            }
        }
    }
}