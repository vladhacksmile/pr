/*
* Класс, хранящий в себе улицу и индекс
*/

public class Address {
    private String street; // Строка не может быть пустой, Поле не может быть null
    private String zipCode; // Поле может быть null

    /**
     * Конструктор класса адреса, хранящий в себе улицу и индекс
     * @param street принимает значение улицы (не может быть null и пустым)
     * @param zipCode принимает значение индекса (может быть null)
     * @throws IncorrectValueException если значение улицы null или пустое
     */

    public Address(String street, String zipCode) throws IncorrectValueException {
        setStreet(street);
        setZipCode(zipCode);
    }

    /**
     * Возвращает значение улицы
     * @return значение улицы
     */

    public String getStreet() {
        return street;
    }

    /**
     * Возвращает значение индекса
     * @return значение индекса
     */

    public String getZipCode() {
        return zipCode;
    }

    /**
     * Устанавливает улицу
     * @param street не может быть null и пустым
     * @throws IncorrectValueException если пользователь вводит значение null или пустое
     */

    public void setStreet(String street) throws IncorrectValueException {
        if(!street.isEmpty()) {
            this.street = street;
        } else {
            throw new IncorrectValueException("Поле улицы не может быть null или пустым");
        }
    }

    /**
     * Устанавливает индекс
     * @param zipCode принимает любое значение
     */

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "(" + street + "; " + zipCode + ")";
    }
}