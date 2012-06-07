package ch.alexi.fractgen.logic;

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

	@Override
	public int fractIterFunc(double cx, double cy, double max_betrag_quadrat,
			double max_iter, double julia_r, double julia_i) {
		double betrag_quadrat = 0;
		int iter = 0;
		double x = cx,xt;
		double y = cy,yt;

		while (betrag_quadrat <= max_betrag_quadrat && iter < max_iter) {
			xt = x * x - y*y + julia_r;
			yt = 2*x*y + julia_i;
			x = xt;
			y = yt;
			iter += 1;
			betrag_quadrat = x*x + y*y;
		}
		return iter;
	}
}
