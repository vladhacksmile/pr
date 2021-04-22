/**
 * Модуль чтения запроса и отправки его на получение ответа
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;

public class RequestHandler extends Thread {
    private static final Logger userLogger = LogManager.getLogger(RequestHandler.class);

    @Override
    public void run() {
        while(UDPSocketServer.isRunning()) {
            try {
                byte[] incomingData = new byte[65515];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                UDPSocketServer.getSocket().receive(incomingPacket);
                userLogger.info(incomingPacket.getAddress() + ":" + incomingPacket.getPort() + " received packet " + incomingPacket.getLength() + " bytes");
            } catch (IOException e) {
                userLogger.error("Возникли проблемы с чтением запроса!");
            }
        }
    }
}
