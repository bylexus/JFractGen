package ch.alexi.fractgen.gui;

import java.util.EventListener;

public interface IZoomListener extends EventListener {
	public void clickZoom(int x, int y);
	public void rubberBandZoom(int x, int y, int width, int height);
}
