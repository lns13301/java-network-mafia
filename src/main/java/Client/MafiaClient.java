package Client;

import java.io.IOException;
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
}
