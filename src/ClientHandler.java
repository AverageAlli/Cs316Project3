import java.io.*;
import java.net.Socket;

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                String[] parts = request.split(" ", 2);
                String command = parts[0];
                String argument = (parts.length > 1) ? parts[1] : "";

                switch (command) {
                    case "L": // List files
                        out.println(listFiles());
                        break;
                    case "D": // Delete file
                        out.println(deleteFile(argument));
                        break;
                    case "R": // Rename file
                        String[] args = argument.split(" ", 2);
                        if (args.length == 2) {
                            out.println(renameFile(args[0], args[1]));
                        } else {
                            out.println("Invalid rename command");
                        }
                        break;
                    case "U": // Upload file
                        receiveFile(argument, in);
                        out.println("File uploaded successfully");
                        break;
                    case "G": // Download file
                        sendFile(argument, out);
                        break;
                    default:
                        out.println("Invalid command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String listFiles() {
        File dir = new File("server_files");
        String[] files = dir.list();
        return files != null ? String.join("\n", files) : "No files available";
    }

    private String deleteFile(String filename) {
        File file = new File("server_files/" + filename);
        return file.delete() ? "File deleted successfully" : "Failed to delete file";
    }

    private String renameFile(String oldName, String newName) {
        File oldFile = new File("server_files/" + oldName);
        File newFile = new File("server_files/" + newName);
        return oldFile.renameTo(newFile) ? "File renamed successfully" : "Rename failed";
    }

    private void receiveFile(String filename, BufferedReader in) throws IOException {
        try (BufferedWriter fileOut = new BufferedWriter(new FileWriter("server_files/" + filename))) {
            String line;
            while (!(line = in.readLine()).equals("EOF")) {
                fileOut.write(line);
                fileOut.newLine();
            }
        }
    }

    private void sendFile(String filename, PrintWriter out) throws IOException {
        File file = new File("server_files/" + filename);
        if (file.exists()) {
            try (BufferedReader fileIn = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileIn.readLine()) != null) {
                    out.println(line);
                }
                out.println("EOF");
            }
        } else {
            out.println("File not found");
        }
    }
}
