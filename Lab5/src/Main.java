public class Main {
    public static void main(String[] args) {
        // ././src/data.csv
        String path = System.getenv("pathLab");
        Console console = new Console(path);
        console.run();
    }
}