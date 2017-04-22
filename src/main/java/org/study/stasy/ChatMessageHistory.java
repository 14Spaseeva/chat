package org.study.stasy;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by ASPA on 21.04.2017.
 */
public class ChatMessageHistory implements Serializable //классы которые имплементят этот интерфейсс подлежат сериализации и десериализации
{
    private LocalDateTime time;
    private String userName;
    private String message;

    ChatMessageHistory(LocalDateTime time_, String userName_, String message_){
        time=time_;
        userName=userName_;
        message=message_;
    }

//    private  static  final long serialVersionUID


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChatMessageHistory)) return false;
        return (this.getMessage().equals(((ChatMessageHistory) obj).getMessage())
                && this.getUserName().equals (((ChatMessageHistory) obj).getUserName())
                && this.getTime().equals(((ChatMessageHistory) obj).getTime()));
    }


//    @Override
//    hashcod

    public LocalDateTime getTime() {
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


