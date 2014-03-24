import com.sun.deploy.util.StringUtils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by Adrian on 3/22/2014.
 */
public class Main {

    // Set defaults
    private String input_folder = "input";
    private String output_folder = "output";
    private String[] tags = {};

    private PtumblrForm form_ui;


    //Load config.properties
    private void loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();

        try {
            InputStream input = Main.class.getResourceAsStream(fileName);
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Can't load properties in " + fileName);
        }

        input_folder = properties.getProperty("input_folder", "input");
        output_folder = properties.getProperty("output_folder", "output");
        String strTags = properties.getProperty("default_tags", "");
        tags = strTags.split(",");
    }

    private void saveProperties(String fileName) {
        try {
            OutputStream output = new FileOutputStream(fileName);

            Properties properties = new Properties();
            properties.setProperty("input_folder", input_folder);
            properties.setProperty("output_folder", output_folder);
            properties.setProperty("default_tags", org.apache.commons.lang.StringUtils.join(tags, ","));

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
            public void windowClosed(WindowEvent e) {
                saveProperties("/config.properties");
            }
        });
    }


    //Start application
    public void start(String args[]) throws IOException {
        loadProperties("/config.properties");
        initializeUI();
    }


    public static void main(String args[]) throws IOException {
        new Main().start(args);
    }
}
