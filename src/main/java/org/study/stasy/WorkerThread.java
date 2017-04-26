package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.Exeptions.*;
//import net.jcip.annotations.GuardedBy;
/**
 * рабочие потоки
 извлекает задачи из очереди и выполняет их. Если очередь пуста, то ожидает,
 пока в очереди не появится новая задача.
 */
public class WorkerThread implements Runnable {
    private static Logger log = LoggerFactory.getLogger("workerThread");
    private final Object lock = new Object();

   //@GuardedBy (lock)
    private final Thread thread;
    // @GuardedBy (lock)
    private final ThreadPool threadPool;
    // @GuardedBy (lock)
    private Runnable currentTask = null;


    public WorkerThread(ThreadPool pool) {
        threadPool = pool;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Если есть задачи на выполнение - выполняем, инача ждем
     * </code> currentTask.run()</code> - выполняется, когда пришла задача
     *В конце
     */
    @Override
    public synchronized void run() {
        while (true) {
            while (currentTask == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    log.trace("method run, lock.wait(), \n {}", e);
                }
            }
            try {
                currentTask.run();
            } catch (RuntimeException e) {
                log.trace("method run, currentTask.run()\n {}", e);
            } finally {
                currentTask = null;
                threadPool.onTaskCompleted(this); //кладем в очередь
            }
        }
    }

    /*
    Вызывается диспетчером, инициируем выполнение задачи,
     */

    /**
     * set </code> task </> to </code> currentTask </>,
     * notify waiting threads.
     * @param task
     * @throws ClientException
     */
    public synchronized void  execute(Runnable task) throws ClientException {
            if (currentTask != null) {
                throw new ClientException("method execute: currentTask != null");
            }
            currentTask = task;
            lock.notify();
        }


}




