package ch.alexi.fractgen.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.RGB;

/**
 * The legend panel shows information about the actual displayed fractal view, including
 * the color palette and some math information. 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class LegendPanel extends JPanel {
	ColorPalettePanel colorPalettePanel;
	private JTextArea infoTextArea;
	
	public LegendPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		colorPalettePanel = new ColorPalettePanel();
		colorPalettePanel.setAlignmentY(Component.TOP_ALIGNMENT);
		add(colorPalettePanel);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		add(infoPanel);
		infoPanel.setLayout(new BorderLayout(0, 0));
		
		infoTextArea = new JTextArea();
		infoTextArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,11));
		infoPanel.add(infoTextArea);
		Dimension d = infoPanel.getPreferredSize();
		d.height = infoPanel.getFontMetrics(infoPanel.getFont()).getHeight()*3; // 3 lines of text
		infoPanel.setPreferredSize(d);
		
		d = this.getPreferredSize();
		d.height = colorPalettePanel.getPreferredSize().height + infoPanel.getPreferredSize().height;
		this.setMinimumSize(d);
		this.setPreferredSize(d);
	}
	
	public void updateInfo(FractParam param, RGB[] colorPalette) {
		this.colorPalettePanel.setColorPalette(colorPalette, param.maxIterations);
		if (param != null) {
			infoTextArea.setText("");
			infoTextArea.append("Fractal Information:\n");
			infoTextArea.append("min C(r): "+param.min_cx+", max C(r): "+param.max_cx+", diameter C(r): "+(param.max_cx-param.min_cx)+"\n");
			infoTextArea.append("min C(i): "+param.min_cy+", max C(i): "+param.max_cy+", diameter C(i): "+(param.max_cy-param.min_cy));
		}
	}

}
