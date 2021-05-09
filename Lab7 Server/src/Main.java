public class Main {
    public static void main(String[] args) {
        UDPSocketServer server = new UDPSocketServer("src/data.csv");
        server.start();
    }
}
