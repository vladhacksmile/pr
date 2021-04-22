import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class ClientRecord {
    public SocketAddress clientAddress;
    public ByteBuffer buffer;
    byte[] incomingData = new byte[65634];
    private boolean isConnected = false;

    public ClientRecord() {
        buffer = ByteBuffer.wrap(incomingData);
    }

    public Command blyat() throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(incomingData);
        ObjectInputStream is = new ObjectInputStream(in);
        Command command = (Command) is.readObject();
        if(command.getArgs()[0].equals("connect")) {
            isConnected = true;
        }
        return command;
    }

    public boolean isConnected() {
        return isConnected;
    }
}