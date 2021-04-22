/**
 * Модуль чтения запроса и отправки его на получение ответа
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

public class RequestHandler extends Thread {
    static final Logger userLogger = LogManager.getLogger(RequestHandler.class);

    @Override
    public void run() {
        while(UDPSocketServer.isRunning()) {
            try {
                byte[] incomingData = new byte[128000];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                UDPSocketServer.getSocket().receive(incomingPacket);
                userLogger.info(incomingPacket.getAddress() + ":" + incomingPacket.getPort() + " received packet " + incomingPacket.getLength() + " bytes");
                UDPSocketServer.getAnswerHandler().getAnswer(incomingPacket);
            } catch (IOException e) {
                userLogger.error("Возникли проблемы с чтением запроса!");
            }
        }
    }
}
