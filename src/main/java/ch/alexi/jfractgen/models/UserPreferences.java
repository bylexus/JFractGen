package ch.alexi.jfractgen.models;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserPreferences {
	public static final String NR_OF_HISTORY_ENTRIES = "nrOfHistoryEntries";
	public static final String NR_OF_WORKERS = "perCPU";
	public static final int WORKERS_PER_CPU = -1;


	private Properties prefs;

	public UserPreferences() {
		prefs = new Properties();
	}

	public void loadPrefsFromFile(File f) {
		try {
			prefs.load(new FileInputStream(f));
			System.out.println("User loaded from: "+f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void savePrefsToFile(File f) {
		try {
			prefs.store(new FileOutputStream(f), "User preferences for JFractGen");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public int getNrOfHistoryEntries() {
		String value = prefs.getProperty(NR_OF_HISTORY_ENTRIES, "25");
		int entries = Integer.parseInt(value);
		if (entries < 1) {
			entries = 25;
		}
		return entries;
	}

	public void setNrOfHistoryEntries(int entries) {
		prefs.setProperty(NR_OF_HISTORY_ENTRIES, Integer.toString(Math.max(0, entries)));
	}

	public boolean cpuDependantNrOfWorkers() {
		String value = prefs.getProperty(NR_OF_WORKERS, "perCPU");
		if (value.equals("perCPU")) {
			return true;
		} else {
			return false;
		}
	}

	public int getNrOfWorkers() {
		String value = prefs.getProperty(NR_OF_WORKERS, "perCPU");
		int nr;
		if (value.equals("perCPU")) {
			nr = Runtime.getRuntime().availableProcessors();
		} else {
			nr = Integer.parseInt(value);
		}
		if (nr < 1) {
			nr = 2;
		}
		return nr;
	}

	public void setNrOfWorkers(int value) {
		if (value == WORKERS_PER_CPU) {
			prefs.setProperty(NR_OF_WORKERS, "perCPU");
		} else {
			prefs.setProperty(NR_OF_WORKERS, Integer.toString(Math.max(1, value)));
		}
	}

	public String getLastPresetExportPath() {
		if (prefs.containsKey("lastPresetExportPath")) {
			return prefs.getProperty("lastPresetExportPath");
		} else {
			return null;
		}
	}

	public void setLastPresetExportPath(String path) {
		prefs.setProperty("lastPresetExportPath", path);
	}


	public String getLastSavePath() {
		if (prefs.containsKey("lastSavePath")) {
			return prefs.getProperty("lastSavePath");
		} else {
			return null;
		}
	}

	public void setLastSavePath(String path) {
		prefs.setProperty("lastSavePath", path);
	}

	/**
	 * a read-only, not well-known property. If set, no copyright info is
	 * drawn in images.
	 * @return
	 */
	public boolean copyrightDisabled() {
		if (prefs.containsKey("disableCopyright")) {
			String value = prefs.getProperty("disableCopyright");
			if (value.equals("true") || value.equals("1")) {
				return true;
			}
		}
		return false;
	}


	public Color getBackgroundColor() {
		if (prefs.containsKey("backgroundColor")) {
			Color c = Color.decode("#"+prefs.getProperty("backgroundColor"));
			if (c != null) {
				return c;
			}
		}
		return new Color(0,0,0);
	}

	public void setBackgroundColor(Color c) {
		String cssValue = Integer.toHexString(c.getRGB());
		cssValue = cssValue.substring(2, cssValue.length());
		prefs.setProperty("backgroundColor", cssValue);
	}



}
