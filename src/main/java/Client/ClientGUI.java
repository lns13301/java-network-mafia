package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextArea jTextArea = new JTextArea(40,25);
    private JTextField jTextField = new JTextField(25);
    private MafiaClient client = new MafiaClient();
    private static String id;

    public ClientGUI() {
        add(jTextArea, BorderLayout.CENTER);
        add(jTextField, BorderLayout.SOUTH);
        jTextField.addActionListener(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setBounds(800, 100, 400, 600);
        setTitle("MafiaClient");

        client.setGUI(this);
        this.id = "익명";
        client.setId(id);
        client.connect();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = jTextField.getText();

        if (message.equals("/지우기") || message.equals("/c") || message.equals("/ㅈ")) {
            clearChatLog();
        }
        else {
            client.sendMessage(message);
        }
        jTextField.setText("");
    }

    public void appendMessage(String message) {
        jTextArea.append(message);
    }

    public void clearChatLog() {
        jTextArea.setText("");
    }
}
