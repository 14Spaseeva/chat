package org.study.stasy.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyAdapter;
import java.io.*;
import java.net.Socket;

import org.study.stasy.ChatMessage;
import org.study.stasy.ClientGUI.ClientApp;
import org.study.stasy.Exeptions.*;

import static java.lang.System.out;

public class Client {
    private static Logger log = LoggerFactory.getLogger(Client.class.getSimpleName());

    private static final String STOP_MSG = "@exit";
    private static final String CTRL_MSG = "ok";
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private Socket fromServer;
    private String userName;
    private static final String HELLO_MSG = "#I'm fine";
    private ClientApp clientApp;

    public Client(String host, String port) throws ClientException {

        log.info("Connection...");
        try {


            fromServer = new Socket(host, Integer.parseInt(port));
            objOut = new ObjectOutputStream(this.fromServer.getOutputStream());
            objIn = new ObjectInputStream(this.fromServer.getInputStream());
            userName = userName = String.format("[%s:%s]", fromServer.getInetAddress().getHostAddress(),
                    Integer.toString(fromServer.getPort()));
            getCtrlMsg();
            sendHelloMsg();

        } catch (IOException e) {
            throw new ClientException("Client constructor: Socket error:");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public Client(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        fromServer = socket;
        objOut = oos;
        objIn = ois;

    }


    public String getUserName() {
        return userName;
    }

    private void getCtrlMsg() throws ClientException, IOException, ClassNotFoundException {
        ChatMessage fromServerCtrlMsg = (ChatMessage) objIn.readObject();
        log.info("ctrl msh is received: " + fromServerCtrlMsg.getMessage());
        if (!(fromServerCtrlMsg.getMessage().equals(CTRL_MSG))) {
            throw new ClientException("Client constructor: invalid control message=", fromServerCtrlMsg.getMessage());
        } else log.info("Ctrl msg is right");

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
        out.println("Dear User, to exit this app use command @exit");
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

    private void sendHelloMsg() throws IOException {
        ChatMessage confirmMsg = new ChatMessage(userName, HELLO_MSG);
        objOut.writeObject(confirmMsg);
    }

    /**
     * When client sent STOP_MSG or closed console/window/app this method is called.
     */
    public void shutDownClient() throws ClientException {
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

    public Object getOutputStream() {
        return objOut;
    }

    public Object getIutputStream() {
        return objIn;
    }

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }
}
