package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 */
public class Dispatcher implements Runnable {

    private static Logger log = LoggerFactory.getLogger("dispatcher");
    private Channel<Runnable> channel;

    public Dispatcher(Channel<Runnable> chan, ThreadPool threadPool) {
        channel = chan;
    }


    @Override
    public void run()  {
        while (true) {
            Runnable task = channel.get();
            Thread client = new Thread(task);
            client.start();
        }
    }

    void start(){
        Thread dispatcher = new Thread(this);
        dispatcher.setDaemon(true);
        dispatcher.setName(Dispatcher.class.getSimpleName());
        dispatcher.start();
    }
}

