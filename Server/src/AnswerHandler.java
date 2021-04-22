import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class AnswerHandler {

    private static Collection collection;
    private String path;
    private static final Logger userLogger = LogManager.getLogger(AnswerHandler.class);

    public AnswerHandler(String path) {
        this.path = path;
        collection = new Collection(path);
    }

    public String getPath() {
        return path;
    }

    public Answer getAnswer(Client client) {
        try {
            byte[] data = client.getIncomingData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            Command command = (Command) is.readObject();
            InetAddress IPAddress = ((InetSocketAddress) client.getClientAddress()).getAddress();
            int port = ((InetSocketAddress) client.getClientAddress()).getPort();
            Object object = null;
            if (command.getOrganization() != null) {
                object = command.getOrganization();
            } else if (command.getUpdate() != null) {
                object = command.getUpdate();
            } else if (command.getRemoveByElement() != null) {
                object = command.getRemoveByElement();
            }
            userLogger.info(IPAddress + ":" + port + " received command " + command.getArgs()[0]);
            if (command.getArgs()[0].equals("connect")) {
               ArrayList<String> arrayList = new ArrayList<>();
               arrayList.add("connected");
               userLogger.info("New user connected " + IPAddress + ":" + port);
               return new Answer(arrayList, false);
            }
            return new Answer(execute(command.getArgs(), object), false);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> execute(String[] trimmedCommand, Object object) {
        // String[] trimmedCommand = command.trim().split(" ", 2);
        ArrayList<String> answer = new ArrayList<>();
        try {
            switch (trimmedCommand[0]) {
                case "": break;
                case "help":
                    answer.add("help - вывести справку по доступным командам\n" +
                            "info - вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                            "show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                            "add {element} - добавить новый элемент в коллекцию\n" +
                            "update id {element} - обновить значение элемента коллекции, id которого равен заданному\n" +
                            "remove_by_id id - удалить элемент из коллекции по его id\n" +
                            "clear - очистить коллекцию\n" +
                            "execute_script file_name - считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                            "exit - завершить программу (без сохранения в файл)\n" +
                            "head - вывести первый элемент коллекции\n" +
                            "remove_greater {element} - удалить из коллекции все элементы, превышающие заданный\n" +
                            "remove_lower {element} - удалить из коллекции все элементы, меньшие, чем заданный\n" +
                            "remove_any_by_type type - удалить из коллекции один элемент, значение поля type которого эквивалентно заданному\n" +
                            "sum_of_annual_turnover - вывести сумму значений поля annualTurnover для всех элементов коллекции\n" +
                            "print_field_ascending_annual_turnover - вывести значения поля annualTurnover всех элементов в порядке возрастания");
                    break;
                case "info":
                    answer.add(collection.info());
                    break;
                case "show":
                    answer.addAll(collection.show());
                    break;
                case "add":
                    if(object != null) {
                        Organization organization = (Organization) object;
                        collection.addToCollectionFromClient(organization);
                        answer.add("Элемент добавлен в коллекцию!");
                    }
                    break;
                case "update":
                    try {
                        Update update = (Update) object;
                        answer.addAll(collection.updateFromClient(update.getId(), update.getField(), update.getValue()));
                    } catch (NumberFormatException e) {
                        answer.add("Ошибка при вводе аргумента. ID должен быть натуральным числом");
                    } catch(NullPointerException e) {
                        answer.add("Произошла ошибка при обновлении элемента (1)");
                    }
                    break;
                case "remove_by_id":
                    try {
                        answer.add(collection.remove_by_id(parseInt(trimmedCommand[1])));
                    } catch (NumberFormatException e) {
                        answer.add("Ошибка при вводе аргумента. ID должен быть натуральным числом");
                    }
                    break;
                case "clear":
                    answer.add(collection.clear());
                    break;
                case "exit":
                    UDPSocketServer.stop();
                    answer.add("Сервер остановлен!");
                    break;
                case "head":
                    answer.add(collection.head());
                    break;
                case "remove_greater":
                    RemoveByElement removeByElementGreater = (RemoveByElement) object;
                    answer.add(collection.remove_greater(removeByElementGreater.getValue()));
                    break;
                case "remove_lower":
                    RemoveByElement removeByElementLower = (RemoveByElement) object;
                    answer.add(collection.remove_lower(removeByElementLower.getValue()));
                    break;
                case "remove_any_by_type":
                    answer.add(collection.remove_any_by_type(trimmedCommand[1]));
                    break;
                case "sum_of_annual_turnover":
                    answer.add(collection.sum_of_annual_turnover());
                    break;
                case "print_field_ascending_annual_turnover":
                    answer.addAll(collection.print_field_ascending_annual_turnover());
                    break;
                default:
                    answer.add("Несуществующая команда. Введите help для справки.");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            answer.add("Отсутствует аргумент команды.");
        }
        return answer;
    }

    public static Collection getCollection() {
        return collection;
    }

}
