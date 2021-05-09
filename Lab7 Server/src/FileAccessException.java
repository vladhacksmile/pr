/**
 * Класс для вывода ошибки о доступе к файлам
 */

public class FileAccessException extends Exception {
    /**
     * Конструктор класса для вывода ошибок доступа к файлам
     * @param error текст ошибки
     */
    public FileAccessException(String error) {
        super(error);
    }
}
