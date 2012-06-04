package ch.alexi.fractgen.gui;

import javax.swing.JComboBox;

import ch.alexi.fractgen.models.ColorPresets;

public class ColorPresetsCombo extends JComboBox {
	public ColorPresetsCombo() {
		super(ColorPresets.getColorPresets());
		this.setRenderer(new ColorPaletteListCellRenderer());
	}
}
