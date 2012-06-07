package ch.alexi.fractgen.models;

import org.json.JSONException;
import org.json.JSONObject;
import ch.alexi.fractgen.logic.IFractFunction;

/**
 * The parameter object for calculating a fractal view.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class FractParam {
	public String name = "Mandelbrot Total";

	public double maxBetragQuadrat = 4;
	public int maxIterations = 40;

	public double centerCX = -0.7;
	public double centerCY = 0.0;
	public double diameterCX = 3.0769;


	public IFractFunction iterFunc = FractFunctions.mandelbrot;
	
	public double juliaKr = -0.6;
	public double juliaKi = 0.6;

	public int picWidth = 800;
	public int picHeight = 600;

	public int nrOfWorkers = 2;

	// calculated values:
	public double min_cx = 0.0;
	public double min_cy = 0.0;
	public double max_cx = 0.0;
	public double max_cy = 0.0;
	public double punkt_abstand = 0.0;

	public ColorPreset colorPreset = ColorPresets.getColorPresets().firstElement();

	/**
	 * before calling, the following values need to be set: - picWidth,
	 * picHeight, - diameterCX, - centerCX, centerCY
	 */
	public void initFractParams() {
		double aspect, fract_width, fract_heigth;

		aspect = this.picWidth / (double) this.picHeight;
		fract_width = this.diameterCX;
		fract_heigth = this.diameterCX / aspect;

		this.min_cx = this.centerCX - (fract_width / 2);
		this.max_cx = this.min_cx + fract_width;
		this.min_cy = this.centerCY - (fract_heigth / 2);
		this.max_cy = this.min_cy + fract_heigth;

		this.punkt_abstand = (this.max_cx - this.min_cx) / this.picWidth;
	}

	@Override
	public String toString() {
		return new String(name);
	}

	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		try {
			o.put("name", this.name);
			o.put("maxBetragQuadrat", this.maxBetragQuadrat);
			o.put("maxIterations", this.maxIterations);
			o.put("centerCX", this.centerCX);
			o.put("centerCY", this.centerCY);
			o.put("diameterCX", this.diameterCX);
			o.put("iterFunc", this.iterFunc);
			o.put("juliaKr", this.juliaKr);
			o.put("juliaKi", this.juliaKi);
			o.put("picWidth", this.picWidth);
			o.put("picHeight", this.picHeight);
			o.put("nrOfWorkers", this.nrOfWorkers);
			o.put("colorPreset",this.colorPreset.name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return o;
	}

	public static FractParam fromJSONObject(JSONObject o) {
		FractParam p = new FractParam();
		try {
			p.name = o.getString("name");

			p.maxBetragQuadrat = o.getDouble("maxBetragQuadrat");
			p.maxIterations = o.getInt("maxIterations");
			p.centerCX = o.getDouble("centerCX");
			p.centerCY = o.getDouble("centerCY");
			p.diameterCX = o.getDouble("diameterCX");
			p.iterFunc = (o.getString("iterFunc").equals("Julia") ? FractFunctions.julia: FractFunctions.mandelbrot);
			
			if (o.has("juliaKr")) {
				p.juliaKr = o.getDouble("juliaKr");
			}
			if (o.has("juliaKi")) {
				p.juliaKi = o.getDouble("juliaKi");
			}
			
			p.picWidth = o.getInt("picWidth");
			p.picHeight = o.getInt("picHeight");
			p.nrOfWorkers = o.getInt("nrOfWorkers");
			
			if (o.has("colorPreset")) {
				ColorPreset cp = ColorPresets.getColorPresetByName(o.getString("colorPreset"));
				if (cp != null) {
					p.colorPreset = cp;
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
}
