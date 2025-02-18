import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {

    private static boolean validateApiVersion(byte[] version){
        short value = ByteBuffer.wrap(version).getShort();
        return value >= 0 && value <= 4;
    }

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

            byte[] message = inputStream.readNBytes(4);
            byte[] requestApiKey = inputStream.readNBytes(2);
            byte[] requestApiVersion = inputStream.readNBytes(2);
            byte[] correlationId = inputStream.readNBytes(4);


            byte[] errorCode = {0x00, 0x23};
            if (validateApiVersion(requestApiVersion)){
                errorCode = new byte[2];
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(new byte[] {0,1,0,0});
            outputStream.write(correlationId);
            outputStream.write(errorCode);

            //testing
            outputStream.write(requestApiKey);
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
