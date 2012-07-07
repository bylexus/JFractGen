package ch.alexi.fractgen.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresetsCollection {
	public final static String FRACT_PRESETS = "fractalPresets";
	public final static String COLOR_PRESETS = "colorPresets";
	
	private FractParamPresets fractPresets = new FractParamPresets();
	private ColorPresets colorPresets = new ColorPresets();
	
	public PresetsCollection() {
	}
	
	public void addFractalPreset(FractParam p) {
		fractPresets.add(p);
	}
	
	public void addColorPreset(ColorPreset p) {
		colorPresets.add(p);
	}
	
	public FractParamPresets getFractalPresets() {
		return fractPresets;
	}
	
	public ColorPresets getColorPresets() {
		return colorPresets;
	}
	
	public JSONArray getFractalPresetsJsonArray() {
		/*
		JSONArray arr = new JSONArray();
		fractPresets.getJSONArray();
		for (FractParam p : fractPresets) {
			arr.put(p.toJSONObject());
		}
		*/
		return getFractalPresets().getJSONArray();
	}
	
	public JSONArray getColorPresetsJsonArray() {
		return getColorPresets().getJSONArray();
	}
	
	/**
	 * Returns one color preset identified by its name (presets.json, preset.name property)
	 * @param name
	 * @return
	 */
	public ColorPreset getColorPresetByName(String name) {
		ColorPresets pres = getColorPresets();
		for (ColorPreset p : getColorPresets()) {
			if (name.equals(p.name)) {
				return p;
			}
		}
		return null;
	}
	
	public JSONObject getPresetsJsonObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put(FRACT_PRESETS, getFractalPresetsJsonArray());
			obj.put(COLOR_PRESETS, getColorPresetsJsonArray());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public String getPresetsJsonString() {
		String ret = null;
		try {
			ret = getPresetsJsonObject().toString(4);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean saveToJsonFile(File f) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(f));
			bw.write(getPresetsJsonString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean loadFromJsonStream(InputStream is) {
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			StringBuffer buf = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buf.append(line);
			}
			
			JSONObject obj = new JSONObject(buf.toString());
			if (obj.has(FRACT_PRESETS)) {
				JSONArray fractPresetsArray = obj.getJSONArray(FRACT_PRESETS);
				this.fractPresets.clear();
				for (int i = 0; i < fractPresetsArray.length(); i++) {
					JSONObject fractObj = fractPresetsArray.getJSONObject(i);
					this.fractPresets.add(FractParam.fromJSONObject(fractObj));
				}
			}
			if (obj.has(COLOR_PRESETS)) {
				JSONArray colPresetsArray = obj.getJSONArray(COLOR_PRESETS);
				this.colorPresets.clear();
				for (int i = 0; i < colPresetsArray.length(); i++) {
					JSONObject colObj = colPresetsArray.getJSONObject(i);
					this.colorPresets.add(ColorPreset.fromJSONObject(colObj));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean loadFromJsonFile(File f) {
		try {
			return loadFromJsonStream(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
}
