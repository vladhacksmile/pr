import java.io.Serializable;
import java.util.Date;

/**
 * Класс организации
 */
public class Organization implements Comparable<Organization>, Serializable {
    private int id; // Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private Date creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int annualTurnover; // Значение поля должно быть больше 0
    private OrganizationType type; // Поле может быть null
    private Address officialAddress; // Поле может быть null
    private String author;

    private static int lastId = 0;

    /**
     * Конструктор класса организации
     * @param name имя организации (не может быть null и пустым)
     * @param coordinates координаты организации (не может быть null)
     * @param annualTurnover годовой оборот организации (должен быть больше 0)
     * @param type тип организации (может быть null)
     * @param officialAddress адрес организации (может быть null)
     * @throws IncorrectValueException если нарушены условия инициализации имени, координат, годового оборота
     */
    public Organization(String name, Coordinates coordinates, int annualTurnover, OrganizationType type, Address officialAddress) throws IncorrectValueException{
        setName(name);
        setCoordinates(coordinates);
        this.creationDate = new Date();
        setAnnualTurnover(annualTurnover);
        setType(type);
        setOfficialAddress(officialAddress);
        lastId++;
        id = lastId; // Если мы прошли кучу проверок полей и смогли создать объект, значит можем присвоить ID
    }

    /**
     * Возвращает ID организации
     * @return ID организации
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает имя организации
     * @return имя организации
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает координаты организации
     * @return объект класса Coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Возвращает дату создания организации
     * @return объект класса Date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает годовой оборот организации
     * @return годовой оборот организации
     */
    public int getAnnualTurnover() {
        return annualTurnover;
    }

    /**
     * Возвращает тип организации
     * @return объект перечисления OrganiizationType
     */
    public OrganizationType getType() {
        return type;
    }

    /**
     * Возвращает адрес организации
     * @return объект класса Address
     */
    public Address getOfficialAddress() {
        return officialAddress;
    }

    /**
     * Устанавливает имя организации
     * @param name имя организации (не может быть null или пустым)
     * @throws IncorrectValueException если имя null или пустое
     */
    public void setName(String name) throws IncorrectValueException {
        if (!name.isEmpty()) {
            this.name = name;
        } else {
            throw new IncorrectValueException("Поле имени не может быть null или пустым");
        }
    }

    /**
     * Устанавливает ID организации
     * @param id ID организации
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Устанавливает дату добавления организации
     * @param creationDate дата добавления организации
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Устанавливает координаты организации
     * @param coordinates объект класса Coordinates (не может быть null)
     * @throws IncorrectValueException если координаты null
     */
    public void setCoordinates(Coordinates coordinates) throws IncorrectValueException {
        if (coordinates != null) {
            this.coordinates = coordinates;
        } else {
            throw new IncorrectValueException("Поле координат не может быть null");
        }
    }

    /**
     * Устанавливает годовой оборот организации
     * @param annualTurnover годовой оборот компании (должен быть больше 0)
     * @throws IncorrectValueException если годовой оборот меньше или равен 0
     */
    public void setAnnualTurnover(int annualTurnover) throws IncorrectValueException {
        if (annualTurnover > 0) {
            this.annualTurnover = annualTurnover;
        } else {
            throw new IncorrectValueException("Поле годового оборота должно быть больше 0");
        }
    }

    /**
     * Устанавливает тип организации
     * @param type объект класса OrganizationType (может быть null)
     */
    public void setType(OrganizationType type) {
        this.type = type;
    }

    /**
     * Устанавливает адрес организации
     * @param officialAddress объект класса Address (может быть null)
     */
    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    /**
     * Стандартный компаратор, сравнивающий ID организаций
     * @param o организация
     * @return результат сравнения организации по ID
     */
    @Override
    public int compareTo(Organization o) {
        return getName().compareTo(o.getName());
    }

    /**
     * Компаратор, сравнивающий годовой оборот организаций
     * @param annualTurnover годовой оборот компании
     * @return результат сравнения организации по обороту
     */
    public int compareAnnualTurnover(int annualTurnover) {
        return Integer.compare(getAnnualTurnover(), annualTurnover);
    }

    /**
     * Выводит информацию о годвом обороте в консоль, указывая при этом ID и имя организации
     */
    public String showAnnualTurnover(){
        return "ID: " + getId() + ", name: " + getName() + ", annualTurnover: " + getAnnualTurnover();
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public static int getLastId() {
        return lastId;
    }

    public static void setLastId(int lastId) {
        Organization.lastId = lastId;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", " +
                "name: " + name + ", " +
                "coordinates (x; y): (" + coordinates.getX() + "; " + coordinates.getY() + "), " +
                "creation date: " + Collection.getFormat().format(creationDate) + ", " +
                "annual turnover: " + annualTurnover + ", " +
                "type: " + type + ", " +
                "official address (street; zipcode): " + officialAddress + ", " +
                "author: " + author;
    }

}