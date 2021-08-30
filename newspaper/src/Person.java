import java.util.Objects;

abstract class Person {
    private static int countofPeople;
    private int id;
    private String name;
    private Position position;
    private Newspaper job;
    private int money;
    private Location location;
    private Location previousLocation;

    public Person() {

    }

    public Person(String name, int money, Location location, Position position) {
        countofPeople++;
        id = countofPeople;
        this.name = name;
        this.money = money;
        this.position = position;
        this.location = location;
        previousLocation  = location;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public int getId() {
        return id;
    }

    public static int getCountofPeople() {
        return countofPeople;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setJob(Newspaper job) {
        this.job = job;
    }

    public Newspaper getJob() {
        return job;
    }

    public void setLocation(Location location) {
        previousLocation = this.location;
        System.out.println(name + " сменил локацию " + previousLocation + " на " + location);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void readTelegram(Telegram telegram) {
        telegram.readTelegram(this);
    }

    public void callTo(Person p, String talk) {
        System.out.println(name + " позвонил " + p.name + " и сказал - " + talk);
    }

   public void meetWith(Person p) {
        if (location == p.location) {
            System.out.println(name + ", мы же в одном месте находимся!");
        } else {
            setLocation(p.location);
            System.out.println(name + " встретился с " + p.name);
        }
    }

    public void unmeetWith(Person p) {
        if(location == p.location) {
            setLocation(previousLocation);
            System.out.println(name + " расстался с " + p.name);
        } else {
            System.out.println(name + " даже не встречался с " + p.name);
        }
    }

    public void think(String about) {
        System.out.println(name + " задумался о " + about);
    }

    public void see(Thing thing){
        System.out.println(name + " увидел вещь " + thing.getName());
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null || obj.getClass() != getClass())
            return false;
        if (obj == this)
            return true;
        Person p = (Person) obj;
        return Objects.equals(id, p.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Человек #" + getId() + " " + getName() + " занимает должность " + position + ", находится в " + location + " баланс " + getMoney() + " монет";
    }

}
