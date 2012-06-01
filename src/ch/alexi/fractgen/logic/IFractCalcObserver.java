package ch.alexi.fractgen.logic;



public interface IFractCalcObserver {
	public void start(FractCalcer w);
	public void progress(FractCalcer worker, String threadName, double progress, double total);
	public void done(FractCalcer worker);
}
