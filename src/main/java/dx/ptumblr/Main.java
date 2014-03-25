package dx.ptumblr;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;

/**
 * Created by Adrian on 3/22/2014.
 */
public class Main {

    // Set defaults
    private String CONFIG_FILE_PATH = "config.properties";
    private String input_folder = "input";
    private String output_folder = "output";
    private String[] tags = {};

    private PtumblrForm form_ui;
    private String appPath = new File(".").getAbsolutePath();


    //Load config.properties
    private void loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();

        try {
            File fileConfig = new File(fileName);
            InputStream input;

            if (fileConfig.exists()) {
                input = new FileInputStream(fileName);
            } else {
                if (!fileName.startsWith("/")) {
                    fileName = "/" + fileName;
                }
                input = Main.class.getResourceAsStream(fileName);
            }

            if (input != null) {
                properties.load(input);
            } else {
                System.out.println("Can't load properties in " + fileName);
            }
        } catch (Exception e) {
            System.out.println("Can't load properties in " + fileName);
        }

        input_folder = properties.getProperty("input_folder", "input");
        output_folder = properties.getProperty("output_folder", appPath + File.separator + "output");
        String strTags = properties.getProperty("default_tags", "");
        tags = strTags.split(",");
    }


    private void checkFolders() {
        File file_input = new File(input_folder);
        if (!file_input.exists()) {
            JOptionPane.showMessageDialog(null, "The input folder doesn't exist.", "Warning", JOptionPane.WARNING_MESSAGE);
            input_folder = "";
        }

        File file_output = new File(output_folder);
        if (!file_output.exists()) {
            JOptionPane.showMessageDialog(null, "The output folder doesn't exist. It uses the default.", "Warning", JOptionPane.WARNING_MESSAGE);

            //set default
            output_folder = appPath + File.separator + "output";
            file_output = new File(output_folder);
            file_output.mkdirs();
        }
    }

    private void saveProperties(String fileName) {
        try {
            //TODO: Save properties in recurse file
            OutputStream output = new FileOutputStream(fileName);

            Properties properties = new Properties();
            properties.setProperty("input_folder", input_folder);
            properties.setProperty("output_folder", output_folder);
            properties.setProperty("default_tags", StringUtils.join(tags, ","));

            properties.store(output, "");
        } catch (IOException e) {
            System.out.println("Can't save properties in " + fileName);
        }
    }


    //Initialize user interface
    public void initializeUI() {
        form_ui = new PtumblrForm(input_folder, output_folder, tags);
        form_ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                input_folder = form_ui.getInputFolder();
                output_folder = form_ui.getOutputFolder();

                saveProperties(CONFIG_FILE_PATH);
            }
        });
    }


    //Start application
    public void start(String args[]) throws IOException {
        loadProperties(CONFIG_FILE_PATH);
        checkFolders();
        initializeUI();
    }


    public static void main(String args[]) throws IOException {
        new Main().start(args);
    }
}
