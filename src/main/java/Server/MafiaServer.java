package Server;

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
    private List<Socket> clientSockets;
    private String localHostAddress;
    private boolean isRunning;
    private Map<String, DataOutputStream> clients;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket();
            clientSockets = new LinkedList<>();
            clients = new HashMap<>();

            localHostAddress = "127.0.0.1";
            serverSocket.bind(new InetSocketAddress(localHostAddress, SERVER_PORT));
            isRunning = true;

            System.out.println("[server] binding! \naddress:" + localHostAddress + ", port:" + SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        waitClientConnection();

        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSockets.get(clientSockets.size() - 1).getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String message;

        while (true) {
            try {
                message = bufferedReader.readLine();

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

        closeServer();
    }

    public void waitClientConnection() {
        try {
            clientSockets.add(serverSocket.accept());

            InetSocketAddress remoteSocketAddress = (InetSocketAddress)clientSockets.get(clientSockets.size() - 1).getRemoteSocketAddress();
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
            }
        }
    }

}
