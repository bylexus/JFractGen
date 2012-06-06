package ch.alexi.fractgen.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import ch.alexi.fractgen.models.ColorPreset;
import ch.alexi.fractgen.models.RGB;

/**
 * The cell renderer for the Color Preset combo box. Shows the color name
 * and the color palette.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */

@SuppressWarnings("serial")
public class ColorPaletteListCellRenderer extends JPanel implements	ListCellRenderer {
	private String  colorName = "";
	private RGB[]   palette = null;
	private boolean isSelected = false;
	
	public ColorPaletteListCellRenderer() {
		super();
		setOpaque(true);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (isSelected) {
            g.setColor(new Color(80,80,255));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }
		
		g.drawString(this.colorName, 3, 15);
		if (this.palette != null) {
			for (int i = 0; i < this.palette.length; i++) {
				g.setColor(new Color(this.palette[i].r,this.palette[i].g,this.palette[i].b));
				g.drawLine(i, 20, i, Math.max(20,this.getHeight()-3));
			}
		}
	}
	
	@Override
	/**
	 * Make sure the entry height is limited to 50px
	 */
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = 50;
		return d;
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean hasFocus) {
		if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
		
		this.colorName = value.toString();
		this.isSelected = isSelected;
		if (value instanceof ColorPreset) {
			// Get color palette with 256 entries:
			this.palette = ((ColorPreset)value).createFixedSizeColorPalette(256);
		}
		 
		return this;
	}

}
