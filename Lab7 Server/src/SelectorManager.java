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
    private static Selector selector;
    private static ForkJoinPool forkJoinPool = new ForkJoinPool(10);
    private static ExecutorService executorService = Executors.newCachedThreadPool();

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

                for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
                    SelectionKey key = i.next();
                    i.remove();
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            forkJoinPool.invoke(new HandleRead(key));
                        } else if (key.isWritable()) {
                            new Thread(new HandleWrite(key)).start();
                            key.interestOps(SelectionKey.OP_READ);
                        }
                    }
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
