package ch.alexi.fractgen.logic;

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
