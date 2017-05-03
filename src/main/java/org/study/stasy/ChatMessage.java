package org.study.stasy;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by ASPA on 21.04.2017.
 */
public class ChatMessage implements Serializable //классы которые имплементят этот интерфейсс подлежат сериализации и десериализации
{
    private static final long serialVersionUID = 1L;
    private LocalDateTime time;
    private String userName;
    private String message;

    public ChatMessage(String userName_, String message_) {
        time = LocalDateTime.now();
        userName = userName_;
        message = message_;
    }

    public ChatMessage(String message) {
        this.userName="[server]";
        time = LocalDateTime.now();
        this.message = message;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChatMessage && (this.getMessage().equals(((ChatMessage) obj).getMessage())
                && this.getUserName().equals(((ChatMessage) obj).getUserName())
                && this.getTime().equals(((ChatMessage) obj).getTime()));
    }

//hashcode


    private LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}


