package Client;

import java.io.DataInputStream;
import java.net.Socket;

public class ReceiveThread extends Thread{
    private Socket socket;
    private DataInputStream in;

    public ReceiveThread(Socket socket){
        this.socket = socket;

        try{
            in = new DataInputStream(this.socket.getInputStream());
        }catch(Exception e){
            System.out.println("예외:"+e);
        }
    }

    @Override

    public void run() {
        while (in != null) {
            try {
                System.out.println(in.readUTF());
            } catch (Exception e) {
                System.out.println("예외:" + e);
            }
        }
    }
}
