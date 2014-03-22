import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public PtumblrForm() {
        super("PTumblr App");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPanel,
                        "Eggs are not supposed to be green.");
            }
        });

        try {
            BufferedImage myPicture = ImageIO.read(new File("C:\\Users\\Adrian\\Downloads\\IMG-20131123-WA0000.jpg"));
            imageLabel.setIcon(new ImageIcon(myPicture));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPanel,
                    "Image could not be loaded.", "Error", JOptionPane.ERROR_MESSAGE);
        }




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

        setVisible(true);
    }
}
