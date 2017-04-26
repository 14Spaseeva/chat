package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

import org.study.stasy.Exeptions.*;

public class Client {
    private static Logger log = LoggerFactory.getLogger("client");
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream dataInputStream;
    private static final String STOP_MSG = "@exit";
    private static final String CTRL_MSG = "ok";

    private Client(String host, int portNum) throws ClientException {
        try {
            socket = new Socket(host, portNum);
            out = new DataOutputStream(socket.getOutputStream());
            String ctrlMsg = dataInputStream.readUTF();
            if (!ctrlMsg.equals(CTRL_MSG)) {
                throw new ClientException("Client can't be created: invalid control message: ", ctrlMsg);
            }
            log.info("Client is started");
        } catch (IOException e) {
            log.trace("Socket can't be created", e);
        }
    }

    /**
     * While client has not sent STOP_MSG he can send  @clientMsg
     */
    private void sendMessages() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in));
        String clientMsg = null;
        while (!clientMsg.equals(STOP_MSG)) {
            clientMsg = bufferedReader.readLine();
            out.writeUTF(clientMsg);
            log.info("sent");
        }
    }


    public static void main(String[] args) {
        Client newClient = null;
        try {
            newClient = new Client(args[0], Integer.parseInt(args[1]));
            newClient.sendMessages();
        } catch (ClientException | IOException e) {
            log.error("Oops!", e);
        }

    }
}
