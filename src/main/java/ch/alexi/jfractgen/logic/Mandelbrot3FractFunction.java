package ch.alexi.jfractgen.logic;

import org.apfloat.Apfloat;

import ch.alexi.jfractgen.models.FractFunctionResult;

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

	public FractFunctionResult fractIterFunc(Apfloat cx, Apfloat cy, Apfloat max_betrag_quadrat,
			double max_iter, Apfloat julia_r, Apfloat julia_i) {
		Apfloat betrag_quadrat = Constants.ZERO;
		double iter = 0;
		Apfloat x = Constants.ZERO, xt;
		Apfloat y = Constants.ZERO, yt;

		while (betrag_quadrat.compareTo(max_betrag_quadrat) <= 0 && iter < max_iter) {
			// Z^3 + c:
			// xt = x*(x*x - 3*y*y) + cx;
			xt = x.multiply((x.multiply(x).subtract(y.multiply(y).multiply(Constants.THREE)))).add(cx);
			
			// yt = y*(3*x*x - y*y) + cy;
			yt = y.multiply((x.multiply(x).multiply(Constants.THREE).subtract(y.multiply(y)))).add(cy);
			x = xt;
			y = yt;
			iter += 1;
			betrag_quadrat = x.multiply(x).add(y.multiply(y));
		}
		FractFunctionResult r = new FractFunctionResult();
		r.iterValue = iter;
		r.bailoutValue = betrag_quadrat;
		return r;
	}
}
