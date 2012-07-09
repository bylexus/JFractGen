package ch.alexi.fractgen.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ch.alexi.fractgen.logic.AppManager;
import ch.alexi.fractgen.models.UserPreferences;

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
		
		JLabel lblNumberOfWorkers = new JLabel("Number of workers:");
		contentPanel.add(lblNumberOfWorkers, "2, 4");
		
		rdbtnNrOfCpus = new JRadioButton("nr of CPUs");
		contentPanel.add(rdbtnNrOfCpus, "4, 4");
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.LEADING);
		contentPanel.add(panel, "4, 6, fill, fill");
		
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
		
		if (rdbtnNrOfCpus.isSelected()) {
			p.setNrOfWorkers(UserPreferences.WORKERS_PER_CPU);
		} else {
			p.setNrOfWorkers(Integer.parseInt(nrOfWorkersTxt.getText()));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) {
			this.setVisible(false);
			this.dispose();
		}
		
		if (e.getSource() == btnOK) {
			storeValues();
			this.setVisible(false);
			this.dispose();
		}
		
	}
}

