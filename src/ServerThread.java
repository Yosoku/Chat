import core.Message;
import core.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ServerThread extends Thread {
    private static HashSet<String> usernames = new HashSet<>(); //all the users usernames
    public static int totalMessages = 0;
    public static int totalUsers = 0;
    private int TOKEN = -1;
    private String username = "";
    public static HashMap<String,ArrayList<Message>> allInbox = new HashMap<>();
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerThread(Socket clientSocket) {
        super();
        this.clientSocket = clientSocket;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String request = in.readLine();
                System.out.println("Client:" + request);
                if (request != null) {
                    handleRequest(request);
                } else break; // null request means client DCed
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything();
        }
    }

    private void handleRequest(String requestStr) {
        Request request;
        try {
            request = new Request(requestStr);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
            out.println("Error 400: Invalid Syntax");
            return;
        }
        switch (request.FN_ID) {
            case 1 -> {
                // Create account
                System.out.println("Creating account");
                createAccount(request.params);
            }
            case 2 -> {
                // Show users
                System.out.println("Showing users");
                showUsernames(request.params);
            }
            case 3 -> {
                // Send message
                sendMessage(request.params);
                System.out.println("Sending message");
            }
            case 4 -> {
                // Show inbox
                showInbox(request.params);
                System.out.println("Showing inbox");
            }
            case 5 -> {
                // Read message
                readMessage(request.params);
                System.out.println("Reading message");
            }
            case 6 -> {
                // Delete message
                deleteMessage(request.params);
                System.out.println("Deleting message");
            }

        }

    }

    private void deleteMessage(ArrayList<String> params) {
        if(authenticateToken(Integer.parseInt(params.get(0))))
        {
            int messageId = Integer.parseInt(params.get(1));
            boolean found = false;
            for(Message message : allInbox.get(username))
            {
                if(message.getID() == messageId)
                {
                    found = true;
                    allInbox.get(username).remove(message);
                    out.println("OK");
                    break;
                }
            }
            if(!found)
                out.println("Message " + messageId + "does not exist");
        }else
            out.println("Invalid Auth Token");
    }

    private void readMessage(ArrayList<String> params) {
        if(authenticateToken(Integer.parseInt(params.get(0))))
        {
            int messageId = Integer.parseInt(params.get(1));
            boolean found = false;
            for(Message message : allInbox.get(username))
            {
                if(message.getID() == messageId)
                {
                    found = true;
                    out.println(message.showMessage());
                    break;
                }
            }
            if(!found)
                out.println("Message " + messageId + "does not exist");
        }else
            out.println("Invalid Auth Token");
    }

    private void showInbox(ArrayList<String> params) {
        if(authenticateToken(Integer.parseInt(params.get(0))))
        {
            for(Message message : allInbox.get(username)){
                out.println(message.toString());
            }
        }else
            out.println("Invalid Auth Token");
    }

    private void sendMessage(ArrayList<String> params) {
        if(authenticateToken(Integer.parseInt(params.get(0))))
        {
            String receiver = params.get(1);
            if(usernames.contains(receiver))
            {
                String sender = username;
                StringBuilder body = new StringBuilder();
                for (int i = 2; i <params.size() ; i++) {
                    body.append(params.get(i));
                }
                Message message = new Message(totalMessages++,String.valueOf(body),sender,receiver);
                if(allInbox.get(receiver)==null)
                {
                    ArrayList<Message> temp = new ArrayList<>();
                    temp.add(message);
                    allInbox.put(receiver,temp);
                }else{
                    allInbox.get(receiver).add(message);
                }
                out.println("OK");
            }else
                out.println("User does not exist");

        }else
            out.println("Invalid Auth Token");
    }

    private void createAccount(ArrayList<String> params) {
        username = params.get(0);
        if (username.matches("^[A-Za-z][A-Za-z0-9_]{1,20}$")) {

            if (usernames.add(username)) {
                TOKEN = ++totalUsers;
                out.println(TOKEN);

            } else {
                out.println("Sorry,the User already exists");
            }
        }else
            out.println("Invalid Username");
    }


    /**
     * Helps to keep code clean and avoid the ugliest thing after crocks :  nested try-catches
     */
    private void closeEverything() {
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Close everything encountered issues");
        }
    }


    private boolean authenticateToken(int token){
        return TOKEN == token;
    }
    /**
     * Command FN_ID 4 Show Usernames
     */
    public void showUsernames(ArrayList<String> params) {
        if(authenticateToken(Integer.parseInt(params.get(0)))){
            int i = 1;
            for (String username : usernames)
                out.println(i + ". " + username);
        }else
            out.println("Invalid Auth Token");
    }

}
