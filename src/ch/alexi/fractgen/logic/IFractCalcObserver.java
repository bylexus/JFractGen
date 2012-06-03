package ch.alexi.fractgen.logic;

import ch.alexi.fractgen.models.FractCalcerProgressData;



public interface IFractCalcObserver {
	public void start(FractCalcer w);
	public void progress(FractCalcer worker, FractCalcerProgressData progress);
	public void done(FractCalcer worker);
}
