package ch.alexi.fractgen.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;

public class ProgressDialog extends JDialog {
	private int nrOfWorkers;
	private JProgressBar[] progressBars;
	
	public ProgressDialog(int nrOfWorkers) {
		this.nrOfWorkers = nrOfWorkers;
		initGUI();
	}
	
	private void initGUI() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		this.getContentPane().add(new JLabel("Fractal calculation in progress, please stand by..."));
		this.setPreferredSize(new Dimension(300,400));
		this.setBounds(0,0,400,30+nrOfWorkers*50);
		this.setLocationRelativeTo(null);
		
		progressBars = new JProgressBar[this.nrOfWorkers];
		for (int i = 0; i < this.nrOfWorkers; i++) {
			progressBars[i] = new JProgressBar(0,100);
			this.getContentPane().add(progressBars[i]);
		}
		this.setModal(true);
	}
	
	public void updateProgress(int worker, int progress) {
		if (worker < progressBars.length) {
			progressBars[worker].setValue(progress);
		}
	}
}
