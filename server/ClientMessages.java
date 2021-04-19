package server;

public class ClientMessages {
    private int action;
    protected String clientword;//the letter or word that client guesses
    
    // public void execute() {
	// }

    /**
     * gets the letter or word that the client is guessing
     * @return the letter or word that the client is guessing
     */
    public  String getclientword(){
        return clientword;
    }

    /**
     * sets the letter or word that the client is guessing
     */
    public void setclientword(String word) {
        clientword=word;
    }

    /**
     * gets the action that the client is performing
     * @return the action that the client is performing
     */
    public int getaction(){
        return action;
    }

    /**
     * sets the action that the client will perform
     */
    public void setaction(int i) {
        action=i;
    }

}
