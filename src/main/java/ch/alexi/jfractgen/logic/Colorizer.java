package ch.alexi.jfractgen.logic;

import java.awt.image.WritableRaster;
import java.security.NoSuchAlgorithmException;

import ch.alexi.jfractgen.models.FractCalcerResultData;
import ch.alexi.jfractgen.models.RGB;

/**
 * The colorizer class is responsible for create the correct color for the
 * fractal image, by processing the number of iterations used to check if a
 * point is within the Julia/Mandelbrot set. The colors represent the number of
 * iterations used. If the iteration limit is reached for a complex number, this
 * means that it probably is part of the set, and it is blacked. Otherwise it is
 * colored.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in
 * Java/Swing.
 *
 * @author Alexander Schenkel, www.alexi.ch (c) 2012 Alexander Schenkel
 */
public class Colorizer {

    protected IColorizerStrategy colorizerStrategy;

    public Colorizer(IColorizerStrategy colorStrategy) {
        this.colorizerStrategy = colorStrategy;
    }

    /**
     * Set the colors for each pixel in the fractal image, by using the iteration
     * values from a FractCalcer result data object and a given color palette.
     *
     * @param data
     * @param palette
     */
    public void fractDataToRaster(FractCalcerResultData data, RGB[] palette) {
        WritableRaster raster = data.fractImage.getRaster();

        // Loop over all iteration values from the calc result, set the color
        // for each corresponding pixel in the result image:
        try {
            for (int x = 0; x < data.iterValues.length; x++) {
                for (int y = 0; y < data.iterValues[x].length; y++) {
                    this.colorizeRasterPixel(raster, x, y,
                            this.colorizerStrategy.getColorForPixel(x, y, palette, data));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Oops! Colorizer Strategy not implemented!");
        }
    }

    public void colorizeRasterPixel(WritableRaster raster, int x, int y, RGB[] palette, FractCalcerResultData data) {
        try {
            this.colorizeRasterPixel(raster, x, y, this.colorizerStrategy.getColorForPixel(x, y, palette, data));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Oops! Colorizer Strategy not implemented!");
        }
    }

    public void colorizeRasterPixel(WritableRaster raster, int x, int y, RGB color) {
        raster.setPixel(x, y, color.toRGBArray());
    }
}
