package dx.ptumblr;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.tumblr.jumblr.JumblrClient;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;

//TODO: Logs system.
//TODO: Support multilabel.
//TODO: Show total images.
//TODO: Append some text.
//TODO: Form refactors.

public class Main {

    // Set defaults
    private static final String CONFIG_FILE_PATH = "config.properties";

    //Load config.properties
    private PtumblrManager loadProperties() throws IOException {
        Properties properties = new Properties();

        try {
            File fileConfig = new File(CONFIG_FILE_PATH);
            InputStream input;

            if (!fileConfig.exists()) {
                fileConfig.createNewFile();
            }

            input = new FileInputStream(CONFIG_FILE_PATH);

            if (input != null) {
                Reader reader = new InputStreamReader(input, "UTF-8");
                properties.load(reader);
            } else {
                System.out.println("Can't load properties in " + CONFIG_FILE_PATH);
            }
        } catch (Exception e) {
            System.out.println("Can't load properties in " + CONFIG_FILE_PATH);
        }

        String inputFolder = properties.getProperty("input_folder", "");
        String outputFolder = properties.getProperty("output_folder", "");
        String strTags = properties.getProperty("default_tags", "");
        String[] tmpTags = strTags.split(",");
        ArrayList<String> tags = new ArrayList<>();
        Collections.addAll(tags, tmpTags);

        String oauthConsumerKey = properties.getProperty("oauth_consumer_key", "");
        String secretKey = properties.getProperty("secret_key", "");
        String token = properties.getProperty("token", "");
        String tokenSecret = properties.getProperty("token_secret", "");

        PtumblrManager ptumblrManager = new PtumblrManager(inputFolder, outputFolder, tags, oauthConsumerKey, secretKey, token, tokenSecret);

        return ptumblrManager;
    }

    private void saveProperties(PtumblrManager ptumblrManager) {
        try {
            //Save properties in resources file
            OutputStream output = new FileOutputStream(CONFIG_FILE_PATH);
            Writer writer = new OutputStreamWriter(output, "UTF-8");

            Properties properties = new Properties();
            properties.setProperty("input_folder", ptumblrManager.getInputFolder());
            properties.setProperty("output_folder", ptumblrManager.getOutputFolder());
            properties.setProperty("default_tags", StringUtils.join(ptumblrManager.getTags(), ","));

            properties.setProperty("oauth_consumer_key", ptumblrManager.getOauthConsumerKey());
            properties.setProperty("secret_key", ptumblrManager.getSecretKey());
            properties.setProperty("token", ptumblrManager.getToken());
            properties.setProperty("token_secret", ptumblrManager.getTokenSecret());

            properties.store(writer, "");

        } catch (IOException e) {
            System.out.println("Can't save properties in " + CONFIG_FILE_PATH);
        }
    }



    public void connect(PtumblrManager ptumblrManager) {

        final PtumblrConnect ptumblrConnect = new PtumblrConnect(ptumblrManager);
        ptumblrConnect.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                PtumblrManager ptumblrManager = ptumblrConnect.getPtumblrManager();

                if (ptumblrManager.isConnected()){
                    initializeUI(ptumblrManager);
                }
            }
        });
    }

        //Initialize user interface
    public void initializeUI(PtumblrManager ptumblrManager) {
        final PtumblrForm ptumblrForm = new PtumblrForm(ptumblrManager);

        ptumblrForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                PtumblrManager ptumblrManager = ptumblrForm.getPtumblrManager();
                saveProperties(ptumblrManager);
            }
        });
    }


    //Start application
    public void start(String args[]) throws IOException {
        PtumblrManager ptumblrManager = loadProperties();
        connect(ptumblrManager);
    }


    public static void main(String args[]) throws IOException {
        new Main().start(args);
    }
}
