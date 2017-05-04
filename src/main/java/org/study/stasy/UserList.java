package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.Exeptions.ClientException;
import org.study.stasy.app.Client;
import org.study.stasy.netutils.Session;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by ASPA on 04.05.2017.
 */
public class UserList {
    private Logger log = LoggerFactory.getLogger(UserList.class.getSimpleName());

    private Map<String, Client> onlineUsers = new HashMap<String, Client>();

    public void addUser(String userName, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        try {
            log.info("[{}] connected", userName);
            if (!this.onlineUsers.containsKey(userName)) {
                this.onlineUsers.put(userName, new Client(socket, oos, ois));

            } else {
                int i = 1;
                while (this.onlineUsers.containsKey(userName)) {
                    userName = userName + i;
                    i++;
                }
                this.onlineUsers.put(userName, new Client(socket, oos, ois));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String login) {
        this.onlineUsers.remove(login);
    }

    public String[] getUsers() {
        return this.onlineUsers.keySet().toArray(new String[0]);
    }

    public ArrayList<Client> getClientsList() {
        ArrayList<Client> clientsList = new ArrayList<>(this.onlineUsers.entrySet().size());
        String s = "";
        for (Map.Entry<String, Client> m : this.onlineUsers.entrySet()) {
            clientsList.add(m.getValue());
            System.out.println(m.getKey());
            s = s + m.getKey();
        }

        return clientsList;
    }

}
