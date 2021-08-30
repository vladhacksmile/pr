import java.util.Random;
public class TypeSetter extends Person implements IWorker {
    private int countofPrinted;
    public TypeSetter(String name, int money, Location location){
        super(name, money, location, Position.TYPESETTER);
    }

    public void work() {
        if (getJob() != null) {
            if (getLocation() == Location.NEWSPAPER) {
                int count = Utils.getRandom(1, getPosition().getProductivity());
                countofPrinted += count;
                getJob().addNewsPaper(count);
                System.out.println(this.getName() + " напечатал " + count + " газет");
            } else {
                System.out.println(getName() + " нет на рабочем месте!");
            }
        } else {
            System.out.println(getName() + " не указана работа!");
        }
    }

    public int getCountPrinted() {
        return countofPrinted;
    }
}
