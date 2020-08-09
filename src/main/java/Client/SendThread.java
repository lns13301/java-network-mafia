package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SendThread extends Thread{
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private String name;

    public SendThread(Socket socket, String name){

        this.socket = socket;

        try{
            dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            this.name = name;
        }catch(Exception e){
            System.out.println("예외:"+e);
        }

    }

    @Override
    public void run(){
        Scanner s = new Scanner(System.in);
        try {
            dataOutputStream.writeUTF(name);
        } catch (IOException e) {
            System.out.println("예외:"+e);
        }
        while(dataOutputStream!=null){
            try {
                dataOutputStream.writeUTF(name+" : "+s.nextLine());
            } catch (IOException e) {
                System.out.println("예외:"+e);
            }
        }
    }
}
