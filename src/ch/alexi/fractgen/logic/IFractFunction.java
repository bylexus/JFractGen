package ch.alexi.fractgen.logic;

import ch.alexi.fractgen.models.FractFunctionResult;

/**
 * An algorithm for iterating a single Complex number to check if it is part of the fractal set or not.
 * Currently only mandelbrot/julia are supported. 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public interface IFractFunction {
	@Override
	public String toString();
	
	public FractFunctionResult fractIterFunc(double cx,double cy,double max_betrag_quadrat, double max_iter,double julia_r,double julia_i);
}
