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
    private static RequestHandler requestHandler = new RequestHandler();
    private static ResponseHandler responseHandler = new ResponseHandler();
    private static AnswerHandler answerHandler;
    static final Logger userLogger = LogManager.getLogger(UDPSocketServer.class);
    private static Selector selector;
    public UDPSocketServer(String path) {
        this.path = path;
    }

    public void start() {
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
                answerHandler = new AnswerHandler(path);
                new Thread(new ServerConsole()).start();
                datagramChannel.register(selector, SelectionKey.OP_READ, new Client());
                userLogger.info("Сервер слушает " + PORT + " порт");
                SelectorManager.run();
            } catch (SocketException e) {
                System.err.println("Ошибка сокета!");
            } catch (ClosedChannelException e) {
                System.err.println("Канал закрыт!");
            } catch (IOException e) {
                System.err.println("Произошла ошибка при запуске сервера!");
            }
        }
    }

    public static void stop() {
        try {
            AnswerHandler.getCollection().save(path);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при сохранении файла!");
        }
        System.out.println("Программа завершена по требованию пользователя!");
        running = false;
    }

    public static DatagramSocket getSocket() {
        return socket;
    }

    public static RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public static ResponseHandler getResponseHandler() {
        return responseHandler;
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
}