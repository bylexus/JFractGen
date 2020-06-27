package ch.alexi.jfractgen.models;
/**
 * Represents the fractal calculation progress data during the calculation.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class FractCalcerProgressData {
	public int threadNr;
	public String threadName;
	public double threadProgress;
	public double totalProgress;
}
