package org.study.stasy.netutils;

import org.study.stasy.ChatMessage;

public interface MessageHandler {
    void handle (String name, String msg);
    void handle (ChatMessage msg, Session session);

}
