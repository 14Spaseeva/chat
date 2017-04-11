package org.study.stasy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;

public class Client {
    private Logger log = LoggerFactory.getLogger("client");
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private Client(String host, int portNum){
        try {
            socket = new Socket(host, portNum);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

            if (!dataInputStream.readUTF().equals("Sok")){
                log.warn("Client can't be created");
                System.exit(0);
            }
            log.info("Client is started");
        } catch (IOException e) {
            log.error("Socket can't be created");
            e.printStackTrace();
        }
    }

    private void  chat() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String clientMsg="";

        while (!clientMsg.equals("@exit")) {
            try {
                clientMsg = bufferedReader.readLine();
                dataOutputStream.writeUTF(clientMsg);
                log.info("sent");
            } catch (Exception e) {
                if(e.getMessage().contains("Connection reset")){
                    log.error("Server is not connected");
                    socket.close();
                    System.exit(-1);
                }
                else
                    e.printStackTrace();

            }
        }


    }

    public static void main(String[] args) {
        Client newClient = new Client(args[0] ,Integer.parseInt(args[1]));
        try {
            newClient.chat();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
