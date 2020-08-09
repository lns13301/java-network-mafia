package Server;

import sun.net.NetworkClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MafiaServer extends Thread{
    private static final int SERVER_PORT = 2345;
    private ServerSocket serverSocket;
    private List<Socket> clientSockets;
    private String localHostAddress;
    private boolean isRunning;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket();
            clientSockets = new LinkedList<>();

            localHostAddress = "127.0.0.1";
            serverSocket.bind(new InetSocketAddress(localHostAddress, SERVER_PORT));
            isRunning = true;

            System.out.println("[server] binding! \naddress:" + localHostAddress + ", port:" + SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        waitClientConnection();

        BufferedReader tmpBuf = null;

        try {
            tmpBuf = new BufferedReader(new InputStreamReader(clientSockets.get(clientSockets.size() - 1).getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String message;

        while (true) {
            try {
                message = tmpBuf.readLine();

                if (message == null) {
                    System.out.println("[서버] 상대방과 연결이 끊어졌습니다.");
                    break;
                }
                else {
                    System.out.println("[서버] 클라이언트 : " + message);
                    sendToClientMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void waitClientConnection() {
        try {
            Socket clientSocket = serverSocket.accept();
            clientSockets.add(clientSocket);

            InetSocketAddress remoteSocketAddress = (InetSocketAddress)clientSocket.getRemoteSocketAddress();
            String remoteHostName = remoteSocketAddress.getAddress().getHostAddress();
            int remoteHostPort = remoteSocketAddress.getPort();

            System.out.println("[server] connected! \nconnected socket address:" + remoteHostName
                    + ", port:" + remoteHostPort);
            System.out.println("[서버] 현재 연결된 클라이언트 수 : " + clientSockets.size());
            System.out.println("\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onReceivePacket() {

    }

    public void sendToClientMessage(String message) {
        try {
            PrintWriter printWriter = new PrintWriter(clientSockets.get(clientSockets.size() - 1).getOutputStream());

            printWriter.println(message);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
