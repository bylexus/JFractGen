package ch.alexi.jfractgen.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import ch.alexi.jfractgen.logic.AppManager;
import ch.alexi.jfractgen.logic.ImportPresetsAction;

/**
 * The main menu of the FractGen program.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class FileMenu extends JMenu implements ActionListener{

	private JMenuItem exportPresetsItem;
	private JMenuItem importPresetsItem;
	private JMenuItem preferencesItem;
	private JMenuItem quitItem;

	public FileMenu() {
		super("File");

		exportPresetsItem = new JMenuItem("Export Presets to JSON");
		exportPresetsItem.addActionListener(this);
		importPresetsItem = new JMenuItem(new ImportPresetsAction());
		preferencesItem = new JMenuItem("Preferences ...");
		preferencesItem.addActionListener(this);
		quitItem = new JMenuItem("Quit program");
		quitItem.addActionListener(this);

		this.add(exportPresetsItem);
		this.add(importPresetsItem);
		this.add(new JSeparator());
		this.add(preferencesItem);
		this.add(new JSeparator());
		this.add(quitItem);

	}

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

		if (e.getSource() == preferencesItem) {
			PreferencesDialog prefDlg = new PreferencesDialog();
			prefDlg.pack();
			prefDlg.setLocationRelativeTo(null);
			prefDlg.setVisible(true);
		}
	}
}
