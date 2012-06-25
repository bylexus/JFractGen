package ch.alexi.fractgen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {
	public static AboutDialog inst = new AboutDialog();
	
	private AboutDialog() {
		setPreferredSize(new Dimension(450, 300));
		
		setModal(true);
		
		JTextArea textArea = new JTextArea();
		getContentPane().add(textArea, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		
		final Image logo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/fract_logo.png"));
		JPanel logoPanel = new JPanel() {
			
			@Override
			public void paint(Graphics g) {
				g.drawImage(logo, 0, 0, this);
			}
			
			@Override
			public Dimension getMinimumSize() {
				return new Dimension(200,125);
			}
			@Override
			public Dimension getMaximumSize() {
				return new Dimension(200,125);
			}
		};
		panel.add(logoPanel);
		
		JLabel lblJfractgen = new JLabel("JFractGen");
		lblJfractgen.setFont(new Font("Lucida Grande", Font.BOLD, 18));
		panel.add(lblJfractgen);
	}
	
	public static void display() {
		inst.pack();
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);
	}

}
