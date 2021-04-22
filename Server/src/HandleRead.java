import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class HandleRead {
    private static final Logger userLogger = LogManager.getLogger(HandleWrite.class);

    public static void handleRead(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        channel.configureBlocking(false);
        Client client = (Client) key.attachment();
        client.getBuffer().clear();
        client.setClientAddress(channel.receive(client.getBuffer()));

        try {
            client.getCommand();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (client.getClientAddress() != null) {
            userLogger.info(((InetSocketAddress) client.getClientAddress()).getAddress() + ":" + ((InetSocketAddress) client.getClientAddress()).getPort() + " received packet");
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }
}
