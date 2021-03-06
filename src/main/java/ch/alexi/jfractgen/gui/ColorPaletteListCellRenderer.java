package ch.alexi.jfractgen.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import ch.alexi.jfractgen.models.ColorPreset;
import ch.alexi.jfractgen.models.RGB;

/**
 * The cell renderer for the Color Preset combo box. Shows the color name
 * and the color palette.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */

@SuppressWarnings("serial")
public class ColorPaletteListCellRenderer extends JPanel implements	ListCellRenderer<ColorPreset> {
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
			for (int i = 0; i < 256; i++) {
				int pIndex = new Double(1.0*i / 256 * this.palette.length).intValue();
				g.setColor(new Color(this.palette[pIndex].r,this.palette[pIndex].g,this.palette[pIndex].b));
				g.drawLine(i, 20, i, Math.max(20,this.getHeight()-3));
			}
		}
	}

	public Component getListCellRendererComponent(JList<? extends ColorPreset> list, ColorPreset value,
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
		// Get color palette with 256 entries:
		this.palette = value.createDynamicSizeColorPalette(256, 1);

		Dimension d = new Dimension();
		if (list.getSelectedIndex() != index) {
			d.height = 50;
		} else {
			d.height = 0;
		}
		this.setPreferredSize(d);

		return this;
	}
}
