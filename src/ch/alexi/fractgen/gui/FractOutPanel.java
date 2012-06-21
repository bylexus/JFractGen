package ch.alexi.fractgen.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ch.alexi.fractgen.logic.MathLib;

/**
 * The FractOutPanel draws the Fractal image and allows / initiates zooming.
 * It informs IZoomListeners of zoom events. 
 * It gets updated via drawImage() method, which simply takes an image to be drawn in the panel. 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class FractOutPanel extends JScrollPane {
	private Image fractImage;
	private JPanel drawPanel;
	private JLayeredPane layerPane;
	private JPanel rubberBand;
	private boolean mouseMoved = false;
	private Point mouseStartPoint;
	private List<IZoomListener> zoomListeners;
	
	public FractOutPanel() {
		zoomListeners = new Vector<IZoomListener>();
		layerPane = new JLayeredPane();
		
		drawPanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				if (fractImage != null) {
					g.drawImage(fractImage, 0, 0, this);
				}
			}
		};
		
		
		/**
		 * on mouse down:
		 *   save the start point, mark the mouse as not moved.
		 * On mouse move (dragged): 
		 *   When a start point is set, it means that the mouse key is still down,
		 *   so the user wants to draw a zoom rubber band. Do so.
		 * On mouse up:
		 *   If the mouse was not moved, it was a click.
		 *   If the mouse was moved, it was a rubber band zoom. Get the selected rectangle. 
		 */
		drawPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mouseMoved = false;
				mouseStartPoint = e.getPoint();
			}
			public void mouseReleased(MouseEvent e) {
				if (mouseMoved) {
					performRubberbandZoom(rubberBand.getX(), rubberBand.getY(), rubberBand.getWidth(), rubberBand.getHeight());
				} else {
					performClickZoom(e.getPoint().x, e.getPoint().y);
				}
				rubberBand.setVisible(false);
				mouseMoved = false;
			}
		});
		
		drawPanel.addMouseMotionListener(new MouseAdapter() {
			
			/**
			 * While drag, calculate the size/dimension of the rubber band
			 * and draw it.
			 */
			public void mouseDragged(MouseEvent e) {
				mouseMoved = true;
				if (mouseStartPoint != null) {
					Dimension evtBound = new Dimension(Math.abs(e.getPoint().x-mouseStartPoint.x), Math.abs(e.getPoint().y-mouseStartPoint.y));
					
					int left = MathLib.minInt(mouseStartPoint.x, e.getPoint().x);
					int top = MathLib.minInt(mouseStartPoint.y, e.getPoint().y);
					
					rubberBand.setBounds(left, top, evtBound.width,evtBound.height);
					rubberBand.setVisible(true);
				}
			}
		});
		
		/**
		 * The rubber band is the zoom area the user can span with the mouse.
		 */
		rubberBand = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				g.drawRect(1, 1, this.getWidth()-1, this.getHeight() - 1);
				
				g.setColor(Color.WHITE);
				g.drawRect(0, 0, this.getWidth()-2, this.getHeight() - 2);
			}
		};
		rubberBand.setOpaque(false);
		rubberBand.setVisible(false);
		
		layerPane.add(drawPanel,JLayeredPane.DEFAULT_LAYER);
		layerPane.add(rubberBand,JLayeredPane.DRAG_LAYER);
		
		this.setViewportView(layerPane);
	}
	
	public void drawImage(Image i) {
		if (i != null) {
			this.fractImage = i;
			drawPanel.setPreferredSize(new Dimension(i.getWidth(this),i.getHeight(this)));
			layerPane.setPreferredSize(new Dimension(i.getWidth(this),i.getHeight(this)));
			drawPanel.setBounds(0, 0, fractImage.getWidth(this), fractImage.getHeight(this));
			drawPanel.repaint();
			this.revalidate(); // makes sure the scroll bars get update when needed after the fract image size changed
		}
	}
	
	public Image getFractalImage() {
		return this.fractImage;
	}
	
	@Override
	public void addMouseListener(MouseListener l) {
		// Mouse events are directly redirected to the inner component:
		drawPanel.addMouseListener(l);
	}
	
	public void addZoomListener(IZoomListener l) {
		if (!zoomListeners.contains(l)) {
			zoomListeners.add(l);
		}
	}
	
	private void performClickZoom(int x, int y) {
		for(IZoomListener l : zoomListeners) {
			l.clickZoom(x, y);
		}
	}
	
	private void performRubberbandZoom(int x, int y, int width, int height) {
		for(IZoomListener l : zoomListeners) {
			l.rubberBandZoom(x, y, width, height);
		}
	}

	
}
