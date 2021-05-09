import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console extends Thread {

    private Scanner scanner = new Scanner(System.in);
    @Override
    public void run() {
        while(UDPSocketClient.started) {
            try {
                String line = scanner.nextLine();
                if(UDPSocketClient.started) {
                    Object object = UDPSocketClient.execute(line);
                    if (object != null) {
                        UDPSocketClient.send(object);
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("Программа завершена по требованию пользователя!");
                UDPSocketClient.runnable = false;
            }
        }
    }
}
