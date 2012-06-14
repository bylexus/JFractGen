package ch.alexi.fractgen.models;

import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ch.alexi.fractgen.logic.AppManager;


@SuppressWarnings("serial")
/**
 * A list of available fractal parameter presets, see presets.json.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class FractParamPresets extends Vector<FractParam> {
	private static FractParamPresets inst = new FractParamPresets();
	
	private FractParamPresets() {
		
	}
	
	public static Vector<FractParam> getPresets(JSONObject presets) {
		if (inst.isEmpty()) {
			if (presets != null && presets.has("fractalPresets")) {
				try {
					JSONArray entries = presets.getJSONArray("fractalPresets");
					for (int i = 0; i < entries.length(); i++) {
						JSONObject entry = entries.getJSONObject(i);
						inst.add(FractParam.fromJSONObject(entry));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return inst;
	}
	
	public static Vector<FractParam> getSystemPresets() {
		return getPresets(AppManager.getInstance().getPresetsJSONObject());
	}
	
	public static Vector<FractParam> getUserPresets() {
		return getPresets(AppManager.getInstance().getUserPresetsJSONObject());
	}
	
	public static JSONArray getSystemJSONArray() {
		JSONArray arr = new JSONArray();
		for (FractParam p : getSystemPresets()) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
	
	public static JSONArray getUserJSONArray() {
		JSONArray arr = new JSONArray();
		for (FractParam p : getUserPresets()) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
	
	
}
