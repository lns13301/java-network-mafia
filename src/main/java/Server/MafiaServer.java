package Server;

import Client.ReceiveThread;
import sun.net.NetworkClient;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MafiaServer extends Thread{
    private static final int SERVER_PORT = 2345;
    private ServerSocket serverSocket;
    private String localHostAddress;
    private boolean isRunning;
    private Map<String, DataOutputStream> clients;
    private int connectedCount;

    @Override
    public void run() {
        try {
            Collections.synchronizedMap(clients);
            serverSocket = new ServerSocket();
            clients = new HashMap<>();
            connectedCount = 0;

            localHostAddress = "127.0.0.1";
            serverSocket.bind(new InetSocketAddress(localHostAddress, SERVER_PORT));
            isRunning = true;

            System.out.println("[server] binding! \naddress:" + localHostAddress + ", port:" + SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        waitClientConnection();
        closeServer();
    }

    public void waitClientConnection() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                connectedCount++;

                InetSocketAddress remoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
                String remoteHostName = remoteSocketAddress.getAddress().getHostAddress();
                int remoteHostPort = remoteSocketAddress.getPort();

                System.out.println("[server] connected! \nconnected socket address:" + remoteHostName
                        + ", port:" + remoteHostPort);
                System.out.println("\n");

                Receiver receiver = new Receiver(socket);
                receiver.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onReceivePacket() {

    }

    public void closeServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String id, DataOutputStream dataOutputStream) {
        String message = id + "님이 접속하였습니다.";
        sendMessage(message);
        clients.put(id, dataOutputStream);
    }

    public void removeClient(String id) {
        String message = id + "님이 퇴장하였습니다.";
        sendMessage(message);
    }

    public void sendMessage(String message) {
        Iterator<String> iterator = clients.keySet().iterator();
        String key = "";

        while (iterator.hasNext()) {
            key = iterator.next();
            try {
                clients.get(key).writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Receiver extends Thread{
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private String id;
        private String message;

        public Receiver(Socket socket) {
            try{
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                id = "" + ThreadLocalRandom.current().nextInt(2147483647);
                addClient(id, dataOutputStream);
            }catch(Exception e){
                System.out.println("예외:"+e);
            }
        }

        @Override
        public void run() {
            try {
                while (dataInputStream != null) {
                    message = dataInputStream.readUTF();
                    sendMessage(message);
                }
            }
            catch (IOException e){
                e.printStackTrace();
                removeClient(id);
            }
        }
    }

}
