import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java FileClient <server_ip> <server_port>");
            return;
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(serverIP, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server. Enter a command:");
            String command;
            while ((command = userInput.readLine()) != null) {
                out.println(command);
                String response;
                while ((response = in.readLine()) != null && !response.equals("EOF")) {
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
