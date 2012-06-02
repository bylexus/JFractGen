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
	
	int[][] palette;
	
	class FractCalcThread extends Thread {
		int minX,minY,maxX,maxY;
		FractParam fractParam;
		WritableRaster raster;
		
		public FractCalcThread(FractParam param,WritableRaster raster, int minX, int minY, int maxX, int maxY) {
			this.fractParam = param;
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
			this.raster = raster;
		}
		
		@Override
		public void run() {
			
			int nrOfLoops = (maxY - minY)*(maxX - minX);
			int[] cols = new int[3];
			double cx, cy;
			Double percCol;
			int res;
			
			for (int y = minY; y <= maxY; y++) {
				cy = fractParam.min_cy + (maxY - y) * fractParam.punkt_abstand; // imaginaerteil von c
				for (int x = minX; x <= maxX; x++) {
					cx = fractParam.min_cx + x * fractParam.punkt_abstand; // realteil von Pixelwert errechnet (skaliert)
					
					res = fractParam.iterFunc.fractIterFunc(cx,cy,fractParam.maxBetragQuadrat, fractParam.maxIterations,-0.8,0.8);
					raster.setPixel(x, y, palette[res]);
				}
				
				// Progress update:
				if (y % 10 == 0) {
					FractCalcerProgressData pdata = new FractCalcerProgressData();
					pdata.threadName = this.getName();
					pdata.threadProress = (y*(maxX - minX))/(double)nrOfLoops;
					FractCalcer.this.publish(pdata);
				}
			}
		}
	};
	
	public FractCalcer(FractParam params, IFractCalcObserver o) {
		this.fractParam = params;
		this.observer = o;
	}
	
	private void createColorPalette(int nrOfIters, ColorPreset preset) {
		nrOfIters++;
		this.palette = new int[nrOfIters][3];
		
		int stepsPerFade = nrOfIters / (preset.colors.length - 1);
		RGB actBase, nextBase;
		int rStep,gStep,bStep;
		int r,g,b;
		int counter = 0;
		
		for (int i = 0; i < preset.colors.length - 1; i++) {
			actBase = preset.colors[i];
			nextBase = preset.colors[i+1];
			r = actBase.r;
			g = actBase.g;
			b = actBase.b;
			rStep = (nextBase.r-actBase.r) / stepsPerFade;
			bStep = (nextBase.b-actBase.b) / stepsPerFade;
			gStep = (nextBase.g-actBase.g) / stepsPerFade;
			for (int j = 0; j < stepsPerFade;j++) {
				this.palette[counter][0] = r;
				this.palette[counter][1] = g;
				this.palette[counter][2] = b;
				
				r += rStep;
				g += gStep;
				b += bStep;
				counter++;
			}
		}
		this.palette[this.palette.length-1][0] = 0;
		this.palette[this.palette.length-1][1] = 0;
		this.palette[this.palette.length-1][2] = 0;
	}
	
	private void initFractParams(FractParam p) {
		p.initFractParams();
	}
	
	@Override
	public Image doInBackground() {
		// Create color palette
		createColorPalette(fractParam.maxIterations,fractParam.colorPreset);
		initFractParams(this.fractParam);
		
		BufferedImage img = new BufferedImage(fractParam.picWidth, fractParam.picHeight, BufferedImage.TYPE_INT_RGB);
		
		Thread[] workers = new Thread[fractParam.nrOfWorkers];
		for (int i = 0; i < workers.length; i++) {
			int minX = fractParam.picWidth / fractParam.nrOfWorkers * i;
			int maxX = fractParam.picWidth / fractParam.nrOfWorkers * (i+1) - 1;
			workers[i] = new FractCalcThread(fractParam, img.getRaster(), minX, 0, maxX, fractParam.picHeight-1);
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
	
	@Override
	protected void process(List<FractCalcerProgressData> pdata) {
		for (FractCalcerProgressData p : pdata) {
			observer.progress(this, p.threadName, p.threadProress, p.totalProgress);
		}
	}
	
	@Override
	protected void done() {
		observer.done(this);
	}
	
	
	
}
