import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UDPSocketServer {
    private static int PORT = 9876;
    private static boolean running;
    private static String path;
    private static DatagramSocket socket;
    private static RequestHandler requestHandler = new RequestHandler();
    private static ResponseHandler responseHandler = new ResponseHandler();
    private static AnswerHandler answerHandler;
    private boolean connected = false;
    static final Logger userLogger = LogManager.getLogger(UDPSocketServer.class);
    public static ArrayList<Client> clientList = new ArrayList<>();

    public UDPSocketServer(String path) {
        this.path = path;
    }

    public void start() {
        if(running) {
            userLogger.error("Сервер уже запущен!");
        } else {
            try {
                running = true;
                DatagramChannel datagramChannel = DatagramChannel.open();
                datagramChannel.configureBlocking(false);
                socket = datagramChannel.socket();
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
                Selector selector = Selector.open();
                answerHandler = new AnswerHandler(path);
                new Thread(new ServerConsole()).start();
                datagramChannel.register(selector, SelectionKey.OP_READ, new ClientRecord());
                userLogger.info("Сервер слушает " + PORT + " порт");
                while(true){
                    if (selector.select(3000) == 0) {
                        continue;
                    }
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();

                    // Iterate through the Set of keys.
                    for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
                        SelectionKey key = i.next();
                        i.remove();
                        if(key.isValid()) {
                            if (key.isReadable()) {
                                handleRead(key);
                            } else if (key.isWritable()) {
                                handleWrite(key);
                            }
                        }
                        }
                    }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //answerHandler = new AnswerHandler(path);
                //new Thread(new ServerConsole()).start();
                //new Thread(requestHandler).start();
                //userLogger.info("Сервер слушает " + PORT + " порт");
            /*} catch (SocketException e) {
                userLogger.error("Ошибка при запуске сервера! " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    public static void handleRead(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ClientRecord clntRec = (ClientRecord) key.attachment();
        clntRec.buffer.clear();    // Prepare buffer for receiving
        clntRec.clientAddress = channel.receive(clntRec.buffer);
        try {
            clntRec.blyat();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clntRec.clientAddress != null) {  // Did we receive something?
            // Register write with the selector
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    public static void handleWrite(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ClientRecord clntRec = (ClientRecord) key.attachment();
        clntRec.buffer.flip(); // Prepare buffer for sending
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        Answer answer = answerHandler.getAnswer(clntRec);
        if(answer != null) {
            os.writeObject(answer);
            byte[] replyBytes = outputStream.toByteArray();
            ByteBuffer buff = ByteBuffer.wrap(replyBytes);
            channel.send(buff, clntRec.clientAddress);
            //if (bytesSent != 0) { // Buffer completely written?
            // No longer interested in writes
            key.interestOps(SelectionKey.OP_READ);
            //}
        }
    }

    private void read(SelectionKey key) throws IOException {
        DatagramChannel chan = (DatagramChannel) key.channel();
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel a = channel.accept();
        byte[] incomingData = new byte[65634];
        ByteBuffer byteBuffer = ByteBuffer.wrap(incomingData);
        chan.receive(byteBuffer);
        byteBuffer.flip();
        try {
        ByteArrayInputStream in = new ByteArrayInputStream(incomingData);
        ObjectInputStream is = new ObjectInputStream(in);
        Command command = (Command) is.readObject();
        DatagramPacket datagramPacket = new DatagramPacket(incomingData, incomingData.length);
        //answerHandler.getAnswer(datagramPacket);
            chan.socket().send(datagramPacket);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            // Пропускаем ошибки Header'а, если ничего не пришло
        }
    }

    private void write(SelectionKey key) throws IOException {
        DatagramChannel chan = (DatagramChannel) key.channel();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
       // os.writeObject(new Answer(answerHandler.getAnswer()));
        ByteBuffer buffer = ByteBuffer.wrap(outputStream.toByteArray());
       // chan.send(buffer, serverAddress);
    }

    private void onConnect(SelectionKey key) throws IOException {
        DatagramChannel chan = (DatagramChannel) key.channel();
        byte[] incomingData = new byte[65634];
        ByteBuffer byteBuffer = ByteBuffer.wrap(incomingData);
        chan.receive(byteBuffer);
        byteBuffer.flip();
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(incomingData);
            ObjectInputStream is = new ObjectInputStream(in);
            Command command = (Command) is.readObject();
            System.out.println(command.getArgs()[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            // Пропускаем ошибки Header'а, если ничего не пришло
        }
    }

    public static void stop() {
        try {
            AnswerHandler.getCollection().save(path);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при сохранении файла!");
        }
        System.out.println("Программа завершена по требованию пользователя!");
        running = false;
    }

    public static DatagramSocket getSocket() {
        return socket;
    }

    public static RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public static ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public static AnswerHandler getAnswerHandler() {
        return answerHandler;
    }

    public static boolean isRunning() {
        return running;
    }
}