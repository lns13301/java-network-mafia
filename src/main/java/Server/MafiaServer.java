package Server;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MafiaServer{
    private static final int SERVER_PORT = 2345;
    private ServerSocket serverSocket;
    private String localHostAddress;
    private boolean isRunning;
    private Map<String, DataOutputStream> clients;
    private int connectedCount = 0;
    private String message;

    private ServerGUI serverGUI;
    private JTextArea jTextArea;
    private boolean isCommand;

    public MafiaServer(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    public void setGui(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }

    public void setting() {
        try {
            //Collections.synchronizedMap(clients);
            serverSocket = new ServerSocket();
            clients = new HashMap<>();
            connectedCount = 0;
            isCommand = false;

            localHostAddress = "202.30.32.219";
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
        while (isRunning) {
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
        clients.remove(id);
    }

    public void sendMessage(String id, String message) throws IOException {
        if (isCommand) {
            clients.get(id).writeUTF("");
            isCommand = false;
            return;
        }

        Iterator<String> iterator = clients.keySet().iterator();
        String key;
        jTextArea.append(id + " : " + message + "\n");

        while (iterator.hasNext()) {
            key = iterator.next();
            try {
                clients.get(key).writeUTF(id + " : " + message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        Iterator<String> iterator = clients.keySet().iterator();
        String key;
        jTextArea.append(message + "\n");

        while (iterator.hasNext()) {
            key = iterator.next();
            try {
                clients.get(key).writeUTF(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Receiver extends Thread{
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private String id;

        public Receiver(Socket socket) {
            try{
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                id = dataInputStream.readUTF();
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

                    switch (message) {
                        case "/":
                        case "/도움":
                            sendMessageHelp();
                            isCommand = true;
                            break;
                        case "/게임시작":
                            sendMessage("게임을 시작합니다.\n");
                            isCommand = true;
                            break;
                        case "/참가자":
                            sendMessagePeople();
                            isCommand = true;
                            break;
                    }

                    if (message.length() > 6 && message.startsWith("/이름변경")) {
                        removeClient(id);
                        id = message.substring(6);
                        addClient(id, dataOutputStream);
                        clients.get(id).writeUTF("닉네임이 '" + id + "'(으)로 변경되었습니다.\n");
                        isCommand = true;
                    }

                    sendMessage(id, message);
                }
            }
            catch (IOException e){
                e.printStackTrace();
                removeClient(id);
            }
        }

        public void sendMessageHelp() {
            String message = "\n" + "도움말입니다.\n"
                    + "/이름변경 : /이름변경 OOOO \n(OOOO 부분에 원하는 닉네임 입력 시 변경됨)\n"
                    + "/지우기 (/c, /ㅈ) : 이전까지 작성된 채팅기록을 지웁니다.\n"
                    + "/게임시작 : 게임을 시작할 수 있습니다.\n"
                    + "/규칙 : 게임규칙을 확인할 수 있습니다.\n"
                    + "/직업 : 직업의 종류와 능력을 확인할 수 있습니다.\n"
                    + "/참가자 : 참가인원의 수를 확인합니다.\n\n";
            try {
                clients.get(id).writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessagePeople() {
            String message = connectedCount + "명이 접속중입니다.\n";
            try {
                clients.get(id).writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
