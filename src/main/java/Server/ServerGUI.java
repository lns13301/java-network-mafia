package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerGUI extends JFrame implements ActionListener {
    private static final long serialVersionUID =1L;
    private JTextArea jTextArea = new JTextArea(40,25);
    private JTextField jTextField = new JTextField(25);
    private MafiaServer server = new MafiaServer();

    public ServerGUI() throws IOException {

        add(jTextArea, BorderLayout.CENTER);
        add(jTextField, BorderLayout.SOUTH);
        jTextField.addActionListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setBounds(200, 100, 400, 600);
        setTitle("MafiaServer");
        server.setGui(this);
        server.setting();
    }

    public static void main(String[] args) throws IOException{
        new ServerGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = "서버 : " +jTextField.getText() + "\n";
        System.out.print(message);
        server.sendMessage(message);
        appendMessage(message);
        jTextField.setText("");
    }

    public void appendMessage(String message) {
        jTextArea.append(message);
    }
}
