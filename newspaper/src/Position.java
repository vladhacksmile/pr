public enum Position {
    OWNER("владелец", 100, 0), EDITOR("редактор", 30, 10), TYPESETTER("наборщик текста", 15, 5), UNKNOWN("неизвестно", 0, 0);

    private String name;
    private int procent;
    private int productivity;

    Position(String name, int procent, int productivity) {
        this.name = name;
        this.procent = procent;
        this.productivity = productivity;
    }

    public int getProcent() {
        return procent;
    }

    public int getProductivity() {
        return productivity;
    }

    @Override
    public String toString(){
        return name;
    }
}
