package org.study.stasy.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

import org.study.stasy.ClientGUI.SendMsgForm;
import org.study.stasy.Exeptions.*;

public class Client {
    private static Logger log = LoggerFactory.getLogger(Client.class.getSimpleName());

    private static final String STOP_MSG = "@exit";
    private static final String CTRL_MSG = "ok";
    private boolean status;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket fromServer;
    private String fromServerCtrlMsg;

    public Client(String host, String port) throws ClientException {

        log.info("Connection...");
        status = true;
        try {
            fromServer = new Socket(host, Integer.parseInt(port));
            out = new DataOutputStream(fromServer.getOutputStream());
            in = new DataInputStream(fromServer.getInputStream());
            fromServerCtrlMsg = in.readUTF();
            log.info("ctrl msh is received");
        } catch (IOException e) {
            throw new ClientException("Client constructor: Socket error:");
        }
        if (!fromServerCtrlMsg.equals(CTRL_MSG))
            throw new ClientException("Client constructor: invalid control message=", fromServerCtrlMsg);
        else log.info("Ctrl msg is right");
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

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String recieveMsg() throws IOException {
        return in.readUTF();
    }

    /**
     * While client has not sent  STOP_MSG he can send  clientMsg to Server
     */
    private void sendMessages() throws ClientException {
        SendMsgForm sendMsgForm = new SendMsgForm();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //  sendMsgForm.out("Dear User, to exit this app use command @exit");
        System.out.println("Dear User, to exit this app use command @exit");
        String clientMsg = "";
        try {
            while (!clientMsg.equals(STOP_MSG)) {
                clientMsg = bufferedReader.readLine();
                //  clientMsg = sendMsgForm.getSendingMsg();
                out.writeUTF(clientMsg);
                log.info("sent!");
                //  sendMsgForm.out("sent!");
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new ClientException("Client sendMessage stream error: {}", e);
        }
    }

    /**
     * When client sent STOP_MSG or closed console/window/app this method is called.
     */
    private void shutDownClient() throws ClientException {
        try {
            status = false;
            fromServer.shutdownInput();
            fromServer.shutdownOutput();
            fromServer.close();
        } catch (IOException e) {
            throw new ClientException("Client shutDownClient: socket error{}", e);
        }
    }

}