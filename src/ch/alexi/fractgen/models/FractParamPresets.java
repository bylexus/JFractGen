package ch.alexi.fractgen.models;

import java.util.Vector;

import org.json.JSONArray;


@SuppressWarnings("serial")
/**
 * A list of available fractal parameter presets, see presets.json.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class FractParamPresets extends Vector<FractParam> {
	
	public JSONArray getJSONArray() {
		JSONArray arr = new JSONArray();
		for (FractParam p : this) {
			arr.put(p.toJSONObject());
		}
		return arr;
	}
}
