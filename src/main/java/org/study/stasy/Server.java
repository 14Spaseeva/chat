package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Logger log = LoggerFactory.getLogger("server");
    private final static int MAX_CLIENT_NUM = 3;
    private static int currentClientNum = 0;
    private ServerSocket serverSocket = null;

    private static final Object lock = new Object();

    private Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Server is started on the {} port", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static void closeSession (Socket socket, String clName) {

        try {
            socket.close();
        } catch (IOException e) {
            log.error(" Socket can't be closed ");
        }
        synchronized (lock) {
            currentClientNum--;
        }
        log.info(String.format ( "[%s] was stopped", clName));

    }

    /*
    работа по добавлению клиента
     */
    private void work(){
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                if (currentClientNum < MAX_CLIENT_NUM) {
                    addClient(socket, dataOutputStream);
                } else {
                    rejectClient(dataOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)      {
        Server server = new Server (Integer.parseInt( args[0]));
        server.work();
    }

    /*
    создание клиента или оповещение что кл не может быть добавлен
     */
    private void addClient(Socket socket, DataOutputStream dataOutputStream){
        try {
            Thread new_client = new Thread(new Session(socket));
            new_client.setName(String.format("%s:%s",
                    socket.getInetAddress().getHostAddress(), Integer.toString(socket.getPort())));
            new_client.start();
            synchronized (lock) {
                currentClientNum++;
            }
            dataOutputStream.writeUTF("Sok");
            log.info("[New client] : " + new_client.getName() +"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rejectClient(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF("U can't connect. Please, try later ");
        log.warn("new client is rejected");
    }


    static int getCurrentClientNum() {
        return currentClientNum;
    }

    static void setCurrentClientNum(int currentClientNum) {
       synchronized (lock) {
        Server.currentClientNum = currentClientNum;
       }
    }
}
