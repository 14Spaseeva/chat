package org.study.stasy.netutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.concurrentutils.Channel;
import org.study.stasy.concurrentutils.Stoppable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//Singleton

public class Host implements Stoppable {
    private static Logger log = LoggerFactory.getLogger("host");
    private boolean status;
    private int portNumber;
    private ServerSocket serverSocket = null;
    private final Channel<Stoppable> channel;
    private final MessageHandler messageHandler;
    private Thread host;

    public Host(int portNumber, Channel<Stoppable> channel,
                MessageHandlerFactory messageHandlerFactory) {


        this.status = true;
        this.portNumber = portNumber;
        this.host = new Thread(this);
        this.channel = channel;
        this.messageHandler = messageHandlerFactory.create();
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            log.error("Oops! :{}", e);
        }
    }


    @Override
    public void run() {
        log.info("Host is runned on the {} port", portNumber);
        try {
            while (status) {
                Socket clientSocket = serverSocket.accept(); // заставляем хост ждать подключений
                //Как только клиент подключается - сокет для клиента сразу же создается. (clientSocket.isconnected iscreated is bound = true)
                // Исполнение программы зависает в этом месте, пока клиент не подключится
                log.info("Create new session");
                Session newSession= new Session(clientSocket, messageHandler);
                channel.put(newSession);
            }
        } catch (IOException e) {
            log.error(" Host run(): Socket error{}", e);
            this.stop();
        }

    }

    public void start() {
        host.setName(Host.class.getSimpleName());
        host.start();
    }

//добавить свой эксепшн

    @Override
    public void stop() {
        if (status) {
            status = false;
            host.interrupt();
            try {
                serverSocket.close();
            } catch (IOException e) {
                log.error("Oops! {}", e);
                return;
            }
            log.info("host is stopped");
        }
    }
}




