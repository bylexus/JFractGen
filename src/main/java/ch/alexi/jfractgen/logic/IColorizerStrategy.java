package ch.alexi.jfractgen.logic;

import ch.alexi.jfractgen.models.FractCalcerResultData;
import ch.alexi.jfractgen.models.RGB;

import java.security.NoSuchAlgorithmException;

public interface IColorizerStrategy {
    public enum COLORIZER_STRATEGY {
        ROTATING,
        PERCENTAGE
    };

    public static RGB RGB_BLACK = new RGB(0,0,0,255);

	/**
	 * Evaluates the correct color for a single pixel in the given raster image,
     * based on the actual fractal calculation results.
     *
	 * @param x The X coordinate of the pixel to colorize
	 * @param y The Y  coordinate of the pixel to colorize
	 * @param palette The chosen color palette
	 * @param data The fractal calculation result, including number of iterations and the chosen parameters
	 */
	public RGB getColorForPixel(int x, int y, RGB[] palette, FractCalcerResultData data) throws NoSuchAlgorithmException;
}
