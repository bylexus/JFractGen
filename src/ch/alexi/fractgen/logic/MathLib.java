package ch.alexi.fractgen.logic;

public class MathLib {
	public static final double LOG_1000 = Math.log(1000);
	public static String byteString(long bytes) {
		
		int decade = new Double(Math.log(bytes) / LOG_1000).intValue();
		
		String decadeStr;
		double decadeValue = bytes / Math.pow(1000.0, decade);;
		switch (decade) {
		case 1: 
			decadeStr = "kB";
			break;
		case 2: 
			decadeStr = "MB";
			break;
		case 3: 
			decadeStr = "GB";
			break;
		case 4: 
			decadeStr = "TB";
			break;
		case 5: 
			decadeStr = "PB";
			break;
		case 6: 
			decadeStr = "EB";
			break;
		default:
			decadeStr = "bytes";
			decadeValue = bytes / Math.pow(1000.0, decade);
			break;
		}
		return Math.round(decadeValue) + " " + decadeStr;
		
	}
}
