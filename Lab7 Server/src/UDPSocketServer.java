import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class UDPSocketServer {
    private static int PORT = 50001;
    private static boolean running;
    private static String path;
    private static DatagramSocket socket;
    private static AnswerHandler answerHandler;
    static final Logger userLogger = LogManager.getLogger(UDPSocketServer.class);
    private static Selector selector;
    private static DatabaseManager databaseManager = null;

    public UDPSocketServer(String path) {
        this.path = path;
    }

    public static void start() {
        if(running) {
            userLogger.error("Сервер уже запущен!");
        } else {
            try {
                running = true;
                DatagramChannel datagramChannel = DatagramChannel.open();
                datagramChannel.configureBlocking(false);
                datagramChannel.bind(new InetSocketAddress(PORT));
                socket = datagramChannel.socket();
                selector = Selector.open();
                databaseManager = new DatabaseManager();
                if(databaseManager.connect()) {
                    answerHandler = new AnswerHandler(path);
                    new Thread(new ServerConsole()).start();
                    datagramChannel.register(selector, SelectionKey.OP_READ, new Client());
                    userLogger.info("Сервер слушает " + PORT + " порт");
                    SelectorManager.run();
                }
            } catch (SocketException e) {
                userLogger.fatal("Ошибка сокета!");
            } catch (ClosedChannelException e) {
                userLogger.fatal("Канал закрыт!");
            } catch (IOException e) {
                userLogger.fatal("Произошла ошибка при запуске сервера!");
            }
        }
    }

    public static void stop() {
        try {
            AnswerHandler.getCollection().save(path);
        } catch (IOException e) {
            userLogger.error("Произошла ошибка при сохранении файла!");
        }
        userLogger.info("Программа завершена по требованию пользователя!");
        running = false;
    }

    public static DatagramSocket getSocket() {
        return socket;
    }

    public static AnswerHandler getAnswerHandler() {
        return answerHandler;
    }

    public static boolean isRunning() {
        return running;
    }

    public static Selector getSelector() {
        return selector;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}