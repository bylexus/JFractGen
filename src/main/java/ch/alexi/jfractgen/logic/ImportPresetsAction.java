package ch.alexi.jfractgen.logic;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.alexi.jfractgen.models.PresetsCollection;

@SuppressWarnings("serial")
public class ImportPresetsAction extends AbstractAction {
	public ImportPresetsAction() {
		super("Import preset from JSON File");
	}

	public void actionPerformed(ActionEvent e) {
		// Show file open dialog to select a JSON file
		String lastSavePath = AppManager.getInstance().getUserPrefs().getLastPresetExportPath();

		JFileChooser dialog = new JFileChooser(lastSavePath);
		dialog.setFileFilter(new FileNameExtensionFilter("JSON File","json"));
		int ret = dialog.showOpenDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File f = dialog.getSelectedFile();

			PresetsCollection pc = new PresetsCollection();
			boolean res = pc.loadFromJsonFile(f);
			if (!res) {
				JOptionPane.showMessageDialog(null,"Unable to import presets from JSON file.","Error",JOptionPane.ERROR_MESSAGE);
			} else {
				// Ask the user if he wants to append / replace the existing presets:
				ret = JOptionPane.showConfirmDialog(null, "Do you want to append the presets (no for replace)?","Append/Override",JOptionPane.YES_NO_CANCEL_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					AppManager.getInstance().getPresets().appendPresets(pc);
				} else if (ret == JOptionPane.NO_OPTION) {
					AppManager.getInstance().getPresets().replacePresets(pc);
				}

				AppManager.getInstance().getUserPrefs().setLastPresetExportPath(f.getParent());
			}
		}
	}
}
