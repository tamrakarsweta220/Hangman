import java.io.*;
import java.net.*;

class Client {

    public static void main(String argv[]) throws Exception {

        String userInput;
        String serverResponse;
        String serverResponse2;
        String host = argv[0];
        int port = (new Integer(argv[1])).intValue();
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket(host, port);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("Welcome to the hangman game!");
        System.out.println("If you are the creator, enter creator. If you are the guesser, enter guesser.");
        System.out.println("creator or guesser");
        String player = inFromUser.readLine();
        System.out.println("The player is a: " + player);
        outToServer.writeBytes(player + "\n");
        System.out.println("sent playr info to server");

        if (player.equals("creator")) {
            serverResponse = inFromServer.readLine();
            System.out.println("got player related respond from server");
            System.out.println(serverResponse);

            String hangmanWord = inFromUser.readLine();
            System.out.println("got hangmanWord from user: " + hangmanWord);
            outToServer.writeBytes(hangmanWord + "\n");
            while (true) {

                System.out.println("This is where the while loop for creator starts");
                System.out.println("now waiting to get something from server first");

                serverResponse = inFromServer.readLine();
                System.out.println("got the serverResponse");

                System.out.println(serverResponse);

                System.out.println("getting input from user");
                userInput = inFromUser.readLine();
                System.out.println("got input from user and sending that to server");

                outToServer.writeBytes(userInput + "\n");
                System.out.println("sent the input to the server");
            }
        }
        if (player.equals("guesser")) {
            serverResponse = inFromServer.readLine();
            System.out.println("got player related respond from server");
            System.out.println(serverResponse);

            String waiting = inFromUser.readLine();
            System.out.println("got waiting confirmation: " + waiting);
            outToServer.writeBytes(waiting + "\n");

            serverResponse = inFromServer.readLine();
            System.out.println("got the guessign word from server. Now start playing");
            System.out.println(serverResponse);
            while (true) {

                System.out.println("This is where the while loop for guesser starts");
                System.out.println("please enter a letter");

                System.out.println("getting input from user");
                userInput = inFromUser.readLine();
                System.out.println("got input from user and sending that to server");

                outToServer.writeBytes(userInput + "\n");
                System.out.println("sent the input to the server");

                System.out.println("now waiting to get something from server first");
                serverResponse = inFromServer.readLine();
                System.out.println("got the serverResponse");

                System.out.println(serverResponse);
            }
        }
    }
}