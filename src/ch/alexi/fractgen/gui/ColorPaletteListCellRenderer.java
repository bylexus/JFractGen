package ch.alexi.fractgen.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import ch.alexi.fractgen.models.ColorPreset;
import ch.alexi.fractgen.models.RGB;

public class ColorPaletteListCellRenderer extends JPanel implements	ListCellRenderer {
	private String colorName = "";
	private RGB[] palette = null;
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
			this.palette = ((ColorPreset)value).createColorPalette(256);
		}
		 
		return this;
	}

}
