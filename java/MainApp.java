import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.gson.Gson;

public class MainApp {
	public Map<Object, Object> main_settings = new HashMap<>();

	public Map<Object, Object> setDefaultsSettings() {
		Map<Object, Object> defSettings = new HashMap<>();

		//TODO: Initialize all settings.
		
		return defSettings;
	}

	public Map<Object, Object> loadSettings(String fileName) {
		Map<Object, Object> settings = setDefaultsSettings();

		try {
			File file = new File(fileName);

			if (file.exists()) {
				InputStream inputStream = new FileInputStream(file);

				if (inputStream != null) {					
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					settings = new Gson().fromJson(reader, Map.class);

					//TODO: Check all parameters.
				}
			}
		} catch (final Exception e) {
			
			//TODO: Put a message.
		}
		
		return settings;
	}

	public void start() {
		main_settings = loadSettings("config.json");

		
		//TODO: Create folders.
		
		
		//TODO: Create form.
		
		
		//TODO: Create slides.
			
		
		
		JFrame f = new JFrame("Type the name of frame");
		JLabel l = new JLabel("Anurag jain(csanuragjain)");
		f.add(l);
		f.setSize(400, 400);
		f.setVisible(true);
	}

	public static void main(String args[]) {
		new MainApp().start();
	}
}
