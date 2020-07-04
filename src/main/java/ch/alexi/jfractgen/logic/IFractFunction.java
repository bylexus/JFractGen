package ch.alexi.jfractgen.logic;

import org.apfloat.Apfloat;

import ch.alexi.jfractgen.models.FractFunctionResult;

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

	public FractFunctionResult fractIterFunc(Apfloat cx,Apfloat cy,Apfloat max_betrag_quadrat, double max_iter,Apfloat julia_r,Apfloat julia_i);
}
