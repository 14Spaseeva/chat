package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.Exeptions.ClientException;

import java.util.LinkedList;

/*
тут складируются все запущенные потоки
 При многократном использовании потоков для решения многочисленных задач,
    издержки создания потока распространяются на многие задачи.
В качестве бонуса, поскольку поток уже существует, когда прибывает запрос, задержка,
    произошедшая из-за создания потока, устраняется.
Таким образом, запрос может быть обработан немедленно,
    что делает приложение более быстрореагирующим.
 */

/**
 * создать новый канал
 * freeWorkers -  те потоки, которые будут исполнять наши задачи. если свободных нет, ждем (свободные рабочие)
 * allWorkers - база работников
 */
public class ThreadPool {
    private static Logger log = LoggerFactory.getLogger("threadPool");

    private final LinkedList<Runnable> allWorkers = new LinkedList<>();
    private final Channel<Runnable> freeWorkers;
    private final int maxSize;
    private final Object lock = new Object();

    /**
     * @param maxSize
     */
    public ThreadPool(int maxSize) {
        this.maxSize = maxSize;
        freeWorkers = new Channel<>(maxSize);
        WorkerThread worker = new WorkerThread(this);
        allWorkers.addLast(worker);
        freeWorkers.put(worker);
    }

    /**
     * if there is no freeWorkers, but allWorkers size != max, create new WorkerThread, put it
     * in allWorkers, freeWorkers
     * pass
     *
     * @param task for execution to free workerThread
     */
    public void execute(Runnable task) {
        synchronized (lock) {
            if (freeWorkers.getSize() == 0) {
                if (allWorkers.size() < maxSize) {
                    WorkerThread worker = new WorkerThread(this);
                    allWorkers.addLast(worker);
                    freeWorkers.put(worker);
                }
            }
        }
        try {
            ((WorkerThread) freeWorkers.get()).execute(task);
        } catch (ClientException e) {
            e.printStackTrace();
            log.trace(" method execute : \n", e);
        }


    }

    /**
     * put @param workerThread to freeWorkers (кладем свободного worker в очередь)
     */
    void onTaskCompleted(WorkerThread workerThread) {
        freeWorkers.put(workerThread);
    }

}
