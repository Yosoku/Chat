import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {

        Socket socket;
        PrintWriter out = null;


        try {
            // the first argument is the ip address of the server
            // while the second one is its port
            socket = new Socket("localhost", 1234);
            out = new PrintWriter(socket.getOutputStream(), true);
            Thread inputThread = new Thread(new ClientListener(socket));
            inputThread.start();
            System.out.println("Socket initiated at " + socket.getInetAddress().getHostAddress());
        } catch (Exception e) {
            System.err.println("Could not initiate a connection to server");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String message = stdIn.readLine();
            if (message.equals("exit"))
                break;
            out.println(message);
        }


        System.out.println("All closed bye bitch");
    }
}