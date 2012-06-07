package ch.alexi.fractgen.logic;

import ch.alexi.fractgen.models.FractCalcerProgressData;

/**
 * Implementing classes can get progress updates from the calculation thread. 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public interface IFractCalcObserver {
	public void start(FractCalcer w);
	public void progress(FractCalcer worker, FractCalcerProgressData progress);
	public void done(FractCalcer worker);
}
