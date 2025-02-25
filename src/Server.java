import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Server class listens for client connections and sends random proverbs.
 */
public class Server {

    private final List<String> messages = new ArrayList<>();
    private final Random random = new Random();

    // Create a logger instance for this class
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    // Server running status
    private boolean isRunning = true;

    /**
     * Adds a proverb to the list of messages.
     *
     * @param message The proverb to add.
     */
    public void addMessage(String message) {
        messages.add(message);
    }

    /**
     * Retrieves a random proverb from the list.
     *
     * @return A random proverb.
     */
    public String getRandomMessage() {
        int index = random.nextInt(messages.size());
        return messages.get(index);
    }

    /**
     * Handles the client connection by sending a random proverb.
     *
     * @param socket The client socket.
     */
    private void handleClientConnection(Socket socket) {
        try (OutputStream outputStream = socket.getOutputStream()) {

            // Conditional logging to avoid unnecessary method call
            if (logger.isLoggable(Level.INFO)) {
                logger.info("Client connected from: " + socket.getRemoteSocketAddress());
            }

            String proverb = getRandomMessage();

            // Conditional logging for proverb message
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, String.format("Sending proverb: %s", proverb));
            }

            outputStream.write(proverb.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            // Log exception details without concatenation
            logger.log(Level.SEVERE, "I/O error with client connection: {0}", e.getMessage());
        }
    }

    /**
     * Starts the server and listens for client connections.
     */
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {

            logger.info("Server started. Listening for incoming connections...");

            while (isRunning) {
                Socket socket = serverSocket.accept();
                handleClientConnection(socket);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server socket error: {0}", e.getMessage());
        }
    }

    /**
     * Stops the server gracefully.
     */
    public void stopServer() {
        isRunning = false;
        logger.info("Server is stopping...");
    }

    /**
     * Main method to start the server.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.addMessage("A stitch in time saves nine.");
        server.addMessage("Actions speak louder than words.");
        server.addMessage("The early bird catches the worm.");
        server.addMessage("A penny saved is a penny earned.");
        server.addMessage("When in Rome, do as the Romans do.");
        server.addMessage("You can't judge a book by its cover.");
        server.addMessage("A watched pot never boils.");
        server.addMessage("Better late than never.");
        server.addMessage("Don't count your chickens before they hatch.");
        server.addMessage("An apple a day keeps the doctor away.");

        // Start the server in a separate thread
        new Thread(server::startServer).start();

        // Example to stop the server after a specific condition (e.g., after 60 seconds)
        try {
            Thread.sleep(60000); // Server runs for 60 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        server.stopServer();
    }
}
