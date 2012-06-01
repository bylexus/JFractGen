package ch.alexi.fractgen.models;

import java.util.Vector;


public class FractParamPresets extends Vector<FractParam> {
	private static FractParamPresets inst = new FractParamPresets();
	
	private FractParamPresets() {
		FractParam tmp;
		
		this.add(new FractParam());
		
		tmp = new FractParam();
		tmp.name = "Mandelbrot Seahorse Valley";
		tmp.centerCX = -0.87591;
		tmp.centerCY = 0.20464;
		tmp.diameterCX = 0.53184;
		tmp.maxIterations = 100;
		this.add(tmp);
		
		tmp = new FractParam();
		tmp.name = "Julia Total";
		tmp.centerCX = 0;
		tmp.centerCY = 0;
		tmp.diameterCX = 3;
		tmp.maxIterations = 80;
		tmp.iterFunc = FractFunctions.julia;
		this.add(tmp);
		
	}
	
	public static Vector<FractParam> getPresets() {
		return inst;
	}
	
	
}
