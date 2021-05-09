import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class Client {
    private SocketAddress clientAddress;
    private byte[] incomingData = new byte[65515];
    private ByteBuffer buffer = ByteBuffer.wrap(incomingData);
    private boolean isConnected = false;

    public Command getCommand() throws IOException, ClassNotFoundException {
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

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public byte[] getIncomingData() {
        return incomingData;
    }

    public SocketAddress getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(SocketAddress clientAddress) {
        this.clientAddress = clientAddress;
    }
}