package ch.alexi.jfractgen.models;

import org.apfloat.Apfloat;
import org.json.JSONException;
import org.json.JSONObject;

import ch.alexi.jfractgen.logic.Constants;
import ch.alexi.jfractgen.logic.IFractFunction;
import ch.alexi.jfractgen.logic.MathLib;

/**
 * The parameter object for calculating a fractal view.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class FractParam {
	public String name = "Mandelbrot Total";

	public Apfloat maxBetragQuadrat = new Apfloat(800, Constants.precision);
	public int maxIterations = 40;

	public Apfloat centerCX = new Apfloat(-0.7, Constants.precision);
	public Apfloat centerCY = new Apfloat(0.0, Constants.precision);
	public Apfloat diameterCX = new Apfloat(3.0769, Constants.precision);

	public IFractFunction iterFunc = FractFunctions.mandelbrot;

	public Apfloat juliaKr = new Apfloat(-0.6, Constants.precision);
	public Apfloat juliaKi = new Apfloat(0.6, Constants.precision);

	public int picWidth = 800;
	public int picHeight = 600;

	//public int nrOfWorkers = 2;

	public boolean smoothColors = true;

	// calculated values:
	public Apfloat min_cx = new Apfloat(0.0, Constants.precision);
	public Apfloat min_cy = new Apfloat(0.0, Constants.precision);
	public Apfloat max_cx = new Apfloat(0.0, Constants.precision);
	public Apfloat max_cy = new Apfloat(0.0, Constants.precision);
	public Apfloat punkt_abstand = new Apfloat(0.0, Constants.precision);

	//public ColorPreset colorPreset = ColorPresets.getColorPresets().firstElement();
	public String colorPreset = "Patchwork";
	public int colorPresetRepeat = 1;

	/**
	 * before calling, the following values need to be set: - picWidth,
	 * picHeight, - diameterCX, - centerCX, centerCY
	 */
	public void initFractParams() {
		double aspect;
		Apfloat fract_width, fract_heigth;

		aspect = this.picWidth / (double) this.picHeight;
		fract_width = this.diameterCX;
		fract_heigth = this.diameterCX.divide(new Apfloat(aspect, Constants.precision));

		this.min_cx = this.centerCX.subtract(fract_width.divide(Constants.TWO));
		this.max_cx = this.min_cx.add(fract_width);
		this.min_cy = this.centerCY.subtract(fract_heigth.divide(Constants.TWO));
		this.max_cy = this.min_cy.add(fract_heigth);

		this.punkt_abstand = (this.max_cx.subtract(this.min_cx)).divide(new Apfloat(this.picWidth, Constants.precision));
	}

	@Override
	public String toString() {
		return new String(name);
	}

	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		try {
			o.put("name", this.name);
			o.put("maxIterations", this.maxIterations);
			o.put("centerCX", this.centerCX.toString());
			o.put("centerCY", this.centerCY.toString());
			o.put("diameterCX", this.diameterCX.toString());
			o.put("iterFunc", this.iterFunc);
			o.put("juliaKr", this.juliaKr.toString());
			o.put("juliaKi", this.juliaKi.toString());
			o.put("picWidth", this.picWidth);
			o.put("picHeight", this.picHeight);
			//o.put("nrOfWorkers", this.nrOfWorkers);
			o.put("colorPreset",this.colorPreset);
			o.put("colorPresetRepeat",MathLib.maxInt(1, this.colorPresetRepeat));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return o;
	}

	public static FractParam fromJSONObject(JSONObject o) {
		FractParam p = new FractParam();
		try {
			if (o.has("name"))
				p.name = o.getString("name");

			if (o.has("maxIterations"))
				p.maxIterations = o.getInt("maxIterations");
			if (o.has("centerCX"))
				p.centerCX = new Apfloat(o.getString("centerCX"), Constants.precision);
			if (o.has("centerCY"))
				p.centerCY = new Apfloat(o.getString("centerCY"), Constants.precision);
			if (o.has("diameterCX"))
				p.diameterCX = new Apfloat(o.getString("diameterCX"), Constants.precision);

			if (o.has("iterFunc")) {
				Object obj = o.get("iterFunc");
				if (! (obj instanceof String)) {
					System.out.println(obj);
				}
				IFractFunction f = FractFunctions.getFractFunction(o.getString("iterFunc"));
				if (f != null) {
					p.iterFunc = f;
				} else {
					p.iterFunc = FractFunctions.mandelbrot;
				}
			}

			if (o.has("juliaKr"))
				p.juliaKr = new Apfloat(o.getString("juliaKr"), Constants.precision);
			if (o.has("juliaKi"))
				p.juliaKi = new Apfloat(o.getString("juliaKi"), Constants.precision);

			if (o.has("picWidth"))
				p.picWidth = o.getInt("picWidth");
			if (o.has("picHeight"))
				p.picHeight = o.getInt("picHeight");
			/*

			if (o.has("nrOfWorkers"))
				p.nrOfWorkers = o.getInt("nrOfWorkers");
			*/

			if (o.has("colorPreset"))
				p.colorPreset = o.getString("colorPreset");
			if (o.has("colorPresetRepeat"))
				p.colorPresetRepeat = MathLib.maxInt(1, o.getInt("colorPresetRepeat"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
}
