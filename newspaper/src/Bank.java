import java.util.Objects;

public class Bank {
    private String name;
    private int id;
    private static int countBanks;
    private int cash;

    public Bank(String name, int cash) {
        countBanks++;
        id = countBanks;
        this.name = name;
        this.cash = cash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getCash() {
        return cash;
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
        Bank b = (Bank) obj;
        return Objects.equals(id, b.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return getName();
    }
}
