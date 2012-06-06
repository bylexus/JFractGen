package ch.alexi.fractgen.gui;

import javax.swing.JComboBox;
import ch.alexi.fractgen.models.FractFunctions;

/**
 * The fractal functions combobox
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class FractFunctionsCombo extends JComboBox {
	public FractFunctionsCombo() {
		super(FractFunctions.getFunctions());
	}
}
