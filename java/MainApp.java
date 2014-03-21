import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainApp
{

	
	
    public void start()
    {
        JFrame f=new JFrame("Type the name of frame");
        JLabel l=new JLabel("Anurag jain(csanuragjain)");
        f.add(l);
        f.setSize(400,400);
        f.setVisible(true);
    }

    public static void main(String args[])
    {
        new MainApp().start();
    }
}
