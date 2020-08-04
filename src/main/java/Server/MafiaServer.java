package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class MafiaServer extends Thread{
    private static final int SERVER_PORT = 2345;
    private ServerSocket serverSocket;
    private List<Socket> clientSockets;
    private String localHostAddress;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket();

            localHostAddress = "127.0.0.1";
            serverSocket.bind(new InetSocketAddress(localHostAddress, SERVER_PORT));

            System.out.println("[server] binding! \naddress:" + localHostAddress + ", port:" + SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
