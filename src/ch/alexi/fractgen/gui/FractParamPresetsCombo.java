package ch.alexi.fractgen.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import ch.alexi.fractgen.logic.AppManager;
import ch.alexi.fractgen.models.FractParamPresets;

/**
 * The Fractal presets combo box 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class FractParamPresetsCombo extends JComboBox {
	public static final int PRESET_SYSTEM = 1;
	public static final int PRESET_USER = 2;
	
	private int preset;
	
	public FractParamPresetsCombo(int preset) {
		super();
		this.preset = preset;
		this.reloadPresets();
	}
	
	
	public void reloadPresets() {
		if (preset == PRESET_SYSTEM) {
			this.setModel(new DefaultComboBoxModel(AppManager.getInstance().getSystemPresets()));
		}
		if (preset == PRESET_USER) {
			this.setModel(new DefaultComboBoxModel(AppManager.getInstance().getUserPresets()));
		}
	}
}
