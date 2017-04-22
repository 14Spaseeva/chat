package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class Channel <T> {
    private static Logger log = LoggerFactory.getLogger("channel");


    private final int maxSize; // максимальное количество сессий на канале
    private final LinkedList<T> queue = new LinkedList<T>();
    private final Object lock = new Object();

    public Channel(int maxNum_) {
        maxSize = maxNum_;
    }

    public int getChannelSize(){
        return queue.size();
    }

    public void put(T obj) {
        synchronized (lock) {
            while (queue.size() == maxSize) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    log.trace("wait (?)");
                }
            }
            queue.addLast(obj);
            lock.notifyAll();
        }
    }

    public T getFirst() throws InterruptedException {
        synchronized (lock) {
            while (queue.isEmpty()) {
                lock.wait();
            }
            lock.notifyAll();
            return (T) queue.removeFirst();
        }
    }

}
