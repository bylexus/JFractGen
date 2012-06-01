package ch.alexi.fractgen.logic;

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
