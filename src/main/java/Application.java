import Client.ClientGUI;
import Server.ServerGUI;

import java.io.IOException;
import java.util.Scanner;

public class Application {
    private static String id;

    public static void main(String[] args) throws IOException {
        new ServerGUI();

//        initialize();
    }

    public static void initialize() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("닉네임을 설정하세요 : ");
        id = scanner.nextLine();
        scanner.close();
        new ClientGUI(id);
    }
}
