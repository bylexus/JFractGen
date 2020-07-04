package ch.alexi.jfractgen.logic;

import org.apfloat.Apfloat;

import ch.alexi.jfractgen.models.FractFunctionResult;

/**
 * An implementing algorithm for the fractal function: Julia set.
 *
 * The julia set is defined by:
 *
 * Z(n+1) = Z(n)^2 + K
 *
 * while K = a constant complex number (e.g. -0.6 + 0.6i)
 * and
 * Z(0) = (cx + (cy)i) + K;
 * cx = initial real value, calculated from the actual pixel's x position
 * cy = initial imaginary value, calculated from the actual pixel's y position
 *
 * The number is iterated as long as it is clear that is is either reaching the border |Z^2| > max
 * or the max. number of iterations is reached.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class JuliaFractFunction implements IFractFunction {
	public String toString() {
		return "Julia";
	}

	public FractFunctionResult fractIterFunc(Apfloat cx, Apfloat cy, Apfloat max_betrag_quadrat,
			double max_iter, Apfloat julia_r, Apfloat julia_i) {
		Apfloat betrag_quadrat = Constants.ZERO;
		double iter = 0;
		Apfloat x = cx,xt;
		Apfloat y = cy,yt;

		while (betrag_quadrat.compareTo(max_betrag_quadrat) <= 0 && iter < max_iter) {
			//xt = x * x - y*y + julia_r;
			xt = x.multiply(x).subtract(y.multiply(y)).add(julia_r);
			
			//yt = 2*x*y + julia_i;
			yt = x.multiply(y).multiply(Constants.TWO).add(julia_i);
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
