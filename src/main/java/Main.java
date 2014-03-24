import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private void loadProperties(String fileName) throws IOException{
        Properties properties = new Properties();
        InputStream input = Main.class.getResourceAsStream(fileName);
        properties.load(input);

        input_folder = properties.getProperty("input_folder", "input");
        output_folder = properties.getProperty("output_folder", "output");
        String strTags = properties.getProperty("default_tags", "");
        tags =  strTags.split(",");
    }


    //Initialize user interface
    public void initializeUI(){
        form_ui = new PtumblrForm(input_folder, output_folder, tags);
    }


    //Start application
    public void start(String args[]) throws IOException{
        loadProperties("/config.properties");
        initializeUI();
    }


    public static void main(String args[]) throws IOException {
        new Main().start(args);
    }
}
