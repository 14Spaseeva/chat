package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.app.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ASPA on 04.05.2017.
 */

//todo синхронизация,
public class UserList {
    private Logger log = LoggerFactory.getLogger(UserList.class.getSimpleName());

    private Map<String, Client> onlineUsers = new HashMap<String, Client>();

   synchronized public void addUser(String userName, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        try {
            if (!this.onlineUsers.containsKey(userName)) {
                this.onlineUsers.put(userName, new Client(socket, oos, ois));

            } else {
                int i = 1;
                while (this.onlineUsers.containsKey(userName)) {
                    userName = userName + i;
                    i++;
                }
                this.onlineUsers.put(userName, new Client(socket, oos, ois));
                log.info("[{}] is added", userName);

            }
        } catch (Exception e) {
            log.error("user can't be added to the userList");
        }
    }

   synchronized public void deleteUser(String login) {
        this.onlineUsers.remove(login);
        log.info("user [{}] is deleted from userList", login);
    }

  synchronized public ArrayList<Client> getClientsList() {
        ArrayList<Client> clientsList = new ArrayList<>(this.onlineUsers.entrySet().size());
        String s = "";
        for (Map.Entry<String, Client> m : this.onlineUsers.entrySet()) {
            clientsList.add(m.getValue());
            System.out.println(m.getKey()); //вывод юзера на экран
            s = s + m.getKey();
        }

        return clientsList;
    }

}
