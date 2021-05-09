import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger rootLogger = LogManager.getRootLogger();
    private static final Logger userLogger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        userLogger.trace("Получен путь src/data.csv. Начинаем инициализацию.");
        UDPSocketServer server = new UDPSocketServer("src/data.csv");
        server.start();
    }
}
