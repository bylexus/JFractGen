package ch.alexi.fractgen.logic;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;
import ch.alexi.fractgen.gui.MainFrame;
import ch.alexi.fractgen.models.FractCalcerResultData;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.FractParamPresets;

/**
 * The AppManager offers some application-wide functions and data. Singleton. 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class AppManager implements ApplicationListener{
	private static AppManager inst = new AppManager();
	private MainFrame mainFrame;
	private Stack<FractCalcerResultData> history;
	private JSONObject presets;
	private JSONObject userPresets;
	private Properties userProperties;
	
	private FractParamPresets userFractParamPresets;
	private FractParamPresets systemFractParamPresets;
	
	private AppManager() {
		this.history = new Stack<FractCalcerResultData>();
		this.userProperties = new Properties();
		this.loadUserSettings();
	}
	
	public static AppManager getInstance() {
		return inst;
	}
	
	/**
	 * Called directly from the Main class, the AppManager is responsible
	 * for setting up and displaying the GUI. It also starts the 
	 * first calculation.
	 * 
	 * @return
	 */
	public MainFrame createAndShowGUI() {
		UIManager.put("Label.font",new Font("Sans Serif",Font.PLAIN,11));
		UIManager.put("ComboBox.font",new Font("Sans Serif",Font.PLAIN,11));
		UIManager.put("TextField.font",new Font("Sans Serif",Font.PLAIN,11));
		UIManager.put("Button.font",new Font("Sans Serif",Font.PLAIN,11));

		
		if (mainFrame == null) {
			// Create and set up the window.
	        mainFrame = new MainFrame("JFractGen");
	        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	        mainFrame.addWindowListener(new WindowAdapter() {
	        	@Override
	        	public void windowClosing(WindowEvent e) {
	        		shutdown();
	        	}
			});
	        mainFrame.pack();
	        mainFrame.setVisible(true);
	        mainFrame.setFractParam(this.getSystemPresets().get(0));
	        
			// Start the first calculation:
			mainFrame.startCalculation();
		}
		
		// OS X? see http://simplericity.org/macify/
		Application app = new DefaultApplication();
		//app.addPreferencesMenuItem();
		//app.setEnabledPreferencesMenu(false);
		app.addApplicationListener(this);
		
        return mainFrame;
    }
	
	/**
	 * Reads the presets configuration file (presets.json) and returns
	 * a JSONObject.
	 * 
	 * @return All presets from the presets.json configuration file.
	 */
	public JSONObject getSystemPresetsJSONObject() {
		if (this.presets == null) {
			InputStream is = getClass().getResourceAsStream("/presets.json");
			if (is != null) {
				StringBuffer s = new StringBuffer();
				String tmp;
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				try {
					while ((tmp = reader.readLine()) != null) {
						s.append(tmp);
					}
					presets = new JSONObject(s.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return presets;
	}
	
	/**
	 * Reads the user presets file, if exists, from the user's configuration dir,
	 * and returns a (possibly empty) json object.
	 * @return
	 */
	public JSONObject getUserPresetsJSONObject() {
		if (this.userPresets == null) {
			File presetFile = new File(getUserSettingsDir() + File.separator + "presets.json");
			if (presetFile.exists()) {
				
				StringBuffer s = new StringBuffer();
				String tmp;
				BufferedReader reader;
				try {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(presetFile)));
					while ((tmp = reader.readLine()) != null) {
						s.append(tmp);
					}
					userPresets = new JSONObject(s.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// make sure the  user presets object is not null:
			if (userPresets == null) {
				userPresets = new JSONObject();
				try {
					userPresets.put("fractalPresets", new JSONArray());
					userPresets.put("colorPresets", new JSONArray());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
			
		return userPresets;
	}
	
	
	private FractParamPresets getFractParamPresets(JSONObject presets) {
		FractParamPresets p = new FractParamPresets();
		if (presets != null && presets.has("fractalPresets")) {
			try {
				JSONArray entries = presets.getJSONArray("fractalPresets");
				for (int i = 0; i < entries.length(); i++) {
					JSONObject entry = entries.getJSONObject(i);
					p.add(FractParam.fromJSONObject(entry));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return p;
	}
	
	public FractParamPresets getSystemPresets() {
		if (this.systemFractParamPresets == null) {
			this.systemFractParamPresets = getFractParamPresets(getSystemPresetsJSONObject());
		}
		return this.systemFractParamPresets;
	}
	
	public FractParamPresets getUserPresets() {
		if (this.userFractParamPresets == null) {
			this.userFractParamPresets = getFractParamPresets(getUserPresetsJSONObject());
		}
		return this.userFractParamPresets;
	}
	
	public JSONObject addUserFractalPreset(FractParam p) {
		JSONObject obj = this.getUserPresetsJSONObject();
		//obj.getJSONArray("fractalPresets").put(p.toJSONObject());
		this.getUserPresets().add(p);
		this.saveUserPresets();
		return obj;
	}
	
	public void removeUserFractalPreset(FractParam p) {
		JSONObject obj = this.getUserPresetsJSONObject();
		this.getUserPresets().remove(p);
		this.saveUserPresets();
	}
	
	/**
	 * Adds a fractal result data object to the history stack.
	 * @param data
	 */
	public void addHistory(FractCalcerResultData data) {
		history.push(data);
	}
	
	/**
	 * Pops a fractal result data object from the history stack, returning the data.
	 * @return
	 */
	public FractCalcerResultData popHistory() {
		if (!this.history.isEmpty()) {
			FractCalcerResultData res = this.history.pop();
			System.gc();
			return res;
		} else return null;
	}
	
	/**
	 * How many entries does the history actually have?
	 * @return
	 */
	public int getHistoryCount() {
		return this.history.size();
	}
	
	public String getUserProperty(String key) {
		return this.userProperties.getProperty(key);
	}
	
	public void setUserProperty(String key, String value) {
		this.userProperties.setProperty(key, value);
	}
	
	protected void loadUserSettings() {
		File userSettingsFile = new File(getUserSettingsDir() + File.separator + "settings.xml");
		if (userSettingsFile.exists()) {
			try {
				this.userProperties.load(new FileInputStream(userSettingsFile));
				System.out.println("User loaded from: "+userSettingsFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public String getUserSettingsDir() {
		return System.getProperty("user.home") + File.separator + ".jfractgen";
	}
	
	
	/**
	 * Exits the application, and do necessary things before.
	 */
	protected void shutdown() {
		File userSettingsFile = new File(getUserSettingsDir() + File.separator + "settings.xml");
		System.out.println("User settings go to: "+userSettingsFile);
		try {
			userSettingsFile.mkdirs();
			userSettingsFile.delete();
			
			// save user preferences:
			this.userProperties.store(new FileOutputStream(userSettingsFile), "User preferences for JFractGen");
			
			// save user presets:
			this.saveUserPresets();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
		
	}
	
	private void saveUserPresets() {
		File f = new File(getUserSettingsDir() + File.separator + "presets.json");
		BufferedWriter w;
		try {
			w = new BufferedWriter(new FileWriter(f));
			w.write(getUserPresetsJSONObject().put("fractalPresets",this.getUserPresets().getJSONArray()).toString(4));
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handleAbout(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleOpenApplication(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleOpenFile(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePreferences(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePrintFile(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleQuit(ApplicationEvent arg0) {
		shutdown();
		
	}

	@Override
	public void handleReOpenApplication(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
