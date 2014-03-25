package dx.ptumblr;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.PhotoPost;
import org.apache.commons.io.FileUtils;
import com.tumblr.jumblr.types.User;
import com.tumblr.jumblr.types.Blog;

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
    private JButton btnCancel;

    private File currentImage = null;
    private List<JLabel> lblTags = new ArrayList<JLabel>();
    private List<File> images = new ArrayList<File>();
    private Random rand = new Random();
    private JumblrClient tumblrClient;

    public String getInputFolder() {
        return txtInputFolder.getText();
    }

    public String getOutputFolder() {
        return txtOutputFolder.getText();
    }

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

    private void moveCurrentImage() {
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
                prbApp.setValue(prbApp.getValue() + 1);
                sendTumbler();
                loadNextImage();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prbApp.setValue(prbApp.getValue() + 1);
                moveCurrentImage();
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
        File input_folder = new File(txtInputFolder.getText());

        if (input_folder.getPath().length() > 0) {
            for (File fileEntry : input_folder.listFiles()) {
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
        //TODO: reset custom labels tags

        if (images.size() > 0) {
            //TODO: get random image.
            int indexImage = (rand.nextInt() % images.size()) + 1;
            currentImage = images.get(indexImage);

            try {
                BufferedImage myPicture = ImageIO.read(currentImage);

                int height, width;
                float ratio;

                if (myPicture.getHeight() > myPicture.getWidth()) {
                    width = lblImage.getWidth();
                    ratio = (float) myPicture.getHeight() / myPicture.getWidth();
                    height = (int) (lblImage.getHeight() / ratio);
                } else {
                    height = lblImage.getHeight();
                    ratio = (float) myPicture.getWidth() / myPicture.getHeight();
                    width = (int) (lblImage.getWidth() / ratio);
                }

                Image dimg = myPicture.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(dimg));

                images.remove(currentImage);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(rootPanel,
                        "Image could not be loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(rootPanel,
                    "The input folder is empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void postImage(List<String> tags) {
        // Write the user's name
        User user = tumblrClient.user();
        System.out.println(user.getName());

        // And list their blogs
        for (Blog blog : user.getBlogs()) {
            System.out.println("\t" + blog.getName());
            try {
                PhotoPost post = tumblrClient.newPost(blog.getName(), PhotoPost.class);

                //TODO: generate comment
                //post.setQuote("hello world");
                post.setData(currentImage);
                post.setState("queue");
                post.setTags(tags);
                post.save();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPanel,
                        e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void sendTumbler() {

        if (currentImage != null) {
            //GET YELLOW TAGS
            List<String> aTags = new ArrayList<String>();
            for (JLabel lbl : lblTags) {
                if (lbl.getBackground() == Color.YELLOW) {
                    aTags.add(lbl.getText());
                }
            }

            //TODO: append custom tags


            //TODO: send to tumblr.com
            postImage(aTags);

            //move file.
            moveCurrentImage();
        }


    }


    public PtumblrForm(String input_folder, String output_folder, String[] tags, JumblrClient tumblrClient) {
        super("PTumblr App");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBtnEvents();

        this.tumblrClient = tumblrClient;
        txtInputFolder.setText(input_folder);
        txtOutputFolder.setText(output_folder);

        loadTags(tags);
        clickScanFolders();

        setVisible(true);
    }
}
