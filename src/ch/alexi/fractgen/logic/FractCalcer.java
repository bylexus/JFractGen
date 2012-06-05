package ch.alexi.fractgen.logic;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.List;

import javax.swing.SwingWorker;

import ch.alexi.fractgen.models.FractCalcerProgressData;
import ch.alexi.fractgen.models.FractCalcerResultData;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.RGB;

public class FractCalcer extends SwingWorker<FractCalcerResultData, FractCalcerProgressData>{
	
	private FractParam fractParam;
	private IFractCalcObserver observer;
	private RGB[] palette;
	private Colorizer colorizer = new Colorizer();
	
	class FractCalcThread extends Thread {
		int minX,minY,maxX,maxY, threadNr;
		FractParam fractParam;
		WritableRaster raster;
		int[][] iterValues;
		
		public FractCalcThread(int threadNr, FractParam param,WritableRaster raster, int[][] iterValues, int minX, int minY, int maxX, int maxY) {
			this.fractParam = param;
			this.threadNr = threadNr;
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
			this.raster = raster;
			this.iterValues = iterValues;
		}
		
		@Override
		public void run() {
			
			int nrOfLoops = (maxY - minY)*(maxX - minX);
			double cx, cy;
			int res;
			RGB black = new RGB(0,0,0);
			
			FractCalcerProgressData pdata = new FractCalcerProgressData();
			pdata.threadNr = this.threadNr;
			pdata.threadName = this.getName();
			
			for (int y = minY; y <= maxY; y++) {
				cy = fractParam.min_cy + (maxY - y) * fractParam.punkt_abstand; // imaginaerteil von c
				for (int x = minX; x <= maxX; x++) {
					cx = fractParam.min_cx + x * fractParam.punkt_abstand; // realteil von Pixelwert errechnet (skaliert)
					
					res = fractParam.iterFunc.fractIterFunc(cx,cy,fractParam.maxBetragQuadrat, fractParam.maxIterations,-0.8,0.8);
					iterValues[x][y] = res;
					
					int index = new Double((double)res / fractParam.maxIterations * palette.length).intValue();
					if (index > palette.length-1) {
						colorizer.colorizeRasterPixel(raster, x, y, black);
						//raster.setPixel(x, y, black);
					} else {
						colorizer.colorizeRasterPixel(raster, x, y, palette[index]);
						//raster.setPixel(x, y, palette[index].toRGBArray());
					}
				}
				
				// Progress update:
				if (y % 50 == 0) {
					pdata.threadProgress = (y*(maxX - minX))/(double)nrOfLoops;
					FractCalcer.this.publish(pdata);
				}
			}
			
			// publish 100% message:
			pdata.threadProgress = 1;
			FractCalcer.this.publish(pdata);
		}
	};
	
	public FractCalcer(FractParam params, IFractCalcObserver o) {
		this.fractParam = params;
		this.observer = o;
	}
	
	
	
	private void initFractParams(FractParam p) {
		p.initFractParams();
	}
	
	@Override
	public FractCalcerResultData doInBackground() {
		// Create color palette
		//this.palette = fractParam.colorPreset.createFixedSizeColorPalette(fractParam.maxIterations);
		this.palette = fractParam.colorPreset.createDynamicSizeColorPalette(256);
		initFractParams(this.fractParam);
		
		BufferedImage img = new BufferedImage(fractParam.picWidth, fractParam.picHeight, BufferedImage.TYPE_INT_RGB);
		
		FractCalcerResultData result = new FractCalcerResultData(this.fractParam, img,this.palette);
		
		Thread[] workers = new Thread[fractParam.nrOfWorkers];
		for (int i = 0; i < workers.length; i++) {
			int minX = fractParam.picWidth / fractParam.nrOfWorkers * i;
			int maxX = fractParam.picWidth / fractParam.nrOfWorkers * (i+1) - 1;
			workers[i] = new FractCalcThread(i,fractParam, img.getRaster(), result.iterValues,minX, 0, maxX, fractParam.picHeight-1);
			workers[i].start();
		}
		
		
		try {
			for (Thread w : workers) {
				if (w != null) {
					w.join();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public RGB[] getPalette() {
		return this.palette;
	}
	
	public FractParam getFractParam() {
		return this.fractParam;
	}
	
	@Override
	protected void process(List<FractCalcerProgressData> pdata) {
		for (FractCalcerProgressData p : pdata) {
			observer.progress(this, p);
		}
	}
	
	@Override
	protected void done() {
		observer.done(this);
	}
	
	
	
}
