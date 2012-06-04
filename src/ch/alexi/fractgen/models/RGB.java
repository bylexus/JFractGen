package ch.alexi.fractgen.models;

import org.json.JSONException;
import org.json.JSONObject;

public class RGB {
	public int r;
	public int g;
	public int b;
	
	public RGB() {
		
	}
	
	public RGB(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		try {
			o.put("r", this.r);
			o.put("g", this.g);
			o.put("b", this.b);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return o;
	}
	
	public static RGB fromJSONObject(JSONObject o) {
		RGB col = new RGB();
		try {
			col.r = o.getInt("r");
			col.g = o.getInt("g");
			col.b = o.getInt("b");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return col;
	}
	
	public int[] toRGBArray() {
		int[] arr = new int[3];
		arr[0] = this.r;
		arr[1] = this.g;
		arr[2] = this.b;
		return arr;
	}
	
}
