import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Client class connects to a server and receives a proverb.
 */
public class Client {

    // Create a logger instance for this class
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    /**
     * Main method to establish a connection to the server and receive a proverb.
     *
     * @param args Command-line arguments (not used).
     * @throws UnknownHostException If the IP address of the host cannot be determined.
     * @throws IOException If an I/O error occurs when creating the socket.
     */
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8000)) {
            logger.info("The Client is connected to the server.");

            byte[] buffer = new byte[1024];
            int bytesRead = socket.getInputStream().read(buffer);
            String proverb = new String(buffer, 0, bytesRead);

            // Use conditional logging to avoid unnecessary string concatenation
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Received proverb: %s", proverb));
            }
        } catch (UnknownHostException e) {
            logger.log(Level.SEVERE, "Unknown host: {0}", e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "I/O error: {0}", e.getMessage());
        }
    }
}
