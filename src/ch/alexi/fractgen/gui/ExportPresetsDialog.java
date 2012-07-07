package ch.alexi.fractgen.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.alexi.fractgen.logic.AppManager;
import ch.alexi.fractgen.models.ColorPreset;
import ch.alexi.fractgen.models.FractParam;
import ch.alexi.fractgen.models.PresetsCollection;

@SuppressWarnings("serial")
/**
 * The export (fractal/color) presets dialog. Lets the user
 * select some presets / color schemes and creates an output JSON
 * file containing the presets.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class ExportPresetsDialog extends JDialog implements ActionListener {
	private JButton btnCancel;
	private JButton btnExport;
	private JList fractPresetsList;
	private JList colorPresetsList;
	
	
	public ExportPresetsDialog(Frame owner) {
		super(owner,true);
		initGUI();
	}
	
	private void initGUI() {
		this.setTitle("Export presets to JSON");
		JLabel lblSelectFractalAndor = new JLabel("Select Fractal and/or Color presets to export:");
		getContentPane().add(lblSelectFractalAndor, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		fractPresetsList = new JList();
		fractPresetsList.setListData(AppManager.getInstance().getFractParamPresets());
		scrollPane.setViewportView(fractPresetsList);
		
		JLabel lblFractalPresets = new JLabel("Fractal Presets:");
		scrollPane.setColumnHeaderView(lblFractalPresets);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1);
		
		colorPresetsList = new JList();
		
		colorPresetsList.setListData(AppManager.getInstance().getPresets().getColorPresets());
		scrollPane_1.setViewportView(colorPresetsList);
		
		JLabel lblColorPresets = new JLabel("Color Presets:");
		scrollPane_1.setColumnHeaderView(lblColorPresets);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		btnCancel = new JButton("cancel");
		btnCancel.addActionListener(this);
		panel_1.add(btnCancel);
		
		btnExport = new JButton("export");
		btnExport.addActionListener(this);
		panel_1.add(btnExport);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) {
			this.close();
		}
		
		if (e.getSource() == btnExport) {
			showSaveDialogAndSave();
		}
	}
	
	private void showSaveDialogAndSave() {
		String lastSavePath = AppManager.getInstance().getUserProperty("lastPresetExportPath");
		
		JFileChooser dialog = new JFileChooser(lastSavePath);
		dialog.setFileFilter(new FileNameExtensionFilter("JSON File","json"));
		int ret = dialog.showSaveDialog(getOwner());
		if (ret == JFileChooser.APPROVE_OPTION) {
			File f = dialog.getSelectedFile();
			if (!f.getName().substring(f.getName().lastIndexOf(".")+1).equals("json")) {
				f = new File(f.getAbsolutePath()+".json");
			}
			
			PresetsCollection pc = new PresetsCollection();
			for (Object o : fractPresetsList.getSelectedValues()) {
				if (o instanceof FractParam) {
					pc.addFractalPreset((FractParam)o);
				}
			}
			for (Object o : colorPresetsList.getSelectedValues()) {
				if (o instanceof ColorPreset) {
					pc.addColorPreset((ColorPreset)o);
				}
			}
			
			pc.saveToJsonFile(f);
			JOptionPane.showMessageDialog(this.getOwner(), "Presets saved: "+f.getAbsolutePath(),"Info",JOptionPane.INFORMATION_MESSAGE);
			this.close();
			AppManager.getInstance().setUserProperty("lastPresetExportPath", f.getParent());
		}
	}
	
	public void close() {
		this.setVisible(false);
		this.dispose();
	}
}
