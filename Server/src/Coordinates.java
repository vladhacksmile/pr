import java.io.Serializable;

/**
 * Класс, хранящий в себе координаты организации
 */

public class Coordinates implements Serializable {
    private float x; // Максимальное значение поля: 662
    private float y;

    /**
     * Конструктор класса, хранящий в себе координаты организации
     * @param x принимает значение не превышающее 662
     * @param y принимает любое значение
     * @throws IncorrectValueException если пользователь вводит значение X, превышающее 662
     */

    public Coordinates(float x, float y) throws IncorrectValueException {
        setX(x);
        setY(y);
    }

    /**
     * Возвращает значение координаты Y
     * @return значение координаты y
     */

    public float getX() {
        return x;
    }

    /**
     * Возвращает значение координаты Y
     * @return значение координаты y
     */

    public float getY() {
        return y;
    }

    /**
     * Устанавливает координату x
     * @param x принимает значение не превышающее 662
     * @throws IncorrectValueException если пользователь вводит значение, превышающее 662
     */

    public void setX(float x) throws IncorrectValueException {
        if(x <= 662){
            this.x = x;
        } else {
            throw new IncorrectValueException("Максимальное значение координаты x = 662");
        }
    }

   /**
    * Устанавливает координату Y
    * @param y принимает любое значение
    */

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
}