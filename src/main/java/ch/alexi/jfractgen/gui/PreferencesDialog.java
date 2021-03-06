package ch.alexi.jfractgen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ch.alexi.jfractgen.logic.AppManager;
import ch.alexi.jfractgen.models.UserPreferences;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class PreferencesDialog extends JDialog implements ActionListener {
	private JTextField nrOfHistoryEntriesTxt;
	private JTextField nrOfWorkersTxt;
	private JRadioButton rdbtnNrOfCpus;
	private JRadioButton rdbtnFixed;
	private JButton btnOK;
	private JButton btnCancel;
	private JLabel lblBackgroundColor;
	private JButton btnChooseColor;
	private Color bgColor;

	public PreferencesDialog() {
		initGUI();
		setValues();
	}

	private void initGUI() {
		setTitle("JFractGen Preferences");
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:default"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));

		getContentPane().add(contentPanel,BorderLayout.CENTER);

		JLabel lblMaxNrOf = new JLabel("Max. nr of History Entries:");
		contentPanel.add(lblMaxNrOf, "2, 2, right, default");

		nrOfHistoryEntriesTxt = new JTextField();
		lblMaxNrOf.setLabelFor(nrOfHistoryEntriesTxt);
		contentPanel.add(nrOfHistoryEntriesTxt, "4, 2, left, default");
		nrOfHistoryEntriesTxt.setColumns(3);

		lblBackgroundColor = new JLabel("Background Color:");
		contentPanel.add(lblBackgroundColor, "2, 4");

		btnChooseColor = new JButton("Choose Color ...");
		btnChooseColor.addActionListener(this);
		contentPanel.add(btnChooseColor, "4, 4");

		JLabel lblNumberOfWorkers = new JLabel("Number of workers:");
		contentPanel.add(lblNumberOfWorkers, "2, 6");

		rdbtnNrOfCpus = new JRadioButton("nr of CPUs");
		contentPanel.add(rdbtnNrOfCpus, "4, 6");

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.LEADING);
		contentPanel.add(panel, "4, 8, fill, fill");

		rdbtnFixed = new JRadioButton("fixed:");
		panel.add(rdbtnFixed);

		nrOfWorkersTxt = new JTextField();
		panel.add(nrOfWorkersTxt);
		nrOfWorkersTxt.setColumns(3);

		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(rdbtnFixed);
		bg1.add(rdbtnNrOfCpus);

		JPanel btnPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) btnPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.TRAILING);
		getContentPane().add(btnPanel, BorderLayout.SOUTH);

		btnCancel = new JButton("cancel",AppManager.getInstance().getIcon("cancel"));
		btnCancel.addActionListener(this);
		btnPanel.add(btnCancel);

		btnOK = new JButton("OK", AppManager.getInstance().getIcon("accept"));
		btnOK.addActionListener(this);
		btnPanel.add(btnOK);


	}

	public void setValues() {
		UserPreferences p = AppManager.getInstance().getUserPrefs();
		nrOfHistoryEntriesTxt.setText(Integer.toString(p.getNrOfHistoryEntries()));

		bgColor = p.getBackgroundColor();
		btnChooseColor.setBackground(bgColor);
		btnChooseColor.setOpaque(true);

		if (p.cpuDependantNrOfWorkers()) {
			rdbtnNrOfCpus.setSelected(true);
		} else {
			rdbtnFixed.setSelected(true);
			nrOfWorkersTxt.setText(Integer.toString(p.getNrOfWorkers()));
		}
	}

	public void storeValues() {
		UserPreferences p = AppManager.getInstance().getUserPrefs();

		p.setNrOfHistoryEntries(Integer.parseInt(nrOfHistoryEntriesTxt.getText()));

		p.setBackgroundColor(bgColor);

		if (rdbtnNrOfCpus.isSelected()) {
			p.setNrOfWorkers(UserPreferences.WORKERS_PER_CPU);
		} else {
			p.setNrOfWorkers(Integer.parseInt(nrOfWorkersTxt.getText()));
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) {
			this.setVisible(false);
			this.dispose();
		}

		if (e.getSource() == btnOK) {
			storeValues();
			AppManager.getInstance().getMainFrame().getOutputPanel().repaint();
			this.setVisible(false);
			this.dispose();
		}

		if (e.getSource() == btnChooseColor) {
			Color newColor = JColorChooser.showDialog(PreferencesDialog.this, "Choose Background Color", bgColor);
			if (newColor != null) {
				bgColor = newColor;
				btnChooseColor.setBackground(bgColor);
				btnChooseColor.setOpaque(true);
			}
		}

	}
}

