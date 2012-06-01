package ch.alexi.fractgen;

import javax.swing.JFrame;

import ch.alexi.fractgen.gui.MainFrame;
import ch.alexi.fractgen.logic.AppManager;
import ch.alexi.fractgen.models.FractParam;

public class Main {
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AppManager.getInstance().createAndShowGUI();
            }
        });
	}

}
