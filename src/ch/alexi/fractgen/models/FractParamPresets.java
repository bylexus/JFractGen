package ch.alexi.fractgen.models;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.alexi.fractgen.logic.AppManager;


public class FractParamPresets extends Vector<FractParam> {
	private static FractParamPresets inst = new FractParamPresets();
	
	
	private FractParamPresets() {
		
	}
	
	public static Vector<FractParam> getPresets() {
		if (inst.isEmpty()) {
			JSONObject presets = AppManager.getInstance().getPresetsJSONObject();
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
	
	public static JSONArray getJSONArray() {
		JSONArray arr = new JSONArray();
		for (FractParam p : getPresets()) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
	
	
}
