import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class SelectorManager {
    private static final Selector selector;
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(10);
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    static {
        selector = UDPSocketServer.getSelector();
    }

    public static void run() {
        while(UDPSocketServer.isRunning()) {
            try {
                if (selector.select(3000) == 0) {
                    continue;
                }
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            Client client = (Client) forkJoinPool.invoke(new HandleRead(key));
                            new Thread(new HandleWrite(key, client)).start();
                        }
                        if (key.isWritable()) {
                            key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                    iter.remove();
                }
            } catch (OutOfMemoryError | IllegalArgumentException | BufferOverflowException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
        executorService.shutdown();
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}
