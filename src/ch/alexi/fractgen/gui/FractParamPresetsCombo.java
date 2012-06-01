package ch.alexi.fractgen.gui;

import javax.swing.JComboBox;

import ch.alexi.fractgen.models.FractParamPresets;

public class FractParamPresetsCombo extends JComboBox {
	public FractParamPresetsCombo() {
		super(FractParamPresets.getPresets());
	}
}
