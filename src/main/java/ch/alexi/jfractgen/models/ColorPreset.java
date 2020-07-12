package ch.alexi.jfractgen.models;

import java.util.ArrayList;
import java.util.List;
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
public class ColorPreset implements Cloneable {
	public String name;
	public RGB[] colors;
	public int defaultSteps = 256;

	private Map<String, RGB[]> dynamicPalettes = new HashMap<String, RGB[]>();

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
			o.put("defaultSteps", this.defaultSteps);
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
			if (o.has("defaultSteps") && o.getInt("defaultSteps") > 0) {
				p.defaultSteps = o.getInt("defaultSteps");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}


	/**
	 * Creates a color palette with a dynamic number of colors, taking the number of
	 * steps between colors from the configuration of the object.
	 * @see createFixedSizeColorPalette()
	 * @param length The number of colors calculated: The number of iterations map to the number of the color in the palette,
	 *     rotated if overflown (so the palette repeats indefinitely)
	 * @return
	 */
	public RGB[] createDynamicSizeColorPalette(int length, int repeat) {
		repeat = Math.max(1, repeat); // at least 1 repetition, otherwise we have an empty palette
		String paletteKey = Integer.toString(length) + "." + Integer.toString(repeat);
		
		if (!this.dynamicPalettes.containsKey(paletteKey)) {
			List<RGB> palette = new ArrayList<RGB>();

			RGB actBase, nextBase;
			double rStep,gStep,bStep,aStep;
			double r,g,b,a;
			int nrOfSteps = this.defaultSteps;

			// Create a color palette for all defined colors in the preset
			// that smoothly transist from color to color, independent of the requested length first:
			for (int i = 0; i < this.colors.length * repeat; i++) {
				actBase = this.colors[i % this.colors.length];
				nextBase = this.colors[(i+1) % this.colors.length];
				r = actBase.r;
				g = actBase.g;
				b = actBase.b;
				a = actBase.a;
				nrOfSteps = this.defaultSteps;
				if (actBase.steps > 0) {
					nrOfSteps = actBase.steps;
				}

				rStep = (nextBase.r-actBase.r) / (double)nrOfSteps;
				bStep = (nextBase.b-actBase.b) / (double)nrOfSteps;
				gStep = (nextBase.g-actBase.g) / (double)nrOfSteps;
				aStep = (nextBase.a-actBase.a) / (double)nrOfSteps;
				for (int j = 0; j < nrOfSteps;j++) {
					palette.add(new RGB(
							new Double(r).intValue(),
							new Double(g).intValue(),
							new Double(b).intValue(),
							new Double(a).intValue()
					));

					r += rStep;
					g += gStep;
					b += bStep;
					a += aStep;
				}
			}

			// Now we only need 'length' colors from the generated palette:
			RGB[] paletteArray = new RGB[length];
			for (int i = 0; i < paletteArray.length; i++) {
				// Choose the colors at the percentage index from the original palette:
				paletteArray[i] = palette.get((int)(i / (double)paletteArray.length * palette.size()));
			}
			this.dynamicPalettes.put(
					paletteKey,
					paletteArray);
			palette.clear();
			palette = null;
			paletteArray = null;
		}
		return this.dynamicPalettes.get(paletteKey);
	}


	@Override
	public ColorPreset clone() {
		ColorPreset c = new ColorPreset();
		c.name = this.name;
		c.defaultSteps = this.defaultSteps;
		c.colors = this.colors.clone();

		return c;
	}
}
