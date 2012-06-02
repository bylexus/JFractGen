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
	
	public ColorPreset colorPreset = ColorPresets.PALETTE_PATCHWORK;
	
	
	/**
	 * before calling, the following values need to be set:
	 * - picWidth, picHeight,
	 * - diameterCX,
	 * - centerCX, centerCY
	 */
	public void initFractParams() {
		double aspect, fract_width, fract_heigth;
		
		aspect = this.picWidth / (double)this.picHeight;
		fract_width = this.diameterCX;
		fract_heigth = this.diameterCX / aspect;
		
		this.min_cx = this.centerCX - (fract_width / 2);
		this.max_cx = this.min_cx + fract_width;
		this.min_cy = this.centerCY - (fract_heigth / 2);
		this.max_cy = this.min_cy + fract_heigth;
		
		this.punkt_abstand = (this.max_cx - this.min_cx) / this.picWidth;
	}
	
	@Override
	public String toString() {
		return new String(name);
	}
}
