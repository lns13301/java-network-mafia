package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class Receiver extends Thread{
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String id;

    public Receiver(Socket socket) {
        try{
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            id = "" + ThreadLocalRandom.current().nextInt(2147483647);
        }catch(Exception e){
            System.out.println("예외:"+e);
        }
    }
}
