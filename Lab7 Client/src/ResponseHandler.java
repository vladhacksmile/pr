import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.PortUnreachableException;
import java.nio.ByteBuffer;

public class ResponseHandler extends Thread {

    @Override
    public void run() {
        while(UDPSocketClient.runnable) {
            if (UDPSocketClient.channel.isOpen()) {
                try {
                    byte[] incomingData = new byte[65515];
                    ByteBuffer byteBuffer = ByteBuffer.wrap(incomingData);
                    UDPSocketClient.channel.receive(byteBuffer);
                    byteBuffer.flip();
                    ByteArrayInputStream in = new ByteArrayInputStream(incomingData);
                    ObjectInputStream is = new ObjectInputStream(in);
                    Answer answer = (Answer) is.readObject();
                    if(answer.getAnswer().get(0).equals("connected")) {
                        UDPSocketClient.connected = true;
                        System.out.println("Соединение с сервером установлено!");
                        if (UDPSocketClient.lastCommand != null) {
                            System.out.println("Клиент переотправил команду заново!");
                            UDPSocketClient.send(UDPSocketClient.lastCommand);
                        }
                    } else {
                        answer.printArray();
                        if(!answer.isSuccess()){
                            UDPSocketClient.stop();
                        }
                    }
                    UDPSocketClient.lastCommand = null;
                    if(!UDPSocketClient.started) {
                        if ((!answer.isSuccess() && Auth.isAuth()) || (answer.isSuccess() && !Auth.isAuth())) {
                            Auth.auth();
                            continue;
                        }
                        if (!answer.isSuccess() && !Auth.isAuth()) {
                            Auth.register();
                            continue;
                        }
                        if (answer.isSuccess() && Auth.isAuth()) {
                            Auth.authorized();
                        }
                    }
                } catch (PortUnreachableException e) {
                    System.out.print("Не удалось подключиться к серверу! Попытка подключиться к серверу через 3 секунды");
                    try {
                        Thread.sleep(1000);
                        System.out.print(".");
                        Thread.sleep(1000);
                        System.out.print(".");
                        Thread.sleep(1000);
                        System.out.print(".\n");
                        UDPSocketClient.connect();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Не удалось определить класс, возможно что-то с сериализацией данных или недостаток размера буфера!");
                    UDPSocketClient.runnable = false;
                } catch (StreamCorruptedException ignored) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
   }
}
