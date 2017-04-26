package org.study.stasy.app;

import org.study.stasy.netutils.MessageHandler;
import org.study.stasy.netutils.MessageHandlerFactory;

public class PrintMessageHandlerFactory implements MessageHandlerFactory{
    /**
     *  создается и возвращается экземпляр класса PrintMessageHandler
     * @return
     */
    @Override
    public MessageHandler create() {
        return new PrintMessageHandler();
    }
}
