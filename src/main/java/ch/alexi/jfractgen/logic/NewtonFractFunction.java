package ch.alexi.jfractgen.logic;

import ch.alexi.jfractgen.models.FractFunctionResult;

/**
 * An implementing algorithm for the fractal function: Newton Fractal.
 *
 * The Newton set is defined by:
 *
 * Z(n+1) = Z(n) - (p(z(n) / p'(z(n)))
 *
 * while p(z) = z^3-1 and p'(z) = 3*z^2
 *
 * while Z(0) = (cx + (cy)i);
 * cx = initial real value, calculated from the actual pixel's x position
 * cy = initial imaginary value, calculated from the actual pixel's y position
 *
 * The number is iterated as long as it is clear that is is not converging towards 0.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class NewtonFractFunction implements IFractFunction {
	public String toString() {
		return "Newton";
	}

	private Complex f(Complex z) {
		//return z.times(z).times(z).minus(1.0d);
		return z.times(z).times(z).minus(z.times(z)).plus(2.0d);
	}

	public FractFunctionResult fractIterFunc(double cx, double cy, double max_betrag_quadrat,
			double max_iter, double julia_r, double julia_i) {
		double threshold = 0.00001;
		Complex h = new Complex(0.000006, 0.000006);
		double result = 1;
		double iter = 0;

		/**
		 * Z(n+1)  = Z(n) - (p(z(n) / p'(z(n)))
		 */
		Complex dz;
		Complex z, z0;

		z = new Complex(cx, cy);


		while (result > threshold*threshold && iter < max_iter) {
			dz = (f(z.plus(h)).minus(f(z))).dividedBy(h);
			z0 = z.minus(f(z).dividedBy(dz));

			dz = z0.minus(z);
			result = dz.re*dz.re + dz.im*dz.im; // abs(z) = sqrt(re^2+im^2) --> we only calc re^2+im^2
			z = z0;
			iter += 1;
		}
		z = dz = z0 = null;
		FractFunctionResult r = new FractFunctionResult();
		r.iterValue = iter;
		r.bailoutValue = result;
		return r;
	}
}
