package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.netutils.MessageHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {
    private Logger log = LoggerFactory.getLogger("session");
    private Socket socket;
    private String clName;
    private MessageHandler messageHandler;
    private static final String CTRL_MSG = "ok";
    private static final String STOP_MSG = "@exit";

    Session(Socket socket, MessageHandler messageHandler) {
        this.socket = socket;
        this.messageHandler = messageHandler;
        clName = (String.format("%s:%s", socket.getInetAddress().getHostAddress(), Integer.toString(socket.getPort())));
        log.info("Created new session", this);
    }

    public void run() {


        try {
            DataInputStream dIS = new DataInputStream(socket.getInputStream());
            DataOutputStream dOS = new DataOutputStream(socket.getOutputStream());
            dOS.writeUTF(CTRL_MSG);
            String receivedMsg = null;
            while (!receivedMsg.equals(STOP_MSG)) {
                receivedMsg = dIS.readUTF();
                messageHandler.handle(String.format("[%s]: %s ", clName, receivedMsg));
            }

        } catch (IOException e) {
            log.error("Oops! : ", e);
        } finally {

            try {
                socket.close();
            } catch (IOException e1) {
                log.error("Oops! :", e1);
            }
        }
    }


}
