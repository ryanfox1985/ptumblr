package dx.ptumblr;


import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PtumblrManager {

    private String inputFolder;
    private String outputFolder;
    private ArrayList<String> defaultTags;

    private String oauthConsumerKey;
    private String secretKey;
    private String token;
    private String tokenSecret;

    private JumblrClient tumblrClient;

    private boolean connected;

    public PtumblrManager(String inputFolder, String outputFolder, ArrayList<String> defaultTags, String oauthConsumerKey, String secretKey, String token, String tokenSecret) {
        this.inputFolder = inputFolder;
        this.outputFolder = outputFolder;
        this.defaultTags = defaultTags;
        this.oauthConsumerKey = oauthConsumerKey;
        this.secretKey = secretKey;
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.connected = false;
    }

    public boolean connect(){
        try {
            tumblrClient = new JumblrClient(oauthConsumerKey, secretKey);
            tumblrClient.setToken(token, tokenSecret);
            tumblrClient.user();
            connected = true;
        } catch (IllegalArgumentException e){
            System.out.println("Jumblr error; " + e.getMessage());
            connected = false;
        }
        catch (JumblrException e){
            System.out.println("Jumblr error: " + e.getMessage());
            connected = false;
        }

        return connected;
    }

    public boolean validateApiParams(){
        return !oauthConsumerKey.isEmpty() && !secretKey.isEmpty() && !token.isEmpty() && !tokenSecret.isEmpty();
    }

    public void queueImage(File imageFile, List<String> tags) throws InstantiationException, IllegalAccessException, IOException {
        User user = tumblrClient.user();
        System.out.println(user.getName());

        for (Blog blog : user.getBlogs()) {
            System.out.println("\t" + blog.getName());

                PhotoPost post = tumblrClient.newPost(blog.getName(), PhotoPost.class);

                //TODO: generate comment
                //post.setQuote("hello world");
                post.setData(imageFile);
                post.setState("queue");
                post.setTags(tags);
                post.save();
        }
    }

    public boolean isConnected(){
        return connected;
    }

    public String getInputFolder() {
        return inputFolder;
    }

    public void setInputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public ArrayList<String> getDefaultTags() {
        return defaultTags;
    }

    public void setDefaultTags(ArrayList<String> defaultTags) {
        this.defaultTags = defaultTags;
    }

    public String getOauthConsumerKey() {
        return oauthConsumerKey;
    }

    public void setOauthConsumerKey(String oauthConsumerKey) {
        this.oauthConsumerKey = oauthConsumerKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }
}
