package org.study.stasy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASPA on 04.05.2017.
 */
public class ChatHistory {
    private List<ChatMessage> history;
    private final int  maxSize = 100;
    public ChatHistory (){
        this.history = new ArrayList<>(maxSize);
    }
  public  void addMessage (ChatMessage msg){
        if (this.history.size() > maxSize){
            this.history.remove(0);
        }
        this.history.add(msg);
    }

    List<ChatMessage> getHistory(){
        return history;
    }


}
