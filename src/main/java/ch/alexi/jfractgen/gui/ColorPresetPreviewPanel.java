package ch.alexi.jfractgen.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import ch.alexi.jfractgen.models.ColorPreset;
import ch.alexi.jfractgen.models.RGB;

@SuppressWarnings("serial")
/**
 * This panel draws the color preset set with setColorPreset()
 * as its only content.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class ColorPresetPreviewPanel extends JPanel {
	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;

	private int orientation = HORIZONTAL;
	private ColorPreset preset;

	public ColorPresetPreviewPanel(int orientation) {
		Dimension d = getMinimumSize();
		if (orientation == VERTICAL) {
			this.orientation = VERTICAL;
			d.width = 20;
		} else {
			this.orientation = HORIZONTAL;
			d.height = 20;
		}
		this.setMinimumSize(d);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (this.preset == null) {
			return;
		}
		RGB[] palette = this.preset.createDynamicSizeColorPalette(1);

		if (this.orientation == HORIZONTAL) {
			for (int i = 0; i < this.getWidth(); i++) {
				int pIndex = new Double(1.0*i / this.getWidth() * palette.length).intValue();
				g.setColor(new Color(palette[pIndex].r,palette[pIndex].g,palette[pIndex].b));
				g.drawLine(i, 0, i, this.getHeight());
			}
		}

		if (this.orientation == VERTICAL) {
			for (int i = 0; i < this.getHeight(); i++) {
				int pIndex = new Double(1.0*i / this.getHeight() * palette.length).intValue();
				g.setColor(new Color(palette[pIndex].r,palette[pIndex].g,palette[pIndex].b));
				g.drawLine(i, 0, i, this.getWidth());
			}
		}
	}

	public void setColorPreset(ColorPreset p) {
		this.preset = p;
		this.repaint();
	}
}
