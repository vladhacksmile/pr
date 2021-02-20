import org.apache.commons.csv.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static java.lang.Integer.parseInt;

/**
 * Класс для работы с коллекцией PriorityQueue "Очередь с приоритетом"
 */

public class Collection {
    private final PriorityQueue<Organization> collection = new PriorityQueue<>();
    private final Date date;

    /**
     * Конструктор класса для работы с коллекцией PriorityQueue "Очередь с приоритетом", который инициализирует коллекцию
     * @param path путь к CSV файлу коллекции
     */
    public Collection(String path) {
        load(path);
        date = new Date();
    }

    /**
     * Загрузка коллекции из файла и добавление загруженных элементов в коллекцию
     * @param path путь к CSV файлу коллекции
     */
    public void load(String path) {
        CSVParser reader;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден!");
        } catch (NullPointerException e){
            System.err.println("Произошла ошибка при загрузке файла! Путь к файлу не может быть null или пустым!");
        }
        try {
            if(bufferedReader != null) {
                reader = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .parse(bufferedReader);
                for(CSVRecord record: reader) {
                    String name = record.get("name");
                    String cordX = record.get("cordX");
                    String cordY = record.get("cordY");
                    String annualTurnover = record.get("annualTurnover");
                    String typeofOrganization = (record.get("type")).toUpperCase();
                    String street = record.get("street");
                    String zipcode = record.get("zipcode");

                    try {
                        Coordinates coordinates = null;
                        try {
                            coordinates = new Coordinates(Float.parseFloat(cordX), Float.parseFloat(cordY));
                        } catch(NumberFormatException e) {
                            System.err.println("При чтении координат произошла ошибка! Проверьте формат ввода! Координаты должны быть представлены в Float формате!");
                        }

                        OrganizationType type = Utils.StrToType(typeofOrganization);

                        Address address = null;
                        if(!street.isEmpty()) {
                            address = new Address(street, zipcode);
                        }

                        int annualTurnoverInt = 0;
                        try {
                            annualTurnoverInt = parseInt(annualTurnover);
                        } catch (NumberFormatException e) {
                            System.err.println("При чтении годового оборота произошла ошибка! Проверьте формат ввода! Годовой оборот должен быть предоставлен в Integer формате!");
                        }

                        Organization o = new Organization(name, coordinates, annualTurnoverInt, type, address);
                        addToCollection(o);
                    } catch (IncorrectValueException e) {
                        System.err.println(e.getMessage());
                    }
                }
                System.out.println("Коллекция загружена!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла!");
        } catch (IllegalArgumentException e){
            System.err.println("Произошла ошибка при чтении CSV файла проверьте правильность ввода значений и заголовков! Заголовки в файле должны быть указаны все на 1-ой строке!");
        }
    }

    /**
     * Проверка коллекции на отствуствие в ней элементов
     * @return true - коллекция пуста, false - коллекция содержит элементы
     */
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    /**
     * Добавление организации с указанием полей
     * @param name имя организации (не может быть null и пустым)
     * @param coordinates координаты организации (не может быть null)
     * @param annualTurnover годовой оборот организации (должен быть больше 0)
     * @param type тип организации (может быть null)
     * @param address адрес организации (может быть null)
     */
    public void addToCollection(String name, Coordinates coordinates, int annualTurnover, OrganizationType type, Address address) {
        try {
            Organization organization = new Organization(name, coordinates, annualTurnover, type, address);
            collection.add(organization);
        } catch (IncorrectValueException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавление организации с уже созданным объектом класса Organization
     * @param organization уже созданный объект класса Organization
     */
    public void addToCollection(Organization organization) {
        collection.add(organization);
    }

    /**
     * Контекстное меню для добавления нового элемента в коллекцию (с заполнением полей) и вывода информации о новом элементе
     */
    public void add() {
        System.out.println("Для добавления нового элемента в коллекцию заполните значения полей:");
        try {
            Organization o = new Organization(ConsoleEvent.getName(), ConsoleEvent.getCoordinates(), ConsoleEvent.getAnnualTurnover(), ConsoleEvent.getType(), ConsoleEvent.getAddress());
            addToCollection(o);
            System.out.println("Организация добавлена:");
            System.out.println(o);
        } catch (IncorrectValueException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Вывод информации о коллекции в консоль
     */
    public void info() {
        System.out.println("Тип коллекции: " + collection.getClass().getSimpleName() + ", дата инициализации: " + date + ", количество элементов " + collection.size());
    }

    /**
     * Очистка коллекции и вывод сообщения о том, что коллекция очищена
     */
    public void clear(){
      collection.clear();
      System.out.println("Коллекция очищена!");
    }

    /**
     * Вывод первого элемента коллекции, если он существует
     */
    public void head(){
        if(!isEmpty()) {
            System.out.println("Первый элемент коллекции: " + collection.peek());
        } else {
            System.err.println("Невозможно вывести элементы, так как коллекция пуста!");
        }
    }

    /**
     * Вывод всех элементов коллекции, если коллекция не пуста
     */
    public void show(){
        if(!isEmpty()){
            for(Organization o: collection) {
                System.out.println(o);
            }
        } else {
            System.err.println("Невозможно вывести элементы, так как коллекция пуста!");
        }
    }

    /**
     * Вывод суммы годового оборота всех организаций, если коллекция не пуста
     */
    public void sum_of_annual_turnover() {
        if(!isEmpty()) {
            int sum = 0;
            for (Organization o : collection) {
                sum += o.getAnnualTurnover();
            }
            System.out.println("Сумма годового оборота для всех элементов коллекции: " + sum);
        } else {
            System.err.println("Невозможно вывести элементы, так как коллекция пуста!");
        }
    }

    /**
     * Удаление элемента из коллекции с указанным типом организации
     * @param type тип организации
     */
    public void remove_any_by_type(String type) {
        if (!isEmpty()) {
            for (Organization o : collection) {
                if (o.getType() == Utils.StrToType(type)) {
                    collection.remove(o);
                    return;
                }
            }
            System.err.println("Элемент с указанным типом (" + type + ") не найден! Попробуйте ввести команду еще раз!");
        } else {
            System.err.println("Невозможно вывести элементы, так как коллекция пуста!");
        }
    }

    /**
     * Удаление элемента из коллекции с указанным ID организации
     * @param id ID организации (должен быть больше 0)
     */
    public void remove_by_id(int id) {
        if(!isEmpty()) {
            if (id > 0) {
                for (Organization o : collection) {
                    if (o.getId() == id) {
                        collection.remove(o);
                        return;
                    }
                }
            } else {
                System.err.println("ID должен быть > 0!");
                return;
            }
            System.err.println("Элемент с указанным ID (" + id + ") не найден! Попробуйте ввести команду еще раз!");
        } else {
        System.err.println("Невозможно вывести элементы, так как коллекция пуста!");
        }
    }

    /**
     * Вызов контекстного меню для обновление поля, которое задается пользователем
     * @param id ID организации (должен быть больше 0)
     */
    public void update(int id){
        if(!isEmpty()) {
            if (id > 0) {
                for (Organization o : collection) {
                    if (o.getId() == id) {
                        switch (ConsoleEvent.update()) {
                            case "name":
                                try {
                                    o.setName(ConsoleEvent.getName());
                                } catch (IncorrectValueException e) {
                                    System.err.println(e.getMessage());
                                }
                                System.out.println("Поле имени обновлено!");
                                break;
                            case "coordinates":
                                try {
                                    o.setCoordinates(ConsoleEvent.getCoordinates());
                                } catch (IncorrectValueException e) {
                                    System.err.println(e.getMessage());
                                }
                                System.out.println("Поле координат обновлено!");
                                break;
                            case "annualturnover":
                                try {
                                    o.setAnnualTurnover(ConsoleEvent.getAnnualTurnover());
                                } catch (IncorrectValueException e) {
                                    System.err.println(e.getMessage());
                                }
                                System.out.println("Поле годового оборота обновлено!");
                                break;
                            case "type":
                                o.setType(ConsoleEvent.getType());
                                System.out.println("Поле типа организации обновлено!");
                                break;
                            case "address":
                                o.setOfficialAddress(ConsoleEvent.getAddress());
                                System.out.println("Поле адреса обновлено!");
                                break;
                            case "exit":
                                System.out.println("Обновление поля было отменено пользователем!");
                                break;
                            default:
                                System.err.println("Произошла ошибка при обновлении поля!");
                                break;
                        }
                        return;
                    }
                }
            } else {
                System.err.println("ID должен быть > 0!");
                return;
            }
            System.err.println("Элемент с указанным ID (" + id + ") не найден! Попробуйте ввести команду еще раз!");
        } else {
        System.err.println("Невозможно обновить элементы, так как коллекция пуста!");
        }
    }

    /**
     * Сохранение коллекции по указанному пользователю пути
     * @param path путь к CSV, в котором будет сохраанена коллекция
     * @throws IOException если произошла ошибка при попытке сохранить файл
     */
    public void save(String path) throws IOException {
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(path)), StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("name", "cordX", "cordY", "annualTurnover", "type", "street", "zipcode"));
             ) {
            for(Organization o: collection) {
                String type = "";
                String street = "";
                String zipcode = "";
                try {
                    type = o.getType().toString();
                    street = o.getOfficialAddress().getStreet();
                    zipcode = o.getOfficialAddress().getZipCode();
                } catch (NullPointerException e){}
                csvPrinter.printRecord(o.getName(), o.getCoordinates().getX(), o.getCoordinates().getY(), o.getAnnualTurnover(), type, street, zipcode);
            }
            csvPrinter.flush();
            System.out.println("CSV-коллекция сохранена: " + path);
        }
    }

    /**
     * Вывод годового оборота всех организаций (с указанием ID и имени) в порядке возврастания
     */
    public void print_field_ascending_annual_turnover() {
        if(!isEmpty()){
            PriorityQueue<Organization> sorted = new PriorityQueue<>(annualTurnoverComparator);
            sorted.addAll(collection);
            Iterator<Organization> it = sorted.iterator();
            while (it.hasNext()) {
                sorted.poll().showAnnualTurnover();
            }
        } else {
            System.err.println("Невозможно вывести элементы, так как коллекция пуста!");
        }
    }

    /**
     * Удаление элемента коллекции, превышающее указанное пользователем значение годового оборота организации
     */
    public void remove_greater() {
        if(!isEmpty()){
            int comparable = ConsoleEvent.getAnnualTurnover();
            int sizeBefore = collection.size();
            collection.removeIf(p -> p.compareAnnualTurnover(comparable) == 1);
            int calc = sizeBefore - collection.size();
            System.out.println("Из коллекции удалено " + calc + " элементов, превышающиих годовой оборот на " + comparable);
        } else {
            System.err.println("Невозможно удалить элементы, так как коллекция пуста!");
        }
    }

    /**
     * Удаление элемента коллекции, меньшее чем указанное пользователем значение годового оборота организации
     */
    public void remove_lower() {
        if(!isEmpty()) {
            int comparable = ConsoleEvent.getAnnualTurnover();
            int sizeBefore = collection.size();
            collection.removeIf(p -> p.compareAnnualTurnover(comparable) == -1);
            int calc = sizeBefore - collection.size();
            System.out.println("Из коллекции удалено " + calc + " элементов, меньших по годовому обороту чем " + comparable);
        } else {
            System.err.println("Невозможно удалить элементы, так как коллекция пуста!");
        }
    }

    /**
     * Анонимный класс компаратора для сравнения годового оборота организаций
     */

    public static Comparator<Organization> annualTurnoverComparator = (o1, o2) -> (Integer.compare(o1.getAnnualTurnover(), o2.getAnnualTurnover()));
}