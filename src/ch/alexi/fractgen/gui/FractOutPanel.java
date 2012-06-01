package ch.alexi.fractgen.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FractOutPanel extends JScrollPane {
	private Image fractImage;
	
	private JPanel drawPanel;;
	
	public FractOutPanel() {
		drawPanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				if (fractImage != null) {
					g.drawImage(fractImage, 0, 0, this);
				}
			}
		};
		this.setViewportView(drawPanel);
	}
	
	/**
	 * scrollPane.setViewportView(fractOutPanel);
		fractOutPanel.setLayout(null);
	 * @param i
	 */
	
	public void drawImage(Image i) {
		if (i != null) {
			this.fractImage = i;
			drawPanel.setPreferredSize(new Dimension(i.getWidth(this),i.getHeight(this)));
			drawPanel.repaint();
			this.revalidate();
		}
	}

	
}
