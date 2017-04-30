package org.study.stasy.netutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.Exeptions.SessionException;
import org.study.stasy.concurrentutils.Stoppable;
import org.study.stasy.netutils.MessageHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *  Input/ output streams for communication between Client and Server
 */
public class Session implements Stoppable {
    private Logger log = LoggerFactory.getLogger("session");
    private Socket fromClientSocket;
    private String clName;
    private MessageHandler messageHandler;
    private static final String CTRL_MSG = "ok";
    private static final String STOP_MSG = "@exit";
    private static final String CONFIRM_MSG = "#recieved";
    private DataInputStream dIS;
    private DataOutputStream dOS;
    private boolean status;
    private final Object lock = new Object();

    Session(Socket socket, MessageHandler messageHandler) {

        status = true;
        this.fromClientSocket = socket;
        this.messageHandler = messageHandler;
        clName = (String.format("%s:%s", fromClientSocket.getInetAddress().getHostAddress(),
                Integer.toString(fromClientSocket.getPort())));
        try {
            dIS = new DataInputStream(fromClientSocket.getInputStream());
            dOS = new DataOutputStream(fromClientSocket.getOutputStream());
        } catch (IOException e) {
            log.error("session Constructor: DIS, DOS: {}", e);
        }
    }

    /**
     * While Server has not received  STOP_MSG Session is running
     * After Session stops for this Client
     */
    @Override
    public void run() {
        try {
            log.info("Session started for USER:[{}]", clName);
            dOS.writeUTF(CTRL_MSG);
            log.info("ctrl msg is sent");
            String receivedMsg = "";
            while (!receivedMsg.equals(STOP_MSG)) {
                receivedMsg = dIS.readUTF();
                messageHandler.handle(clName, receivedMsg);
                dOS.writeUTF(CONFIRM_MSG);
            }
            stop();
        } catch (IOException e) {
            log.error(" Session run(): stream error: {}", e);
        } catch (SessionException e) {
            log.error("run() session can't be stopped : {}", e);
        }
    }

    /**
     * Session stops when it's necessary (Incorrect exit or STOP_MSG from the client)
     */
    @Override
    public void stop() throws SessionException {

        synchronized (lock) {
            if (fromClientSocket==null) {
                //TODO подумать об этом
                // if (fromClientSocket.isClosed()) throw new SessionException("Socket is closed ");
                try {
                    dOS.writeUTF(String.format("GOOD BYE, DEAR [%s]", clName));
                    fromClientSocket.shutdownInput();
                    fromClientSocket.shutdownOutput();
                    fromClientSocket.close();
                } catch (IOException e) {
                    throw new SessionException("Session stop(): threads error: {}", e);
                }
                log.info("Session with [{}] was stopped", clName);
                lock.notifyAll();

            }
        }

    }
}
