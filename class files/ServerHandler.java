import java.io.* ; 
import java.net.* ; 
import java.util.* ;

public class ServerHandler implements Runnable {
    Socket socket1;
    Socket socket2;
    // Constructor
    public ServerHandler(Socket socket1,Socket socket2) throws Exception {
        this.socket1 = socket1;
        this.socket2 = socket2;
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
        BufferedReader client1br = new BufferedReader(new InputStreamReader(is1));
        BufferedReader client2br = new BufferedReader(new InputStreamReader(is2)); 

        //welcome both clients to the game
        // os1.writeBytes("Welcome to Hangman\r\n"); 
        // System.out.println("Sent to client1: Welcome to Hangman");
        // os2.writeBytes("Welcome to Hangman\r\n"); 
        // System.out.println("Sent to client2: Welcome to Hangman");
        if(true){
            System.out.println("server is trying to get the hangman word");
            String hangmanWord = client1br.readLine();
            System.out.println("Hangman word is: "+hangmanWord);
            os2.writeBytes("The word that you have to guess is: ");
            os2.writeBytes(hangmanWord); 
            client2br.readLine();
        }
        while (true) {
            System.out.println("This is where the while loop for server starts");
            String msg2 = client2br.readLine();
            //Print message received from client
            System.out.println("Received from client: "); 
            System.out.println(msg2);
            //convert message to upper case
            String outputMsg2 = msg2.toLowerCase();
            //Send modified msg back to client (write to socket)
            os2.writeBytes(outputMsg2); 
            os2.writeBytes("\r\n"); 
            System.out.println("Sent to client: "+outputMsg2);

            // Get the incoming message from the client (read from socket)
            String msg1 = client1br.readLine();
            //Print message received from client
            System.out.println("Received from client: "); 
            System.out.println(msg1);
            //convert message to upper case
            String outputMsg1 = msg1.toUpperCase();
            //Send modified msg back to client (write to socket)
            os1.writeBytes(outputMsg1); 
            os1.writeBytes("\r\n"); 
            System.out.println("Sent to client: "+outputMsg1);

            //ask client1 to give a word to client2 to guess
            // os1.writeBytes("Please give the player a word to guess. The word can have as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n"); 
            // os1.writeBytes("\r\n"); 

            //get the word given by client1 to client2 and send it to client2 to guess
            // String givenWord = client1br.readLine();
            // os2.writeBytes("The word that you have to guess is: ");
            // os2.writeBytes(givenWord); 
            // os2.writeBytes("\r\n"); 
            // os2.writeBytes("Send a letter or the word you want to guess. \r\n");

            //get the letter or word sent by client2 for guessing
            // String guessingString = client2br.readLine();
            // os1.writeBytes("The letter/word that the player guessed is");
            // os1.writeBytes(guessingString);
            // os1.writeBytes(". Is that a right guess? If it is, replace the dashes with the right letter/word. If it is not, give the word with the progress saved earlier.\r\n");

            // again read the word given by client1 and send it to client2


            // System.out.println("Sent to client: ");
            // // Get the incoming message from the client (read from socket)
            // String msg = client1br.readLine();
            // //Print message received from client
            // System.out.println("Received from client: "); 
            // System.out.println(msg);
            // //convert message to upper case
            // String outputMsg = msg.toUpperCase();
            // //Send modified msg back to client (write to socket)
            // os1.writeBytes(outputMsg); os1.writeBytes("\r\n"); 
            // System.out.println("Sent to client: ");
        }
    }
}
