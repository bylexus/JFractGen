package ch.alexi.fractgen.logic;

import java.awt.image.WritableRaster;

import ch.alexi.fractgen.models.FractCalcerResultData;
import ch.alexi.fractgen.models.RGB;

public class Colorizer {
	public static RGB RGB_BLACK = new RGB(0,0,0);
	public Colorizer() {
		
	}
	
	public void fractDataToRaster(FractCalcerResultData data, RGB[] palette) {
		WritableRaster raster = data.fractImage.getRaster();
		
		for (int x = 0; x < data.iterValues.length; x++) {
			for (int y = 0; y < data.iterValues[x].length; y++) {
				double percentageIterValue = data.iterValues[x][y] / (double)data.fractParam.maxIterations;
				this.colorizeRasterPixel(raster, x, y, palette, percentageIterValue);
			}
		}
	}
	
	public void colorizeRasterPixel(WritableRaster raster, int x, int y, RGB[] palette, double percentualIterValue) {
		int palettePos = new Double(percentualIterValue * palette.length).intValue();
		if (palettePos < palette.length) {
			this.colorizeRasterPixel(raster, x, y, palette[palettePos]);
		} else {
			this.colorizeRasterPixel(raster, x, y, RGB_BLACK);
		}
	}
	
	public void colorizeRasterPixel(WritableRaster raster, int x, int y, RGB color) {
		raster.setPixel(x, y, color.toRGBArray());
	}
}
