package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

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
            sendToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            BufferedReader serverBuf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;

            message = bufferedReader.readLine();

            printWriter.println(message);
            printWriter.flush();

            while(true) {
                message = bufferedReader.readLine();

                if (message.equals("exit") || message.equals("종료")) {
                    break;
                }

                printWriter.println(message);
                printWriter.flush();

                // sString receiveMessage = serverBuf.readLine();
            }

            String name = "" + ThreadLocalRandom.current().nextInt(2147483647);
            Thread sender = new SendThread(socket, name);
            Thread receiver = new ReceiveThread(socket);

            sender.start();
            receiver.start();

            printWriter.close();
            bufferedReader.close();
            disconnectServer();
        } catch (IOException e) {
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
}
