package ch.alexi.fractgen.gui;

import javax.swing.JPanel;

import ch.alexi.fractgen.models.ColorPreset;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Dimension;

@SuppressWarnings("serial")
/**
 * This is the main edit panel for editing a single color preset.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class ColorPresetEditPanel extends JPanel {
	private ColorPreset actPreset;
	private JLabel presetNameLabel;
	private ColorPresetPreviewPanel colorPresetPreviewPanel;
	
	
	public ColorPresetEditPanel() {
		this.initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		add(panel, BorderLayout.NORTH);
		
		JLabel lblColorPreset = new JLabel("Color Preset:");
		panel.add(lblColorPreset);
		
		presetNameLabel = new JLabel("-");
		panel.add(presetNameLabel);
		
		colorPresetPreviewPanel = new ColorPresetPreviewPanel(ColorPresetPreviewPanel.HORIZONTAL);
		colorPresetPreviewPanel.setPreferredSize(new Dimension(10, 40));
		add(colorPresetPreviewPanel, BorderLayout.SOUTH);
	}
	
	public void setColorPreset(ColorPreset p) {
		this.actPreset = p.clone();
		this.presetNameLabel.setText("<html><b>"+this.actPreset.toString()+"</b></html>");
		this.colorPresetPreviewPanel.setColorPreset(this.actPreset);
	}
	
	public ColorPreset getColorPreset() {
		return actPreset;
	}
}
