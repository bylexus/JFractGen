package ch.alexi.fractgen.models;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresetsCollection {
	private Vector<FractParam> fractPresets = new Vector<FractParam>();
	private Vector<ColorPreset> colorPresets = new Vector<ColorPreset>();
	
	public PresetsCollection() {
	}
	
	public void addFractalPreset(FractParam p) {
		fractPresets.add(p);
	}
	
	public void addColorPreset(ColorPreset p) {
		colorPresets.add(p);
	}
	
	public JSONArray getFractalPresetsJsonArray() {
		JSONArray arr = new JSONArray();
		for (FractParam p : fractPresets) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
	
	public JSONArray getColorPresetsJsonArray() {
		JSONArray arr = new JSONArray();
		for (ColorPreset p : colorPresets) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
	
	public JSONObject getPresetsJsonObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("fractalPresets", getFractalPresetsJsonArray());
			obj.put("colorPresets", getColorPresetsJsonArray());
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
}
