package ch.alexi.jfractgen.models;

import java.util.Vector;

import org.json.JSONArray;

@SuppressWarnings("serial")
/**
 * A list of avaliable color presets.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class ColorPresets extends Vector<ColorPreset> {

	/**
	 * Get available color presets, stored in the presets.json property file.
	 * @return
	 */
	/*
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
	*/



	/**
	 * Returns this list as JSONArray.
	 * @return
	 */
	/*
	public static JSONArray getColorPresetsAsJSONArray() {
		JSONArray arr = new JSONArray();
		for (ColorPreset p : inst) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
	*/

	public JSONArray getJSONArray() {
		JSONArray arr = new JSONArray();
		for (ColorPreset p : this) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
}
