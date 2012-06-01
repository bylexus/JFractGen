package ch.alexi.fractgen.gui;

import javax.swing.JComboBox;

import ch.alexi.fractgen.models.FractFunctions;

public class FractFunctionsCombo extends JComboBox {
	public FractFunctionsCombo() {
		super(FractFunctions.getFunctions());
	}
}
