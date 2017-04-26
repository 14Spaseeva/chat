package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;


/**
 *
 * @param <T>
 */
public class Channel<T> {
    private static Logger log = LoggerFactory.getLogger("channel");

    /**
     * @param maxSize --  максимальное количество сессий на канале
     */
    private final int maxSize;
    private final LinkedList<T> queue = new LinkedList<>();
    private final Object lock = new Object();

    public Channel(int maxNum) {
        maxSize = maxNum;
    }

    public synchronized int getSize() {
        return queue.size();
    }


    public void put(T obj) {
        synchronized (lock) {
            while (queue.size() == maxSize) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    log.trace("wait (?)", e);
                }
            }
            queue.addLast(obj);
            lock.notifyAll();
        }
    }


    public synchronized T get() {
        while (queue.isEmpty()) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                log.trace(" method getFirst() : lock.wait \n {}", e);
            }
        }
        lock.notifyAll();
        return queue.removeFirst();
    }


}
