package org.study.stasy.concurrentutils;

import org.study.stasy.Exeptions.DispatcherException;
import org.study.stasy.Exeptions.SessionException;

import java.io.IOException;

/**
 * Created by ASPA on 26.04.2017.
 */
public interface Stoppable extends Runnable {

    void stop() throws IllegalAccessException, SessionException, IOException, DispatcherException;
}
