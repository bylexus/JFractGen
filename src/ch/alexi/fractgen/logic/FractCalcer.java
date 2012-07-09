package ch.alexi.fractgen.logic;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.List;

import javax.swing.SwingWorker;

import ch.alexi.fractgen.models.ColorPreset;
import ch.alexi.fractgen.models.FractCalcerProgressData;
import ch.alexi.fractgen.models.FractCalcerResultData;
import ch.alexi.fractgen.models.FractFunctionResult;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.RGB;

/**
 * This is the real workhorse class: It calculates the Mandelbrot/Julia set numbers and draws the 
 * fractal output image. Because it is started as background task from the GUI, it is implemented
 * as SwingWorker, but uses Threads itself to split the work between multiple processors: Each worker
 * takes a part of the set to calculate. 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class FractCalcer extends SwingWorker<FractCalcerResultData, FractCalcerProgressData>{
	
	private FractParam fractParam;
	private IFractCalcObserver observer;
	private RGB[] palette;
	private Colorizer colorizer = new Colorizer();
	
	/**
	 * The Worker thread calculating a part of the fractal set.
	 * @author alex
	 */
	class FractCalcThread extends Thread {
		int minX,minY,maxX,maxY, threadNr;
		FractParam fractParam;
		WritableRaster raster;
		double[][] iterValues;
		
		/**
		 * 
		 * @param threadNr
		 * @param param
		 * @param raster
		 * @param iterValues
		 * @param minX
		 * @param minY
		 * @param maxX
		 * @param maxY
		 */
		public FractCalcThread(int threadNr, FractParam param,WritableRaster raster, double[][] iterValues, int minX, int minY, int maxX, int maxY) {
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
		/**
		 * Calculates a part of the Julia/Mandelbrot set and fills the image raster with the correct color.
		 * Loops over each image pixel, calculate the corresponding Complex number, and runs the 
		 * iteration function for all Complex number to see if it is part of Julia/Mandelbrot.
		 * 
		 * Informs the parent thread of its progress in a percentage value.
		 */
		public void run() {
			
			int nrOfLoops = (maxY - minY)*(maxX - minX);
			double cx, cy;
			FractFunctionResult res;
			
			FractCalcerProgressData pdata = new FractCalcerProgressData();
			pdata.threadNr = this.threadNr;
			pdata.threadName = this.getName();
			
			for (int y = minY; y <= maxY; y++) {
				// cy = C(i) for the pixel y (C(i) = imaginary part of C)
				cy = fractParam.min_cy + (maxY - y) * fractParam.punkt_abstand;
				for (int x = minX; x <= maxX; x++) {
					if (isCancelled()) {
						return;
					}
					// cx = C(r) for the pixel x (C(r) = real part of C)
					cx = fractParam.min_cx + x * fractParam.punkt_abstand;
					
					// Check for set membership by executing the selected iteration function (julia, mandelbrot):
					res = fractParam.iterFunc.fractIterFunc(cx,cy,fractParam.maxBetragQuadrat, fractParam.maxIterations,fractParam.juliaKr,fractParam.juliaKi);
					
					if (fractParam.smoothColors == true) {
						// Smooth coloring, see http://de.wikipedia.org/wiki/Mandelbrot-Menge#Iteration_eines_Bildpunktes:
						iterValues[x][y] = res.iterValue - Math.log(Math.log(res.bailoutValue) / Math.log(4)) / Math.log(2);;
					} else {
						// Rough coloring: Escape time algorithm:
						iterValues[x][y] = res.iterValue;
					}
					
					// Colorize the pixel:
					double percentualIterValue = iterValues[x][y] / fractParam.maxIterations;
					colorizer.colorizeRasterPixel(raster, x, y, palette, percentualIterValue);
				}
				
				// Progress update, all 50 lines only to save resources:
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
	/**
	 * Starts the calculation: Invokes the configured number of workers, split the 
	 * image equally between those workers and listen for their done signal.
	 */
	public FractCalcerResultData doInBackground() {
		System.gc();
		
		// Create color palette
		ColorPreset cpreset = AppManager.getInstance().getPresets().getColorPresetByName(fractParam.colorPreset);
		this.palette = cpreset.createDynamicSizeColorPalette(fractParam.colorPresetRepeat);
		initFractParams(this.fractParam);
		
		BufferedImage img = new BufferedImage(fractParam.picWidth, fractParam.picHeight, BufferedImage.TYPE_INT_RGB);
		
		FractCalcerResultData result = new FractCalcerResultData(this.fractParam, img,this.palette);
		
		// Build a vertical image stripe for each worker
		int nrOfWorkers = AppManager.getInstance().getUserPrefs().getNrOfWorkers();
		Thread[] workers = new Thread[nrOfWorkers];
		for (int i = 0; i < workers.length; i++) {
			int minX = fractParam.picWidth / nrOfWorkers * i;
			int maxX = fractParam.picWidth / nrOfWorkers * (i+1) - 1;
			workers[i] = new FractCalcThread(i,fractParam, img.getRaster(), result.iterValues,minX, 0, maxX, fractParam.picHeight-1);
			if (!isCancelled()) {
				workers[i].start();
			}
		}
		
		// Wait on all workers to complete:
		try {
			for (Thread w : workers) {
				if (w != null) {
					w.join();
				}
			}
		} catch (InterruptedException e) {
			return null;
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
	/**
	 * Called by the inner workers on progress updates, inform the observers:
	 */
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
