package org.study.stasy.netutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.ChatMessage;
import org.study.stasy.Exeptions.SessionException;
import org.study.stasy.app.Client;
import org.study.stasy.concurrentutils.Stoppable;

import java.io.*;
import java.net.Socket;


/**
 * TODO A 127.255.255.255 - разослать всем (class A) ??
 * /**
 * Input/ output streams for communication between Client and Server
 */
public class Session implements Stoppable {
    private Logger log = LoggerFactory.getLogger(Client.class.getSimpleName());

    private static final String CTRL_MSG = "ok";
    private static final String STOP_MSG = "@exit";
    private static final String CONFIRM_MSG = "#recieved";

    private Socket fromClientSocket;
    private MessageHandler messageHandler;

    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;


    private final Object lock = new Object();

    Session(Socket socket, MessageHandler messageHandler) {

        try {
            this.fromClientSocket = socket;
            this.messageHandler = messageHandler;
            InputStream in = fromClientSocket.getInputStream();
            OutputStream out = fromClientSocket.getOutputStream();
            objOut = new ObjectOutputStream(out);
            objIn = new ObjectInputStream(in);
        } catch (IOException e) {
            log.error("Session constructor is failed");
        }

    }

    /**
     * While Server has not received  STOP_MSG Session is running
     * After Session stops for this Client
     */
   /* //  @Override
    public void run1() {
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
*/
    @Override
    public void run() {
        try {
            ChatMessage ctrlMessage = new ChatMessage(CTRL_MSG);
            objOut.writeObject(ctrlMessage);

            log.info("ctrl msg is sent");
            String receivedMsg = "";
         //   while (objIn.available() > 0) {
                while (!receivedMsg.equals(STOP_MSG)) {

                    ChatMessage chatMessage;
                    chatMessage = (ChatMessage) objIn.readObject();
                    receivedMsg = chatMessage.getMessage();
                  log.info("[{}]",receivedMsg);
                    messageHandler.handle(chatMessage.getUserName(), receivedMsg);
                    ChatMessage confirmMsg = new ChatMessage(CONFIRM_MSG);
                    objOut.writeObject(confirmMsg);
                }
         //   }
            stop();
        } catch (IOException | ClassNotFoundException | SessionException e) {
            e.printStackTrace();
        }


    }

    /**
     * Session stops when it's necessary (Incorrect exit or STOP_MSG from the client)
     */
    @Override
    public void stop() throws SessionException {

        synchronized (lock) {
            if (fromClientSocket == null) {
                //TODO подумать об этом
                // if (fromClientSocket.isClosed()) throw new SessionException("Socket is closed ");
                try {
                    // dOS.writeUTF(String.format("GOOD BYE, DEAR [%s]"));
                    assert fromClientSocket != null;
                    fromClientSocket.shutdownInput();
                    fromClientSocket.shutdownOutput();
                    fromClientSocket.close();
                    objIn.close();
                    objOut.close();
                    log.info("Session with [{}] was stopped");
                    lock.notifyAll();

                } catch (IOException e) {
                    throw new SessionException("Session stop(): threads error: {}", e);
                }

            }
        }

    }
}
