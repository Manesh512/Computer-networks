import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 12345;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, PORT);

            // Greet the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hello from client!");

            // Receive greeting from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Server says: " + in.readLine());

            // Prompt user for filename
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter filename to send: ");
            String filename = userInput.readLine();
            out.println(filename);

            // Send file to server
            FileInputStream fileInputStream = new FileInputStream(filename);
            OutputStream outputStream = socket.getOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("File sent successfully: " + filename);

            fileInputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}