package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Logger log = LoggerFactory.getLogger("server");
    private final static int MAX_CLIENT_NUM = 2;
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


    void closeSession (Socket socket, String clName) {

        try {
            socket.close();
        } catch (IOException e) {
            log.error(" Socket can't be closed ");
        }
        synchronized (lock) {
            currentClientNum--;
            lock.notify();
        }
        log.info(String.format ( "[%s] was stopped", clName));

    }

    /*
    работа по добавлению клиента
     */
    private void work(){
        Channel<Runnable> channel = new Channel<Runnable>(MAX_CLIENT_NUM);
        createDispatcher(channel);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                addClient(socket, dataOutputStream, channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDispatcher (Channel<Runnable> chan) {
        Thread dispatcher = new Thread(new Dispatcher(chan));
        dispatcher.start();
    }

    public static void main(String[] args)      {
        Server server = new Server (Integer.parseInt( args[0]));
        server.work();
    }

    /*
    создание клиента или оповещение что кл не может быть добавлен
     */
    private void addClient(Socket socket, DataOutputStream dataOutputStream, Channel<Runnable> chan){

        synchronized (lock) {
            while (currentClientNum>= MAX_CLIENT_NUM) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentClientNum++;
        }
        chan.put(new Session(this, socket));
        log.info("[New client]  ({})",  getCurrentClientNum());

    }


//    private void queueClient() {
//
//        try {
//            synchronized (lock) {
//                log.info("New user is waiting for the connection");
//                lock.wait();
//            }
//        } catch (InterruptedException e) {
//            log.error("Connection waiting is failed");
//        }
//
//    }

    static int getCurrentClientNum() {
        return currentClientNum;
    }

    static void setCurrentClientNum(int currentClientNum) {
       synchronized (lock) {
        Server.currentClientNum = currentClientNum;
       }
    }
}
