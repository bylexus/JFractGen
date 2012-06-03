package ch.alexi.fractgen.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ColorPreset {
	public String name;
	public RGB[] colors;
	
	public ColorPreset() {
		
	}
	
	public ColorPreset(String name, RGB[] colors) {
		this.name = name;
		this.colors = colors;
	}
	@Override
	public String toString() {
		return this.name;
	}
	
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		try {
			o.put("name", this.name);
			JSONArray cols = new JSONArray();
			for (RGB col : this.colors) {
				cols.put(col.toJSONObject());
			}
			o.put("colors", cols);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return o;
	}
	
	public static ColorPreset fromJSONObject(JSONObject o) {
		ColorPreset p = new ColorPreset();
		try {
			p.name = o.getString("name");
			JSONArray cols = o.getJSONArray("colors");
			p.colors = new RGB[cols.length()];
			for (int i = 0; i < cols.length(); i++) {
				p.colors[i] = RGB.fromJSONObject(cols.getJSONObject(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
}
