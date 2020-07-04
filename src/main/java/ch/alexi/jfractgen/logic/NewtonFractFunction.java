package ch.alexi.jfractgen.logic;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;

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

	private Apcomplex f(Apcomplex z) {
		return z.multiply(z).multiply(z).subtract(z.multiply(z)).add(Constants.TWO);
	}

	public FractFunctionResult fractIterFunc(Apfloat cx, Apfloat cy, Apfloat max_betrag_quadrat,
			double max_iter, Apfloat julia_r, Apfloat julia_i) {
		double threshold = 0.00001;
		Apcomplex h = new Apcomplex(new Apfloat(0.000006, Constants.precision), new Apfloat(0.000006, Constants.precision));
		double result = 1;
		double iter = 0;

		/**
		 * Z(n+1)  = Z(n) - (p(z(n) / p'(z(n)))
		 */
		Apcomplex dz;
		Apcomplex z, z0;

		z = new Apcomplex(cx, cy);


		while (result > threshold*threshold && iter < max_iter) {
			dz = (f(z.add(h)).subtract(f(z))).divide(h);
			z0 = z.subtract(f(z).divide(dz));

			dz = z0.subtract(z);
			result = dz.real().multiply(dz.real()).add(dz.imag().multiply(dz.imag())).doubleValue(); // abs(z) = sqrt(re^2+im^2) --> we only calc re^2+im^2
			z = z0;
			iter += 1;
		}
		z = dz = z0 = null;
		FractFunctionResult r = new FractFunctionResult();
		r.iterValue = iter;
		r.bailoutValue = new Apfloat(result);
		return r;
	}
}
