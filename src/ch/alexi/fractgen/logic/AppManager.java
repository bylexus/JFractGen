package ch.alexi.fractgen.logic;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ch.alexi.fractgen.gui.MainFrame;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.FractParamPresets;

public class AppManager {
	private static AppManager inst = new AppManager();
	
	private MainFrame mainFrame;
	
	private AppManager() {
	}
	
	public static AppManager getInstance() {
		return inst;
	}
	
	
	
	public MainFrame createAndShowGUI() {
		if (mainFrame == null) {
			// Create and set up the window.
	        mainFrame = new MainFrame("JFractGen");
	        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	        mainFrame.pack();
	        mainFrame.setVisible(true);
	        
	        mainFrame.setFractParam(FractParamPresets.getPresets().get(0));
		}
        return mainFrame;
    }

}
