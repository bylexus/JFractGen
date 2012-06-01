package ch.alexi.fractgen.models;

import ch.alexi.fractgen.logic.IFractFunction;

public class FractParam {
	public String name = "Mandelbrot Total";
	
	public double maxBetragQuadrat = 4;
	public int maxIterations = 40;
	
	public double centerCX = -0.7;
	public double centerCY = 0.0;
	public double diameterCX = 3.0769;
	
	public double initialDiameterCX = 3.0769;
	public double initialMaxIterations = 40;
	
	public IFractFunction iterFunc = FractFunctions.mandelbrot;
	
	public int picWidth = 800;
	public int picHeight = 600;
	
	public double min_cx = 0.0;
	public double min_cy = 0.0;
	public double max_cx = 0.0;
	public double max_cy = 0.0;
	public double punkt_abstand = 0.0;
	public int nrOfWorkers = 2;
	
	
	@Override
	public String toString() {
		return new String(name);
	}
}
