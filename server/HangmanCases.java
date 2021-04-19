package server;

public class HangmanCases {
    public HangmanCases() {
    }

    /**
     * this method keeps track of the messages sent by server to the client and
     * computes the score, no. of failed attempts, and the no. of games played by the client
     * @param chosenWord
     * @param serverMessage
     * @param clientMessage
     */
    public void compute(String chosenWord, ServerMessages serverMessage, ClientMessages clientMessage) {
        
        //System.out.println("I received " + clientMessage.clientword + "   action:" + String.valueOf(clientMessage.getaction()));
        
        //if client sent nothing
        if (clientMessage.clientword.isEmpty()) {
            serverMessage.message = "";
            serverMessage.info = "Click on a letter first!";
        //if the client sent a letter
        } else if (clientMessage.clientword.length() == 1) {
            //Letter does not exist    
            if (chosenWord.indexOf(clientMessage.clientword) == -1) {
                serverMessage.message = "Letter does not exist";
                serverMessage.FailAttempts--;//no.of attempt decreased
                serverMessage.info = String.valueOf(serverMessage.FailAttempts)+" attempts are left!";
            
                //no more chances left, failed attempts = 0
                if (serverMessage.FailAttempts == 0) {
                    serverMessage.message = "You lost!!!";
                    serverMessage.word = new StringBuffer(chosenWord);//server reveals to client the actual word
                    serverMessage.info = "Click on New Game for another chance!";
                    serverMessage.games++;
                }
            }

            char[] c = new char[clientMessage.clientword.length()];
            c = clientMessage.clientword.toCharArray();

            //if the letter has already been guessed 
            if ((serverMessage.word.toString()).indexOf(clientMessage.clientword) != -1) {
                serverMessage.message = "Letter has already been guessed.";
                serverMessage.info = "Try another one!";     
            } else {//Find where the Letter is in the word
                for (int i = 0; i < chosenWord.length(); i++) {
                    if (String.valueOf(chosenWord.charAt(i)).equalsIgnoreCase(String.valueOf(c[0]))) {
                        serverMessage.word.setCharAt(i, c[0]);
                        if (serverMessage.word.indexOf("-") == -1) {//No dash, word is whole!
                            serverMessage.word = new StringBuffer(chosenWord);
                            serverMessage.message = "You win!!!";
                            serverMessage.info = "Click on New Game for another word";
                            serverMessage.score++;
                            serverMessage.games++;
                        } else {
                            serverMessage.message = "Letter correct!";
                            serverMessage.info = "Keep guessing :D";
                        }
                    }

                }
            }
        //if the client sent a word
        } else if (clientMessage.clientword.equalsIgnoreCase(chosenWord)) {//Client sent word
            serverMessage.word = new StringBuffer(chosenWord);
            System.out.println("win word: " + serverMessage.word);
            serverMessage.message = "You win!!!";
            serverMessage.info = "Click on New Game for another word";
            serverMessage.score++;
            serverMessage.games++;
        //Client sent wrong word
        } else {
            serverMessage.message = "Word is not correct";
            serverMessage.info = "Try again!";
            serverMessage.FailAttempts--;
            if (serverMessage.FailAttempts == 0) {
                serverMessage.message = "You lost!!!";
                serverMessage.word = new StringBuffer(chosenWord);
                serverMessage.info = "Click on New Game for another chance!";
                serverMessage.games++;

            }
        }
    }
}
