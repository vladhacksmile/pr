public class Editor extends Person implements IWorker {

    private int countofPrinted;

    public Editor(String name,  int money, Location location){
        super(name, money, location, Position.EDITOR);
    }

    public void work() {
        if (getLocation() == Location.NEWSPAPER) {
            int count = Utils.getRandom(1, getPosition().getProductivity());
            countofPrinted += count;
            getJob().addNewsPaper(count);
            System.out.println(this.getName() + " напечатал " + count + " газет");
        } else { System.out.println(getName() + " нет на рабочем месте!");
            System.out.println(getName() + " нет на рабочем месте!");
        }
    }

    public int getCountPrinted() {
        return countofPrinted;
    }
}
