package org.study.stasy.ClientGUI;

import com.intellij.uiDesigner.core.GridLayoutManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.stasy.ChatMessage;
import org.study.stasy.Exeptions.ClientException;
import org.study.stasy.app.Client;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by ASPA on 03.05.2017.
 */
public class ClientApp extends JFrame {
    private static Logger log = LoggerFactory.getLogger(Client.class.getSimpleName());

    private String host = "localhost";
    private String port = "6647";
    private JPanel rootPanel;
    private Client user = null;
    private String userName;
    private JButton sendButton;
    private JTextField messageBox; //многострочное текстовое поле
    private JTextPane chatPane;  //текстовый редактор со стилям
    private JTextField loginField;
    private JButton loginButton;
    private JCheckBox checkBox;
    private JTextField portFiels;
    private JTextField hostField;
    private static final String EXIT_MSG = "@exit";

    private ClientApp() {
        try {
            ImageIcon image = new ImageIcon(this.getClass().getResource("/1461249892_brain.png"));
            this.setIconImage(image.getImage());
            this.setTitle("Group Chat ");
            mainWindow();
        } catch (IOException | ClassNotFoundException e) {
            log.error("constructor error");
        }
    }


    /**
     * if connection with server is failed call this method from Client
     */
    public void connectionFailed() {
        loginButton.setText("Connect");
        loginButton.setIcon(new ImageIcon(this.getClass().getResource("/1457625583_connect.png")));
        checkBox.setSelected(false);
        checkBox.setEnabled(true);
        loginField.setEditable(true);
        sendButton.setEnabled(true);
        try {
            user.sendMsg(EXIT_MSG);
        } catch (IOException e) {
            log.error("user can't send exit-msg");
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }


    private class loginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doConnection();
        }

    }

    private void doConnection() {
        if (loginButton.getText().equals("Connect")) {
            if (loginField.getText().length() < 4) {
                JOptionPane.showMessageDialog(loginButton, "Nick name should contain no less, then 4 symbols",
                        "Nick Name is invalid", JOptionPane.ERROR_MESSAGE);

            } else {

                checkBox.setEnabled(false);
                checkBox.setSelected(false);
                sendButton.setEnabled(true);
                loginButton.setText("Reset");
                chatPane.setText(String.format("%s\n%s [SYSTEM]: WAIT :) ", chatPane.getText(),
                        LocalDateTime.now())); //todo мб есть что-то типо append(msg)
                chatPane.setCaretPosition(chatPane.getText().length());
                loginButton.setIcon(new ImageIcon(this.getClass().getResource("/1457625590_disconnect.png")));
                userName = loginField.getText();
                loginField.setEditable(false);
                host = hostField.getText();
                port = portFiels.getText();


                try {
                    user = new Client(host, port, userName, this);
                    chatPane.setText(String.format("%s\n%s [SYSTEM]: WELCOME! :) ", chatPane.getText(),
                            LocalDateTime.now())); //todo мб есть что-то типо append(msg)
                } catch (ClientException e1) {
                    log.error("", e1);
                    loginButton.setText("Connect");
                    loginButton.setIcon(new ImageIcon(this.getClass().getResource("/1457625583_connect.png")));
                    checkBox.setSelected(false);
                    checkBox.setEnabled(true);
                    loginField.setEditable(true);
                    sendButton.setEnabled(true);
                    chatPane.setText(String.format("%s\n%s [SYSTEM]: TRY AGAIN :) ", chatPane.getText(),
                            LocalDateTime.now())); //todo мб есть что-то типо append(msg)
                    JOptionPane.showMessageDialog(loginButton, "Please, check host & port",
                            "Connection i failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            connectionFailed();
        }

    }

    private void mainWindow() throws IOException, ClassNotFoundException {
        loginField = new JTextField("username", 15);
        loginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doConnection();
                }
            }

        });


        loginButton = new JButton("Connect");
        loginButton.setIcon(new ImageIcon(this.getClass().getResource("/1457625583_connect.png")));

        loginButton.addActionListener(new loginButtonListener());

        hostField = new JTextField(host, 5);
        hostField.setEditable(false);
        portFiels = new JTextField(port, 5);
        portFiels.setEditable(false);

        checkBox = new JCheckBox("Edit host/port", false);
        checkBox.setHorizontalTextPosition(SwingConstants.LEFT);

        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (checkBox.isSelected()) {
                    portFiels.setEditable(true);
                    hostField.setEditable(true);
                } else {
                    portFiels.setEditable(false);
                    hostField.setEditable(false);
                }
            }
        });
        GridBagConstraints startRight = new GridBagConstraints();
        startRight.insets = new Insets(0, 0, 0, 10);
        startRight.anchor = GridBagConstraints.EAST;
        startRight.fill = GridBagConstraints.HORIZONTAL;
        startRight.gridwidth = GridBagConstraints.REMAINDER;


        GridBagConstraints startCenter = new GridBagConstraints();
        startCenter.insets = new Insets(0, 10, 0, 10);
        startCenter.anchor = GridBagConstraints.PAGE_START;


        GridBagConstraints startLeft = new GridBagConstraints();
        startLeft.insets = new Insets(0, 10, 0, 10);
        startLeft.anchor = GridBagConstraints.WEST;


        JPanel clientData = new JPanel(new GridBagLayout());
        clientData.setLayout(new GridBagLayout());
        clientData.add(loginField, startLeft);
        clientData.add(loginButton, startRight);


        JPanel systemData = new JPanel(new GridBagLayout());
        systemData.setLayout(new GridBagLayout());
        systemData.add(checkBox, startLeft);
        systemData.add(hostField, startCenter);
        systemData.add(portFiels, startRight);


        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.add(systemData, startCenter);
        loginPanel.add(clientData, startRight);


        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());

        this.setSize(630, 300);
        this.setLocation(700, 400);
        this.setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel southPanel = new JPanel(); //нижняя
        southPanel.setBackground(Color.lightGray);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField("", 50);
        messageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        sendMsg();
                    } catch (IOException e1) {
                        log.error("sendmsg error", e);
                    } catch (ClientException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });

        messageBox.requestFocus();
        sendButton = new JButton("Send");

        sendButton.setIcon(new ImageIcon(this.getClass().getResource("/1457631818_send.png")));

        sendButton.addActionListener(new ClientApp.ButtonActionSendMsg());
        chatPane = new JTextPane();
        chatPane.setEditable(false);

        this.setResizable(true);
        rootPanel.add(new
                JScrollPane(chatPane), BorderLayout.CENTER);
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512;
        left.weighty = 1;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);

        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 2;
        right.weighty = 2;

        southPanel.add(messageBox, left);
        southPanel.add(sendButton, right);

        rootPanel.add(BorderLayout.SOUTH, southPanel);
        rootPanel.add(BorderLayout.NORTH, loginPanel);
        this.add(rootPanel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO IF (СЕРВЕР РАБОТАЕТ)
                if (user != null) {
                    try {
                        user.sendMsg(EXIT_MSG);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    System.exit(0);
                }
            }
        }); //закрытие окна через крестик


        setVisible(true);


    }


    private void sendMsg() throws IOException, ClientException {
        if (loginButton.getText().equals("Connect")) {
            JOptionPane.showMessageDialog(new JFrame(), "U r not connected to the server");
        } else {
            String text = messageBox.getText();
            if (!Objects.equals(text, "")) {
                user.sendMsg(text);

                ChatMessage userMsg = new ChatMessage(userName, text);
                if (!Objects.equals(text, EXIT_MSG)) {
                    printMytMsg(userMsg);
                    messageBox.setText("");
                } else {
                    loginButton.setText("Connect");
                    loginButton.setIcon(new ImageIcon(this.getClass().getResource("/1457625583_connect.png")));

                    user.shutDownClient();
                    JOptionPane.showMessageDialog(new JFrame(), "Bye bye!");
                }
            }
        }
    }

    private void printMytMsg(ChatMessage userMsg) {
        chatPane.setText(String.format("%s\n%s [Я]: \t%s", chatPane.getText(),
                LocalDateTime.now(), userMsg.getMessage())); //todo мб есть что-то типо append(msg)
        chatPane.setCaretPosition(chatPane.getText().length());
    }


    /**
     * вывод сообщения отправителя на экран
     * called from ButtonActionSendMsg and Client's private class "Listen to server"
     *
     * @param message сообщение, выводимое на экран пользователя с сервера
     */
    public void printReceivedMsg(ChatMessage message) {
        if (!Objects.equals(message.getUserName(), userName)) {
            chatPane.setText(String.format("%s\n%s [%s]: \t%s", chatPane.getText(),
                    LocalDateTime.now(), message.getUserName(), message.getMessage())); //todo мб есть что-то типо append(msg)
            chatPane.setCaretPosition(chatPane.getText().length());
        }
    }


    class ButtonActionSendMsg implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                sendMsg();
            } catch (IOException e1) {
                log.error("sendmsg error", e);
            } catch (ClientException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ClientApp();
    }

}