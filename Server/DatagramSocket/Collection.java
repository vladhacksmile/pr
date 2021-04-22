import org.apache.commons.csv.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

/**
 * Класс для работы с коллекцией PriorityQueue "Очередь с приоритетом"
 */

public class Collection {
    private final PriorityQueue<Organization> collection = new PriorityQueue<>();
    private Date date; // final вообще была
    private static final Logger userLogger = LogManager.getLogger(RequestHandler.class);

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
    public ArrayList<String> load(String path) {
        CSVParser reader;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader = null;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            File file = new File(path);
            if(file.exists() && !file.canRead()) {
                throw new FileAccessException("Нет прав на чтение файла!");
            }
            inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
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

                        Coordinates coordinates = null;
                        try {
                            coordinates = new Coordinates(Float.parseFloat(cordX), Float.parseFloat(cordY));
                        } catch(NumberFormatException e) {
                            // System.err.println("При чтении координат произошла ошибка! Проверьте формат ввода! Координаты должны быть представлены в Float формате!");
                            userLogger.error("При чтении координат произошла ошибка! Проверьте формат ввода! Координаты должны быть представлены в Float формате!");
                            arrayList.add("При чтении координат произошла ошибка! Проверьте формат ввода! Координаты должны быть представлены в Float формате!");
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
                            // System.err.println("При чтении годового оборота произошла ошибка! Проверьте формат ввода! Годовой оборот должен быть предоставлен в Integer формате!");
                            userLogger.error("При чтении годового оборота произошла ошибка! Проверьте формат ввода! Годовой оборот должен быть предоставлен в Integer формате!");
                            arrayList.add("При чтении годового оборота произошла ошибка! Проверьте формат ввода! Годовой оборот должен быть предоставлен в Integer формате!");
                        }

                        Organization o = new Organization(name, coordinates, annualTurnoverInt, type, address);
                        addToCollection(o);
                }
                userLogger.info("Коллекция загружена! Путь " + path);
                arrayList.add("Коллекция загружена! Путь " + path);
            }
        } catch (FileNotFoundException e) {
            // System.err.println("Файл не найден!");
            userLogger.error("Файл не найден!");
            arrayList.add("Файл не найден!");
        } catch (IOException e) {
            // System.err.println("Ошибка при чтении файла!");
            userLogger.error("Ошибка при чтении файла!");
            arrayList.add("Ошибка при чтении файла!");
        } catch (IllegalArgumentException e){
            // System.err.println("Произошла ошибка при чтении CSV файла проверьте правильность ввода значений и заголовков! Заголовки в файле должны быть указаны все на 1-ой строке!");
            userLogger.error("Произошла ошибка при чтении CSV файла проверьте правильность ввода значений и заголовков! Заголовки в файле должны быть указаны все на 1-ой строке!");
            arrayList.add("Произошла ошибка при чтении CSV файла проверьте правильность ввода значений и заголовков! Заголовки в файле должны быть указаны все на 1-ой строке!");
        } catch (IncorrectValueException e) {
            System.err.println(e.getMessage());
        } catch (NullPointerException e){
            // System.err.println("Произошла ошибка при загрузке файла! Путь к файлу не может быть null или пустым!");
            userLogger.error("Произошла ошибка при загрузке файла! Путь к файлу не может быть null или пустым!");
            arrayList.add("Произошла ошибка при загрузке файла! Путь к файлу не может быть null или пустым!");
        } catch (FileAccessException e) {
            // System.err.println(e.getMessage());
            userLogger.error(e.getMessage());
            arrayList.add(e.getMessage());
        }
        return arrayList;
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
     * Добавление организации с уже созданным объектом класса Organization
     * Метод предназначен для добавления организации от клиента
     * @param organization
     */
    public void addToCollectionFromClient(Organization organization) {
        try {
            collection.add(new Organization(organization.getName(), organization.getCoordinates(), organization.getAnnualTurnover(), organization.getType(), organization.getOfficialAddress()));
        } catch (IncorrectValueException e) {
            e.printStackTrace();
        }
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
     * Контекстное меню для добавления нового элемента в коллекцию (с заполнением полей) и вывода информации о новом элементе
     * @param bufferedReader сканнер
     */
    public void add(BufferedReader bufferedReader) {
        System.out.println("Для добавления нового элемента в коллекцию заполните значения полей:");
        try {
            Organization o = new Organization(ConsoleEvent.getName(bufferedReader), ConsoleEvent.getCoordinates(bufferedReader), ConsoleEvent.getAnnualTurnover(bufferedReader), ConsoleEvent.getType(bufferedReader), ConsoleEvent.getAddress(bufferedReader));
            addToCollection(o);
            System.out.println("Организация добавлена:");
            System.out.println(o);
        } catch (IncorrectValueException e) {
            System.err.println(e.getMessage());
        } catch(NullPointerException e) {
            System.err.println("\nВсе поля должны быть введены корректно! Проверьте скрипт!");
        }
    }

    /**
     * Вывод информации о коллекции в консоль
     */
    public String info() {
       return "Тип коллекции: " + collection.getClass().getSimpleName() + ", дата инициализации: " + date + ", количество элементов " + collection.size();
    }

    /**
     * Очистка коллекции и вывод сообщения о том, что коллекция очищена
     */
    public String clear(){
        if(!isEmpty()) {
            collection.clear();
            return "Коллекция очищена!";
        } else {
            return "Ошибка: коллекция пуста!";
        }
    }

    /**
     * Вывод первого элемента коллекции, если он существует
     */
    public String head(){
        if(!isEmpty()) {
            return "Первый элемент коллекции: " + collection.peek();
        } else {
            // System.err.println("Невозможно вывести элемент, так как коллекция пуста!");
            return "Ошибка: невозможно вывести элемент, так как коллекция пуста!";
        }
    }

    /**
     * Вывод всех элементов коллекции, если коллекция не пуста
     */
    public ArrayList<String> show(){
        ArrayList<String> arrayList = new ArrayList<>();
        if(!isEmpty()){
            collection.forEach(o -> arrayList.add(o.toString()));
        } else {
            // System.err.println("Невозможно вывести элементы, так как коллекция пуста!");
            arrayList.add("Ошибка: невозможно вывести элементы, так как коллекция пуста!");
        }
        return arrayList;
    }

    /**
     * Вывод суммы годового оборота всех организаций, если коллекция не пуста
     */
    public String sum_of_annual_turnover() {
        if(!isEmpty()) {
            return "Сумма годового оборота для всех элементов коллекции: " + collection.stream().mapToInt(Organization::getAnnualTurnover).sum();
        } else {
            return "Ошибка: невозможно вывести сумму годового оборота, так как коллекция пуста!";
        }
    }

    /**
     * Удаление элемента из коллекции с указанным типом организации
     * @param type тип организации
     */
    public String remove_any_by_type(String type) {
        if (!isEmpty()) {
            for (Organization o : collection) {
                if (o.getType() == Utils.StrToType(type)) {
                    collection.remove(o);
                    return "Элемент удален (" + Utils.StrToType(type) + ")";
                }
            }
            collection.stream()
                    .filter(o -> o.getType() == Utils.StrToType(type))
                    .forEach(collection::remove);
            return "Ошибка: элемент с указанным типом (" + type + ") не найден! Попробуйте ввести команду еще раз!";
            // System.err.println("Элемент с указанным типом (" + type + ") не найден! Попробуйте ввести команду еще раз!")
        } else {
            // System.err.println("Невозможно удалить элемент, так как коллекция пуста!");
            return "Ошибка: невозможно удалить элемент, так как коллекция пуста!";
        }
    }

    /**
     * Удаление элемента из коллекции с указанным ID организации
     * @param id ID организации (должен быть больше 0)
     */
    public String remove_by_id(int id) {
        if(!isEmpty()) {
            if (id > 0) {
                for (Organization o : collection) {
                    if (o.getId() == id) {
                        collection.remove(o);
                        return "Элемент удален (" + id + ")";
                    }
                }
            } else {
                // System.err.println("ID должен быть > 0!");
                return "Ошибка: ID должен быть > 0!";
            }
            // System.err.println("Элемент с указанным ID (" + id + ") не найден! Попробуйте ввести команду еще раз!");
            return "Ошибка: элемент с указанным ID (" + id + ") не найден! Попробуйте ввести команду еще раз!";
        } else {
            // System.err.println("Невозможно удалить элемент, так как коллекция пуста!");
            return "Ошибка: невозможно удалить элемент, так как коллекция пуста!";
        }
    }

    /**
     * Вызов контекстного меню для обновление поля, которое задается пользователем (от клиента)
     * @param id ID организации (должен быть больше 0)
     */
    public ArrayList<String> updateFromClient(int id, String field, Object value) {
        ArrayList<String> arrayList = new ArrayList<>();
        if(!isEmpty()) {
            if (id > 0) {
                for (Organization o : collection) {
                    if (o.getId() == id) {
                        switch (field) {
                            case "name":
                                try {
                                    o.setName((String) value);
                                } catch (IncorrectValueException e) {
                                    // System.err.println(e.getMessage());
                                    arrayList.add(e.getMessage());
                                }
                                // System.out.println("Поле имени обновлено!");
                                arrayList.add("Поле имени обновлено!");
                                break;
                            case "coordinates":
                                try {
                                    o.setCoordinates((Coordinates) value);
                                } catch (IncorrectValueException e) {
                                    // System.err.println(e.getMessage());
                                    arrayList.add(e.getMessage());
                                }
                                // System.out.println("Поле координат обновлено!");
                                arrayList.add("Поле координат обновлено!");
                                break;
                            case "annualturnover":
                                try {
                                    o.setAnnualTurnover((int) value);
                                } catch (IncorrectValueException e) {
                                    // System.err.println(e.getMessage());
                                    arrayList.add(e.getMessage());
                                }
                                // System.out.println("Поле годового оборота обновлено!");
                                arrayList.add("Поле годового оборота обновлено!");
                                break;
                            case "type":
                                o.setType((OrganizationType) value);
                                // System.out.println("Поле типа организации обновлено!");
                                arrayList.add("Поле типа организации обновлено!");
                                break;
                            case "address":
                                o.setOfficialAddress((Address) value);
                                // System.out.println("Поле адреса обновлено!");
                                arrayList.add("Поле адреса обновлено!");
                                break;
                            case "exit":
                                // System.out.println("Обновление поля было отменено пользователем!");
                                arrayList.add("Обновление поля было отменено пользователем!");
                                break;
                            default:
                                // System.err.println("Произошла ошибка при обновлении поля!");
                                arrayList.add("Произошла ошибка при обновлении поля!");
                                break;
                        }
                        return arrayList;
                    }
                }
            } else {
                // System.err.println("ID должен быть > 0!");
                arrayList.add("Ошибка: ID должен быть > 0!");
                return arrayList;
            }
            // System.err.println("Элемент с указанным ID (" + id + ") не найден! Попробуйте ввести команду еще раз!");
            arrayList.add("Элемент с указанным ID (" + id + ") не найден! Попробуйте ввести команду еще раз!");
        } else {
            // System.err.println("Невозможно обновить элементы, так как коллекция пуста!");
            arrayList.add("Невозможно обновить элементы, так как коллекция пуста!");
        }
        return arrayList;
    }

    /**
     * Вызов контекстного меню для обновление поля, которое задается пользователем
     * @param id ID организации (должен быть больше 0)
     * @param bufferedReader сканнер
     */
    public void update(int id, BufferedReader bufferedReader){
        if(!isEmpty()) {
            if (id > 0) {
                for (Organization o : collection) {
                    if (o.getId() == id) {
                        switch (ConsoleEvent.update(bufferedReader)) {
                            case "name":
                                try {
                                    o.setName(ConsoleEvent.getName(bufferedReader));
                                } catch (IncorrectValueException e) {
                                    System.err.println(e.getMessage());
                                }
                                System.out.println("Поле имени обновлено!");
                                break;
                            case "coordinates":
                                try {
                                    o.setCoordinates(ConsoleEvent.getCoordinates(bufferedReader));
                                } catch (IncorrectValueException e) {
                                    System.err.println(e.getMessage());
                                }
                                System.out.println("Поле координат обновлено!");
                                break;
                            case "annualturnover":
                                try {
                                    o.setAnnualTurnover(ConsoleEvent.getAnnualTurnover(bufferedReader));
                                } catch (IncorrectValueException e) {
                                    System.err.println(e.getMessage());
                                }
                                System.out.println("Поле годового оборота обновлено!");
                                break;
                            case "type":
                                o.setType(ConsoleEvent.getType(bufferedReader));
                                System.out.println("Поле типа организации обновлено!");
                                break;
                            case "address":
                                o.setOfficialAddress(ConsoleEvent.getAddress(bufferedReader));
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
        try {
            File file = new File(path);
            if(file.exists() && !file.canWrite()){
                throw new FileAccessException("Нет прав на запись файла!");
            }
            if(file.exists() && !file.canRead()){
                throw new FileAccessException("Нет прав на чтение файла!");
            }
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(osw);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("name", "cordX", "cordY", "annualTurnover", "type", "street", "zipcode"));

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
        } catch(NullPointerException e) {
            System.err.println("Путь к файлу не может быть null!");
        } catch (FileAccessException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Вывод годового оборота всех организаций (с указанием ID и имени) в порядке возврастания
     */
    public ArrayList<String> print_field_ascending_annual_turnover() {
        ArrayList<String> arrayList = new ArrayList<>();
        if(!isEmpty()){
            PriorityQueue<Organization> sorted = new PriorityQueue<>(annualTurnoverComparator);
            sorted.addAll(collection);
            Iterator<Organization> it = sorted.iterator();
            while (it.hasNext()) {
                arrayList.add(sorted.poll().showAnnualTurnover());
            }
        } else {
            System.err.println("Невозможно вывести элементы, так как коллекция пуста!");
            // TODO error annualTurnover
            arrayList.add("Ошибка: невозможно вывести элементы, так как коллекция пуста!");
        }
        return arrayList;
    }

    /**
     * Удаление элемента коллекции, превышающее указанное пользователем значение годового оборота организации
     */
    public String remove_greater(int value) {
        if(!isEmpty()){
            int sizeBefore = collection.size();
            collection.removeIf(p -> p.compareAnnualTurnover(value) == 1);
            int calc = sizeBefore - collection.size();
            return "Из коллекции удалено " + calc + " элементов, превышающиих годовой оборот на " + value;
        } else {
            System.err.println("Невозможно удалить элементы, так как коллекция пуста!");
            return "Ошибка: невозможно удалить элементы, так как коллекция пуста!";
        }
    }

    /**
     * Удаление элемента коллекции, превышающее указанное пользователем значение годового оборота организации
     * @param bufferedReader сканнер
     */
    public String remove_greater(BufferedReader bufferedReader) {
        if(!isEmpty()){
            int comparable = ConsoleEvent.getAnnualTurnover(bufferedReader);
            int sizeBefore = collection.size();
            collection.removeIf(p -> p.compareAnnualTurnover(comparable) == 1);
            int calc = sizeBefore - collection.size();
            return "Из коллекции удалено " + calc + " элементов, превышающиих годовой оборот на " + comparable;
            // System.out.println("Из коллекции удалено " + calc + " элементов, превышающиих годовой оборот на " + comparable);
        } else {
            System.err.println("Невозможно удалить элементы, так как коллекция пуста!");
            return "Ошибка: невозможно удалить элементы, так как коллекция пуста!";
        }
    }

    /**
     * Удаление элемента коллекции, меньшее чем указанное пользователем значение годового оборота организации
     */
    public String remove_lower(int value) { // TODO валидацию бы
        if(!isEmpty()) {
            int sizeBefore = collection.size();
            collection.removeIf(p -> p.compareAnnualTurnover(value) == -1);
            int calc = sizeBefore - collection.size();
            return "Из коллекции удалено " + calc + " элементов, меньших по годовому обороту чем " + value;
            //System.out.println("Из коллекции удалено " + calc + " элементов, меньших по годовому обороту чем " + comparable);
        } else {
            System.err.println("Невозможно удалить элементы, так как коллекция пуста!");
            return "Ошибка: невозможно удалить элементы, так как коллекция пуста!";
        }
    }

    /**
     * Удаление элемента коллекции, меньшее чем указанное пользователем значение годового оборота организации
     * @param bufferedReader сканнер
     */
    public String remove_lower(BufferedReader bufferedReader) {
        if(!isEmpty()) {
            int comparable = ConsoleEvent.getAnnualTurnover(bufferedReader);
            int sizeBefore = collection.size();
            collection.removeIf(p -> p.compareAnnualTurnover(comparable) == -1);
            int calc = sizeBefore - collection.size();
            return "Из коллекции удалено " + calc + " элементов, меньших по годовому обороту чем " + comparable;
            //System.out.println("Из коллекции удалено " + calc + " элементов, меньших по годовому обороту чем " + comparable);
        } else {
            System.err.println("Невозможно удалить элементы, так как коллекция пуста!");
            return "Ошибка: невозможно удалить элементы, так как коллекция пуста!";
        }
    }

    /**
     * Анонимный класс компаратора для сравнения годового оборота организаций
     */

    public static Comparator<Organization> annualTurnoverComparator = (o1, o2) -> (Integer.compare(o1.getAnnualTurnover(), o2.getAnnualTurnover()));
}