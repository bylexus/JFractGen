package ch.alexi.fractgen.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
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
	
	public void drawImage(Image i) {
		if (i != null) {
			this.fractImage = i;
			drawPanel.setPreferredSize(new Dimension(i.getWidth(this),i.getHeight(this)));
			drawPanel.repaint();
			this.revalidate();
		}
	}
	
	@Override
	public void addMouseListener(MouseListener l) {
		// Mouse events are directly redirected to the inner component:
		drawPanel.addMouseListener(l);
	}

	
}
