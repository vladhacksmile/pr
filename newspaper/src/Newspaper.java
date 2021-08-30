import java.util.Objects;

public class Newspaper {
    private static int countofNewspapers;
    final private int id;
    private String name;
    private int balance;
    final private Owner owner;
    private int newspapercount;
    private int newspaperprice;
    private final EditorialBuilding building = new EditorialBuilding("Редакция газеты");

    public Newspaper(String name, Owner owner) {
        countofNewspapers++;
        id = countofNewspapers;
        this.name = name;
        this.owner = owner;
        owner.setJob(this); // Устанавливаем рабочее место владельцу
    }

    public int getNewspapercount() {
        return newspapercount;
    }

    public void setBalance(int balance) throws IncorrectValue {
        if(balance < 0) {
            throw new IncorrectValue("баланс компании " + getName());
        }
        owner.addMoney(-balance);
        addCash(balance);
    }

    public int getNewspaperprice() {
        return newspaperprice;
    }

    public void setNewspaperprice(int newspaperprice) {
        this.newspaperprice = newspaperprice;
    }

    public int getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addNewsPaper(int newspapercount) throws NotEnoughCoins {
        int calcCosts = newspaperprice / 2;
        try {
            if (balance >= calcCosts) {
                balance -= calcCosts; // Будем брать 50% на стоимость бумаги, чернил и прочего
                this.newspapercount += newspapercount;
            } else {
                throw new NotEnoughCoins("невозможно напечатать газету из-за отстствия монет на бумагу и чернила");
            }
        } catch(NotEnoughCoins e) {
            System.err.println(e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void grab(){
        owner.addMoney(balance);
        System.out.println(owner.getName() + " забрал " + balance + " монет из бюджета");
        balance = 0;
    }

    public void addCash(int cash) {
        balance += cash;
        System.out.println("В бюджет редакции добавлено " + cash + " монет");
    }

    public int sell() {
        if(getNewspapercount() > 0) {
            int count = Utils.getRandom(getNewspapercount());
            int profit = count * getNewspaperprice();
            newspapercount -= count;
            addCash(profit);
            System.out.println("Продано " + count + " газет");
            System.out.println("Баланс редакции: " + getBalance() + ", количество оставшихся газет: " + getNewspapercount());
            return count;
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null || obj.getClass() != getClass())
            return false;
        if (obj == this)
            return true;
        Newspaper n = (Newspaper) obj;
        return Objects.equals(id, n.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return building.getName() + " #" + getId() + " " + getName() + " владельцем которой является " + owner.getName() + ", баланс " + getBalance() + " монет," + " цена газеты " + getNewspaperprice() + " монет, количество не проданных газет " + getNewspapercount();
    }
}
