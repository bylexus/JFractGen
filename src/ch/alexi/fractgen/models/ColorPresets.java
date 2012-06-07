package ch.alexi.fractgen.models;

import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ch.alexi.fractgen.logic.AppManager;

@SuppressWarnings("serial")
/**
 * A list of avaliable color presets.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class ColorPresets extends Vector<ColorPreset> {
	public static ColorPresets inst = new ColorPresets();
	
	private ColorPresets() {
	}
	
	
	/**
	 * Get available color presets, stored in the presets.json property file.
	 * @return
	 */
	public static ColorPresets getColorPresets() {
		if (inst.isEmpty()) {
			JSONObject presets = AppManager.getInstance().getPresetsJSONObject();
			if (presets != null && presets.has("colorPresets")) {
				inst.clear();
				try {
					JSONArray entries = presets.getJSONArray("colorPresets");
					for (int i = 0; i < entries.length(); i++) {
						JSONObject entry = entries.getJSONObject(i);
						inst.add(ColorPreset.fromJSONObject(entry));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return inst;
	}
	
	/**
	 * Returns one color preset identified by its name (presets.json, preset.name property)
	 * @param name
	 * @return
	 */
	public static ColorPreset getColorPresetByName(String name) {
		for (ColorPreset p : getColorPresets()) {
			if (name.equals(p.name)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Returns this list as JSONArray.
	 * @return
	 */
	public static JSONArray getColorPresetsAsJSONArray() {
		JSONArray arr = new JSONArray();
		for (ColorPreset p : inst) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
}
