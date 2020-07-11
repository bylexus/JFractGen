package ch.alexi.jfractgen.logic;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.List;

import javax.swing.SwingWorker;

import ch.alexi.jfractgen.models.ColorPreset;
import ch.alexi.jfractgen.models.FractCalcerProgressData;
import ch.alexi.jfractgen.models.FractCalcerResultData;
import ch.alexi.jfractgen.models.FractFunctionResult;
import ch.alexi.jfractgen.models.FractParam;
import ch.alexi.jfractgen.models.RGB;

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
		int width,height, threadNr,pixelIncrement;
		FractParam fractParam;
		WritableRaster raster;
		FractCalcerResultData calcerResult;

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
		public FractCalcThread(int threadNr, FractParam param,WritableRaster raster, FractCalcerResultData result, int width, int height, int pixelIncrement) {
			this.fractParam = param;
			this.threadNr = threadNr;
			this.width = width;
			this.height = height;
			this.raster = raster;
			this.calcerResult = result;
			this.pixelIncrement = pixelIncrement;
		}

		@Override
		/**
		 * Calculates a part of the Julia/Mandelbrot set and fills the image raster with the correct color.
		 * Loops over each nth image pixel (processes each 'pixelIncrement'+threadNr pixels),
		 * calculate the corresponding Complex number, and runs the
		 * iteration function for all Complex number to see if it is part of Julia/Mandelbrot.
		 *
		 * Informs the parent thread of its progress in a percentage value.
		 */
		public void run() {

			double cx, cy;
			FractFunctionResult res;
			double[][] iterValues = this.calcerResult.iterValues;

			FractCalcerProgressData pdata = new FractCalcerProgressData();
			pdata.threadNr = this.threadNr;
			pdata.threadName = this.getName();

			// the threadNr is taken as start offset for this thread, and we increment by pixelIncrement.
			for (int pixel = this.threadNr; pixel < width * height; pixel += pixelIncrement) {
				if (isCancelled()) {
					return;
				}
				
				int y = pixel / width;
				int x = pixel % width;
				
				// cy = C(i) for the pixel y (C(i) = imaginary part of C)
				// and we also reverse the y axis:
				cy = fractParam.min_cy + ((height - y) * fractParam.punkt_abstand);
					
				// cx = C(r) for the pixel x (C(r) = real part of C)
				cx = fractParam.min_cx + x * fractParam.punkt_abstand;

				// Check for set membership by executing the selected iteration function (julia, mandelbrot):
				res = fractParam.iterFunc.fractIterFunc(cx,cy,fractParam.getMaxBetragQuadrat(), fractParam.maxIterations,fractParam.juliaKr,fractParam.juliaKi);

				if (fractParam.smoothColors == true) {
					// Smooth coloring, see http://de.wikipedia.org/wiki/Mandelbrot-Menge#Iteration_eines_Bildpunktes:
					iterValues[x][y] = res.iterValue - Math.log(Math.log(res.bailoutValue) / Constants.LOG_4) / Constants.LOG_2;
				} else {
					// Rough coloring: Escape time algorithm:
					iterValues[x][y] = res.iterValue;
				}

				// Colorize the pixel:
				colorizer.colorizeRasterPixel(raster, x, y, palette, calcerResult);

				// Progress update, all 50 lines only to save resources:
				if (y % 50 == 0) {
					pdata.threadProgress = pixel/(double)(width*height);
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
		this.palette = cpreset.createDynamicSizeColorPalette(
				fractParam.colorPaletteLength,
				fractParam.colorPaletteRepeat
		);
		initFractParams(this.fractParam);

		BufferedImage img = new BufferedImage(fractParam.picWidth, fractParam.picHeight, BufferedImage.TYPE_INT_ARGB);

		FractCalcerResultData result = new FractCalcerResultData(this.fractParam, img,this.palette);

		// Build a vertical image stripe for each worker
		// Each thread processes ever nth pixel: if we have 2 worker threads,
		// thread 1 will process all even pixels (0, 2, 4,...), while thread 2 will
		// work on all odd pixels (1,3,5 ...).
		int nrOfWorkers = AppManager.getInstance().getUserPrefs().getNrOfWorkers();
		Thread[] workers = new Thread[nrOfWorkers];
		for (int i = 0; i < workers.length; i++) {
			
			//int minX = fractParam.picWidth / nrOfWorkers * i;
			//int maxX = fractParam.picWidth / nrOfWorkers * (i+1) - 1;
			workers[i] = new FractCalcThread(i,fractParam, img.getRaster(), result, fractParam.picWidth, fractParam.picHeight, nrOfWorkers);
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
