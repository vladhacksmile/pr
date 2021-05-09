import java.io.*;
import java.text.SimpleDateFormat;

/**
 * Класс для работы с коллекцией PriorityQueue "Очередь с приоритетом"
 */

public class Collection {
    private static SimpleDateFormat format = new SimpleDateFormat("dd.MMM.yyyy HH:mm:ss");

    /**
     * Контекстное меню для добавления нового элемента в коллекцию (с заполнением полей) и вывода информации о новом элементе
     */
    public Organization add() {
        System.out.println("Для добавления нового элемента в коллекцию заполните значения полей:");
        try {
            return new Organization(ConsoleEvent.getName(), ConsoleEvent.getCoordinates(), ConsoleEvent.getAnnualTurnover(), ConsoleEvent.getType(), ConsoleEvent.getAddress());
        } catch (IncorrectValueException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Контекстное меню для добавления нового элемента в коллекцию (с заполнением полей) и вывода информации о новом элементе
     * @param bufferedReader сканнер
     */
    public Organization add(BufferedReader bufferedReader) {
        System.out.println("Для добавления нового элемента в коллекцию заполните значения полей:");
        try {
            return new Organization(ConsoleEvent.getName(bufferedReader), ConsoleEvent.getCoordinates(bufferedReader), ConsoleEvent.getAnnualTurnover(bufferedReader), ConsoleEvent.getType(bufferedReader), ConsoleEvent.getAddress(bufferedReader));
        } catch (IncorrectValueException e) {
            System.err.println(e.getMessage());
        } catch(NullPointerException e) {
            System.err.println("\nВсе поля должны быть введены корректно! Проверьте скрипт!");
        }
        return null;
    }

    public static SimpleDateFormat getFormat() {
        return format;
    }
}