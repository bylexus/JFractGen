package ch.alexi.fractgen.logic;

public interface IFractFunction {
	@Override
	public String toString();
	
	public int fractIterFunc(double cx,double cy,double max_betrag_quadrat, double max_iter,double julia_r,double julia_i);
}
