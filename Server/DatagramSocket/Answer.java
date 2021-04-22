import java.io.Serializable;
import java.util.ArrayList;

public class Answer implements Serializable {
    private ArrayList<String> answer;
    private boolean isError;

    public Answer(ArrayList<String> answer, boolean isError) {
        this.answer = answer;
        this.isError = isError;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public boolean isError() {
        return isError;
    }

    @Override
    public String toString() {
        return answer.toString();
    }

    public void printArray(){
        answer.stream().forEach(System.out::println);
    }
}
