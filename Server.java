import java.io.* ; 
import java.net.* ; 
import java.util.* ;

/**
 * 
 * @author Sweta Tamrakar & Burhan Muhammad
 * @version  version 1.0
 *
 *This class helps us build a multi-threaded Server that is capable of processing
 multiple simultaneous service requests in parallel.
 */
public class Server {
    public static void main(String argv[]) throws Exception {
        // Get the port number from the command line.
        int port = (new Integer(argv[0])).intValue();
        // Establish the listen socket.
        ServerSocket socket = new ServerSocket(port);
        // Process incoming requests in an infinite loop.
        while (true) {
            // Listen for a TCP connection request.
            System.out.println("listening\n");
            Socket connection1 = socket.accept();
            Socket connection2 = socket.accept();
            System.out.println("connection established\n");
            // Construct an object to process the incoming request
            ServerHandler request = new ServerHandler(connection1,connection2);
            // Create a new thread to process the request.
            Thread thread = new Thread(request);
            // Start the thread.
            thread.start();
        }
    }
}