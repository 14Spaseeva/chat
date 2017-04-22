package org.study.stasy;


import java.io.IOException;
import java.net.ServerSocket;

public class Host {

    public static void main(String[] args) {
        int portNumber;
        try {
            portNumber = Integer.parseInt(args[0]); // получение номера порта из аргументов
        } catch (NumberFormatException e) {
            System.err.println("Server: Wrong port format. Should be integer. Try again.");
            return;
        }
        int maxClientNum;
        try {
            maxClientNum = Integer.parseInt(args[1]); // получение максимального количества подключений
        } catch (NumberFormatException e) {
            System.err.println("Server: Wrong maximum number of connections format. Should be integer. Try again.");
            return;
        }

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.err.println("Server: The port " + portNumber + " is busy.");
            return;
        }

        Thread server = new Thread(new Server(serverSocket, portNumber, maxClientNum));
        server.setName("SERVER");
        server.start();

    }
}



