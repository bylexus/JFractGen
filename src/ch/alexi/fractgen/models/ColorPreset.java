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
	
	
	/**
	 * Creates a color palette (Array of RGB value) for a certain number of
	 * iterations (including "0" iterations as value, so for nrOfIterations = 100, the
	 * palette contains 101 entries).
	 * 
	 * @param nrOfIters
	 * @return
	 */
	public RGB[] createColorPalette(int nrOfIters) {
		nrOfIters++;
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
		
		// ... but the last color must be black to indicate Julia / Mandelbrot membership:
		palette[palette.length-1] = new RGB(0,0,0);
		
		return palette;
	}
}
