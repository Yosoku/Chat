import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class responsible for Listening to a Socket and wait for Clients to Connect to it,after the Connection the Server
 * creates a Thread for the Client to speak to and goes back to Listening for more clients to connect
 */
public class Server extends Thread{
    private ServerSocket serverSocket;

    public Server(int port) {
        super();
        try {
            serverSocket = new ServerSocket(port); // Port to Listen to
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Server Thread that Listens for Clients to connect at its port and creates a handler Thread for them. The loop uses
     * blocking with server.accept()
     */
    @Override
    public void run() {
        System.out.println("Server is now running at PORT:" + serverSocket.getLocalPort());
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Connection could not be accepted");
                System.exit(1);
            }
            System.out.println("Client " + socket.getInetAddress().getHostAddress() + " connected");
            ServerThread thread = new ServerThread(socket);
            thread.start();
            System.out.println("ServerThread is now handling the Client at "+ socket.getInetAddress().getHostAddress());
        }
    }

    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
        // 1 create account
        // 2 show users
        // 3 send message
        // 4 show inbox
        // 5 read message
        // 6 delete message

    }
}
