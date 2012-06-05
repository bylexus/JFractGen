package ch.alexi.fractgen.models;

import java.awt.image.BufferedImage;

public class FractCalcerResultData {
	public BufferedImage fractImage;
	public FractParam fractParam;
	public int[][] iterValues;
	public RGB[] colorPalette;
	
	public FractCalcerResultData() {
		
	}
	
	public FractCalcerResultData(FractParam fractParam, BufferedImage img,RGB[] colorPalette) {
		this.fractParam = fractParam;
		this.fractImage = img;
		this.iterValues = new int[fractParam.picWidth][fractParam.picHeight];
		this.colorPalette = colorPalette;
	}
}
