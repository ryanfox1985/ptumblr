package dx.ptumblr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;


public class PtumblrConnect extends JFrame{
    private JTextField txtOauthConsumerKey;
    private JTextField txtSecretKey;
    private JTextField txtToken;
    private JTextField txtTokenSecret;
    private JButton btnConnect;
    private JLabel lblConnectionStatus;
    private JLabel lblTokenSecret;
    private JLabel lblToken;
    private JLabel lblSecretKey;
    private JLabel lblOauthConsumerKey;
    private JLabel lblIntroText;
    private JPanel rootPanel;

    private PtumblrManager ptumblrManager;

    public PtumblrConnect(PtumblrManager ptumblrManager) {
        super("PTumblr App");
        setContentPane(rootPanel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addListeners();

        this.ptumblrManager = ptumblrManager;

        fillForm();

        setVisible(true);

        if (ptumblrManager.validateApiParams()){
            connect();
        }
    }

    public PtumblrManager getPtumblrManager(){
        return ptumblrManager;
    }

    public void closeWindow()
    {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                PtumblrConnect.this.dispatchEvent(new WindowEvent(PtumblrConnect.this, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    private void addListeners(){
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ptumblrManager.setOauthConsumerKey(txtOauthConsumerKey.getText());
                ptumblrManager.setSecretKey(txtSecretKey.getText());
                ptumblrManager.setToken(txtToken.getText());
                ptumblrManager.setTokenSecret(txtTokenSecret.getText());
                connect();
            }
        });
    }


    private void fillForm(){
        txtOauthConsumerKey.setText(ptumblrManager.getOauthConsumerKey());
        txtSecretKey.setText(ptumblrManager.getSecretKey());
        txtToken.setText(ptumblrManager.getToken());
        txtTokenSecret.setText(ptumblrManager.getTokenSecret());
        lblConnectionStatus.setText("");
    }

    private void connect(){
        enableForm(false);
        lblConnectionStatus.setText("Connecting to Tumnblr API... please wait");
        rootPanel.repaint();


        if (ptumblrManager.connect()){
            lblConnectionStatus.setText("Connected!");
            closeWindow();
            return;
        }

        lblConnectionStatus.setText("Could not connect. Please recheck your API credentials and try again.");
        enableForm(true);
    }

    private void enableForm(boolean enabled){
        txtOauthConsumerKey.setEnabled(enabled);
        txtSecretKey.setEnabled(enabled);
        txtToken.setEnabled(enabled);
        txtTokenSecret.setEnabled(enabled);
        btnConnect.setEnabled(enabled);
    }
}


