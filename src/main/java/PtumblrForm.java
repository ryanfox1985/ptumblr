import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 3/22/2014.
 */
public class PtumblrForm extends JFrame{
    private JProgressBar prbApp;
    private JButton btnSend;
    private JPanel rootPanel;
    private JTextField txtInputFolder;
    private JButton btnInputFolder;
    private JTextField txtOutputFolder;
    private JButton btnOutputFolder;
    private JButton btnScan;
    private JPanel pnlTags;
    private JPanel pnlImage;

    private List<JLabel> lblTags = new ArrayList<JLabel>();


    private void loadTags(String[] tags){
        for(JLabel lbl: lblTags){
            pnlTags.remove(lbl);
        }

        lblTags.clear();

        for(String tag: tags){
            JLabel lblTag = new JLabel(tag);
            lblTag.setOpaque(true);
            lblTag.setBackground(Color.YELLOW);
            lblTag.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel lbl = (JLabel) e.getComponent();
                    if (lbl.getBackground() == Color.GRAY){
                        lbl.setBackground(Color.YELLOW);
                    } else{
                        lbl.setBackground(Color.GRAY);
                    }

                }
            });

            pnlTags.add(lblTag);
            lblTags.add(lblTag);
        }
    }

    private void resetTags(){
        for(JLabel lbl: lblTags){
            lbl.setBackground(Color.YELLOW);
        }
    }


    private void setBtnEvents(){
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPanel,
                        "Eggs are not supposed to be green.");
            }
        });

        btnInputFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser j = new JFileChooser();
                j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                Integer opt = j.showSaveDialog(PtumblrForm.this);


                if(opt == JFileChooser.APPROVE_OPTION){
                    txtInputFolder.setText(j.getSelectedFile().getPath());
                }
            }
        });

        btnOutputFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser j = new JFileChooser();
                j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                Integer opt = j.showSaveDialog(PtumblrForm.this);


                if(opt == JFileChooser.APPROVE_OPTION){
                    txtOutputFolder.setText(j.getSelectedFile().getPath());
                }
            }
        });

        btnScan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickScanFolders();
            }
        });
    }

    public void clickScanFolders(){
//        try {
            //TODO: each input folder.
            resetTags();


            //BufferedImage myPicture = ImageIO.read(new File("C:\\Users\\Adrian\\Downloads\\IMG-20131123-WA0000.jpg"));
            //pnlImage.setIcon(new ImageIcon(myPicture));

//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(rootPanel,
//                    "Image could not be loaded.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }


    public PtumblrForm(String input_folder, String output_folder, String[] tags) {
        super("PTumblr App");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBtnEvents();

        txtInputFolder.setText(input_folder);
        txtOutputFolder.setText(output_folder);

        //TODO: Load labels
        loadTags(tags);
        clickScanFolders();

        setVisible(true);
    }
}
