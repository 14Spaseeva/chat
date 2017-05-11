package org.study.stasy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASPA on 04.05.2017.
 * В итоге было решено не хранить историю. Секретные чаты актуальны :)
 */

public class ChatHistory implements Serializable {
    private List<ChatMessage> history;
    private final int maxSize = 100;

    public ChatHistory() {
        this.history = new ArrayList<ChatMessage>(maxSize);
    }

    public void addMessage(ChatMessage msg) {
        if (this.history.size() > maxSize) {
            this.history.remove(0);
        }
        this.history.add(msg);
    }

    List<ChatMessage> getHistory() {
        return history;
    }


}
