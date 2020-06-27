package ch.alexi.jfractgen.gui;

import java.util.EventListener;

/**
 * Implementing classes can act as zoom listeners when the user zooms into
 * the fractal image either by click or by rubber band zoom.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public interface IZoomListener extends EventListener {
	public void clickZoom(int x, int y);
	public void rubberBandZoom(int x, int y, int width, int height);
	public void dragPan(int dx, int dy);
}
