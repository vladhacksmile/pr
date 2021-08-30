import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<TypeSetter> typeSetters = new ArrayList<TypeSetter>();

    public static void main(String[] args) throws IncorrectValue{
        Human crabs = new Human("Крабс", 500, Location.STREET);
        Human miga = new Human("Мига", 100, Location.STREET);
        Human julio = new Human("Жулио", 100, Location.HOME);
        Bank bank = new Bank("Банк", 2000000);
        Owner spruts = new Owner("Спрутс", 1000000, Location.NEWSPAPER);
        Newspaper newspaper = new Newspaper("Давилонские юморески", spruts);
        newspaper.setNewspaperprice(15);
        Editor editor = new Editor("Редактор Тарас", 100, Location.NEWSPAPER);
        editor.setJob(newspaper);

        int t = 10;
        for (int i = 0; i < t; i++) {
            TypeSetter ts = new TypeSetter("Наборщик текста №" + (i + 1), 100, Location.NEWSPAPER);
            ts.setJob(newspaper);
            typeSetters.add(ts);
        }

        System.out.println("~~~~~~~~~~~ Сцена 1 ~~~~~~~~~~~");
        crabs.setLocation(Location.HOTEL);
        Telegram telegram = new Telegram("Телеграмма от Спрутса", "С ослами кончайте. Два миллиона  получите  в  банке.  Об  исполнении телеграфируйте. Спрутс", spruts);
        crabs.readTelegram(telegram);
        crabs.callTo(miga, "Приезжай ко мне!");
        crabs.callTo(julio, "Приезжай ко мне!");
        miga.meetWith(crabs);
        julio.meetWith(crabs);
        miga.unmeetWith(crabs);
        julio.unmeetWith(crabs);
        crabs.think("Может съездить в " + bank + "? А хотя, лучше сначала в редакцию газеты съезжу!");
        crabs.setLocation(Location.NEWSPAPER);

        System.out.println("~~~~~~~~~~~ Сцена 2 ~~~~~~~~~~~");
        System.out.println(newspaper);

        newspaper.setBalance(5000);

        editor.work();
        for(int i = 0; i < t;  i++){
            for (int j = 0; j < Utils.getRandom(1, 10); j++) {
                typeSetters.get(i).work();
            }
        }
        System.out.println(spruts);
        newspaper.sell();
        newspaper.grab();
        System.out.println(spruts);
        spruts.payMoney(editor, editor.getCountPrinted());

        for (int i = 0; i < t; i++) {
            spruts.payMoney(typeSetters.get(i), typeSetters.get(i).getCountPrinted());
        }

        System.out.println(spruts);

        System.out.println("~~~~~~~~~~~ Сцена 3 ~~~~~~~~~~~");
        spruts.setLocation(Location.COMPANY);
        Company company = new Company("Spruts Inc.", spruts);
        company.setBalance(10000);
        company.start();
        company.setClothprice(15);
        company.setSugarprice(10);
        company.sugarFabric.sell(newspaper);
        company.clothFabric.sell(newspaper);
        company.grab();
        System.out.println(spruts);
    }
}
