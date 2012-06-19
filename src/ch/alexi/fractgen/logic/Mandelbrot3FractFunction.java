package ch.alexi.fractgen.logic;

import ch.alexi.fractgen.models.FractFunctionResult;

/**
 * An implementing algorithm for the fractal function: Mandelbrot set.
 * 
 * This Mandelbrot set is defined by:
 * 
 * Z(n+1) = Z(n)^3 + c
 * 
 * while c = a constant complex number (cx + (cy)i)
 * and
 * Z(0) = 0;
 * 
 * cx = initial real value, calculated from the actual pixel's x position
 * cy = initial imaginary value, calculated from the actual pixel's y position
 * 
 * The number is iterated as long as it is clear that is is either reaching the border |Z^3| > max
 * or the max. number of iterations is reached. 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class Mandelbrot3FractFunction implements IFractFunction {
	@Override
	public String toString() {
		return "Mandelbrot Z^3";
	}

	@Override
	public FractFunctionResult fractIterFunc(double cx, double cy, double max_betrag_quadrat,
			double max_iter, double julia_r, double julia_i) {
		double betrag_quadrat = 0;
		double iter = 0;
		double x = 0, xt;
		double y = 0, yt;

		while (betrag_quadrat <= max_betrag_quadrat && iter < max_iter) {
			// Z^3 + c:
			xt = x*(x*x - 3*y*y) + cx;
			yt = y*(3*x*x - y*y) + cy;
			x = xt;
			y = yt;
			iter += 1;
			betrag_quadrat = x*x + y*y;
		}
		FractFunctionResult r = new FractFunctionResult();
		r.iterValue = iter;
		r.bailoutValue = betrag_quadrat;
		return r;
	}
}
