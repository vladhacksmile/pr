import java.io.Serializable;

public class Update implements Serializable {

    private int id;
    private String field;
    private Object value;

    public Update(int id, String field, Object value) {
        this.id = id;
        this.field = field;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
