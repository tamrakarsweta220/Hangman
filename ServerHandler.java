import java.io.* ; 
import java.net.* ; 
import java.util.* ;

public class ServerHandler implements Runnable {
    Socket socket1;
    Socket socket2;
    String hangmanWord;
    String dashedWord;
    String progress; //new modified word
    String progressTracker; //previous progress made
    int attempts; //no.of attempts given to the guesser.
    String waiting;
    String player1;
    String player2;
    BufferedReader client1br;
    BufferedReader client2br;
    boolean gameOn;
    // Constructor
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
            // System.out.println("This is where the while loop for server starts.\n");

                // Get the incoming message from the client (read from socket)
                String msg1 = client1br.readLine();
                System.out.println("Server is checking if client1 is a creator or a guesser.");
                String msg2 = client2br.readLine();
                System.out.println("Server is checking if client2 is a creator or a guesser.\n");

                if(msg1.toLowerCase().equals("creator")){
                    // System.out.println("Server is sending message to the creator.\n");
                    os1.writeBytes("Please give a word for the hangman game. Eg: KANGAROO\r\n");
                    System.out.println("Client 1 confirmed as creator. Server is asking creator for the hangman word.\n");
                    hangmanWord = client1br.readLine();
                    System.out.println("Server received hangman word from the creator. It is: "+hangmanWord+".\n");
                    os1.writeBytes("Place some dashes in the word for the guesser to guess the word. The word can have as many letters and as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n");
                    dashedWord = client1br.readLine();
                    progress=dashedWord; //to help automate the process of calculating the correct or incorrect guess
                    attempts=hangmanWord.length();
                }
                if(msg2.toLowerCase().equals("guesser")){
                    // System.out.println("Server is sending message to client2.\n");
                    os2.writeBytes("Are you ready to play the game?. Say OK if you're ready.\r\n");
                    System.out.println("Client 2 confirmed as guesser. Server is asking if the guesser is ready to play the game.\n");
                    waiting = client2br.readLine();
                    System.out.println("The guesser is ready to play the game.\n");
                    os2.writeBytes("You need to guess: "+dashedWord+". You have "+attempts+" attempts. Please start by entering a letter to guess.\r\n"); 
                }

                /**
                 * This loop will first read the letter from the guesser and send it to the creator.
                 * Then, the creator will respond with the progress made in the word.
                 * If the guess is correct, it will replace the dashed letters and send the modified word.
                 * If the guess is incorrect, it will just write the same word as before.
                 * Then, we will read what the creator sent and send it to the guesser
                 * and ask the guesser to enter a letter again (in the loop).
                */
                for(int i=attempts-1; i>=0; i--){
                    System.out.println("Server is reading the letter sent by guesser.\n");
                    String letter2 = client2br.readLine();
                    System.out.println("Their guess is:"+letter2+"\n");
                    // System.out.println("Server is sending the letter guessed by guesser to creator.");
                    os1.writeBytes("The guesser guessed this letter: "+letter2+". Is it a correct guess? If it is, replace the dashes with the letter. If not, give the same word from before."+ "\n");
                    // System.out.println("Server sent the letter guessed by guesser to the creator.\n");
                    progressTracker=progress;
                    System.out.println("Server sent the guessing letter to creator and is waiting to hear back from the creator about the progress.\n");
                    progress = client1br.readLine();
                    System.out.println("Creator replied with this progress: "+progress+"\n");
                    if(progress.equals(hangmanWord)){
                        os2.writeBytes("Congratulations, you won! You guessed the hangman word correctly: "+hangmanWord+"\n");
                        System.out.println("The guesser won the game. So the game has ended.\n");
                        break;
                    }else if(i==0 && !progress.equals(hangmanWord)){
                        os2.writeBytes("You lost the game. You ran out of attempts."+"\n");
                        os1.writeBytes("The guesser lost the game. They ran out of attempts."+"\n");
                        System.out.println("The guesser lost the game. So the game has ended.\n");
                        break;
                    }else if(progress.equals(progressTracker)){
                        os2.writeBytes("Your guess was incorrect. The progress so far is: "+progress+". You have "+i+" attempts remaining. Please enter another letter to guess."+ "\n");
                    }else{
                        os2.writeBytes("Your guess was correct. The progress so far is: "+progress+". You have "+i+" attempts remaining. Please enter another letter to guess."+ "\n");
                    }
                }

                System.out.println("Now, waiting for new players.");
                gameOn=false;

                //close all streams and sockets
                // socket1.close();
                // System.out.println("socket1 is closed.");
                // socket2.close();
                // System.out.println("socket 2 is closed.");
                // client1br.close();
                // System.out.println("buffer reader 1 is closed.");
                // client2br.close();
                // System.out.println("buffer reader 2 is closed.");
                // os1.close();
                // System.out.println("output stream 1 is closed.");
                // os2.close();
                // System.out.println("output stream 2 is closed.");
        }
    }
}
