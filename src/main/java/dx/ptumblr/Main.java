package dx.ptumblr;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

import com.tumblr.jumblr.JumblrClient;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;

//TODO: Logs system.
//TODO: Support multilabel.
//TODO: Show total images.
//TODO: Append some text.
//TODO: Form refactors.

/**
 * Created by Adrian on 3/22/2014.
 */
public class Main {

    // Set defaults
    private String CONFIG_FILE_PATH = "config.properties";
    private String input_folder = "input";
    private String output_folder = "output";
    private String[] tags = {};

    private String oauth_consumer_key = "";
    private String secret_key = "";
    private String token = "";
    private String token_secret = "";

    private PtumblrForm form_ui;
    private JumblrClient tumblrClient = new JumblrClient();
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
                Reader reader = new InputStreamReader(input, "UTF-8");
                properties.load(reader);
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

        oauth_consumer_key = properties.getProperty("oauth_consumer_key", "");
        secret_key = properties.getProperty("secret_key", "");
        token = properties.getProperty("token", "");
        token_secret = properties.getProperty("token_secret", "");

        try {
            tumblrClient = new JumblrClient(oauth_consumer_key, secret_key);
            tumblrClient.setToken(token, token_secret);
        } catch (IllegalArgumentException e){
            System.out.println("jumblr error-> " + e.getMessage());
        }

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
            //Save properties in resources file
            OutputStream output = new FileOutputStream(fileName);
            Writer writer = new OutputStreamWriter(output, "UTF-8");

            Properties properties = new Properties();
            properties.setProperty("input_folder", input_folder);
            properties.setProperty("output_folder", output_folder);
            properties.setProperty("default_tags", StringUtils.join(tags, ","));

            properties.setProperty("oauth_consumer_key", oauth_consumer_key);
            properties.setProperty("secret_key", secret_key);
            properties.setProperty("token", token);
            properties.setProperty("token_secret", token_secret);

            properties.store(writer, "");
        } catch (IOException e) {
            System.out.println("Can't save properties in " + fileName);
        }
    }


    //Initialize user interface
    public void initializeUI() {
        form_ui = new PtumblrForm(input_folder, output_folder, tags, tumblrClient);
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
