import java.util.ArrayList;
import java.util.List;

public class Company {
    private String name;
    private Owner owner;
    private int balance;
    private int clothprice;
    private int sugarprice;

    SugarFabric sugarFabric = new SugarFabric();
    ClothFabric clothFabric = new ClothFabric();
    public Company(String name, Owner owner){
        this.name = name;
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(int balance) throws IncorrectValue{
        if(balance < 0) {
            throw new IncorrectValue("баланс компании " + getName());
        }
        owner.addMoney(-balance);
        this.balance = balance;
    }

    public void grab(){
        owner.addMoney(balance);
        System.out.println(owner.getName() + " забрал " + balance + " монет из бюджета " + getName());
        balance = 0;
    }
    public int getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void start(){
        sugarFabric.start();
        clothFabric.start();
    }

    public int advertising(int budget, Newspaper newspaper) {
        int ads = AdsManager.ads(owner, this, newspaper, budget);
        return ads;
    }

    public void setClothprice(int clothprice) {
        this.clothprice = clothprice;
    }

    public int getClothprice() {
        return clothprice;
    }

    public int getSugarprice() {
        return sugarprice;
    }

    public void addCash(int cash){
        balance += cash;
    }

    public void setSugarprice(int sugarprice) {
        this.sugarprice = sugarprice;
    }

    static class AdsManager {
        public static int ads(Person p, Company company, Newspaper newspaper, int sum) {
            if(p.getMoney() >= sum) {
                System.out.println(p.getName() + " рекламирует компанию " + company.getName());
                p.addMoney(-sum);
                int count = Utils.getRandom(1, (sum / 2)); // Рассчитываем рандомно кол-во людей, которые захотят купить товар
                newspaper.addCash(sum);
                return count;
            } else {
                return 0;
            }
        }
    }

    class SugarFabric implements IFabric {
        private List<Sugar> packets = new ArrayList<>();
        public void start() {
            int task = Utils.getRandom(1000);
            int count = 0;
            System.out.println("Фабрика начала производство сахара");
            for(int i = 0; i < task; i++) {
                try {
                    if (balance >= 5) {
                        balance -= 5;
                        Sugar t = new Sugar("Сахар \"Спрутовский\"", 1000);
                        packets.add(t);
                        count++;
                    } else {
                        throw new NotEnoughCoins("Для производства сахара нужны деньги");
                    }
                } catch (NotEnoughCoins e){
                    System.err.println(e.getMessage());
                }
            }
            System.out.println("Фабрика сахара произвела " + count + " пакетов сахара");
        }

        public List<Sugar> getPackets() {
            return packets;
        }

        public int sell(Newspaper newspaper) {
            if(packets.size() > 0 && owner.getMoney() >= 1000) {
                int count = advertising(Utils.getRandom(1000), newspaper);
                for (int i = 0; i < count; i++) {
                    try {
                        System.out.println("Было продано " + packets.get((packets.size() - i - 1)).getName() + " массой " + packets.get(packets.size() - i - 1).getMass());
                        packets.remove(packets.size() - i - 1);
                        addCash(getSugarprice());
                    } catch (IndexOutOfBoundsException e){
                        System.err.println("Фабрика по производству сахара не может удовлетворить спрос покупателей! Производство продолжается!");
                        start();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        i--;
                    }
                }
                System.out.println("Продано " + count + " пакетов сахара");
                System.out.println("Баланс компании: " + getBalance() + ", количество оставшихся пакетов сахара: " + (packets.size() - 1));
                return count;
            }
            return 0;
        }

    }

    class ClothFabric implements IFabric {
        private List<Cloth> clothes = new ArrayList<>();

        public void start() {
            int task = Utils.getRandom(500);
            int count = 0;
            System.out.println("Фабрика начала производство одежды");
            for(int i = 0; i < task; i++){
                try {
                    if (balance >= 5) {
                        balance -= 5;
                        Cloth t = new Cloth("Худи \"Spruts\"") {
                            @Override
                            public Color getColor() {
                                int id = Utils.getRandom(3);
                                if (id == 0) {
                                    return Color.BLACK;
                                } else if (id == 1) {
                                    return Color.WHITE;
                                } else {
                                    return Color.ORANGE;
                                }
                            }
                        };
                        clothes.add(t);
                        count++;
                    } else {
                        throw new NotEnoughCoins("Для производства толстовок нужны деньги");
                    }
                } catch (NotEnoughCoins e) {
                    System.err.println(e.getMessage());
                }
            }
            System.out.println("Фабрика одежды произвела " + count + " толстовок");
        }

        public int sell(Newspaper newspaper) {
            if(clothes.size() > 0 && owner.getMoney() >= 500) {
                int count = advertising(Utils.getRandom(500), newspaper);
                int sell = 0;
                for (int i = 0; i < count; i++) {
                    try {
                        System.out.println("Было продано " + clothes.get((clothes.size() - i - 1)).getName() + " цвета " + clothes.get(clothes.size() - i - 1).getColor());
                        clothes.remove(clothes.size() - i - 1);
                        addCash(getSugarprice());
                        sell++;
                    } catch (IndexOutOfBoundsException e){
                        System.err.println("Фабрика по производству худи не может удовлетворить спрос покупателей! Производство продолжается!");
                        start();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        i--;
                    }
                }
                System.out.println("Продано " + count + " худи (" + sell + ")");
                System.out.println("Баланс компании: " + getBalance() + ", количество оставшихся худи: " + (clothes.size() - 1));
                return count;
            }
            return 0;
        }

        public List<Cloth> getClothes() {
            return clothes;
        }
    }
}
