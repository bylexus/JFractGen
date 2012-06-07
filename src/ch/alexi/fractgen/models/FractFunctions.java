package ch.alexi.fractgen.models;

import java.util.Vector;
import ch.alexi.fractgen.logic.IFractFunction;
import ch.alexi.fractgen.logic.JuliaFractFunction;
import ch.alexi.fractgen.logic.MandelbrotFractFunction;

@SuppressWarnings("serial")
/**
 * A list of available fractal algorithm functions, current only julia and mandelbrot.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class FractFunctions extends Vector<IFractFunction> {
	public static IFractFunction mandelbrot = new MandelbrotFractFunction();
	public static IFractFunction julia = new JuliaFractFunction();
	
	private static FractFunctions inst = new FractFunctions();
	
	
	private FractFunctions() {
		this.add(mandelbrot);
		this.add(julia);
	}
	
	public static Vector<IFractFunction> getFunctions() {
		return inst;
	}
}
