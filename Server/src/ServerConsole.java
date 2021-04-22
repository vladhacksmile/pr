import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerConsole extends Thread {
    private static final Logger userLogger = LogManager.getLogger(ServerConsole.class);

    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);
        userLogger.info("Серверная консоль запущена!");
        try {
            while (UDPSocketServer.isRunning()) {
                executeServer(scanner.nextLine());
            }
        } catch (NoSuchElementException e) {
           UDPSocketServer.stop();
        }
    }

    public void executeServer(String command) {
        String[] trimmedCommand = command.trim().split(" ", 2);
        if (AnswerHandler.getCollection() != null) {
            switch (trimmedCommand[0]) {
                case "help":
                    System.out.println("Доступные команды на сервере:\n" +
                            "save - сохранить коллекцию со стандартным путем\n" +
                            "save path - сохранить коллекцию с указанным путем\n" +
                            "exit - завершить работу сервера и сохранить коллекцию со стандартным путем\n" +
                            "help - вывод всех доступных команд на сервере");
                    break;
                case "save":
                    try {
//                        System.out.println(Arrays.toString(Arrays.stream(trimmedCommand).toArray()));
                        if (trimmedCommand.length == 2) {
                            AnswerHandler.getCollection().save(trimmedCommand[1]);
                        } else {
                            AnswerHandler.getCollection().save(UDPSocketServer.getAnswerHandler().getPath());
                       }
                    } catch (IOException e) {
                        System.out.println("Произошла ошибка при сохранении файла!");
                    }
                    break;
                case "exit":
                    UDPSocketServer.stop();
                    break;
                case "":
                    break;
                default:
                    System.err.println("Введена некорректная команда");
                    break;
            }
        } else {
            System.err.println("Коллекция не инициализированна!");
        }
    }
}