package org.study.stasy.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import org.study.stasy.ChatMessage;
import org.study.stasy.ClientGUI.SendMsgForm;
import org.study.stasy.Exeptions.*;
import org.study.stasy.UserName;

public class Client {
    private static Logger log = LoggerFactory.getLogger(Client.class.getSimpleName());

    private static final String STOP_MSG = "@exit";
    private static final String CTRL_MSG = "ok";
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private Socket fromServer;
    private String userName;

    public Client(String host, String port) throws ClientException {

        log.info("Connection...");
        try {
            fromServer = new Socket(host, Integer.parseInt(port));

            objOut = new ObjectOutputStream(this.fromServer.getOutputStream());
            objIn = new ObjectInputStream(this.fromServer.getInputStream());

            ChatMessage fromServerCtrlMsg = (ChatMessage) objIn.readObject();
            log.info("ctrl msh is received: " + fromServerCtrlMsg.getMessage());
            if (!(fromServerCtrlMsg.getMessage().equals(CTRL_MSG))) {
                throw new ClientException("Client constructor: invalid control message=", fromServerCtrlMsg.getMessage());
            } else log.info("Ctrl msg is right");

            userName = String.format("[%s:%s]", fromServer.getInetAddress().getHostAddress(),
                    Integer.toString(fromServer.getPort()));
        } catch (IOException e) {
            throw new ClientException("Client constructor: Socket error:");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getUserName() {
        return userName;
    }


    public static void main(String[] args) {

        try {
            Client client = new Client(args[0], args[1]);
            client.sendMessages();
            client.shutDownClient();
        } catch (ClientException e) {
            log.error("Client can't be created: ", e);
        }
    }

    //для GUI
    public void sendMsg(String msg) throws IOException {
        ChatMessage chatMessage = new ChatMessage(userName, msg);
        objOut.writeObject(chatMessage);
    }

    public ChatMessage recieveMsg() throws ClassNotFoundException, IOException {
            return (ChatMessage) objIn.readObject();
    }

    /**
     * While client has not sent  STOP_MSG he can send  clientMsg to Server
     */
    private void sendMessages() throws ClientException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Dear User, to exit this app use command @exit");
        String clientMsg = "";
        try {
            while (!clientMsg.equals(STOP_MSG)) {
                clientMsg = bufferedReader.readLine();
                ChatMessage chatMessage = new ChatMessage(userName, clientMsg);
                objOut.writeObject(chatMessage);
                log.info("sent! ");
                ChatMessage confirmMsg = (ChatMessage) objIn.readObject();
                log.info(confirmMsg.getMessage());
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new ClientException("Client sendMessage stream error: {}", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * When client sent STOP_MSG or closed console/window/app this method is called.
     */
    private void shutDownClient() throws ClientException {
        try {
            fromServer.shutdownInput();
            fromServer.shutdownOutput();
            fromServer.close();

            objOut.close();
            objIn.close();

        } catch (IOException e) {
            throw new ClientException("Client shutDownClient: socket error{}", e);
        }
    }

}
