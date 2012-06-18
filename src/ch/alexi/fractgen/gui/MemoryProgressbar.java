package ch.alexi.fractgen.gui;

import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import ch.alexi.fractgen.logic.MathLib;

@SuppressWarnings("serial")
public class MemoryProgressbar extends JProgressBar {
	private long maxMem = new Long(0);
	private long actMem = new Long(0);
	
	private Object semaphore = new Object();
	
	public MemoryProgressbar() {
		super(0,100);
		setStringPainted(true);
		init();
	}
	
	
	private void init() {
		SwingWorker<Integer, Integer> sw = new SwingWorker<Integer, Integer>() {
			
			@Override
			protected Integer doInBackground() throws Exception {
				while (!isCancelled()) {
					synchronized (semaphore) {
						maxMem = Runtime.getRuntime().totalMemory();
						actMem = maxMem - Runtime.getRuntime().freeMemory();
					}
					publish(new Long(Math.round(100.0*actMem / maxMem)).intValue());
					Thread.sleep(1000);
				}
				return new Integer(0);
			}
			
			@Override
			protected void process(List<Integer> l) {
				synchronized (semaphore) {
					MemoryProgressbar.this.setValue(l.get(l.size()-1));
					MemoryProgressbar.this.setString(MathLib.byteString(actMem) + " / " + MathLib.byteString(maxMem));
				}
			}
		};
		sw.execute();
	}
}
