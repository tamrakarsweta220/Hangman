package server;

import java.io.*;
import java.util.Random;

public class WordLibrary {
    /**
     * counts the number of words present in a file by counting the number of lines present in the file
     * @param filename the file to be read (wordLibrary.txt)
     * @return the number of words present in the file
     * @throws IOException
     */
    public int countlines(String filename) throws IOException {
        int cnt = 0;
        String lineRead = "";
        try{
            LineNumberReader reader  = new LineNumberReader(new FileReader(filename));
            lineRead = reader.readLine();
            while (lineRead != null) {
                cnt++;
                lineRead = reader.readLine();
            }
            cnt = reader.getLineNumber(); 
            reader.close();
        }catch(IOException e) {
            System.out.println(e.toString());
        }
        return cnt;
    }
    
    /**
     * chooses a random word from the words file (wordLibrary.txt)
     * @param file the file from which we want to choose a word
     * @param count the number of words present in that file
     * @return a word chosen randomly from the words library file
     */
    public String chooseword(String file,int count){
        String chosen = "";

         /* Generating random number */
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(count);

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            int cnt=0;
            
            while((cnt<randomNumber) && (chosen = in.readLine()) != null ){ //skip other lines till the random number chosen
                cnt ++;
            }
            
            in.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return chosen;
    }
    
    /**
     * Create a dashed word which contains equal number of dashes as the letters of the word picked from the word library
     * @param word the chosen word which is to be converted to dashes
     * @return the dashed word
     * */
    public String dashWord(String word){
        /* Setting dashes */
	    char letters[] = new char[word.length()];
        for (int i = 0; i < word.length(); i++) {
            letters[i] = '-';
        }
        String dashString = new String(letters);

        return dashString;
    }
}
