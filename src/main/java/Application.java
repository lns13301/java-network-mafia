import Client.MafiaClient;
import Server.ServerGUI;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        new ServerGUI();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

/*        (new MafiaClient()).start();
        (new MafiaClient()).start();*/
    }
}
