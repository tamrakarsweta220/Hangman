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

        // if(true){
        //     System.out.println("server is trying to get the hangman word");
        //     player1 = client1br.readLine();
        //     if(player1.equals("creator")){
        //         os1.writeBytes("Please give a word to the guesser. The word can have as many letters and as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n");
        //         hangmanWord = client1br.readLine();
        //         System.out.println("Hangman word is: "+hangmanWord);
        //     }
        // }
        // if(true){
        //     System.out.println("server is trying to send the hangman word");
        //     player2 = client2br.readLine();
        //     if(player2.equals("guesser")){
        //         os2.writeBytes("The word that you have to guess is: "+hangmanWord);
        //     }
        // }

        //PROBLEM: every time the server runs, it expects client 1 to say something first
        //if we use ready method, pritns the while loop continuously ebcause both clients are empty/not ready
        while (true) {
            System.out.println("This is where the while loop for server starts");
            // if(client1br.ready()){
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
                    os2.writeBytes("You need to guess: "+hangmanWord+"\r\n Please enter a letter."); 
                }

                for(int i=0; i<6; i++){
                    String letter2 = client2br.readLine();
                    System.out.println(letter2);
                    System.out.println("sending the letter guessed by guesser to creator");
                    os1.writeBytes("Client 2 sent this letter: "+letter2+"Is it a correct guess? If it is, replace the dashes with the letter. If not, give the same word from before.");
                    System.out.println("sent the letter guessed to creator");
                    String progress = client1br.readLine();
                    System.out.println(progress);
                    os2.writeBytes("Your guess was correct/incorrect. The progress so far is: "+progress+"please enter another letter");
                }
                //loop
                //read client2
                //client 2 sends a letter/word-send that to client 1
                //read from client 1, send to client 2


                // else{
                //     //Print message received from client
                //     System.out.println("Received from client1: "+msg1); 
                //     //convert message to upper case
                //     String outputMsg1 = responses(client1br, msg1);
                //     //Send modified msg back to client (write to socket)
                //     os1.writeBytes(outputMsg1+"\r\n"); 
                //     System.out.println("Sent to client1: "+outputMsg1);
                // }
            // }
            
            // if(client2br.ready()){
                // Get the incoming message from the client (read from socket)
                
                // if(msg2.equals("guesser")){
                //     os2.writeBytes("The word that you have to guess is: "+hangmanWord);
                // }else{
                //     //Print message received from client
                //     System.out.println("Received from client2: "+msg2); 
                //     //convert message to upper case
                //     String outputMsg2 = responses(client2br, msg2);
                //     //Send modified msg back to client (write to socket)
                //     os2.writeBytes(outputMsg2+"\r\n"); 
                //     System.out.println("Sent to client2: "+outputMsg2);
                // }

                // String msg2 = client2br.readLine();
                // //Print message received from client
                // System.out.println("Received from client2: "+msg2); 
                // //convert message to upper case
                // String outputMsg2 = responses(msg2);
                // //Send modified msg back to client (write to socket)
                // os2.writeBytes(outputMsg2+"\r\n"); 
                // System.out.println("Sent to client2: "+outputMsg2);

                // // Get the incoming message from the client (read from socket)
                // String msg1 = client1br.readLine();
                // //Print message received from client
                // System.out.println("Received from client1: "+msg1); 
                // //convert message to upper case
                // String outputMsg1 = msg1.toUpperCase();
                // //Send modified msg back to client (write to socket)
                // os1.writeBytes(outputMsg1+"\r\n"); 
                // System.out.println("Sent to client1: "+outputMsg1);
            // }
        }
    }
    private String responses(BufferedReader client, String clientInput){
        if(client==client1br){
            if(clientInput.toLowerCase().equals("creator")){
                return "Please give a word to the guesser. The word can have as many letters and as many dashes as you wish. Eg: K--N--R-- for KANGAROO\r\n";
            }
            hangmanWord=clientInput;
            return "Great! The word has been sent to guesser";
        }else if(client==client2br && clientInput.toLowerCase().equals("guesser")){
            return "The word that you have to guess is: "+hangmanWord;
        }
        return "Server response:"+clientInput.toUpperCase();

        // if(clientInput.equals("creator")){
        //     return "Please enter a word for the guesser to guess ";
        // }else if(clientInput.equals("guesser")){
        //     return "The word that you have to guess is: "+hangmanWord;
        // }else{
        //     return clientInput.toUpperCase();
        // }
    }
}
