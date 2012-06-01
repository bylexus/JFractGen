package ch.alexi.fractgen.models;

import java.util.Vector;

import ch.alexi.fractgen.logic.IFractFunction;
import ch.alexi.fractgen.logic.JuliaFractFunction;
import ch.alexi.fractgen.logic.MandelbrotFractFunction;

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
