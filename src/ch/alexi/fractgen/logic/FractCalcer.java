package ch.alexi.fractgen.logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.List;
import javax.swing.SwingWorker;
import ch.alexi.fractgen.models.FractCalcerProgressData;
import ch.alexi.fractgen.models.FractParam;

public class FractCalcer extends SwingWorker<Image, FractCalcerProgressData>{
	
	private FractParam fractParam;
	private IFractCalcObserver observer;
	
	int[][] palette;
	
	class FractCalcThread extends Thread {
		int minX,minY,maxX,maxY;
		FractParam fractParam;
		BufferedImage image;
		
		public FractCalcThread(FractParam param,BufferedImage img, int minX, int minY, int maxX, int maxY) {
			this.fractParam = param;
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
			this.image = img;
		}
		
		@Override
		public void run() {
			Graphics2D g2 = (Graphics2D)image.getGraphics();
			WritableRaster r = image.getRaster();
			
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
					
					
					r.setPixel(x, y, palette[res]);
					
					
				}
				if (y % 10 == 0) {
					FractCalcerProgressData pdata = new FractCalcerProgressData();
					pdata.threadName = this.getName();
					pdata.threadProress = (y*(maxX - minX))/(double)nrOfLoops;
					FractCalcer.this.publish(pdata);
				}
				
				
				
			}
			
			
			/**
			 * var calcWidth = to_x - from_x;
	for (pix_x = from_x; pix_x <= to_x; pix_x++) {
		col = [];
		cx = min_cx + pix_x * punkt_abstand; // realteil von Pixelwert errechnet (skaliert)
		for (pix_y = from_y; pix_y <= to_y; pix_y++) {
			cy = min_cy + pix_y * punkt_abstand;

			res = iter_func(cx,cy,max_betrag_quadrat, max_iter,julia_r,julia_i);
			iter_wert = res.iterations;
			col.push(choose_color(iter_wert, max_iter,pix_x,pix_y,res.betrag_quadrat));
		}
		percDone = (pix_x-from_x) / calcWidth;
		self.postMessage({msg: 'result',x:pix_x,data: col,params: params,done:percDone});
	}
			 */
			
			
		}
	};
	
	public FractCalcer(FractParam params, IFractCalcObserver o) {
		this.fractParam = params;
		this.observer = o;
	}
	
	private void createColorPalette(int nrOfIters) {
		nrOfIters++;
		this.palette = new int[nrOfIters][3];
		for (int i = 0; i < this.palette.length; i++) {
			Double percCol = 255*i/(double)nrOfIters;
			
			this.palette[i][0] = percCol.intValue();
			this.palette[i][1] = (percCol.intValue()*5)%255;
			this.palette[i][2] = percCol.intValue();
		}
	}
	
	private void initFractParams(FractParam p) {
		/**
		 * if (initial_diameter == null) {
		initial_diameter = fractParams.diameter_cx;
	}
	var aspect, fract_width, fract_height;
	$('#fractCanvas').width(fractParams.pic_width);
	$('#fractCanvas').height(fractParams.pic_height);
	$('#fractCanvas').attr('width',fractParams.pic_width);
	$('#fractCanvas').attr('height',fractParams.pic_height);

	aspect = fractParams.pic_width / fractParams.pic_height;
	fract_width = fractParams.diameter_cx;
	fract_height = fractParams.diameter_cx / aspect;

	fractParams.min_cx = fractParams.center_cx - (fract_width / 2);
	fractParams.max_cx = fractParams.min_cx + fract_width;
	fractParams.min_cy = fractParams.center_cy - (fract_height / 2);
	fractParams.max_cy = fractParams.min_cy + fract_height;

	fractParams.punkt_abstand = (fractParams.max_cx - fractParams.min_cx) / fractParams.pic_width;
		 */
		
		double aspect, fract_width, fract_heigth;
		
		aspect = p.picWidth / (double)p.picHeight;
		fract_width = p.diameterCX;
		fract_heigth = p.diameterCX / aspect;
		
		p.min_cx = p.centerCX - (fract_width / 2);
		p.max_cx = p.min_cx + fract_width;
		p.min_cy = p.centerCY - (fract_heigth / 2);
		p.max_cy = p.min_cy + fract_heigth;
		
		p.punkt_abstand = (p.max_cx - p.min_cx) / p.picWidth;
	}
	
	@Override
	public Image doInBackground() {
		// Create color palette
		createColorPalette(fractParam.maxIterations);
		initFractParams(this.fractParam);
		
		BufferedImage img = new BufferedImage(fractParam.picWidth, fractParam.picHeight, BufferedImage.TYPE_INT_RGB);
		
		Thread[] workers = new Thread[fractParam.nrOfWorkers];
		for (int i = 0; i < workers.length; i++) {
			int minX = fractParam.picWidth / fractParam.nrOfWorkers * i;
			int maxX = fractParam.picWidth / fractParam.nrOfWorkers * (i+1) - 1;
			workers[i] = new FractCalcThread(fractParam, img, minX, 0, maxX, fractParam.picHeight-1);
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
