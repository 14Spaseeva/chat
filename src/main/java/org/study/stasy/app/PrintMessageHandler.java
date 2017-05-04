package org.study.stasy.app;

import org.study.stasy.ChatMessage;
import org.study.stasy.netutils.MessageHandler;
import org.study.stasy.netutils.Session;

import static org.study.stasy.app.Server.getUserList;


public class PrintMessageHandler implements MessageHandler{

    /**
     * обработки сообщения от клиента
     */
    @Override
    public void handle(String name, String msg)
    {
        System.out.println(String.format("[%s] : %s", name, msg));

    }

    @Override
    public void handle(ChatMessage msg, Session session) {
        session.broadcast(session, getUserList().getClientsList(), msg);

    }

}
