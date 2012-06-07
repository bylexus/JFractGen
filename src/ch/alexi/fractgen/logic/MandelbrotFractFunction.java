package ch.alexi.fractgen.logic;

/**
 * An implementing algorithm for the fractal function: Mandelbrot set.
 * 
 * The Mandelbrot set is defined by:
 * 
 * Z(n+1) = Z(n)^2 + c
 * 
 * while c = a constant complex number (cx + (cy)i)
 * and
 * Z(0) = 0;
 * 
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
public class MandelbrotFractFunction implements IFractFunction {
	@Override
	public String toString() {
		return "Mandelbrot";
	}

	@Override
	public int fractIterFunc(double cx, double cy, double max_betrag_quadrat,
			double max_iter, double julia_r, double julia_i) {
		double betrag_quadrat = 0;
		int iter = 0;
		double x = 0, xt;
		double y = 0, yt;

		while (betrag_quadrat <= max_betrag_quadrat && iter < max_iter) {
			xt = x * x - y*y + cx;
			yt = 2*x*y + cy;
			x = xt;
			y = yt;
			iter += 1;
			betrag_quadrat = x*x + y*y;
		}
		return iter;
	}
}
