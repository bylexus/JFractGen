package ch.alexi.jfractgen.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import ch.alexi.jfractgen.logic.AppManager;
import ch.alexi.jfractgen.logic.MathLib;

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
	public static final int MOVE_MODE_ZOOM = 1;
	public static final int MOVE_MODE_DRAG = 2;

	private Image fractImage;
	private JPanel drawPanel;
	private JLayeredPane layerPane;
	private JPanel rubberBand;
	private boolean mouseMoved = false;
	private Point mouseStartPoint;
	private List<IZoomListener> zoomListeners;

	/*private Color white = new Color(255,255,255);
	private Color gray = new Color(196,196,196);
	private int transparentTileSize = 20;
	*/
	private Point drawOffset = new Point(0, 0);


	private int moveMode = MOVE_MODE_ZOOM;

	public FractOutPanel() {
		zoomListeners = new Vector<IZoomListener>();
		layerPane = new JLayeredPane();
		drawPanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				// Draw tiles for transparental indication
				/*
				int tilesX = this.getWidth() / FractOutPanel.this.transparentTileSize + 1;
				int tilesY = this.getHeight() / FractOutPanel.this.transparentTileSize + 1;
				boolean toggle = false;
				for (int ty = 0; ty < tilesY; ty++) {
					for (int tx = 0; tx < tilesX; tx++) {
						if (toggle) {
							g.setColor(white);
						} else {
							g.setColor(gray);
						}
						toggle = !toggle;
						g.fillRect(tx*transparentTileSize, ty*transparentTileSize, transparentTileSize, transparentTileSize);
					}
				}*/

				// Draw Background Color:
				//g.setColor()
				g.setColor(AppManager.getInstance().getUserPrefs().getBackgroundColor());
				g.fillRect(0, 0, getWidth(), getHeight());

				// Draw the fractal image:
				if (fractImage != null) {
					g.drawImage(fractImage, drawOffset.x, drawOffset.y,this);
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
				if (moveMode == MOVE_MODE_ZOOM) {
					if (mouseMoved) {
						performRubberbandZoom(rubberBand.getX(), rubberBand.getY(), rubberBand.getWidth(), rubberBand.getHeight());
					} else {
						performClickZoom(e.getPoint().x, e.getPoint().y);
					}
					rubberBand.setVisible(false);
				}

				if (moveMode == MOVE_MODE_DRAG) {
					if (mouseStartPoint != null) {
						int dx = e.getPoint().x-mouseStartPoint.x;
						int dy = e.getPoint().y-mouseStartPoint.y;
						if (dx != 0 || dy != 0) {
							performDragPan(dx, dy);
						}
					}
				}
				drawOffset.x = 0;
				drawOffset.y = 0;
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
					if (moveMode == MOVE_MODE_ZOOM) {
						int dX = e.getPoint().x-mouseStartPoint.x;
						// dY is based on the image's aspect ration:
						int dY = Math.abs((int)(dX * (getFractalImage().getHeight(FractOutPanel.this) / (double)(getFractalImage().getWidth(FractOutPanel.this)))));
						dY = mouseStartPoint.y > e.getY() ? dY * -1 : dY;
						Dimension evtBound = new Dimension(Math.abs(dX), Math.abs(dY));

						int left = MathLib.minInt(mouseStartPoint.x, e.getPoint().x);
						int top = MathLib.minInt(mouseStartPoint.y, mouseStartPoint.y + dY);

						rubberBand.setBounds(left, top, evtBound.width,evtBound.height);
						rubberBand.setVisible(true);
					}

					if (moveMode == MOVE_MODE_DRAG) {
						drawOffset.x = e.getPoint().x-mouseStartPoint.x;
						drawOffset.y = e.getPoint().y-mouseStartPoint.y;
						drawPanel.repaint();
					}
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

			// add copyright information
			if (!AppManager.getInstance().getUserPrefs().copyrightDisabled()) {
				Graphics2D g = (Graphics2D)i.getGraphics();
				g.setFont(new Font("Helvetica", Font.ITALIC, Math.round(i.getHeight(this)*2.0f/100.0f)));
				FontMetrics fm = g.getFontMetrics();
				String text = "© JFractGen Alexander Schenkel";
				int offsetX = i.getWidth(this)-fm.stringWidth(text)-10;
				int offsetY = i.getHeight(this)-fm.getHeight();

				g.setColor(new Color(0x66000000, true));
				g.drawString(text, offsetX+1, offsetY+1);

				g.setColor(new Color(0x66ffffff, true));
				g.drawString(text, offsetX, offsetY);
			}

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

	private void performDragPan(int dx, int dy) {
		for(IZoomListener l : zoomListeners) {
			l.dragPan(dx, dy);
		}
	}

	public void setMoveMode(int mode) {
		if (mode == MOVE_MODE_ZOOM || mode == MOVE_MODE_DRAG) {
			this.moveMode = mode;
		}
	}

	public int getMoveMode() {
		return this.moveMode;
	}


}
