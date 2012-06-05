package ch.alexi.fractgen.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FooterPanel extends JPanel {
	public FooterPanel() {
		super();
		initGUI();
	}

	private void initGUI() {
		add(new JLabel("<html>JFractGen - &copy; 2012</html>"));
		JButton linkBtn = new JButton();
		linkBtn.setText("<html><font color='blue'><u>Alexander Schenkel</u></font></html>");
		linkBtn.setHorizontalAlignment(SwingConstants.LEFT);
		linkBtn.setBorderPainted(false);
		linkBtn.setOpaque(false);
		linkBtn.setBackground(Color.lightGray);
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

		add(linkBtn);
		
		add(new JLabel("Version 0.1-alpha - do not complain!"));
	}
}
