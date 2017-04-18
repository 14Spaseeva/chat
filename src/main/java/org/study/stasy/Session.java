package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {
    private Logger log = LoggerFactory.getLogger("session");

    private Server server;
    private Socket socket;
    private String clName;



    Session(Server serv, Socket socket_) {
        server = serv;
        socket = socket_;
        clName= (String.format ("%s:%s", socket.getInetAddress().getHostAddress() , Integer.toString(socket.getPort())));
    }

    public void run() {
        try {

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            try {
                dataOutputStream.writeUTF("Sok");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String message = "";
            while (!message.equals("@exit")) {
                message = dataInputStream.readUTF();
                System.out.println(String.format ("[%s]: %s ", clName,  message));
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Connection reset")) {
                log.error(String.format (" connection was reset by [%s] ", clName));
            }
            else log.error(String.format("Session.run() -> Exception : %s", e)   );
        }
        finally {
            server.closeSession ( socket, clName) ;
        }
    }
}
