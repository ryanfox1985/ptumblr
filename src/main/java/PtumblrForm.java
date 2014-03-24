import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Adrian on 3/22/2014.
 */
public class PtumblrForm extends JFrame{
    private JProgressBar progressBar1;
    private JButton button1;
    private JPanel rootPanel;
    private JLabel imageLabel;
    private JTextField txtInputFolder;
    private JButton btnInputFolder;
    private JTextField txtOutputFolder;
    private JButton btnOutputFolder;
    private JButton btnScan;


    private void setBtnEvents(){
        button1.addActionListener(new ActionListener() {
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
        try {
            //TODO: each input folder.
            BufferedImage myPicture = ImageIO.read(new File("C:\\Users\\Adrian\\Downloads\\IMG-20131123-WA0000.jpg"));
            imageLabel.setIcon(new ImageIcon(myPicture));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPanel,
                    "Image could not be loaded.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        clickScanFolders();

        setVisible(true);
    }
}
