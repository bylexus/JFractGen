package ch.alexi.jfractgen.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import ch.alexi.jfractgen.logic.AppManager;
import ch.alexi.jfractgen.models.ColorPreset;

@SuppressWarnings("serial")
/**
 * A combobox for the Color presets. Shows the color palette directly
 * in the combobox list entries.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class ColorPresetsCombo extends JComboBox<ColorPreset> {
	public ColorPresetsCombo() {
		super();
		this.reloadPresets();
		this.setRenderer(new ColorPaletteListCellRenderer());
	}

	public void reloadPresets() {
		this.setModel(new DefaultComboBoxModel<ColorPreset>(AppManager.getInstance().getPresets().getColorPresets()));
	}
}
