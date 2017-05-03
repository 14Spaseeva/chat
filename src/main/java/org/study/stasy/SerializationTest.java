package org.study.stasy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by ASPA on 21.04.2017.
 */
public class SerializationTest
{

    public static void main(String args[]) throws Exception {
        Socket socket1;
        int portNumber = 6658;
        String str = "";

        socket1 = new Socket(InetAddress.getLocalHost(), portNumber);

        ObjectInputStream ois = new ObjectInputStream(socket1.getInputStream());

        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());

        str = "initialize";
        oos.writeObject(str);

        while ((str = (String) ois.readObject()) != null) {
            System.out.println(str);
            oos.writeObject("bye");

            if (str.equals("bye bye"))
                break;
        }

        ois.close();
        oos.close();
        socket1.close();
    }
}
