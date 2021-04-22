import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class UDPSocketClient {

    public static DatagramChannel channel;
    public static boolean runnable = true;
    public static int PORT = 50001;
    public static InetAddress IPAddress;
    private static List<String> stackEvent = new ArrayList<>();
    private static Collection collection = new Collection("");
    private static SocketAddress serverAddress = new InetSocketAddress("localhost",
            PORT);
    private Scanner scanner = new Scanner(System.in);
    private static boolean init = false;
    public static boolean connected = false;

    public static Object lastCommand = null;

    public boolean init() {
            if (init && runnable) {
                System.err.println("Клиент уже запущен!");
            } else {
                connect();
                new Thread(new ResponseHandler()).start();
                runnable = true;
                init = true;
            }
        return runnable;
    }

    public static boolean connect() {
        try {
            IPAddress = InetAddress.getByName("localhost");
            System.out.println("Попытка подключиться к клиенту " + IPAddress + ":" + PORT);
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.connect(serverAddress);
            String[] connect = {"connect"};
            send(new Command(connect, null));
            runnable = true;
            return true;
        }  catch(PortUnreachableException e) {
            System.err.println("Не удалось подключиться к серверу!");
        } catch (SocketException e) {
            System.err.println("Ошибка сокета!");
            runnable = false;
        } catch (UnknownHostException e) {
            System.err.println("Не удалось подключиться к хосту!");
            runnable = false;
        } catch (IOException e) {
            System.err.println("Ошибка при подключении к серверу!");
            runnable = false;
        }
        return false;
    }

    public void createAndListenSocket() {
        while(runnable) {
            try {
                if (!init) {
                    init();
                    continue;
                }
                Object object = execute(scanner.nextLine());
                if (object != null) {
                    // if (connected) {
                        send(object);
                   /* } else {
                        System.err.println("Нет смысла ничего отправлять, так как не удалось установить соединение с сервером!");
                    } */
                }
            } catch (NoSuchElementException e) {
                System.out.println("Программа завершена по требованию пользователя!");
                runnable = false;
            }
        }
    }

    public static void send(Object object) {
        try {
            if(!((Command) object).getArgs()[0].equals("connect")) {
                lastCommand = object;
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(object);
            ByteBuffer buffer = ByteBuffer.wrap(outputStream.toByteArray());
            // System.out.println(((InetSocketAddress) serverAddress).getPort());
            channel.send(buffer, serverAddress);
        } catch(IOException e) {
            System.err.println("Произошла ошибка при отправке запроса!");
            runnable = false;
        }
    }

    public Object execute(String cmd, BufferedReader bufferedReader) {
        String[] trimmedCommand = cmd.trim().split(" ", 2);
        Object command = null;
        try {
            switch (trimmedCommand[0]) {
                case "": break;
                case "help":
                case "info":
                case "show":
                case "clear":
                case "exit":
                case "head":
                case "sum_of_annual_turnover":
                case "print_field_ascending_annual_turnover":
                    command = new Command(trimmedCommand, cmd);
                    break;
                case "remove_by_id":
                    if(parseInt(trimmedCommand[1]) > 0) {
                        command = new Command(trimmedCommand, cmd);
                    } else {
                        System.err.println("ID должен быть больше 0!");
                    }
                    break;
                case "remove_any_by_type":
                    if(Utils.StrToType(trimmedCommand[1]) != null) {
                        command = new Command(trimmedCommand, cmd);
                    } else {
                        System.err.println("Указан неверный тип!");
                    }
                    break;
                case "add":
                    command = new Command(trimmedCommand, collection.add(bufferedReader));
                    break;
                case "update":
                    if (parseInt(trimmedCommand[1]) > 0) {
                        Object valueObject = null;
                        String field = ConsoleEvent.update(bufferedReader);
                        switch (field) {
                            case "name":
                                valueObject = ConsoleEvent.getName(bufferedReader);
                                break;
                            case "coordinates":
                                valueObject = ConsoleEvent.getCoordinates(bufferedReader);
                                break;
                            case "annualturnover":
                                valueObject = ConsoleEvent.getAnnualTurnover(bufferedReader);
                                break;
                            case "type":
                                valueObject = ConsoleEvent.getType(bufferedReader);
                                break;
                            case "address":
                                valueObject = ConsoleEvent.getAddress(bufferedReader);
                                break;
                            case "exit":
                                System.out.println("Обновление поля было отменено пользователем!");
                                break;
                            default:
                                System.err.println("Произошла ошибка при обновлении поля!");
                                break;
                        }
                        command = new Command(trimmedCommand, new Update(parseInt(trimmedCommand[1]), field, valueObject));
                    } else {
                        System.err.println("ID должен быть больше 0!");
                    }
                    break;
                case "remove_greater":
                    command = new Command(trimmedCommand, new RemoveByElement(ConsoleEvent.getAnnualTurnover(bufferedReader), true));
                    break;
                case "remove_lower":
                    command = new Command(trimmedCommand, new RemoveByElement(ConsoleEvent.getAnnualTurnover(bufferedReader), false));
                    break;
                default:
                    System.err.println("Несуществующая команда. Введите help для справки.");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Не указан аргумент!");
        } catch (NumberFormatException e) {
            System.err.println("Ошибка при вводе аргумента. ID должен быть натуральным числом");
        }
        return command;
    }

    public Object execute(String cmd) {
        String[] trimmedCommand = cmd.trim().split(" ", 2);
        Object command = null;
        try {
            switch (trimmedCommand[0]) {
                case "": break;
                case "help":
                case "info":
                case "show":
                case "clear":
                case "head":
                case "sum_of_annual_turnover":
                case "print_field_ascending_annual_turnover":
                    command = new Command(trimmedCommand, cmd);
                    break;
                case "remove_by_id":
                    if (parseInt(trimmedCommand[1]) > 0) {
                        command = new Command(trimmedCommand, cmd);
                    } else {
                        System.err.println("ID должен быть больше 0!");
                    }
                    break;
                case "remove_any_by_type":
                    if(Utils.StrToType(trimmedCommand[1]) != null){
                        command = new Command(trimmedCommand, cmd);
                    } else {
                        System.err.println("Указан неверный тип!");
                    }
                    break;
                case "add":
                    command = new Command(trimmedCommand, collection.add());
                    break;
                case "exit":
                    System.out.println("Вызвана команда закрытия программы");
                    System.exit(0);
                    break;
                case "update":
                    try {
                        if (parseInt(trimmedCommand[1]) > 0) {
                            Object valueObject = null;
                            String field = ConsoleEvent.update();
                            switch (field) {
                                case "name":
                                    valueObject = ConsoleEvent.getName();
                                    break;
                                case "coordinates":
                                    valueObject = ConsoleEvent.getCoordinates();
                                    break;
                                case "annualturnover":
                                    valueObject = ConsoleEvent.getAnnualTurnover();
                                    break;
                                case "type":
                                    valueObject = ConsoleEvent.getType();
                                    break;
                                case "address":
                                    valueObject = ConsoleEvent.getAddress();
                                    break;
                                case "exit":
                                    System.out.println("Обновление поля было отменено пользователем!");
                                    break;
                                default:
                                    System.err.println("Произошла ошибка при обновлении поля!");
                                    break;
                            }
                            command = new Command(trimmedCommand, new Update(parseInt(trimmedCommand[1]), field, valueObject));
                        } else {
                            System.err.println("ID должен быть больше 0!");
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Не указан аргумент!");
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка при вводе аргумента. ID должен быть натуральным числом");
                    }
                    break;
                case "execute_script":
                    execute_script(trimmedCommand[1]);
                    break;
                case "remove_greater":
                    command = new Command(trimmedCommand, new RemoveByElement(ConsoleEvent.getAnnualTurnover(), true));
                    break;
                case "remove_lower":
                    command = new Command(trimmedCommand, new RemoveByElement(ConsoleEvent.getAnnualTurnover(), false));
                    break;
                default:
                    System.err.println("Несуществующая команда. Введите help для справки.");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Не указан аргумент!");
        } catch (NumberFormatException e) {
            System.err.println("Ошибка при вводе аргумента. ID должен быть натуральным числом");
        }
        return command;
    }

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
                        send(execute(cmd, bufferedReader));
                    } else {
                        send(new Command(trimmedCommand, null));
                    }
                } catch (RecursionException e) {
                    System.out.println(e.getMessage());
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
}