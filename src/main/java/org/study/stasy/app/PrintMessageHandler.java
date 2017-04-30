package org.study.stasy.app;

import org.study.stasy.netutils.MessageHandler;


public class PrintMessageHandler implements MessageHandler{

    /**
     * обработки сообщения от клиента
     */
    @Override
    public void handle(String name, String msg) {
        System.out.println(String.format("[%s] : %s", name, msg));
    }
}
