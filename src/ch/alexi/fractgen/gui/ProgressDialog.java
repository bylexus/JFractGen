package ch.alexi.fractgen.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog {
	private int nrOfWorkers;
	private JProgressBar[] progressBars;
	private List<ActionListener> actionListeners = new Vector<ActionListener>();
	
	public final static String CANCEL = "cancel";
	
	public ProgressDialog(int nrOfWorkers, Frame parent) {
		super(parent,true);
		this.nrOfWorkers = nrOfWorkers;
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		this.add(new JLabel("Fractal calculation in progress, please stand by..."));
		this.setLocationRelativeTo(this.getParent());
		
		
		progressBars = new JProgressBar[this.nrOfWorkers];
		for (int i = 0; i < this.nrOfWorkers; i++) {
			progressBars[i] = new JProgressBar(0,100);
			this.add(progressBars[i]);
		}
		
		final JButton cancelBtn = new JButton("cancel operation");
		this.add(cancelBtn);
		
		cancelBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelBtn.setText("cancelling...");
				cancelBtn.setEnabled(false);
				e.setSource(CANCEL);
				performAction(e);
			}
		});
	}
	
	private void performAction(ActionEvent e) {
		for (ActionListener l : this.actionListeners) {
			l.actionPerformed(e);
		}
	}
	
	public void addActionListener(ActionListener a) {
		if (!this.actionListeners.contains(a)) {
			this.actionListeners.add(a);
		}
	}
	
	
	public void updateProgress(int worker, int progress) {
		if (worker < progressBars.length) {
			progressBars[worker].setValue(progress);
		}
	}
}
