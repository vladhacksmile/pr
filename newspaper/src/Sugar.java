public class Sugar extends Thing {
    private int mass;
    public Sugar(String name, int mass){
        super(name);
        this.mass = mass;
    }

    public int getMass() {
        return mass;
    }
}
