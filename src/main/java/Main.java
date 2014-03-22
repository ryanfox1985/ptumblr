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

    public static void main(String args[]) throws IOException {
        Properties properties = new Properties();
        InputStream input = Main.class.getResourceAsStream("/config.properties");
        properties.load(input);

        String inputFolder = properties.getProperty("input_folder");
        String outputFolder = properties.getProperty("output_folder");
        String strTags = properties.getProperty("default_tags");
        String[] tags =  strTags.split(",");

        PtumblrForm form = new PtumblrForm();

    }
}
