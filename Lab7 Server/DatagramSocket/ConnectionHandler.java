import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ConnectionHandler extends Thread {

    @Override
    public void run() {
        while(UDPSocketServer.isRunning()) {
            try {
                byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                UDPSocketServer.getSocket().receive(incomingPacket);
                System.out.println(incomingPacket.getAddress() + ":" + incomingPacket.getPort() + " recived packet " + incomingPacket.getLength() + " bytes");
                ByteArrayInputStream in = new ByteArrayInputStream(incomingData);
                ObjectInputStream is = new ObjectInputStream(in);
                Command command = (Command) is.readObject();
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                if (command.getArgs()[0].equals("connect")) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("connected");
                    ResponseHandler.send(new Answer(arrayList, false), IPAddress, port);
                    Client c = UDPSocketServer.clientList
                            .stream()
                            .filter( client ->
                            {
                                return client.getIp().equals(IPAddress);
                            }).findFirst().orElse( null );
                    if(c == null) {
                        //System.err.println("Connected " + port);
                        UDPSocketServer.clientList.add(new Client(IPAddress.toString(), port));
                    }
                    // new Thread(new RequestHandler()).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
