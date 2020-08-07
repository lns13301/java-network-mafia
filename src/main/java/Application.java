import Client.MafiaClient;
import Server.MafiaServer;

public class Application {
    public static void main(String[] args) {
        (new MafiaServer()).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        (new MafiaClient()).start();
        (new MafiaClient()).start();
    }
}
