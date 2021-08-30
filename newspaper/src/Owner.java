public class Owner extends Person implements IOwner {
    public Owner(String name, int money, Location location) {
        super(name, money, location, Position.OWNER);
    }

    @Override
    public boolean payMoney(Person person, int countofnewspapers) {
        if(getJob() != null && getJob() == person.getJob()) {
            int count = Utils.getProcent((countofnewspapers * getJob().getNewspaperprice()), person.getPosition().getProcent());
            if (getMoney() >= count) {
                addMoney(-count);
                person.addMoney(count);
                System.out.println(getName() + " выплатил " + person.getName() + " " + count + " монет");
                return true;
            } else {
                System.out.println("Денег недостаточно! Не могу выплатить!");
                return false;
            }
        } else {
            System.out.println(person.getName() + " не работает в моей компании!");
            return false;
        }
    }

}
