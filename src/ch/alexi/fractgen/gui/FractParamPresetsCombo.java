package ch.alexi.fractgen.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import ch.alexi.fractgen.logic.AppManager;

/**
 * The Fractal presets combo box 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class FractParamPresetsCombo extends JComboBox {
	public FractParamPresetsCombo() {
		super();
		this.reloadPresets();
	}
	
	
	public void reloadPresets() {
			this.setModel(new DefaultComboBoxModel(AppManager.getInstance().getFractParamPresets()));
	}
}
