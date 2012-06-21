package ch.alexi.fractgen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.alexi.fractgen.logic.AppManager;
import ch.alexi.fractgen.logic.Colorizer;
import ch.alexi.fractgen.logic.FractCalcer;
import ch.alexi.fractgen.logic.IFractCalcObserver;
import ch.alexi.fractgen.logic.IFractFunction;
import ch.alexi.fractgen.logic.JuliaFractFunction;
import ch.alexi.fractgen.logic.MathLib;
import ch.alexi.fractgen.models.ColorPreset;
import ch.alexi.fractgen.models.FractCalcerProgressData;
import ch.alexi.fractgen.models.FractCalcerResultData;
import ch.alexi.fractgen.models.FractFunctions;
import ch.alexi.fractgen.models.FractParam;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.SwingConstants;

/**
 * The MainFrame is the GUI Workhorse here: It represents the main window with all its 
 * components and "glues" them together through their respective event handlers. 
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements IFractCalcObserver, ActionListener, FocusListener {
	private JTextField picWidth;
	private JTextField picHeight;
	private JTextField centerCX;
	private JTextField centerCY;
	private JTextField diameterCX;
	private JTextField maxIters;
	private JTextField nrOfWorkers;
	private FractOutPanel outPanel;
	private JComboBox functionCB;
	private JButton btnStartCalculation;
	private JPanel settingsPanel;
	private JComboBox colorPresetsCombo;
	private FractParamPresetsCombo fractParamPresetsCB;
	private JButton btnBack;
	private JButton btnSaveToPng;
	private ProgressDialog progressDialog;
	private JSplitPane outputSplitPane;
	private LegendPanel legendPanel;
	private FractCalcerResultData actualFractCalcerResult;
	private JTextField juliaKrField;
	private JTextField juliaKiField;
	
	private boolean suspendUpdate = false;
	private JTextField paletteRepeat;
	private JButton btnSaveAsFractalPreset;
	private JButton btnDelFractParamPreset;
	private JPanel panel;
	private JPanel panel_2;
	private JCheckBox chckbxSmoothColors;
	private JToolBar.Separator separator_3;
	private JToggleButton btnPinchzoom;
	private JToggleButton btnDragpan;
	
	public MainFrame(String title) {
		super(title);
		this.initGUI();
	}
	
	/**
	 * GUI Setup function
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		FooterPanel footerPanel = new FooterPanel();
		getContentPane().add(footerPanel, BorderLayout.SOUTH);
		
		outputSplitPane = new JSplitPane();
		getContentPane().add(outputSplitPane, BorderLayout.CENTER);
		outputSplitPane.setOneTouchExpandable(true);
		outputSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		outPanel = new FractOutPanel();
		outPanel.addZoomListener(new IZoomListener() {
			
			@Override
			public void rubberBandZoom(int x, int y, int width, int height) {
				MainFrame.this.zoomByRubberband(x, y, width, height);
			}
			
			@Override
			public void clickZoom(int x, int y) {
				MainFrame.this.zoomByClick(x,y);
				
			}

			@Override
			public void dragPan(int dx, int dy) {
				MainFrame.this.dragPan(dx, dy);
				
			}
		});
		
		outPanel.setPreferredSize(new Dimension(805, 605));
		
		outputSplitPane.setTopComponent(outPanel);
		
		legendPanel = new LegendPanel();
		outputSplitPane.setRightComponent(legendPanel);
		outputSplitPane.setResizeWeight(1.0d);
		
		settingsPanel = new JPanel();
		getContentPane().add(settingsPanel, BorderLayout.WEST);
		settingsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(55dlu;default)"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("160px"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(35dlu;pref)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel_2 = new JLabel("Presets");
		lblNewLabel_2.setFont(new Font("Sans Serif", Font.BOLD, 14));
		settingsPanel.add(lblNewLabel_2, "2, 2, 3, 1, fill, default");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Fractals", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		settingsPanel.add(panel_1, "2, 4, 3, 1, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));
		
		fractParamPresetsCB = new FractParamPresetsCombo();
		panel_1.add(fractParamPresetsCB);
		fractParamPresetsCB.addActionListener(this);
		
		btnDelFractParamPreset = new JButton(AppManager.getInstance().getIcon("delete"));
		btnDelFractParamPreset.setToolTipText("Delete the current fractal preset");
		panel_1.add(btnDelFractParamPreset, BorderLayout.EAST);
		btnDelFractParamPreset.addActionListener(this);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Color Schemes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		settingsPanel.add(panel, "2, 6, 3, 1, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));
		
		colorPresetsCombo = new ColorPresetsCombo();
		panel.add(colorPresetsCombo);
		
		panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panel_2, BorderLayout.SOUTH);
		
		JLabel lblColorPresetRepeat = new JLabel("Color preset repeat:");
		panel_2.add(lblColorPresetRepeat);
		
		paletteRepeat = new JTextField();
		paletteRepeat.setColumns(5);
		panel_2.add(paletteRepeat);
		
		paletteRepeat.addFocusListener(this);
		colorPresetsCombo.addActionListener(this);
		
		chckbxSmoothColors = new JCheckBox("smooth colors");
		settingsPanel.add(chckbxSmoothColors, "2, 8, 3, 1");
		chckbxSmoothColors.setSelected(true);
		chckbxSmoothColors.addActionListener(this);
		
		JSeparator separator = new JSeparator();
		settingsPanel.add(separator, "2, 10, 3, 1, fill, default");
		
		JLabel lblFractalSettings = new JLabel("Fractal Settings");
		settingsPanel.add(lblFractalSettings, "2, 11, 3, 1, fill, default");
		
		JLabel lblPixelW = new JLabel("Pixel W:");
		settingsPanel.add(lblPixelW, "2, 13, right, default");
		
		picWidth = new JTextField();
		lblPixelW.setLabelFor(picWidth);
		settingsPanel.add(picWidth, "4, 13, fill, default");
		picWidth.setColumns(10);
		
		JLabel lblPixelH = new JLabel("Pixel H:");
		settingsPanel.add(lblPixelH, "2, 15, right, default");
		
		picHeight = new JTextField();
		settingsPanel.add(picHeight, "4, 15, fill, default");
		picHeight.setColumns(10);
		
		JLabel lblCenterCx = new JLabel("Center cx:");
		settingsPanel.add(lblCenterCx, "2, 17, right, default");
		
		centerCX = new JTextField();
		settingsPanel.add(centerCX, "4, 17, fill, default");
		centerCX.setColumns(10);
		
		JLabel lblCenterCy = new JLabel("Center cy:");
		settingsPanel.add(lblCenterCy, "2, 19, right, default");
		
		centerCY = new JTextField();
		settingsPanel.add(centerCY, "4, 19, fill, default");
		centerCY.setColumns(10);
		
		JLabel lblDiameterX = new JLabel("Diameter x:");
		settingsPanel.add(lblDiameterX, "2, 21, right, default");
		
		diameterCX = new JTextField();
		settingsPanel.add(diameterCX, "4, 21, fill, default");
		diameterCX.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		settingsPanel.add(separator_1, "2, 23, 3, 1, fill, default");
		
		JLabel lblNewLabel_3 = new JLabel("Calculation Settings");
		settingsPanel.add(lblNewLabel_3, "2, 25, 3, 1, fill, default");
		
		JLabel lblNewLabel_4 = new JLabel("max. Iterations:");
		settingsPanel.add(lblNewLabel_4, "2, 27, right, center");
		
		maxIters = new JTextField();
		settingsPanel.add(maxIters, "4, 27, fill, default");
		maxIters.setColumns(10);
		
		JLabel lblFunction = new JLabel("Function:");
		settingsPanel.add(lblFunction, "2, 29, right, default");
		
		functionCB = new FractFunctionsCombo();
		settingsPanel.add(functionCB, "4, 29, fill, default");
		functionCB.addActionListener(this);
		
		JLabel lblJuliaK = new JLabel("Julia K(r):");
		settingsPanel.add(lblJuliaK, "2, 31, right, default");
		
		juliaKrField = new JTextField();
		settingsPanel.add(juliaKrField, "4, 31, fill, default");
		juliaKrField.setColumns(10);
		
		JLabel lblJuliaKi = new JLabel("Julia K(i):");
		settingsPanel.add(lblJuliaKi, "2, 33, right, default");
		
		juliaKiField = new JTextField();
		settingsPanel.add(juliaKiField, "4, 33, fill, default");
		juliaKiField.setColumns(10);
		
		JLabel lblOfWorkers = new JLabel("# of Workers:");
		settingsPanel.add(lblOfWorkers, "2, 35, right, default");
		
		nrOfWorkers = new JTextField();
		settingsPanel.add(nrOfWorkers, "4, 35, fill, default");
		nrOfWorkers.setColumns(10);
		
		JSeparator separator_2 = new JSeparator();
		settingsPanel.add(separator_2, "2, 37, 3, 1");
		
		btnSaveAsFractalPreset = new JButton("Save as Fractal Preset",AppManager.getInstance().getIcon("disk"));
		settingsPanel.add(btnSaveAsFractalPreset, "2, 39, 3, 1");
		btnSaveAsFractalPreset.addActionListener(this);
		
		
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnStartCalculation = new JButton(AppManager.getInstance().getIcon("calculator"));
		btnStartCalculation.setToolTipText("Start calculation");
		btnStartCalculation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startCalculation();
			}
		});
		toolBar.add(btnStartCalculation);
		
		btnBack = new JButton(AppManager.getInstance().getIcon("arrow_undo"));
		btnBack.setToolTipText("Back to the last image");
		btnBack.setEnabled(false);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				popHistory();
				
			}
		});
		toolBar.add(btnBack);
		
		btnSaveToPng = new JButton(AppManager.getInstance().getIcon("picture_go"));
		btnSaveToPng.setToolTipText("Save to PNG");
		btnSaveToPng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.saveToPng();
			}
		});
		toolBar.add(btnSaveToPng);
		
		separator_3 = new JToolBar.Separator();
		toolBar.add(separator_3);
		
		btnPinchzoom = new JToggleButton(AppManager.getInstance().getIcon("magnifier"));
		btnPinchzoom.setSelected(true);
		btnPinchzoom.setToolTipText("Click/Rubberband zoom into the Fractal");
		btnPinchzoom.addActionListener(this);
		toolBar.add(btnPinchzoom);
		
		btnDragpan = new JToggleButton(AppManager.getInstance().getIcon("drag_hand"));
		btnDragpan.setSelected(false);
		btnDragpan.setToolTipText("Drag-pan the viewport of the fractal");
		btnDragpan.addActionListener(this);
		toolBar.add(btnDragpan);
	}
	
	
	/**
	 * Shows a file save dialog and saves the actual displayed fractal image to a PNG file.
	 */
	private void saveToPng() {
		final BufferedImage img = (BufferedImage)outPanel.getFractalImage();
		if (img != null) {
			String lastPath = AppManager.getInstance().getUserProperty("lastSavePath");
			JFileChooser dialog = new JFileChooser(lastPath);
			dialog.setFileFilter(new FileNameExtensionFilter("PNG Image","png"));
			int ret = dialog.showSaveDialog(MainFrame.this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				final File f = dialog.getSelectedFile();
				AppManager.getInstance().setUserProperty("lastSavePath", f.getParent());
				final JDialog d = new JDialog(this);
				d.setModal(true);
				d.getContentPane().setLayout(new BorderLayout());
				d.setLocationRelativeTo(this);
				d.setPreferredSize(new Dimension(200,100));
				
				d.getContentPane().add(new JLabel("Saving in progress ..."),BorderLayout.CENTER);
				JProgressBar dialogPBar = new JProgressBar();
				dialogPBar.setIndeterminate(true);
				d.getContentPane().add(dialogPBar,BorderLayout.SOUTH);
				dialogPBar = null;
				d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						ImageIO.write(img, "png", f);
						return null;
					}
					
					@Override
					protected void done() {
						if (d != null) {
							d.setVisible(false);
							d.dispose();
						}
						JOptionPane.showMessageDialog(MainFrame.this, "Image saved: "+f.getAbsolutePath(),"Info",JOptionPane.INFORMATION_MESSAGE);
					}
					
				};
				w.execute();
				d.pack();d.setVisible(true);
			}
		}
	}
	
	/**
	 * Pops / displays the last fractal in history, if any.
	 * @param restoreGUI If true, popping the history also restores the GUI
	 *   instead of just popping the history. If false it has the same effect as
	 *   to "forget" one history entry.
	 */
	private void popHistory(boolean restoreGUI) {
		// Back in history:
		FractCalcerResultData h = AppManager.getInstance().popHistory();
		
		if (restoreGUI && h != null) {
			MainFrame.this.setFractParam(h.fractParam);
			MainFrame.this.updateOutput(h);
			MainFrame.this.actualFractCalcerResult = h;
		}
		if (AppManager.getInstance().getHistoryCount() == 0) {
			btnBack.setEnabled(false);
		}
	}
	
	/**
	 * @see this.popHistory(boolean restoreGUI), just the restoreGUI = true variant.
	 */
	private void popHistory() {
		this.popHistory(true);
	}
	
	/**
	 * Updates the fractal image and the legend panel from a given
	 * fract calcer result.
	 * @param data A FractCalcerResultData object to be displayed in the GUI.
	 */
	private void updateOutput(FractCalcerResultData data) {
		if (!this.suspendUpdate) {
			outPanel.drawImage(data.fractImage);
			legendPanel.updateInfo(data.fractParam, data.colorPalette);
		}
	}
	
	/**
	 * Creates a new FractParam object from the actual settings panel data.
	 * @return A new FractParam object with the actual settings.
	 */
	private FractParam getActualFractParam() {
		FractParam p = new FractParam();
		
		p.picWidth = Integer.parseInt(picWidth.getText());
		p.picHeight = Integer.parseInt(picHeight.getText());
		
		p.centerCX = Double.parseDouble(centerCX.getText());
		p.centerCY = Double.parseDouble(centerCY.getText());
		
		p.diameterCX = Double.parseDouble(diameterCX.getText());
		p.iterFunc = (IFractFunction)functionCB.getSelectedItem();
		p.juliaKr = Double.parseDouble(juliaKrField.getText());
		p.juliaKi = Double.parseDouble(juliaKiField.getText());
		p.maxIterations = Integer.parseInt(maxIters.getText());
		p.nrOfWorkers = Integer.parseInt(nrOfWorkers.getText());
		
		p.colorPreset = (ColorPreset)colorPresetsCombo.getSelectedItem();
		p.colorPresetRepeat = MathLib.maxInt(Integer.parseInt( paletteRepeat.getText()),1);
		
		p.smoothColors = chckbxSmoothColors.isSelected();
		
		return p;
	}
	
	/**
	 * Sets the settings panel's setting from a given FractParam object.
	 * @param p
	 */
	public void setFractParam(FractParam p) {
		picWidth.setText(Integer.toString(p.picWidth));
		picHeight.setText(Integer.toString(p.picHeight));
		
		centerCX.setText(Double.toString(p.centerCX));
		centerCY.setText(Double.toString(p.centerCY));
		
		diameterCX.setText(Double.toString(p.diameterCX));
		
		functionCB.setSelectedItem(p.iterFunc);
		
		if (p.iterFunc == FractFunctions.julia) {
			juliaKrField.setText(Double.toString(p.juliaKr));
			juliaKiField.setText(Double.toString(p.juliaKi));
			juliaKrField.setEnabled(true);
			juliaKiField.setEnabled(true);
		} else {
			juliaKrField.setText("0");
			juliaKiField.setText("0");
			juliaKrField.setEnabled(false);
			juliaKiField.setEnabled(false);
		}
		
		maxIters.setText(Integer.toString(p.maxIterations));
		nrOfWorkers.setText(Integer.toString(p.nrOfWorkers));
		
		colorPresetsCombo.setSelectedItem(p.colorPreset);
		
		chckbxSmoothColors.setSelected(p.smoothColors);
		
		paletteRepeat.setText( Integer.toString(p.colorPresetRepeat));
	}
	
	/**
	 * Performs a click-zoom: The new center is the click position, the 
	 * scale is divided by 2, so the zoom level is doubled.
	 * Implies a calculation start. 
	 * @param centerX The x pixel value for the new center, relative to the fractal image
	 * @param centerY The y pixel value for the new center, relative to the fractal image
	 */
	public void zoomByClick(int centerX, int centerY) {
		if (this.actualFractCalcerResult != null) {
			this.setFractParam(this.actualFractCalcerResult.fractParam);
			FractParam p = this.getActualFractParam();
			p.initFractParams();
			p.diameterCX = p.diameterCX * 0.5;
			p.maxIterations = new Double(p.maxIterations * 1.3).intValue(); // Nr of iterations is +30% per zoom doubling
			
			p.centerCX = p.min_cx + centerX * p.punkt_abstand;
			p.centerCY = p.min_cy + (p.picHeight - centerY) * p.punkt_abstand; // inverse y-axis on draw
			
			this.setFractParam(p);
			this.startCalculation();
		}
		
	}
	
	/**
	 * Performs a rubberband-zoom: Shows as much as possible from the given rectangle of the
	 * actual fractal, by re-calculating the area. The 
	 * Implies a calculation start. 
	 * @param left The left x pixel of the rubber band, relative to the actual fractal image
	 * @param top The top y pixel of the rubber band, relative to the actual fractal image
	 * @param width The width in pixel of the rubber band
	 * @param height The height in pixel of the rubber band
	 */
	public void zoomByRubberband(int left, int top, int width, int height) {
		if (this.actualFractCalcerResult != null) {
			this.setFractParam(this.actualFractCalcerResult.fractParam);
			FractParam p = this.getActualFractParam();
			p.initFractParams();
			
			// new pixel center: middle of the rubber band rectangle:
			int pixelCenterX = left + width / 2;
			int pixelCenterY = top + height / 2;
			
			// The new fractal diameter on the real axis, in fractal scale (rubberband width percentage * old diameter)
			p.diameterCX = width / (double)p.picWidth * p.diameterCX;
			
			// The scale factor: selected area is scaleFactor times smaller than the original width
			double scaleFactor = (double)p.picWidth / width;
			
			// Iterations: 1.3^(2log(scaleFactor)) --> Iterations are 1.2 times increased by every doubling of the zoom level:
			p.maxIterations = new Double(p.maxIterations * (Math.pow(1.2, Math.log(scaleFactor)/Math.log(2.0)))).intValue();
			
			// New center in fractal coordinates:
			p.centerCX = p.min_cx + pixelCenterX * p.punkt_abstand;
			p.centerCY = p.min_cy + (p.picHeight - pixelCenterY) * p.punkt_abstand; // inverse y-axis on draw
			
			this.setFractParam(p);
			this.startCalculation();
		}
	}
	
	/**
	 * Performs a move of the fractal viewport, by re-calculating the
	 * new center and re-calc the new image.
	 * 
	 * @param dx The x-distance of the move position relative to the start position
	 * @param dy The y-distance of the move position relative to the start position
	 */
	public void dragPan(int dx, int dy) {
		if (this.actualFractCalcerResult != null) {
			this.setFractParam(this.actualFractCalcerResult.fractParam);
			FractParam p = this.getActualFractParam();
			p.initFractParams();
			
			p.centerCX -= dx * p.punkt_abstand;
			p.centerCY += dy * p.punkt_abstand; // inverse y-axis on draw
			
			this.setFractParam(p);
			this.startCalculation();
		}
	}
	
	/**
	 * Starts the fractal calculation in the background, using a SwingWorker.
	 * Shows a progress dialog during this time. A history entry from the 
	 * old/actual fractal is taken before the calculation starts.
	 */
	public void startCalculation() {
		// set up GUI for waiting:
		this.suspendUpdate = true;
		btnStartCalculation.setEnabled(false);
		
		// Create history entry, if we have a fract already:
		if (this.actualFractCalcerResult != null) {
			AppManager.getInstance().addHistory(this.actualFractCalcerResult);
		}
		
		// Get all params
		FractParam p = getActualFractParam();
		
		final FractCalcer sw = new FractCalcer(p,this);
		
		progressDialog = new ProgressDialog(p.nrOfWorkers, this);
		progressDialog.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sw.cancel(true);
			}
		});
		
		// Start calc:
		sw.execute();
		progressDialog.pack();
		progressDialog.setVisible(true);
	}

	@Override
	/**
	 * Listener for the start of the fractal calculation.
	 */
	public void start(FractCalcer worker) {
	}
	
	@Override
	/**
	 * Listens for calculation updates from the fractal calculator,
	 * and updates the progress dialog.
	 */
	public void progress(FractCalcer worker,FractCalcerProgressData progress) {
		if (progressDialog != null) {
			progressDialog.updateProgress(progress.threadNr, new Double(progress.threadProgress*100).intValue());
		}
	}
	
	@Override
	/**
	 * Listens the fract calcer for a done event, updates the
	 * output with the result, if necessary.
	 */
	public void done(FractCalcer worker) {
		this.suspendUpdate = false;
		try {
			if (worker.isCancelled()) {
				throw new InterruptedException();
			}
			
			FractCalcerResultData result = worker.get();
			this.actualFractCalcerResult = result;
			
			this.updateOutput(result);
			btnBack.setEnabled(true);
		} catch (InterruptedException e) {
			// Thrown when the user hits the cancel button
			//e.printStackTrace();
			this.popHistory(false);  // When canceled, restore the old state.
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		btnStartCalculation.setEnabled(true);
		if (progressDialog != null) {
			progressDialog.setVisible(false);
			progressDialog.dispose();
			progressDialog = null;
		}
		outputSplitPane.resetToPreferredSizes();
	}

	@Override
	/**
	 * action listeners for various comboboxes
	 */
	public void actionPerformed(ActionEvent a) {
		
		// user selects a system fractal preset: 
		// set the fractal params from the selected preset and start the calculation.
		if (!this.suspendUpdate && a.getSource() == this.fractParamPresetsCB) {
			this.suspendUpdate = true;
			this.setFractParam((FractParam)this.fractParamPresetsCB.getSelectedItem());
			startCalculation();
		}
		
		// user selects a color preset:
		// re-calculate the image colors if needed / wanted:
		if (a.getSource() == this.colorPresetsCombo) {
			// Re-render the color values of the actual fractal image:
			if (!this.suspendUpdate && this.actualFractCalcerResult != null) {
				ColorPreset preset = (ColorPreset)this.colorPresetsCombo.getSelectedItem();
				this.actualFractCalcerResult.colorPalette = preset.createDynamicSizeColorPalette(this.actualFractCalcerResult.fractParam.colorPresetRepeat);
				this.actualFractCalcerResult.fractParam.colorPreset = preset;
				Colorizer c = new Colorizer();
				c.fractDataToRaster(this.actualFractCalcerResult, this.actualFractCalcerResult.colorPalette);
				this.updateOutput(this.actualFractCalcerResult);
			}
		}

		// user selected the fractal function: enable/disable julia start constant fields:
		if (a.getSource() == this.functionCB) {
			IFractFunction f = (IFractFunction)this.functionCB.getSelectedItem();
			if (f instanceof JuliaFractFunction) {
				juliaKrField.setEnabled(true);
				juliaKiField.setEnabled(true);
			} else {
				juliaKrField.setEnabled(false);
				juliaKiField.setEnabled(false);
			}
		}
		
		// Save a fractal preset clicked:
		if (a.getSource() == btnSaveAsFractalPreset) {
			FractParam p = this.getActualFractParam();
			String name = JOptionPane.showInputDialog(this,"Enter a preset name");
			if (name == null || name.trim().length() == 0) {
				name = "New Preset";
			}
			p.name = name;
			AppManager.getInstance().addFractalPreset(p);
			this.fractParamPresetsCB.reloadPresets();
			
			this.suspendUpdate = true;
			this.fractParamPresetsCB.setSelectedItem(p);
			this.suspendUpdate = false;
		}
		
		// Delete a fract param preset:
		if (a.getSource() == btnDelFractParamPreset) {
			this.suspendUpdate = true;
			FractParam p = (FractParam)fractParamPresetsCB.getSelectedItem();
			AppManager.getInstance().removeUserFractalPreset(p);
			fractParamPresetsCB.reloadPresets();
			this.suspendUpdate = false;
		}
		
		// enable / disable color smoothing: recalc necessary:
		if (!this.suspendUpdate && a.getSource() == chckbxSmoothColors) {
			startCalculation();
		}
		
		// User clicks the "zoom" toggle btn
		if (a.getSource() == btnPinchzoom) {
			btnPinchzoom.setSelected(true);
			btnDragpan.setSelected(false);
			outPanel.setMoveMode(FractOutPanel.MOVE_MODE_ZOOM);
		}
		
		// user clicks the drag-pan button
		if (a.getSource() == btnDragpan) {
			btnPinchzoom.setSelected(false);
			btnDragpan.setSelected(true);
			outPanel.setMoveMode(FractOutPanel.MOVE_MODE_DRAG);
		}
	}

	@Override
	public void focusGained(FocusEvent ev) {
		// nothing todo here
		
	}

	@Override
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == this.paletteRepeat) {
			// If the palette repeat value changes, redraw the actual fractal using the new palette repeat value:
			// Re-render the color values of the actual fractal image:
			if (!this.suspendUpdate && this.actualFractCalcerResult != null) {
				ColorPreset preset = (ColorPreset)this.colorPresetsCombo.getSelectedItem();
				this.actualFractCalcerResult.fractParam.colorPresetRepeat = Integer.parseInt(this.paletteRepeat.getText());
				this.actualFractCalcerResult.colorPalette = preset.createDynamicSizeColorPalette(this.actualFractCalcerResult.fractParam.colorPresetRepeat);
				Colorizer c = new Colorizer();
				c.fractDataToRaster(this.actualFractCalcerResult, this.actualFractCalcerResult.colorPalette);
				this.updateOutput(this.actualFractCalcerResult);
			}
		}
		
	}
}
