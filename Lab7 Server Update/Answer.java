import java.io.Serializable;
import java.util.ArrayList;

public class Answer implements Serializable {
    private ArrayList<String> answer;
    private boolean success;

    public Answer(ArrayList<String> answer, boolean success) {
        this.answer = answer;
        this.success = success;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return answer.toString();
    }

    public void printArray(){
        answer.stream().forEach(System.out::println);
    }
}