import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Класс для работы консольного приложения и инициализаации коллекции
 */

public class Console extends Thread {
    private String command = "";
    private Collection collection;
    private String path;
    private List<String> stackEvent = new ArrayList<>();
    private boolean running = false;
    private DatagramSocket socket;
    private byte[] buf = new byte[1024];

    /**
     * Конструктор для инициализации коллекции
     * @param path путь к CSV файлу коллекции
     */

    public Console(String path) {
        this.path = path;
        collection = new Collection(path);
        try {
            socket = new DatagramSocket(4445);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            buf = new byte[16384];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Server received " + received);
            String k;
            k = execute(received);
            System.out.println(k);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (received.equals("exit")) {
                running = false;
                continue;
            }
            try {
                //socket.send(packet);
                System.out.println(k.length());
                socket.send(new DatagramPacket(String.valueOf(k).getBytes(), k.getBytes().length, address, port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    /**
     * Обработчик команд
     * @param command команда в строковом представлении
     */

    public String execute(String command) {
        String[] trimmedCommand = command.trim().split(" ", 2);
        try {
            switch (trimmedCommand[0]) {
                case "": break;
                case "help":
                    return ("help - вывести справку по доступным командам\n" +
                            "info - вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                            "show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                            "add {element} - добавить новый элемент в коллекцию\n" +
                            "update id {element} - обновить значение элемента коллекции, id которого равен заданному\n" +
                            "remove_by_id id - удалить элемент из коллекции по его id\n" +
                            "clear - очистить коллекцию\n" +
                            "save - сохранить коллекцию в файл\n" +
                            "execute_script file_name - считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                            "exit - завершить программу (без сохранения в файл)\n" +
                            "head - вывести первый элемент коллекции\n" +
                            "remove_greater {element} - удалить из коллекции все элементы, превышающие заданный\n" +
                            "remove_lower {element} - удалить из коллекции все элементы, меньшие, чем заданный\n" +
                            "remove_any_by_type type - удалить из коллекции один элемент, значение поля type которого эквивалентно заданному\n" +
                            "sum_of_annual_turnover - вывести сумму значений поля annualTurnover для всех элементов коллекции\n" +
                            "print_field_ascending_annual_turnover - вывести значения поля annualTurnover всех элементов в порядке возрастания");
                case "info":
                    collection.info();
                    break;
                case "show":
                    collection.show();
                    break;
                case "add":
                    collection.add();
                    break;
                case "update":
                    try {
                       // collection.update(parseInt(trimmedCommand[1]));
                    } catch (NumberFormatException e) {
                        printer("Ошибка при вводе аргумента. ID должен быть натуральным числом");
                    }
                    break;
                case "remove_by_id":
                    try {
                        collection.remove_by_id(parseInt(trimmedCommand[1]));
                    } catch (NumberFormatException e) {
                        printer("Ошибка при вводе аргумента. ID должен быть натуральным числом");
                    }
                    break;
                case "clear":
                    return collection.clear();
                case "save":
                    collection.save(path);
                    break;
                case "exit":
                    stopIt();
                    println("Вызвана команда выхода из программы без сохранения");
                    break;
                case "execute_script":
                    execute_script(trimmedCommand[1]);
                    break;
                case "head":
                    return collection.head();
                case "remove_greater":
                    //collection.remove_greater();
                    break;
                case "remove_lower":
                    //collection.remove_lower();
                    break;
                case "remove_any_by_type":
                    collection.remove_any_by_type(trimmedCommand[1]);
                    break;
                case "sum_of_annual_turnover":
                    collection.sum_of_annual_turnover();
                    break;
                case "print_field_ascending_annual_turnover":
                    collection.print_field_ascending_annual_turnover();
                    break;
                default:
                    return "Несуществующая команда. Введите help для справки.";
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            printer("Отсутствует аргумент команды.");
            return "Отсутствует аргумент команды.";
        } catch(IOException e) {
            System.err.println("Ошибка при записи файла!");
            return "Ошибка при записи файла!";
        }
        return "какая-то хуня";
    }

    /**
     * Обработчик команд с контекстным меню (используется для обработки скрипта)
     * @param command команда в строковом представлении
     * @param bufferedReader сканнер
     */

    public String execute(String command, BufferedReader bufferedReader) {
        String[] trimmedCommand = command.trim().split(" ", 2);
        try {
            switch (trimmedCommand[0]) {
                case "add":
                    collection.add(bufferedReader);
                    break;
                case "update":
                    try {
                        collection.update(parseInt(trimmedCommand[1]), bufferedReader);
                    } catch (NumberFormatException e) {
                        printer("Ошибка при вводе аргумента. ID должен быть натуральным числом! Проверьте скрипт!");
                    }
                    break;
                case "remove_greater":
                    collection.remove_greater(bufferedReader);
                    break;
                case "remove_lower":
                    collection.remove_lower(bufferedReader);
                    break;
                default:
                    printer("Несуществующая команда. Введите help для справки. Проверьте скрипт1");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            printer("Отсутствует аргумент команды. Проверьте скрипт!");
        }
        return "из скриптера";
    }

    /**
     * Запуск консоли, ожидающей ввода команды
     * При вызове команды exit, останавливается
     */
    /*public void run() {
        println("Консоль запущена...");
        running = true;
        try(Scanner scanner = new Scanner(System.in)) {
            while(running) {
                command = scanner.nextLine();
                execute(command);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Программа завершена по требованию пользователя");
        }
    }*/

    public void stopIt(){
        running = false;
    }

    /**
     * Выполнение консольных команд записанных в файл
     * @param path путь к файлу, содержащему консольные команды
     */

    public void execute_script(String path) {
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String cmd;
        stackEvent.add(path);
        try {
            File file = new File(path);
            if(file.exists() && !file.canWrite()) {
                throw new FileAccessException("Нет прав на запись файла!");
            }
            inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            while ((cmd = bufferedReader.readLine()) != null) {
                String[] trimmedCommand = cmd.trim().split(" ", 2);
                try {
                    if (trimmedCommand[0].equals("execute_script")) {
                        for (String s : stackEvent) {
                            if (trimmedCommand[1].equals(s)) throw new RecursionException("Рекурсия запрещена!");
                        }
                    }
                    if (trimmedCommand[0].equals("add") || trimmedCommand[0].equals("update") || trimmedCommand[0].equals("remove_greater") || trimmedCommand[0].equals("remove_lower")) {
                        execute(cmd, bufferedReader);
                    } else {
                        execute(cmd);
                    }
                } catch (RecursionException e) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    System.err.println(e.getMessage());
                }
            }
        } catch(UnsupportedEncodingException e){
            System.err.println("Неподдерживаемая кодировка!");
        } catch(FileNotFoundException e){
            System.err.println("Файл не найден! Проверьте название файла и повторите попытку!");
        } catch(IOException e) {
            System.err.println("Ошибка при чтении файла!");
        } catch (FileAccessException e) {
            System.err.println(e.getMessage());
        } finally {
            stackEvent.remove(stackEvent.size() - 1);
        }
    }

    /**
     * Вывод текста в консоль
     * @param text текст, который будет выведен в консоль
     */

    public void println(String text) {
        System.out.println(text);
    }

    /**
     * Вывод ошибки в консоль
     * @param error ошибка, которая будет выведена в консоль
     */

    public void printer(String error) {
        System.err.println(error);
    }

    /**
     * Возврщает объект коллекции
     * @return коллекция
     */

    public Collection getCollection() {
        return collection;
    }

    /**
     * Возврщает путь к CSV файлу коллекции, который был использован при инициализации
     * @return путь к CSV файлу коллекции
     */

    public String getPath() {
        return path;
    }
}