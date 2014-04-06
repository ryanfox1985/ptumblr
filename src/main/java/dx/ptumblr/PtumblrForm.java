package dx.ptumblr;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


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
    private JButton btnDiscard;

    private File currentImage = null;
    private List<JLabel> lblTags = new ArrayList<>();
    private List<File> images = new ArrayList<>();
    private Random rand = new Random();

    private PtumblrManager ptumblrManager;

    public PtumblrManager getPtumblrManager(){
        return ptumblrManager;
    }

    public String getInputFolder() {
        return txtInputFolder.getText();
    }

    public String getOutputFolder() {
        return txtOutputFolder.getText();
    }

    private void loadTags() {
        for (JLabel lbl : lblTags) {
            pnlTags.remove(lbl);
        }

        lblTags.clear();

        for (String tag : ptumblrManager.getDefaultTags()) {
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

    private void moveToOutput() {
        Date now = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String outputFileName = txtOutputFolder.getText() + File.separator + dt.format(now) + currentImage.getName();

        try {
            FileUtils.moveFile(currentImage, new File(outputFileName));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can't move the image to output folder..", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setBtnEvents() {
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendTumbler();
                moveToOutput();
                loadNextImage();
            }
        });

        btnDiscard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveToOutput();
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
        images.clear();
        File inputFolder = new File(txtInputFolder.getText());

        if (inputFolder.getPath().length() > 0) {
            for (File fileEntry : inputFolder.listFiles()) {
                if (!fileEntry.isDirectory() && !fileEntry.getName().startsWith(".")) {
                    //Check extension
                    if (fileEntry.getName().endsWith(".jpg") || fileEntry.getName().endsWith(".jpeg") || fileEntry.getName().endsWith(".gif") || fileEntry.getName().endsWith(".png")) {
                        images.add(fileEntry);
                    }
                }
            }
        }

        prbApp.setMinimum(0);
        prbApp.setMaximum(images.size());
        prbApp.setValue(0);
        prbApp.setStringPainted(true);

        loadNextImage();
    }

    public void loadNextImage() {
        resetTags();
        //TODO: reset custom comment

        if (images.size() > 0) {
            currentImage = images.get(0);

            try {
                BufferedImage myPicture = ImageIO.read(currentImage);

                int height = myPicture.getHeight();
                int width = myPicture.getWidth();

                if (myPicture.getWidth() > lblImage.getWidth()){
                    width = lblImage.getWidth();
                    height = (width * myPicture.getHeight()) / myPicture.getWidth();
                }

                if (height > lblImage.getHeight()){
                    height = lblImage.getHeight();
                    width = (height * myPicture.getWidth()) / myPicture.getHeight();
                }

                Image dimg = myPicture.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(dimg));

                images.remove(0);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(rootPanel,
                        "Image could not be loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(rootPanel,
                    "The input folder is empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        prbApp.setValue(prbApp.getValue() + 1);
    }


    private List<String> getTags(){
        //GET YELLOW TAGS
        List<String> aTags = new ArrayList<String>();
        for (JLabel lbl : lblTags) {
            if (lbl.getBackground() == Color.YELLOW) {
                aTags.add(lbl.getText());
            }
        }

        return aTags;
    }

    public void sendTumbler() {

        if (currentImage != null) {

            List<String> tags = getTags();
            //TODO: append custom tags

            try{
                ptumblrManager.queueImage(currentImage, tags);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPanel,
                        e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public PtumblrForm(PtumblrManager ptumblrManager) {
        super("PTumblr App");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBtnEvents();

        this.ptumblrManager = ptumblrManager;

        File inputFolder = new File(ptumblrManager.getInputFolder());
        if (inputFolder.exists() && inputFolder.isDirectory()){
            txtInputFolder.setText(inputFolder.getAbsolutePath());
        }

        File outputFolder = new File(ptumblrManager.getOutputFolder());
        if (outputFolder.exists() && outputFolder.isDirectory()){
            txtOutputFolder.setText(outputFolder.getAbsolutePath());
        }

        loadTags();

        setVisible(true);
    }
}
