package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MafiaClient extends Thread{
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 2345;
    private Socket socket;

    public MafiaClient() {
        socket = new Socket();
    }

    @Override
    public void run() {
        try {
            socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            String message;

            message = bufferedReader.readLine();

            printWriter.println(message);
            printWriter.flush();

            printWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
