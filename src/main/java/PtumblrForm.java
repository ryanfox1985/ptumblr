import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 3/22/2014.
 */
public class PtumblrForm extends JFrame {
    private JProgressBar prbApp;
    private JButton btnSend;
    private JPanel rootPanel;
    private JTextField txtInputFolder;
    private JButton btnInputFolder;
    private JTextField txtOutputFolder;
    private JButton btnOutputFolder;
    private JButton btnScan;
    private JPanel pnlTags;
    private JLabel lblImage;

    private File currentImage = null;
    private List<JLabel> lblTags = new ArrayList<JLabel>();
    private List<File> images = new ArrayList<File>();


    private void loadTags(String[] tags) {
        for (JLabel lbl : lblTags) {
            pnlTags.remove(lbl);
        }

        lblTags.clear();

        for (String tag : tags) {
            JLabel lblTag = new JLabel(tag);
            lblTag.setOpaque(true);
            lblTag.setBackground(Color.YELLOW);
            lblTag.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel lbl = (JLabel) e.getComponent();
                    if (lbl.getBackground() == Color.GRAY) {
                        lbl.setBackground(Color.YELLOW);
                    } else {
                        lbl.setBackground(Color.GRAY);
                    }

                }
            });

            pnlTags.add(lblTag);
            lblTags.add(lblTag);
        }
    }

    private void resetTags() {
        for (JLabel lbl : lblTags) {
            lbl.setBackground(Color.YELLOW);
        }
    }


    private void setBtnEvents() {
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendTumbler();
                loadNextImage();
            }
        });

        btnInputFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser j = new JFileChooser();
                j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                Integer opt = j.showSaveDialog(PtumblrForm.this);


                if (opt == JFileChooser.APPROVE_OPTION) {
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


                if (opt == JFileChooser.APPROVE_OPTION) {
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

    public void clickScanFolders() {
        File input_folder = new File(txtInputFolder.getText());

        for (File fileEntry : input_folder.listFiles()) {
            if (!fileEntry.isDirectory() && !fileEntry.getName().startsWith(".")) {
                //TODO: Check extension
                images.add(fileEntry);
            }
        }

        //TODO: Check progress bar not working
        prbApp.setMinimum(0);
        prbApp.setMaximum(images.size()-1);
        prbApp.setValue(0);


        loadNextImage();
    }

    public void loadNextImage() {
        resetTags();

        if (images.size() > 0) {
            //TODO: get random image.
            currentImage = images.get(0);

            try {
                BufferedImage myPicture = ImageIO.read(currentImage);
                lblImage.setIcon(new ImageIcon(myPicture));

                images.remove(currentImage);
                prbApp.setValue(prbApp.getValue() + 1);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(rootPanel,
                        "Image could not be loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(rootPanel,
                    "The input folder is empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendTumbler() {

        if(currentImage != null){

            //TODO: GET YELLOW TAGS
            //TODO: send to tumblr.com
            //TODO: mov current image to output.
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
        loadTags(tags);
        clickScanFolders();

        setVisible(true);
    }
}
