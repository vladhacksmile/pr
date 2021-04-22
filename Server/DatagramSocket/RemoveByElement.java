import java.io.Serializable;

public class RemoveByElement implements Serializable {
    private int value;
    private boolean isGreater;

    public RemoveByElement(int value, boolean isGreater) {
        this.value = value;
        this.isGreater = isGreater;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isGreater() {
        return isGreater;
    }

    public void setGreater(boolean greater) {
        isGreater = greater;
    }
}
