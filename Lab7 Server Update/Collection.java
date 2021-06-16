import org.apache.commons.csv.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Integer.parseInt;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс для работы с коллекцией PriorityQueue "Очередь с приоритетом"
 */

public class Collection {
    private final PriorityQueue<Organization> collection = new PriorityQueue<>();
    private Date date;
    private static SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
    private static final Logger userLogger = LogManager.getLogger(Collection.class);
    private ReentrantLock locker = new ReentrantLock();

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
        ArrayList<String> arrayList = new ArrayList<>();
        String request = "SELECT * from collection";
        int lastId = 0;
        locker.lock();
        try {
            ResultSet resultSet = UDPSocketServer.getDatabaseManager().getStatement().executeQuery(request);
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                lastId = id;

                String name = resultSet.getString("name");
                String cordX = resultSet.getString("cordX");
                String cordY = resultSet.getString("cordY");
                String annualTurnover = resultSet.getString("annualTurnover");
                String typeofOrganization = resultSet.getString("type");
                String street = resultSet.getString("street");
                String zipcode = resultSet.getString("zipcode");
                String author = resultSet.getString("author");
                String date = resultSet.getString("date");

                Coordinates coordinates = null;

                try {
                    coordinates = new Coordinates(Float.parseFloat(cordX), Float.parseFloat(cordY));
                } catch (NumberFormatException e) {
                    userLogger.error("При чтении координат произошла ошибка! Проверьте формат ввода! Координаты должны быть представлены в Float формате!");
                    arrayList.add("При чтении координат произошла ошибка! Проверьте формат ввода! Координаты должны быть представлены в Float формате!");
                }

                OrganizationType type = Utils.StrToType(typeofOrganization);

                Address address = null;
                if (street != null) {
                    if (!street.isEmpty()) {
                        address = new Address(street, zipcode);
                    }
                }

                int annualTurnoverInt = 0;
                try {
                    annualTurnoverInt = parseInt(annualTurnover);
                } catch (NumberFormatException e) {
                    userLogger.error("При чтении годового оборота произошла ошибка! Проверьте формат ввода! Годовой оборот должен быть предоставлен в Integer формате!");
                    arrayList.add("При чтении годового оборота произошла ошибка! Проверьте формат ввода! Годовой оборот должен быть предоставлен в Integer формате!");
                }

                Organization o = new Organization(name, coordinates, annualTurnoverInt, type, address);
                o.setId(id);
                try {
                    o.setCreationDate(format.parse(date));
                } catch (NullPointerException | ParseException e) {
                    userLogger.error("Произошла ошибка при загрузке даты! Объекту будет установлена текущая дата!");
                }
                o.setAuthor(author);
                addToCollection(o);
            }
            Organization.setLastId(lastId);
            userLogger.info("Коллекция загружена! Путь " + path);
            arrayList.add("Коллекция загружена! Путь " + path);
        } catch (SQLException throwables) {
            userLogger.fatal("Ошибка при загрузке коллекции из базы данных!");
            throwables.printStackTrace();
        } catch (IncorrectValueException e) {
            System.err.println(e.getMessage());
        } /* catch (NullPointerException e){
            userLogger.error("Произошла ошибка при загрузке коллекции из базы данных! База данных не может быть null!");
            arrayList.add("Произошла ошибка при загрузке коллекции из базы данных! База данных не может быть null!");
        } */ finally {
        locker.unlock();
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
     * Добавление организации с уже созданным объектом класса Organization
     * @param organization уже созданный объект класса Organization
     */
    public void addToCollection(Organization organization) {
        locker.lock();
        collection.add(organization);
        locker.unlock();
    }

    /**
     * Добавление организации с уже созданным объектом класса Organization
     * Метод предназначен для добавления организации от клиента
     * @param organization
     */
    public void addToCollectionFromClient(Organization organization, User user) {
        try {
            locker.lock();
            Organization o = new Organization(organization.getName(), organization.getCoordinates(), organization.getAnnualTurnover(), organization.getType(), organization.getOfficialAddress());
            o.setAuthor(user.getUsername());
            if(!UDPSocketServer.getDatabaseManager().addToCollection(o)) {
                userLogger.fatal("Произошла ошибка при добавлении элемента " + organization.getName());
            } else {
                collection.add(o);
            }
        } catch (IncorrectValueException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
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
    public String clear() {
        locker.lock();
        if(!isEmpty()) {
            if(UDPSocketServer.getDatabaseManager().clearCollection()) {
                collection.clear();
                locker.unlock();
                return "Коллекция очищена!";
            } else {
                locker.unlock();
                return "Не удалось очистить коллекцию в базе данных!";
            }
        } else {
            locker.unlock();
            return "Ошибка: коллекция пуста!";
        }
    }

    /**
     * Очистка коллекции и вывод сообщения о том, что коллекция очищена
     */
    public String clearBy(User user) {
        locker.lock();
        if(!isEmpty()) {
            if(UDPSocketServer.getDatabaseManager().clearByAuthor(user)) {
                if(collection.removeIf(p -> p.getAuthor().equals(user.getUsername()))) {
                    locker.unlock();
                    return "Коллекция очищена! Все ваши элементы удалены!";
                }
                locker.unlock();
                return "Произошла ошибка при удалении коллекции на сервере!";
            } else {
                locker.unlock();
                return "Не удалось очистить коллекцию в базе данных или же вам не принадлежит ни один из объектов!";
            }
        } else {
            locker.unlock();
            return "Ошибка: коллекция пуста!";
        }
    }

    /**
     * Вывод первого элемента коллекции, если он существует
     */
    public String head() {
        if(!isEmpty()) {
            return "Первый элемент коллекции: " + collection.peek();
        } else {
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
    public String remove_any_by_type(String type, User user) {
        locker.lock();
        if (!isEmpty()) {
            Organization o = collection.stream().filter(p -> (p.getType() == Utils.StrToType(type) && (p.getAuthor().equals(user.getUsername())))).findAny().orElse(null);
            if(o != null && o.getAuthor().equals(user.getUsername())) {
                if (UDPSocketServer.getDatabaseManager().remove_by_id(o.getId(), user)) {
                    if (collection.remove(o)) {
                        locker.unlock();
                        return "Элемент с типом " + Utils.StrToType(type) + " удален (" + o.getId() + ")";
                    }
                }
            }
            locker.unlock();
            return "Ошибка: элемент с указанным типом (" + type + ") не найден или вы не можете удалить его из-за отсутствия прав доступа! Попробуйте ввести команду еще раз!";
        } else {
            locker.unlock();
            return "Ошибка: невозможно удалить элемент, так как коллекция пуста!";
        }
    }

    /**
     * Удаление элемента из коллекции с указанным ID организации
     * @param id ID организации (должен быть больше 0)
     */
    public String remove_by_id(int id, User user) {
        locker.lock();
        if(!isEmpty()) {
            if (id > 0) {
                if (UDPSocketServer.getDatabaseManager().remove_by_id(id, user)) {
                    if(collection.removeIf(p -> ((p.getId() == id) && (p.getAuthor().equals(user.getUsername()))))) return "Элемент удален (" + id + ")";
                }
            } else {
                locker.unlock();
                return "Ошибка: ID должен быть > 0!";
            }
            locker.unlock();
            return "Ошибка: элемент с указанным ID (" + id + ") не найден или вы не можете удалить его из-за отсутствия прав доступа! Попробуйте ввести команду еще раз!";
        } else {
            locker.unlock();
            return "Ошибка: невозможно удалить элемент, так как коллекция пуста!";
        }
    }

    /**
     * Вызов контекстного меню для обновление поля, которое задается пользователем (от клиента)
     * @param id ID организации (должен быть больше 0)
     */
    public ArrayList<String> updateFromClient(int id, String field, Object value, User user) {
        ArrayList<String> arrayList = new ArrayList<>();
        locker.lock();
        if(!isEmpty()) {
            if (id > 0) {
                for (Organization o : collection) {
                    if (o.getId() == id) {
                        if(o.getAuthor().equals(user.getUsername())) {
                            if(UDPSocketServer.getDatabaseManager().update(id, field, value)) {
                                switch (field) {
                                    case "name":
                                        try {
                                            o.setName((String) value);
                                        } catch (IncorrectValueException e) {
                                            arrayList.add(e.getMessage());
                                        }
                                        arrayList.add("Поле имени обновлено!");
                                        break;
                                    case "coordinates":
                                        try {
                                            o.setCoordinates((Coordinates) value);
                                        } catch (IncorrectValueException e) {
                                            arrayList.add(e.getMessage());
                                        }
                                        arrayList.add("Поле координат обновлено!");
                                        break;
                                    case "annualturnover":
                                        try {
                                            o.setAnnualTurnover((int) value);
                                        } catch (IncorrectValueException e) {
                                            arrayList.add(e.getMessage());
                                        }
                                        arrayList.add("Поле годового оборота обновлено!");
                                        break;
                                    case "type":
                                        o.setType((OrganizationType) value);
                                        arrayList.add("Поле типа организации обновлено!");
                                        break;
                                    case "address":
                                        o.setOfficialAddress((Address) value);
                                        arrayList.add("Поле адреса обновлено!");
                                        break;
                                    case "exit":
                                        arrayList.add("Обновление поля было отменено пользователем!");
                                        break;
                                    default:
                                        arrayList.add("Произошла ошибка при обновлении поля!");
                                        break;
                                }
                            } else {
                                arrayList.add("Ошибка: не удалось обновить элемент коллекции в базе данных!");
                            }
                        } else {
                            arrayList.add("Вы не можете обновить элемент коллекции с указанным ID (" + id + "), так как не являетесь его автором!");
                        }
                        locker.unlock();
                        return arrayList;
                    }
                }
            } else {
                arrayList.add("Ошибка: ID должен быть > 0!");
                locker.unlock();
                return arrayList;
            }
            arrayList.add("Элемент с указанным ID (" + id + ") не найден! Попробуйте ввести команду еще раз!");
        } else {
            arrayList.add("Невозможно обновить элементы, так как коллекция пуста!");
        }
        locker.unlock();
        return arrayList;
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
            collection.stream().sorted(annualTurnoverComparator).forEach(p -> arrayList.add(p.showAnnualTurnover()));
        } else {
            arrayList.add("Ошибка: невозможно вывести элементы, так как коллекция пуста!");
        }
        return arrayList;
    }

    /**
     * Удаление элемента коллекции, превышающее указанное пользователем значение годового оборота организации
     */
    public String remove_greater(int value, User user) {
        locker.lock();
        if(!isEmpty()){
            int sizeBefore = collection.size();
            if(UDPSocketServer.getDatabaseManager().remove_greater(value, user)) {
                collection.removeIf(p -> ((p.compareAnnualTurnover(value) == 1) && p.getAuthor().equals(user.getUsername())));
            } else {
                locker.unlock();
                return "Ошибка: не удалось удалить элемент коллекции в базе данных или вы не можете удалить его из-за отсутствия прав доступа!";
            }
            int calc = sizeBefore - collection.size();
            locker.unlock();
            return "Из коллекции удалено " + calc + " элементов, превышающиих годовой оборот на " + value;
        } else {
            locker.unlock();
            return "Ошибка: невозможно удалить элементы, так как коллекция пуста!";
        }
    }

    /**
     * Удаление элемента коллекции, меньшее чем указанное пользователем значение годового оборота организации
     */
    public String remove_lower(int value, User user) {
        locker.lock();
        if(!isEmpty()) {
            int sizeBefore = collection.size();
            if(UDPSocketServer.getDatabaseManager().remove_lower(value, user)) {
                collection.removeIf(p -> ((p.compareAnnualTurnover(value) == -1) && p.getAuthor().equals(user.getUsername())));
            } else {
                locker.unlock();
               return "Ошибка: не удалось удалить элемент коллекции в базе данных или вы не можете удалить его из-за отсутствия прав доступа!";
            }
            int calc = sizeBefore - collection.size();
            locker.unlock();
            return "Из коллекции удалено " + calc + " элементов, меньших по годовому обороту чем " + value;
        } else {
            locker.unlock();
            return "Ошибка: невозможно удалить элементы, так как коллекция пуста!";
        }
    }

    public static SimpleDateFormat getFormat() {
        return format;
    }

    /**
     * Анонимный класс компаратора для сравнения годового оборота организаций
     */

    public static Comparator<Organization> annualTurnoverComparator = (o1, o2) -> (Integer.compare(o1.getAnnualTurnover(), o2.getAnnualTurnover()));
}