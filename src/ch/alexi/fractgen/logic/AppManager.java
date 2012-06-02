package ch.alexi.fractgen.logic;

import java.awt.Image;
import java.util.Stack;

import javax.swing.JFrame;
import ch.alexi.fractgen.gui.MainFrame;
import ch.alexi.fractgen.models.FractHistory;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.FractParamPresets;

public class AppManager {
	private static AppManager inst = new AppManager();
	
	private MainFrame mainFrame;
	private Stack<FractHistory> history;
	
	private AppManager() {
		this.history = new Stack<FractHistory>();
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
		
		mainFrame.startCalculation();
        return mainFrame;
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
