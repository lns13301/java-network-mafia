package Client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class MafiaClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 2345;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ClientGUI clientGUI;
    private String message;
    private String id;

    public MafiaClient() {
        socket = new Socket();
    }

    public void setGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    public void connect() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("서버 연결 완료");

            sendToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            dataOutputStream.writeUTF(id);
            System.out.println("채팅방 입장");

            while (dataInputStream != null) {
                message = dataInputStream.readUTF();
                clientGUI.appendMessage(message);
            }

            disconnectServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            dataOutputStream.writeUTF(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectServer() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }
}
