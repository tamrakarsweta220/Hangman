import java.io.* ; 
import java.net.* ; 
import java.util.* ;

/**
 * 
 * @author Sweta Tamrakar & Burhan Muhammad
 * @version  version 1.0
 *
 *This class helps us build a server handler that is capable of keeping track of
 and processing the communication that is happening between the two clients (creator and guesser).
 */
public class ServerHandler implements Runnable {
    Socket socket1;
    Socket socket2;
    String hangmanWord;
    String dashedWord;
    String progress; //keeps track of the new progress made
    String progressTracker; //keeps track of the last progress made
    int attempts; //no.of attempts given to the guesser.
    String waiting;
    String player1; //stores the status of client 1 (creator or guesser)
    String player2; //stores the status of client 2 (creator or guesser)
    BufferedReader client1br;
    BufferedReader client2br;
    boolean gameOn; //a flag that checks if the game is still going on or if it has ended

    /**
         * Constructor for ServerHandler, initializes the connection sockets and the game running flag.
         * @param socket1 a reference to the connection socket for client 1
         * @param socket2 a reference to the connection socket for client 2
         */
    public ServerHandler(Socket socket1,Socket socket2) throws Exception {
        this.socket1 = socket1;
        this.socket2 = socket2;
        gameOn=true;
    }
    // Implement the run() method of the Runnable interface.
    public void run() {
        try { 
            processRequest();
        } catch (Exception e) { 
            System.out.println(e);
        }
    }
    
    private void processRequest() throws Exception {
        // Get a reference for the first socket's input and output streams.
        InputStream is1 = socket1.getInputStream(); 
        DataOutputStream os1 = new DataOutputStream(socket1.getOutputStream());
        // Get a reference for the second socket's input and output streams.
        InputStream is2 = socket2.getInputStream(); 
        DataOutputStream os2 = new DataOutputStream(socket2.getOutputStream());
        // Set up input streams for both sockets
        client1br = new BufferedReader(new InputStreamReader(is1));
        client2br = new BufferedReader(new InputStreamReader(is2)); 

        while (gameOn) {

            // Get the incoming message from client 1 (read from socket)
            String msg1 = client1br.readLine();
            System.out.println("Server is checking if client1 is a creator or a guesser.");

            // Get the incoming message from client 2 (read from socket)
            String msg2 = client2br.readLine();
            System.out.println("Server is checking if client2 is a creator or a guesser.\n");

            // Checks if client 1 is a creator and then sends a prompt to them if they are the
            // creator to set up the hangman word and the dashed word for the guesser to guess
            if(msg1.toLowerCase().equals("creator")){
                // sends the prompt to the creator to set up the hangman word
                os1.writeBytes("Please give a word for the hangman game. You should spell out the entire word. Eg: KANGAROO\r\n");
                System.out.println("Client 1 confirmed as creator. Server is asking creator for the hangman word.\n");

                // gets the hangman word from the creator
                hangmanWord = client1br.readLine();
                System.out.println("Server received hangman word from the creator. It is: "+hangmanWord+".\n");

                // sends another prompt to the creator to set up the dashed word
                os1.writeBytes("Place some dashes in the word for the guesser to guess the word. The word can have as many letters and as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n");

                // gets the dashed word from the creator
                dashedWord = client1br.readLine();

                // stores the dashed word to help automate the process of calculating if the guesser made the correct guess or not
                progress=dashedWord; 

                // keeps track of the number of attempts that the guesser has to guess the word
                attempts=hangmanWord.length();
            }

            // Checks if client 2 is a guesser and asks them if they are ready to play the game
            // If they are ready, they are given the dashed word to start making a guess
            if(msg2.toLowerCase().equals("guesser")){
                // asks the guesser if they are ready to play the game
                os2.writeBytes("Are you ready to play the game?. Say OK if you're ready.\r\n");
                System.out.println("Client 2 confirmed as guesser. Server is asking if the guesser is ready to play the game.\n");
                
                // gets the confirmation that the guesser is ready to play the game
                waiting = client2br.readLine();
                System.out.println("The guesser is ready to play the game.\n");

                // sends the dashed word to the guesser so that they can start guessing the word
                // also alerts the guesser about how many attempts they have to make the guess
                os2.writeBytes("You need to guess: "+dashedWord+". You have "+attempts+" attempts. Please start by entering a letter or the entire word to guess.\r\n"); 
            }

            // This loop will help facilitate the communication between the two clients via the server
            for(int i=attempts-1; i>=0; i--){
                System.out.println("Server is reading the letter sent by guesser.\n");

                // reads the letter/word sent by the guesser
                String letter2 = client2br.readLine();
                System.out.println("Their guess is:"+letter2+"\n");
                
                // sends the letter guessed to the creator to ask them if the guess was correct or incorrect
                os1.writeBytes("The guesser guessed this letter/word: "+letter2+". Is it a correct guess? If it is, replace the dashes with the letter. If not, give the same word from before."+ "\n");
                
                // saves the last progress that was made by the guesser to help calculate if their new guess is correct or incorrect
                progressTracker=progress;
                System.out.println("Server sent the guessing letter to creator and is waiting to hear back from the creator about the progress.\n");
                
                // reads the progress word sent by the creator; it will be the same word as before if the guess 
                // was incorrect and a modified word if the guess was correct
                progress = client1br.readLine();
                System.out.println("Creator replied with this progress: "+progress+"\n");

                // checks if the guesser made the right guesses and won the game,
                // sends a congratulations message to the guesser if they won,
                // and notifies the creator that the guesser won and ends the game
                if(progress.equals(hangmanWord)){
                    os2.writeBytes("Congratulations, you won! You guessed the hangman word correctly: "+hangmanWord+"\n");
                    System.out.println("The guesser won the game. So the game has ended.\n");
                    break;
                // checks if the guesser is out of attempts and was unable to guess the word,
                // sends the you lost the game message if they lost,
                // and notifies the creator that the guesser lost and ends the game
                }else if(i==0 && !progress.equals(hangmanWord)){
                    os2.writeBytes("You lost the game. You ran out of attempts."+"\n");
                    os1.writeBytes("The guesser lost the game. They ran out of attempts."+"\n");
                    System.out.println("The guesser lost the game. So the game has ended.\n");
                    break;
                // check if the guess made by the guesser was incorrect and if it was incorrect, 
                // the guesser is notified that their guess was incorrect and the game continues
                }else if(progress.equals(progressTracker)){
                    os2.writeBytes("Your guess was incorrect. The progress so far is: "+progress+". You have "+i+" attempts remaining. Please enter another letter or the entire word to guess."+ "\n");
                // if it comes to this point, it means that the guess made by the guesser was correct,
                // so the guesser is notified that their guess was correct and the game continues
                }else{
                    os2.writeBytes("Your guess was correct. The progress so far is: "+progress+". You have "+i+" attempts remaining. Please enter another letter or the entire word to guess."+ "\n");
                }
            }

            // the game has ended, so the server waits for new players
            System.out.println("Now, waiting for new players.");

            //end the while loop
            gameOn=false;
        }
    }
}
