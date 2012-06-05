package ch.alexi.fractgen.logic;

import java.awt.image.WritableRaster;

import ch.alexi.fractgen.models.FractCalcerResultData;
import ch.alexi.fractgen.models.RGB;

public class Colorizer {
	public Colorizer() {
		
	}
	
	public void fractDataToRaster(FractCalcerResultData data, RGB[] palette) {
		WritableRaster raster = data.fractImage.getRaster();
		for (int x = 0; x < data.iterValues.length; x++) {
			for (int y = 0; y < data.iterValues[x].length; y++) {
				raster.setPixel(x, y, palette[data.iterValues[x][y] / data.fractParam.maxIterations].toRGBArray());
			}
		}
	}
	
	public void colorizeRasterPixel(WritableRaster raster, int x, int y, RGB color) {
		raster.setPixel(x, y, color.toRGBArray());
	}
}
