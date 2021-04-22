public class Client {
    private String ip;
    private int port;
    private int id;
    private static int lastId = 0;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        lastId++;
        this.id = lastId;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}