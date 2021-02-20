import java.io.*;
import java.nio.charset.StandardCharsets;
import static java.lang.Integer.parseInt;
import java.util.Scanner;

/**
 * Класс для работы консольного приложения и инициализаации коллекции
 */

public class Console {
    private String command = "";
    private final Collection collection;
    private final String path;

    /**
     * Конструктор для инициализации коллекции
     * @param path путь к CSV файлу коллекции
     */

    public Console(String path) {
        this.path = path;
        collection = new Collection(path);
    }

    /**
     * Обработчик команд
     * @param command команда в строковом представлении
     */

    public void execute(String command) {
        String[] trimmedCommand = command.trim().split(" ", 2);
        try {
            switch (trimmedCommand[0]) {
                case "": break;
                case "help":
                    println("help - вывести справку по доступным командам\n" +
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
                    break;
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
                        collection.update(parseInt(trimmedCommand[1]));
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
                    collection.clear();
                    break;
                case "save":
                    collection.save(path);
                    break;
                case "exit":
                    println("Вызвана команда выхода из программы без сохранения");
                    break;
                case "execute_script":
                    execute_script(trimmedCommand[1]);
                    break;
                case "head":
                    collection.head();
                    break;
                case "remove_greater":
                    collection.remove_greater();
                    break;
                case "remove_lower":
                    collection.remove_lower();
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
                    printer("Несуществующая команда. Введите help для справки.");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            printer("Отсутствует аргумент команды.");
        } catch(IOException e) {
            System.err.println("Ошибка при записи файла!");
        }
    }

    /**
     * Запуск консоли, ожидающей ввода команды
     * При вызове команды exit, останавливается
     */

    public void run() {
        println("Консоль запущена...");
        try(Scanner scanner = new Scanner(System.in)) {
            while(!command.equals("exit")) {
                command = scanner.nextLine();
                execute(command);
            }
        }
    }

    /**
     * Выполнение консольных команд записанных в файл
     * @param path путь к файлу, содержащему консольные команды
     */

    public void execute_script(String path) {
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String cmd;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            while ((cmd = bufferedReader.readLine()) != null) {
                execute(cmd);
            }
            } catch(UnsupportedEncodingException e){
                System.err.println("Неподдерживаемая кодировка!");
            } catch(FileNotFoundException e){
                System.err.println("Файл не найден! Проверьте название файла и повторите попытку!");
            } catch(IOException e) {
                System.err.println("Ошибка при чтении файла!");
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
