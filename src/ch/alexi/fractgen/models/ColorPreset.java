package ch.alexi.fractgen.models;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A color preset is a list of colors that are used to calculate
 * a color palette later: e.g. a preset contains "red", "blue" and "yellow".
 * The calculated color palette then contains intermediate colors between those
 * preset colors to smoothly fade from one color to the other.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class ColorPreset {
	public String name;
	public RGB[] colors;
	private Map<Integer, RGB[]> dynamicPalettes = new HashMap<Integer, RGB[]>();
	
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
	
	/**
	 * Returns a JSONObject representing this color preset.
	 * @return
	 */
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
	
	/**
	 * Creates a color preset from a given JSONObject, see presets.json
	 * @param o
	 * @return
	 */
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
	
	
	/**
	 * Creates a color palette (Array of RGB value) for a certain number of
	 * entries (= nr of iterations).
	 * 
	 * @param nrOfIters
	 * @return
	 */
	public RGB[] createFixedSizeColorPalette(int nrOfIters) {
		RGB[] palette = new RGB[nrOfIters];
		
		int stepsPerFade = nrOfIters / (this.colors.length - 1);
		RGB actBase, nextBase;
		double rStep,gStep,bStep;
		double r,g,b;
		int counter = 0;
		
		for (int i = 0; i < this.colors.length - 1; i++) {
			actBase = this.colors[i];
			nextBase = this.colors[i+1];
			r = actBase.r;
			g = actBase.g;
			b = actBase.b;
			rStep = (nextBase.r-actBase.r) / (double)stepsPerFade;
			bStep = (nextBase.b-actBase.b) / (double)stepsPerFade;
			gStep = (nextBase.g-actBase.g) / (double)stepsPerFade;
			for (int j = 0; j < stepsPerFade;j++) {
				palette[counter] = new RGB(
						new Double(r).intValue(),
						new Double(g).intValue(),
						new Double(b).intValue()
				);
				
				r += rStep;
				g += gStep;
				b += bStep;
				counter++;
			}
		}
		
		// Fill the missing end with the last color:
		while (counter < palette.length) {
			palette[counter] = palette[counter-1];
			counter++;
		}
		
		return palette;
	}
	
	
	/**
	 * Creates a color palette with a dynamic number of colors, but with constant
	 * number of steps between two preset colors.
	 * @see createFixedSizeColorPalette()
	 * @param nrOfStepsPerTransition
	 * @return
	 */
	public RGB[] createDynamicSizeColorPalette(int nrOfStepsPerTransition) {
		Integer key = new Integer(nrOfStepsPerTransition);
		if (!this.dynamicPalettes.containsKey(key)) {
			this.dynamicPalettes.put(
					key, 
					this.createFixedSizeColorPalette(nrOfStepsPerTransition * (this.colors.length - 1)));
		}
		return this.dynamicPalettes.get(key);
	}
}
