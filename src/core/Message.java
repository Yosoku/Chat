package core;

import java.util.Objects;

public class Message {
    private int ID;
    private String body;
    private boolean read;
    private String sender;
    private String receiver;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Message(int ID, String body, String sender, String receiver) {
        this.ID = ID;
        this.body = body;
        this.sender = sender;
        this.receiver = receiver;
        this.read = false;
    }

    public String showMessage(){
        read = true;
        return "(" + sender + ")"+ " " + body + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return ID == message.ID && read == message.read && Objects.equals(body, message.body) && Objects.equals(sender, message.sender) && Objects.equals(receiver, message.receiver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, body, read, sender, receiver);
    }

    @Override
    public String toString() {
         return read? ID+". from: " + sender : ID+". from: " + sender +"*";
    }
}
