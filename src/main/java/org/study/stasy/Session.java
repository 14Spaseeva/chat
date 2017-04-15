package org.study.stasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.net.Socket;

public class Session implements Runnable {
    private Logger log = LoggerFactory.getLogger("session");

    private Socket socket;
    private String clName;

    Session(Socket socket_) {
        this.socket = socket_;
        this.clName= (String.format ("%s:%s", socket.getInetAddress().getHostAddress() , Integer.toString(socket.getPort())));
    }

    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            String message = "";
            while (!message.equals("@exit")) {
                message = dataInputStream.readUTF();
                System.out.println(String.format ("[%s]: %s ", clName,  message));
            }
//            Server.setCurrentClientNum(Server.getCurrentClientNum()-1);
//            log.info(String.format ( "[%s] was stopped", clName));
//            socket.close();

              Server.closeSession ( socket, clName) ;

        } catch (Exception e) {
            if (e.getMessage().equals("Connection reset")) {
//                Server.setCurrentClientNum(Server.getCurrentClientNum()-1);
                Server.closeSession ( socket, clName) ;
                log.error(String.format (" connection was reset by [%s] ", clName));
            }
            else log.error(String.format("Session.run() -> Exception : %s", e)   );
        }
    }



}
