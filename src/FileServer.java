import java.io.*;
import java.net.*;

public class FileServer {
    private static final int PORT = 12345;  // Set port number
    private static final String DIRECTORY = "server_files"; // Directory for storing files

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Thread for handling client requests