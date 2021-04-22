import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class HandleWrite {
    private static final Logger userLogger = LogManager.getLogger(HandleWrite.class);

    public static void handleWrite(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        Client client = (Client) key.attachment();
        client.getBuffer().flip();
        Answer answer = UDPSocketServer.getAnswerHandler().getAnswer(client);
        if(answer != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(answer);
            byte[] replyBytes = outputStream.toByteArray();
            ByteBuffer buff = ByteBuffer.wrap(replyBytes);
            channel.send(buff, client.getClientAddress());
            userLogger.info("send answer " + replyBytes.length + " bytes");
        }
        key.interestOps(SelectionKey.OP_READ);
    }
}
