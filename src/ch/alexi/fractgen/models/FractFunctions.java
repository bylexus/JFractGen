package ch.alexi.fractgen.models;

import java.util.Vector;
import ch.alexi.fractgen.logic.IFractFunction;
import ch.alexi.fractgen.logic.JuliaFractFunction;
import ch.alexi.fractgen.logic.Mandelbrot3FractFunction;
import ch.alexi.fractgen.logic.Mandelbrot4FractFunction;
import ch.alexi.fractgen.logic.MandelbrotFractFunction;
import ch.alexi.fractgen.logic.NewtonFractFunction;

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
	public static IFractFunction mandelbrot3 = new Mandelbrot3FractFunction();
	public static IFractFunction mandelbrot4 = new Mandelbrot4FractFunction();
	public static IFractFunction julia = new JuliaFractFunction();
	public static IFractFunction newton = new NewtonFractFunction();
	
	private static FractFunctions inst = new FractFunctions();
	
	
	private FractFunctions() {
		this.add(mandelbrot);
		this.add(mandelbrot3);
		this.add(mandelbrot4);
		this.add(julia);
		this.add(newton);
	}
	
	public static Vector<IFractFunction> getFunctions() {
		return inst;
	}
	
	public static IFractFunction getFractFunction(String fName) {
		for (IFractFunction f : getFunctions()) {
			if (f.toString().equals(fName)) {
				return f;
			}
		}
		return null;
	}
}
