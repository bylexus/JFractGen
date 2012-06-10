package ch.alexi.fractgen.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * The footer panel, just some info.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class FooterPanel extends Box {
	public FooterPanel() {
		super(BoxLayout.X_AXIS);
		initGUI();
	}

	private void initGUI() {
		JLabel label = new JLabel("<html>JFractGen - &copy; 2012</html>");
		label.setMaximumSize(new Dimension(100,20));
		add(label);
		JButton linkBtn = new JButton();
		add(linkBtn);
		linkBtn.setText("<html><font color='blue'><u>Alexander Schenkel</u></font></html>");
		linkBtn.setHorizontalAlignment(SwingConstants.LEFT);
		linkBtn.setBorderPainted(false);
		linkBtn.setOpaque(false);
		linkBtn.setBackground(Color.lightGray);
		linkBtn.setMaximumSize(new Dimension(100,20));
		linkBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI("mailto:info@alexi.ch"));
					} catch (Exception ex) {
					}
				}
			}
		});
		
		JLabel label_1 = new JLabel("Version 0.1-alpha");
		label_1.setMaximumSize(new Dimension(100,20));
		add(label_1);
		
		Component glue = Box.createHorizontalGlue();
		add(glue);
		
		MemoryProgressbar progressBar = new MemoryProgressbar();
		add(progressBar);
	}
}
