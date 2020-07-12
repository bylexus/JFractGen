package ch.alexi.jfractgen.logic;

import ch.alexi.jfractgen.models.FractCalcerResultData;
import ch.alexi.jfractgen.models.RGB;

public class RotatingPaletteColorizerStrategy implements IColorizerStrategy {

    /**
     * Evaluates the correct color for a single iteration value / pixel.
     *
     * Fixed size palette means: Each color is always indexed exact by the iteration
     * count: If the palette has less colors than the iteration count, it is wrapped
     *
     * @param x
     * @param y
     * @param palette
     * @param data
     */
    @Override
    public RGB getColorForPixel(int x, int y, RGB[] palette, FractCalcerResultData data) {
        int palettePos;
        palettePos = (int) Math.floor((data.iterValues[x][y] % palette.length));
        int dir = (int) Math.floor((data.iterValues[x][y] / (double) palette.length)) % 2;
        if (dir == 1) {
            palettePos = palette.length - palettePos - 1;
        }

        if (data.iterValues[x][y] >= 0 && data.iterValues[x][y] < data.fractParam.maxIterations) {
            return palette[palettePos];
        } else {
            return RGB_BLACK;
        }
    }
}
