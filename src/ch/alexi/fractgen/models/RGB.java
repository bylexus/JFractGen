package ch.alexi.fractgen.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a single RGB color value.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class RGB implements Cloneable {
	public int r;
	public int g;
	public int b;
	public int a = 255;
	public int steps = 0; // 0 means: inherit from Color Preset
	
	public RGB() {
		
	}
	
	public RGB(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		try {
			o.put("r", this.r);
			o.put("g", this.g);
			o.put("b", this.b);
			o.put("a", this.a);
			if (this.steps > 0) {
				o.put("steps",this.steps);
			}
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
			if (o.has("a")) {
				col.a = o.getInt("a");
			}
			if (o.has("steps") && o.getInt("steps") > 0) {
				col.steps = o.getInt("steps");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return col;
	}
	
	public int[] toRGBArray() {
		int[] arr = new int[4];
		arr[0] = this.r;
		arr[1] = this.g;
		arr[2] = this.b;
		arr[3] = this.a;
		return arr;
	}
	
	
	@Override
	public RGB clone() {
		RGB c = new RGB(this.r,this.g,this.b, this.a);
		c.steps = this.steps;
		return c;
	}
}
