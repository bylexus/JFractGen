package ch.alexi.fractgen.models;

import java.util.Vector;

import javax.print.attribute.standard.SheetCollate;

public class ColorPresets extends Vector<ColorPreset> {
	private static RGB[] patchwork = {new RGB(0,0,30),
		new RGB(253,204,6),
		new RGB(186,84,15),
		new RGB(0,0,255),
		new RGB(180,180,30),
		new RGB(103,61,135),
		new RGB(255,0,0),
		new RGB(0,0,30),
		new RGB(255,255,255),
		new RGB(230,0,230),
		new RGB(0,0,30),
		new RGB(255,0,0),
		new RGB(255,255,0),
		new RGB(255,255,255)};
	private static RGB[] shadesofblue = {
		new RGB(0,0,30),
		new RGB(128,128,255),
		new RGB(200,200,255),
		new RGB(64,64,192),
		new RGB(0,0,30),
		new RGB(200,200,255),
		new RGB(0,0,30),
		new RGB(128,128,255),
		new RGB(200,200,255),
		new RGB(64,64,192),
		new RGB(0,0,30),
		new RGB(200,200,255),
		new RGB(0,0,30),
		new RGB(128,128,255),
		new RGB(200,200,255),
		new RGB(64,64,192),
		new RGB(0,0,30),
		new RGB(200,200,255)
	};
	public static ColorPreset  PALETTE_PATCHWORK = new ColorPreset("Patchwork",patchwork);
	public static ColorPreset  PALETTE_SHADES_OF_BLUE = new ColorPreset("Shades of Blue",shadesofblue);
			
			
	
	public static ColorPresets inst = new ColorPresets();
	
	private ColorPresets() {
		this.add(PALETTE_PATCHWORK);
		this.add(PALETTE_SHADES_OF_BLUE);
	}
	
	
	public static ColorPresets getColorPresets() {
		return inst;
	}
}
