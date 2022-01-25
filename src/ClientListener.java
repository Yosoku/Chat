import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Class paired with a Client to listen for Server responses. It has a thread with a blocking listening method and just
 * prints whatever response the Server has. This solves the problem of listening for UserInput and Server responses ,which are
 * both blocking methods, in the same loop
 */
public class ClientListener implements Runnable{
    private final Socket socket;
    private BufferedReader input;
    public ClientListener(Socket socket){

        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Client Thread that Listens for responses from the Server and prints them to the users screen. The loop uses
     * blocking with readLine()
     */
    @Override
    public void run() {
        System.out.println("Socket Client listener started");
        while (!socket.isClosed()){
            String serverResponse = null;
            try {
                serverResponse = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(serverResponse==null)
                break;
            else
                System.out.println("Server:"+serverResponse);
        }
        System.out.println("In clientListener the socket closed or the input was null");
    }
}
