import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!");

     ServerSocket serverSocket = null;
     Socket clientSocket = null;
     int port = 9092;
     try {
       serverSocket = new ServerSocket(port);
       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);
       // Wait for connection from client.
       clientSocket = serverSocket.accept();
       InputStream inputStream = clientSocket.getInputStream();
       System.out.println("Available bytes in the stream: " + inputStream.available());

       byte[] requestMessage = inputStream.readAllBytes();
       System.out.print("Hex representation: ");
       for (byte b : requestMessage) {
             System.out.printf("%02X ", b); // Format each byte as two uppercase hex digits
       }
       byte[] correlationId = Arrays.copyOfRange(requestMessage, requestMessage.length - 4, requestMessage.length);

       System.out.println("parsed correlationId" + new String(correlationId, "UTF-8"));

       System.out.print("Hex representation: ");
       for (byte b : correlationId) {
           System.out.printf("%02X ", b); // Format each byte as two uppercase hex digits
       }

       OutputStream outputStream = clientSocket.getOutputStream();

       outputStream.write(new byte[] {0,0,0,0});
//       outputStream.write(new byte[] {0,0,0,7});

       outputStream.write(correlationId);
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     } finally {
       try {
         if (clientSocket != null) {
           clientSocket.close();
         }
       } catch (IOException e) {
         System.out.println("IOException: " + e.getMessage());
       }
     }
  }
}
