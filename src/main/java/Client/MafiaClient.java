package Client;

import java.net.Socket;

public class MafiaClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 2345;
    private Socket socket;

    public MafiaClient() {
        socket = new Socket();
    }
}
