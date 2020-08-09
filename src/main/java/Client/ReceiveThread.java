package Client;

import java.io.DataInputStream;
import java.net.Socket;

public class ReceiveThread extends Thread{
    private Socket socket;
    private DataInputStream dataInputStream;

    public ReceiveThread(Socket socket){
        this.socket = socket;

        try{
            dataInputStream = new DataInputStream(this.socket.getInputStream());
        }catch(Exception e){
            System.out.println("예외:"+e);
        }
    }

    @Override

    public void run() {
        while (dataInputStream != null) {
            try {
                System.out.println(dataInputStream.readUTF());
            } catch (Exception e) {
                System.out.println("예외:" + e);
            }
        }
    }
}
