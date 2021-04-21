import java.io.* ; 
import java.net.* ; 
import java.util.* ;

public class ServerHandler implements Runnable {
    Socket socket1;
    Socket socket2;
    String hangmanWord;
    String waiting;
    String player1;
    String player2;
    BufferedReader client1br;
    BufferedReader client2br;
    // Constructor
    public ServerHandler(Socket socket1,Socket socket2) throws Exception {
        this.socket1 = socket1;
        this.socket2 = socket2;
        hangmanWord="";
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

        while (true) {
            System.out.println("This is where the while loop for server starts");

                // Get the incoming message from the client (read from socket)
                String msg1 = client1br.readLine();
                String msg2 = client2br.readLine();
                if(msg1.equals("creator")){
                    System.out.println("sending message to client1");
                    os1.writeBytes("Please give a word to the guesser. The word can have as many letters and as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n");
                    hangmanWord = client1br.readLine();
                    System.out.println("Hangman word is: "+hangmanWord);
                }
                if(msg2.equals("guesser")){
                    System.out.println("sending message to client2");
                    os2.writeBytes("Wait to get a word. Say ok if you're ready.\r\n");
                    waiting = client2br.readLine();
                    System.out.println("received a ready response from guesser");
                    os2.writeBytes("You need to guess: "+hangmanWord+"\r\n"); 
                }

                /**
                 * This loop will first read the letter from the guesser and send it to the creator.
                 * Then, the creator will respond with the progress made in the word.
                 * If the guess is correct, it will replace the dashed letters and send the modified word.
                 * If the guess is incorrect, it will just write the same word as before.
                 * Then, we will read what teh creator sent and send it to the guesser
                 * and ask the guesser to enter a letter again (in the loop).
                */
                for(int i=0; i<6; i++){
                    System.out.println("reading the letter sent by guesser.");
                    String letter2 = client2br.readLine();
                    System.out.println(letter2);
                    System.out.println("sending the letter guessed by guesser to creator");
                    os1.writeBytes("Client 2 sent this letter: "+letter2+"Is it a correct guess? If it is, replace the dashes with the letter. If not, give the same word from before.");
                    System.out.println("sent the letter guessed to creator");
                    String progress = client1br.readLine();
                    System.out.println(progress);
                    os2.writeBytes("Your guess was correct/incorrect. The progress so far is: "+progress+"please enter another letter");
                }
        }
    }
    // private String responses(BufferedReader client, String clientInput){
    //     if(client==client1br){
    //         if(clientInput.toLowerCase().equals("creator")){
    //             return "Please give a word to the guesser. The word can have as many letters and as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n";
    //         }
    //         hangmanWord=clientInput;
    //         return "Great! The word has been sent to guesser";
    //     }else if(client==client2br && clientInput.toLowerCase().equals("guesser")){
    //         return "The word that you have to guess is: "+hangmanWord;
    //     }
    //     return "Server response:"+clientInput.toUpperCase();
    // }
}
