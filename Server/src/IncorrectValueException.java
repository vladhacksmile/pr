/**
 * Класс для вывода ошибки о некорректном значениии
 */

public class IncorrectValueException extends Exception {
    /**
     * Конструктор класса для вывода ошибки о некорректном значениии
     * @param error текст ошибки
     */
    public IncorrectValueException(String error) {
        super(error);
    }
}
