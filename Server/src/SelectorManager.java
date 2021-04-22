import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class SelectorManager {
    private static Selector selector;

    static {
        selector = UDPSocketServer.getSelector();
    }

    public static void run() {
        while(UDPSocketServer.isRunning()){
            try {
                if (selector.select(3000) == 0) {
                    continue;
                }
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();

                for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
                    SelectionKey key = i.next();
                    i.remove();
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            HandleRead.handleRead(key);
                        } else if (key.isWritable()) {
                            HandleWrite.handleWrite(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
