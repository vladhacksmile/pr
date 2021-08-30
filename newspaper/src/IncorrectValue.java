public class IncorrectValue extends Exception {
    public IncorrectValue(String cause){
       super("Значение должно быть > 0 " + cause);
    }
}
