package ch.alexi.fractgen.logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.List;
import javax.swing.SwingWorker;

import ch.alexi.fractgen.models.ColorPreset;
import ch.alexi.fractgen.models.FractCalcerProgressData;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.RGB;

public class FractCalcer extends SwingWorker<Image, FractCalcerProgressData>{
	
	private FractParam fractParam;
	private IFractCalcObserver observer;
	private RGB[] palette;
	
	class FractCalcThread extends Thread {
		int minX,minY,maxX,maxY, threadNr;
		FractParam fractParam;
		WritableRaster raster;
		
		public FractCalcThread(int threadNr, FractParam param,WritableRaster raster, int minX, int minY, int maxX, int maxY) {
			this.fractParam = param;
			this.threadNr = threadNr;
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
			this.raster = raster;
		}
		
		@Override
		public void run() {
			
			int nrOfLoops = (maxY - minY)*(maxX - minX);
			double cx, cy;
			int res;
			
			FractCalcerProgressData pdata = new FractCalcerProgressData();
			pdata.threadNr = this.threadNr;
			pdata.threadName = this.getName();
			
			for (int y = minY; y <= maxY; y++) {
				cy = fractParam.min_cy + (maxY - y) * fractParam.punkt_abstand; // imaginaerteil von c
				for (int x = minX; x <= maxX; x++) {
					cx = fractParam.min_cx + x * fractParam.punkt_abstand; // realteil von Pixelwert errechnet (skaliert)
					
					res = fractParam.iterFunc.fractIterFunc(cx,cy,fractParam.maxBetragQuadrat, fractParam.maxIterations,-0.8,0.8);
					
					raster.setPixel(x, y, palette[res].toRGBArray());
					
					
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
	public Image doInBackground() {
		// Create color palette
		this.palette = fractParam.colorPreset.createColorPalette(fractParam.maxIterations);
		initFractParams(this.fractParam);
		
		BufferedImage img = new BufferedImage(fractParam.picWidth, fractParam.picHeight, BufferedImage.TYPE_INT_RGB);
		
		Thread[] workers = new Thread[fractParam.nrOfWorkers];
		for (int i = 0; i < workers.length; i++) {
			int minX = fractParam.picWidth / fractParam.nrOfWorkers * i;
			int maxX = fractParam.picWidth / fractParam.nrOfWorkers * (i+1) - 1;
			workers[i] = new FractCalcThread(i,fractParam, img.getRaster(), minX, 0, maxX, fractParam.picHeight-1);
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
		return img;
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
