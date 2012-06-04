package ch.alexi.fractgen.logic;

import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.json.JSONException;
import org.json.JSONObject;

import ch.alexi.fractgen.gui.MainFrame;
import ch.alexi.fractgen.models.FractHistory;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.FractParamPresets;

public class AppManager {
	private static AppManager inst = new AppManager();
	
	private MainFrame mainFrame;
	private Stack<FractHistory> history;
	
	private JSONObject presets;
	
	private AppManager() {
		this.history = new Stack<FractHistory>();
	}
	
	public static AppManager getInstance() {
		return inst;
	}
	
	public MainFrame createAndShowGUI() {
		UIManager.put("Label.font",new Font("Sans Serif",Font.PLAIN,10));
		UIManager.put("ComboBox.font",new Font("Sans Serif",Font.PLAIN,10));
		UIManager.put("TextField.font",new Font("Sans Serif",Font.PLAIN,10));

		
		if (mainFrame == null) {
			// Create and set up the window.
	        mainFrame = new MainFrame("JFractGen");
	        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	        mainFrame.pack();
	        mainFrame.setVisible(true);
	        
	        mainFrame.setFractParam(FractParamPresets.getPresets().get(0));
		}
		
		mainFrame.startCalculation();
		
        return mainFrame;
    }
	
	public JSONObject getPresetsJSONObject() {
		if (this.presets == null) {
			InputStream is = getClass().getResourceAsStream("/res/presets.json");
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
	
	public void addHistory(FractHistory h) {
		history.push(h);
	}
	
	public FractHistory addHistory(Image image, FractParam fractParam) {
		FractHistory h = new FractHistory();
		h.fractImage = image;
		h.fractParam = fractParam;
		this.addHistory(h);
		return h;
	}
	
	public FractHistory popHistory() {
		if (!this.history.isEmpty()) {
			return this.history.pop();
		} else return null;
	}
	
	public FractHistory getLastHistory() {
		if (!this.history.isEmpty()) {
			return this.history.lastElement();
		} else return null;
	}
}
