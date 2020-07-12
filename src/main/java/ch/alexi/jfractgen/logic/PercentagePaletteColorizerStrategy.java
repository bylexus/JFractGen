package ch.alexi.jfractgen.logic;

import ch.alexi.jfractgen.models.FractCalcerResultData;
import ch.alexi.jfractgen.models.RGB;

public class PercentagePaletteColorizerStrategy implements IColorizerStrategy {

    /**
     * Evaluates the correct color for a single iteration value / pixel.
     *
     * Percentage palette means: The color index is the percentage of the actual
     * iteration count from the maximum iteration count
     *
     * @param raster
     * @param x
     * @param y
     * @param palette
     * @param data
     */
    @Override
    public RGB getColorForPixel(int x, int y, RGB[] palette, FractCalcerResultData data) {
        int palettePos;
        double percentageIterValue = data.iterValues[x][y] / (double) data.fractParam.maxIterations;
        palettePos = new Double(percentageIterValue * palette.length - 1).intValue();

        if (data.iterValues[x][y] >= 0 && data.iterValues[x][y] < data.fractParam.maxIterations) {
            return palette[palettePos];
        } else {
            return RGB_BLACK;
        }
    }
}
