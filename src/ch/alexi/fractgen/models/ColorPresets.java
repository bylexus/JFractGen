package ch.alexi.fractgen.models;

import java.util.Vector;

import javax.print.attribute.standard.SheetCollate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.alexi.fractgen.logic.AppManager;

public class ColorPresets extends Vector<ColorPreset> {
			
			
	
	public static ColorPresets inst = new ColorPresets();
	
	
	
	private ColorPresets() {
	}
	
	
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
	
	public static ColorPreset getColorPresetByName(String name) {
		for (ColorPreset p : inst) {
			if (name.equals(p.name)) {
				return p;
			}
		}
		return null;
	}
	
	public static JSONArray getColorPresetsAsJSONArray() {
		JSONArray arr = new JSONArray();
		for (ColorPreset p : inst) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
}
