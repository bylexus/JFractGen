package ch.alexi.jfractgen.logic;

import java.awt.image.WritableRaster;
import ch.alexi.jfractgen.models.FractCalcerResultData;
import ch.alexi.jfractgen.models.RGB;

/**
 * The colorizer class is responsible for create the correct color
 * for the fractal image, by processing the number of iterations used to
 * check if a point is within the Julia/Mandelbrot set.
 * The colors represent the number of iterations used. If the iteration limit
 * is reached for a complex number, this means that it probably is part of the set, and it is blacked.
 * Otherwise it is colored.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class Colorizer {
	public static RGB RGB_BLACK = new RGB(0,0,0,255);

	/**
	 * Set the colors for each pixel in the fractal image, by using
	 * the iteration values from a FractCalcer result data object and
	 * a given color palette.
	 * @param data
	 * @param palette
	 */
	public void fractDataToRaster(FractCalcerResultData data, RGB[] palette) {
		WritableRaster raster = data.fractImage.getRaster();

		// Loop over all iteration values from the calc result, set the color
		// for each corresponding pixel in the result image:
		for (int x = 0; x < data.iterValues.length; x++) {
			for (int y = 0; y < data.iterValues[x].length; y++) {
				this.colorizeRasterPixel(raster, x, y, palette, data);
			}
		}
	}


	/**
	 * Evaluates and sets the correct color for a single iteration value / pixel.
	 * @param raster
	 * @param x
	 * @param y
	 * @param palette
	 * @param percentualIterValue
	 */
	public void colorizeRasterPixel(WritableRaster raster, int x, int y, RGB[] palette, FractCalcerResultData data) {
		double percentageIterValue = data.iterValues[x][y] / (double)data.fractParam.maxIterations;
		int palettePos = new Double(percentageIterValue * palette.length - 1).intValue();
		//int palettePos = (int)Math.floor((data.iterValues[x][y] % palette.length));
		/*int dir = (int)Math.floor((data.iterValues[x][y] / (double)palette.length)) % 2;
		if (dir == 1) {
			palettePos = palette.length - palettePos - 1;
		}
		*/
		
		if (data.iterValues[x][y] >= 0 && data.iterValues[x][y] < data.fractParam.maxIterations) {
			this.colorizeRasterPixel(raster, x, y, palette[palettePos]);
		} else {
			this.colorizeRasterPixel(raster, x, y, RGB_BLACK);
		}
	}

	public void colorizeRasterPixel(WritableRaster raster, int x, int y, RGB color) {
		raster.setPixel(x, y, color.toRGBArray());
	}
}
