public enum Color {
    BLACK("черный"), WHITE("белый"),ORANGE("оранжевый");

    private String name;

    Color(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name;
    }
}
