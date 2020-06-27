package ch.alexi.jfractgen.models;

import java.awt.image.BufferedImage;

/**
 * Represents the fractal calculation result data.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class FractCalcerResultData {
	public BufferedImage fractImage;
	public FractParam fractParam;
	public double[][] iterValues;
	public RGB[] colorPalette;

	public FractCalcerResultData() {

	}

	public FractCalcerResultData(FractParam fractParam, BufferedImage img,RGB[] colorPalette) {
		this.fractParam = fractParam;
		this.fractImage = img;
		this.iterValues = new double[fractParam.picWidth][fractParam.picHeight];
		this.colorPalette = colorPalette;
	}
}
