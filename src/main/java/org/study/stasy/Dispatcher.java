package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dispatcher implements Runnable {
    
    private static Logger log = LoggerFactory.getLogger("dispatcher");
    private Channel<Runnable> channel;

    public Dispatcher( Channel<Runnable> chan){
        channel = chan;
    }



    public void run() {
        while (true) {
            try {
                Thread client = new Thread(channel.getFirst());
                client.start();
            } catch (InterruptedException e) {
                log.trace("client can't start");
            }
        }
    }

}

