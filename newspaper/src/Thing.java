public abstract class Thing {

    private String name;

    public Thing(String name){
        this.name = name;
    }

    public Thing() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}