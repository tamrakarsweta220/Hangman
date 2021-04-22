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
            System.out.println("This is where the while loop for server starts.\n");

                // Get the incoming message from the client (read from socket)
                String msg1 = client1br.readLine();
                String msg2 = client2br.readLine();
                if(msg1.toLowerCase().equals("creator")){
                    System.out.println("Server is sending message to client1.\n");
                    os1.writeBytes("Please give a word for the hangman game. Eg: KANGAROO\r\n");
                    hangmanWord = client1br.readLine();
                    System.out.println("Hangman word is: "+hangmanWord+".\n");
                    os1.writeBytes("Place some dashes in the word for the guesser to guess the word. The word can have as many letters and as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n");
                    dashedWord = client1br.readLine();
                    progress=dashedWord; //to help automate the process of calculating the correct or incorrect guess
                }
                if(msg2.toLowerCase().equals("guesser")){
                    System.out.println("Server is sending message to client2.\n");
                    os2.writeBytes("The creator is going to send you a word to guess. Say ok if you're ready to play the game.\r\n");
                    waiting = client2br.readLine();
                    System.out.println("The guesser said that they are ready to play the game.\n");
                    os2.writeBytes("You need to guess: "+dashedWord+". Please start by entering a letter.\r\n"); 
                }

                /**
                 * This loop will first read the letter from the guesser and send it to the creator.
                 * Then, the creator will respond with the progress made in the word.
                 * If the guess is correct, it will replace the dashed letters and send the modified word.
                 * If the guess is incorrect, it will just write the same word as before.
                 * Then, we will read what teh creator sent and send it to the guesser
                 * and ask the guesser to enter a letter again (in the loop).
                */
                for(int i=0; i<hangmanWord.length()-1; i++){
                    System.out.println("Server is reading the letter sent by guesser.\n");
                    String letter2 = client2br.readLine();
                    System.out.println(letter2);
                    System.out.println("Server is sending the letter guessed by guesser to creator.");
                    os1.writeBytes("Client 2 guessed this letter: "+letter2+". Is it a correct guess? If it is, replace the dashes with the letter. If not, give the same word from before."+ "\n");
                    System.out.println("Server sent the letter guessed by guesser to the creator.\n");
                    progressTracker=progress;
                    progress = client1br.readLine();
                    System.out.println(progress);
                    if(progress.equals(hangmanWord)){
                        os2.writeBytes("Yay you win! You guessed the hangman word correctly: "+hangmanWord+"\n");
                        System.out.println("Game won.\n");
                        //write something to client 1 to indicate game end?
                        break;
                    }else if(progress.equals(progressTracker)){
                        os2.writeBytes("Your guess was incorrect. The progress so far is: "+progress+". Please enter another letter."+ "\n");
                    }else{
                        os2.writeBytes("Your guess was correct. The progress so far is: "+progress+". Please enter another letter."+ "\n");
                    }
                }

                System.out.println("End of game");

                //close all streams and sockets
                socket1.close();
                System.out.println("socket1 is closed.");
                socket2.close();
                System.out.println("socket 2 is closed.");
                client1br.close();
                System.out.println("buffer reader 1 is closed.");
                client2br.close();
                System.out.println("buffer reader 2 is closed.");
                os1.close();
                System.out.println("output stream 1 is closed.");
                os2.close();
                System.out.println("output stream 2 is closed.");

                gameOn=false;
        }
    }
}

//question 1: do we have to close input stream as well?
//question 2: how to end the game? disconnected server and client connection
//            but having problem with teh loop satrted in client class.
//            It is trying to complete the while loop there.
