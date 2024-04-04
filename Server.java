import java.io.*;
import java.net.*;
import java.util.Date;

public class Server {
    private static final int PORT = 12345;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection established with client.");

            // Greet the client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Hello from server!");

            // Receive greeting from client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Client says: " + in.readLine());

            // Receive file from client
            byte[] buffer = new byte[BUFFER_SIZE];
            InputStream fileInputStream = clientSocket.getInputStream();
            String filename = in.readLine();
            FileOutputStream fileOutputStream = new FileOutputStream(filename);

            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("File received successfully: " + filename);

            // Send confirmation to client
            out.println("File received successfully.");

            // Log file details
            File receivedFile = new File(filename);
            long fileSize = receivedFile.length();
            long lastModified = receivedFile.lastModified();
            Date lastModifiedDate = new Date(lastModified);
            System.out.println("File Details:");
            System.out.println("Name: " + filename);
            System.out.println("Size: " + fileSize + " bytes");
            System.out.println("Last Modified Date: " + lastModifiedDate);

            // Perform parsing and manipulations
            performManipulations(filename);

            // Close the connection
            fileOutputStream.close();
            fileInputStream.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void performManipulations(String filename) {
        try {
            // Read the content of the file
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            int wordCount = 0;
            while ((line = reader.readLine()) != null) {
                // Convert line to uppercase
                String upperCaseLine = line.toUpperCase();
                contentBuilder.append(upperCaseLine).append("\n"); // Append modified line to contentBuilder

                // Count number of words
                String[] words = line.split("\\s+");
                wordCount += words.length;
            }
            reader.close();

            // Write the modified content and word count to a separate file
            String resultFilename = "manipulation_results.txt";
            FileWriter writer = new FileWriter(resultFilename);
            writer.write("Uppercase content:\n" + contentBuilder.toString() + "\n"); // Write modified content
            writer.write("Word count: " + wordCount);
            writer.close();

            System.out.println("Uppercase content and word count saved to: " + resultFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
