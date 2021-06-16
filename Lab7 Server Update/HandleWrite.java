import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutionException;

public class HandleWrite implements Runnable {
    private static final Logger userLogger = LogManager.getLogger(HandleWrite.class);
    private SelectionKey key;
    private Client client;

    public HandleWrite(SelectionKey key, Client client) {
        this.key = key;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            DatagramChannel channel = (DatagramChannel) key.channel();
            Client client = this.client;
            client.getBuffer().flip();
            Answer answer = SelectorManager.getExecutorService().submit(() -> UDPSocketServer.getAnswerHandler().getAnswer(client)).get();
            if(answer != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(answer);
                byte[] replyBytes = outputStream.toByteArray();
                ByteBuffer buff = ByteBuffer.wrap(replyBytes);
                channel.send(buff, client.getClientAddress());
                userLogger.info("send answer " + replyBytes.length + " bytes");
            }
        } catch (OutOfMemoryError | IOException | ExecutionException | InterruptedException e) {
            userLogger.error(e.getMessage());
        }
    }
}
