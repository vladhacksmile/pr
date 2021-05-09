import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferOverflowException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.RecursiveTask;

public class HandleRead extends RecursiveTask {
    private static final Logger userLogger = LogManager.getLogger(HandleWrite.class);
    private SelectionKey key;

    public HandleRead(SelectionKey key) {
        this.key = key;
    }

    @Override
    protected Object compute() {
        Client client = null;
        try {
            DatagramChannel channel = (DatagramChannel) key.channel();
            channel.configureBlocking(false);
            client = (Client) key.attachment();
            client.getBuffer().clear();
            client.setClientAddress(channel.receive(client.getBuffer()));

            client.getCommand();

            if (client.getClientAddress() != null) {
                userLogger.info(((InetSocketAddress) client.getClientAddress()).getAddress() + ":" + ((InetSocketAddress) client.getClientAddress()).getPort() + " received packet");
                key.interestOps(SelectionKey.OP_WRITE);
            }
        } catch (IllegalArgumentException | ClassNotFoundException | BufferOverflowException | IOException e) {
            userLogger.fatal(e.getMessage());
        }
        return client;
    }
}
