package org.study.stasy.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.concurrentutils.*;
import org.study.stasy.Exeptions.DispatcherException;
import org.study.stasy.Exeptions.TreadPoolException;
import org.study.stasy.netutils.Host;
import org.study.stasy.netutils.MessageHandlerFactory;
import sun.plugin2.message.ShutdownJVMMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.ShutdownChannelGroupException;
import java.sql.Time;
import java.time.LocalDateTime;

public class Server {
    private static Logger log = LoggerFactory.getLogger(Server.class.getSimpleName());

    private static int maxSessionNum;
    private static int portNumber;
    private static Class classMHFactory;
    private static MessageHandlerFactory mHFactory;
    private static Channel<Stoppable> channel;
    private static Host host;
    private static Dispatcher dispatcher;
    private static ThreadPool threadPool;


    private Server(String port, String maxSN, String className) {
        try {
            portNumber = Integer.parseInt(port);
            maxSessionNum = Integer.parseInt(maxSN);
            classMHFactory = Class.forName(className);
            mHFactory = (MessageHandlerFactory) classMHFactory.newInstance();
            channel = new Channel<>(maxSessionNum);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.error("{}", e);
        }

    }

    private void launch() {
        // shutdown-ловушка
        MyShutdownHook shutdownHook = new MyShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        host = new Host(portNumber, channel, mHFactory);
        host.start();
        threadPool = new ThreadPool(maxSessionNum);
        dispatcher = new Dispatcher(channel, threadPool); //
        dispatcher.start();
    }


    public static void main(String[] args) {
        Server server = new Server(args[0], args[1], args[2]);
        server.launch();


    }

    private class MyShutdownHook extends Thread {
        public void run() {
            shutdown();
        }

    }

    private void shutdown() {
        try {

            log.info("Shutting down");
            host.stop();
            threadPool.stop();
            dispatcher.stop();

            log.info("Good night!");
        } catch (TreadPoolException | DispatcherException e1) {
            log.error("error of shutting down");
        }
    }

}




