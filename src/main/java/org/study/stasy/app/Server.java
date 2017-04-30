package org.study.stasy.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.concurrentutils.*;
import org.study.stasy.Exeptions.DispatcherException;
import org.study.stasy.Exeptions.TreadPoolException;
import org.study.stasy.netutils.Host;
import org.study.stasy.netutils.MessageHandlerFactory;

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



    /**
     *
     * */
    public static void main(String[] args) {
        setParams(args[0], args[1], args[2]);
        launchServer();
    }

    private static void setParams(String port, String maxSN, String className) {
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

    private static void launchServer() {
        // shutdown-ловушка
/*        MyShutdownHook shutdownHook = new MyShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);*/
        host = new Host(portNumber, channel, mHFactory);
        host.start();
        threadPool = new ThreadPool(maxSessionNum);
        dispatcher = new Dispatcher(channel, threadPool); //
        dispatcher.start();
    }
    private static class MyShutdownHook extends Thread {

        public void run() {
            shutdown();
        }
   }

    private static void shutdown() {
        log.info("Shutting down");
        try {
            host.stop();
            threadPool.stop();
            dispatcher.stop();
        } catch (TreadPoolException | DispatcherException e) {
            log.warn("during shutting down: {}", e);
        }
        log.info("Good night!");
    }

}


