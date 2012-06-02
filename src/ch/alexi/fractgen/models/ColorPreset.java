package ch.alexi.fractgen.models;

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
}
