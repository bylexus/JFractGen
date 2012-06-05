package ch.alexi.fractgen.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import ch.alexi.fractgen.models.RGB;

public class ColorPalettePanel extends JComponent {
	private RGB[] colorPalette;
	private int nrOfIterations;

	public ColorPalettePanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.width = 800;
		d.height = 40;
		return d;
	}
	
	@Override
	public Dimension getMaximumSize() {
		Dimension d = super.getMaximumSize();
		d.height = 40;
		return d;
	}
	
	public void setColorPalette(RGB[] palette, int nrOfIterations) {
		this.colorPalette = palette;
		this.nrOfIterations = nrOfIterations;
		this.repaint();
	}
	
	
	@Override
	public void paint(Graphics g) {
		if (this.colorPalette != null && this.colorPalette.length > 0) {
			g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
			FontMetrics fm = g.getFontMetrics();
			int textHeight = fm.getHeight();
			int padding = 2;
			int yPos = 1;
			String title = "Number of iterations";
			
			Rectangle2D maxIterTextBounds = fm.getStringBounds(new Integer(this.colorPalette.length-1).toString(), g);
			Rectangle2D titleTextBounds = fm.getStringBounds(title, g);
			
			yPos = yPos + textHeight + padding;
			g.drawString(title, new Double((this.getWidth() - titleTextBounds.getWidth())/2).intValue(), yPos);
			
			g.drawString("0", 1, yPos);
			
			g.drawString(
				Integer.toString(this.nrOfIterations),
				this.getWidth() - new Double(maxIterTextBounds.getWidth()).intValue()-1,
				yPos
			);
			
			yPos = yPos + padding;
			for (int i = 0; i < this.getWidth(); i++) {
				int palettePos = new Double(i / (double)this.getWidth() * this.colorPalette.length).intValue();
				g.setColor(new Color(
						this.colorPalette[palettePos].r,
						this.colorPalette[palettePos].g,
						this.colorPalette[palettePos].b));
				g.drawLine(i, yPos, i, this.getHeight());
			}
		}
		
	}
}
