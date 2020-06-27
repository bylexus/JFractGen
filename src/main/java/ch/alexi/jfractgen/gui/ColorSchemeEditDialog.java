package ch.alexi.jfractgen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.alexi.jfractgen.logic.AppManager;
import ch.alexi.jfractgen.models.ColorPreset;

@SuppressWarnings("serial")
public class ColorSchemeEditDialog extends JDialog implements ActionListener,ListSelectionListener {
	class SchemeListModel extends AbstractListModel {
		private Vector<ColorPreset> list = new Vector<ColorPreset>();

		public SchemeListModel(Vector<ColorPreset> list) {
			this.updateList(list);
		}

		public void updateList(Vector<ColorPreset> newList) {
			if (newList != null) {
				this.list.clear();
				this.list.addAll(newList);
				this.fireContentsChanged(this, 0, this.list.size()-1);
			}
		}

		public Object getElementAt(int index) {
			if (index >= 0 && index < list.size()) {
				return list.get(index);
			}
			return null;
		}

		public int getSize() {
			return list.size();
		}
	}


	JButton btnCancel;
	JList schemeList;
	private ColorPresetEditPanel colorPresetEditPanel;

	public ColorSchemeEditDialog(Frame parent) {
		super(parent);

		initGUI();
	}

	private void initGUI() {
		setModal(true);
		setPreferredSize(new Dimension(800, 600));
		setTitle("Edit color schemes");
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

		JPanel listPanel = new JPanel();
		getContentPane().add(listPanel, BorderLayout.WEST);
		listPanel.setLayout(new BorderLayout(0, 0));

		schemeList = new JList(new SchemeListModel(AppManager.getInstance().getPresets().getColorPresets()));
		schemeList.addListSelectionListener(this);
		listPanel.add(new JScrollPane(schemeList), BorderLayout.CENTER);

		JPanel panel = new JPanel();
		listPanel.add(panel, BorderLayout.SOUTH);

		JButton btnAdd = new JButton("add");
		panel.add(btnAdd);

		JButton btnDelete = new JButton("delete");
		panel.add(btnDelete);

		JLabel lblColorSchemes = new JLabel("Color Schemes");
		listPanel.add(lblColorSchemes, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		buttonPanel.add(btnCancel);

		JButton btnOk = new JButton("OK");
		buttonPanel.add(btnOk);

		JScrollPane mainPanel = new JScrollPane();
		getContentPane().add(mainPanel, BorderLayout.CENTER);

		colorPresetEditPanel = new ColorPresetEditPanel();
		mainPanel.setViewportView(colorPresetEditPanel);


		schemeList.setSelectedIndex(0);
	}

	public void display() {
		pack();
		setVisible(true);
		setLocationRelativeTo(getParent());
		((SchemeListModel)schemeList.getModel()).updateList(AppManager.getInstance().getPresets().getColorPresets());
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) {
			this.setVisible(false);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			ColorPreset selected = (ColorPreset)schemeList.getModel().getElementAt(schemeList.getSelectedIndex());
			colorPresetEditPanel.setColorPreset(selected);
		}
	}
}
