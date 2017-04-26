package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.netutils.MessageHandlerFactory;

public class Server {
    private static Logger log = LoggerFactory.getLogger(Server.class.getSimpleName());


    /**
     * @param maxClientNum               -- получение максимального количества подключений
     * @param portNumber                 -- получение номера порта из аргументов
     * @param classMessageHandlerFactory -- название класса реализации фабрики
     * @param messageHandlerFactory
     * @param args
     * @param channel
     */
    private static void setParams(int maxClientNum, int portNumber,
                                  Class classMessageHandlerFactory, MessageHandlerFactory messageHandlerFactory,
                                  Channel<Runnable> channel,
                                  String[] args) {
        try {
            portNumber = Integer.parseInt(args[0]);
            maxClientNum = Integer.parseInt(args[1]);
            classMessageHandlerFactory = Class.forName(args[2]);
            messageHandlerFactory = (MessageHandlerFactory) classMessageHandlerFactory.newInstance();
            channel = new Channel<Runnable>(maxClientNum);
        } catch (NumberFormatException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.error("Wrong params \n {}", e);
            return;
        }
    }

    /**
     *
     * @param args
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException {
        int maxClientNum = 0;
        int portNumber = 0;
        Class classMHFactory = null;
        MessageHandlerFactory mHFactory = null;
        Channel<Runnable> channel = null;

        setParams(maxClientNum, portNumber, classMHFactory, mHFactory, channel, args);

        new Host(portNumber, channel, mHFactory).start();
        new Dispatcher(channel, new ThreadPool(maxClientNum)).start();
    }


}
