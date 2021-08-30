public enum Location {
    COMPANY("компания"), HOTEL("гостиница"), OFFICE("офис"), HOME("дом"), STREET("улица"), BANK("банк"), NEWSPAPER("редакция газеты");
    private String name;

    Location(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
