package ch.alexi.fractgen.gui;

import javax.swing.JComboBox;
import ch.alexi.fractgen.models.ColorPresets;

@SuppressWarnings("serial")
/**
 * A combobox for the Color presets. Shows the color palette directly
 * in the combobox list entries.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class ColorPresetsCombo extends JComboBox {
	public ColorPresetsCombo() {
		super(ColorPresets.getColorPresets());
		this.setRenderer(new ColorPaletteListCellRenderer());
	}
}
