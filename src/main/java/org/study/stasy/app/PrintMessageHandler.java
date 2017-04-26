package org.study.stasy.app;

import org.study.stasy.netutils.MessageHandler;


public class PrintMessageHandler implements MessageHandler{

    /**
     * обработки сообщения от клиента (в нашем случае - вывод сообщения в консоль)
     * @param msg
     */
    @Override
    public void handle(String msg) {
        System.out.printf(msg);
    }
}
