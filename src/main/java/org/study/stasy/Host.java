package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.netutils.MessageHandler;
import org.study.stasy.netutils.MessageHandlerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host implements Runnable {
    private static Logger log = LoggerFactory.getLogger("host");
    private int portNumber;
    private ServerSocket serverSocket = null;
    private Channel<Runnable> channel;
    private MessageHandler messageHandler;



    Host(int portNumber, Channel<Runnable> channel,
         MessageHandlerFactory messageHandlerFactory) {

        this.portNumber = portNumber;
        this.channel = channel;
        this.messageHandler = messageHandlerFactory.create();
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            log.error("Oops! :", e);
        }
        log.info("New host {} is created", this);
    }


    @Override
    public void run() {
        log.info("Host started on the {} port", portNumber);
        while (true) {
            try {
                Socket socket = serverSocket.accept(); // заставляем сервер ждать подключений
                // Исполнение программы зависает в этом месте, пока клиент не подключится
                channel.put(new Session(socket, messageHandler));
            } catch (IOException e) {
                log.error("Oops! \n {}", e);
            }
        }
    }

      void start(){
        Thread thread =new Thread(this);;
        thread.setName(Host.class.getSimpleName());
        thread.start();

    }

}




