package ch.alexi.fractgen.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import ch.alexi.fractgen.logic.AppManager;

/**
 * The main menu of the FractGen program.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class MainMenu extends JMenu implements ActionListener{
	
	private JMenuItem exportPresetsItem;
	private JMenuItem importPresetsItem;
	private JMenuItem quitItem;
	
	public MainMenu() {
		super("File");
		
		exportPresetsItem = new JMenuItem("Export Presets to JSON");
		exportPresetsItem.addActionListener(this);
		importPresetsItem = new JMenuItem("Import Presets to JSON");
		importPresetsItem.addActionListener(this);
		quitItem = new JMenuItem("Quit program");
		quitItem.addActionListener(this);
		
		this.add(exportPresetsItem);
		this.add(importPresetsItem);
		this.add(new JSeparator());
		this.add(quitItem);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == quitItem) {
			AppManager.getInstance().shutdown();
		}
		
		if (e.getSource() == exportPresetsItem) {
			JDialog exportDlg = new ExportPresetsDialog(AppManager.getInstance().getMainFrame());
			exportDlg.pack();
			exportDlg.setLocationRelativeTo(null);
			exportDlg.setVisible(true);
		}
	}
}
