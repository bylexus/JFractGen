package ch.alexi.fractgen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.alexi.fractgen.logic.AppManager;
import ch.alexi.fractgen.logic.FractCalcer;
import ch.alexi.fractgen.logic.IFractCalcObserver;
import ch.alexi.fractgen.logic.IFractFunction;
import ch.alexi.fractgen.models.ColorPreset;
import ch.alexi.fractgen.models.FractCalcerProgressData;
import ch.alexi.fractgen.models.FractHistory;
import ch.alexi.fractgen.models.FractParam;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainFrame extends JFrame implements IFractCalcObserver, ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField picWidth;
	private JTextField picHeight;
	private JTextField centerCX;
	private JTextField centerCY;
	private JTextField diameterCX;
	private JTextField maxIters;
	private JTextField maxBetragQuadrat;
	private JTextField nrOfWorkers;
	private FractOutPanel outPanel;
	private JComboBox functionCB;
	private JButton btnStartCalculation;
	private JPanel settingsPanel;
	private JComboBox colorPresetsCombo;
	private JComboBox fractParamPresetsCB;
	private JButton btnBack;
	private JButton btnSaveToPng;
	
	private ProgressDialog progressDialog;
	private JSplitPane outputSplitPane;
	private LegendPanel legendPanel;
	public MainFrame(String title) {
		super(title);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane mainHorizSplitPane = new JSplitPane();
		getContentPane().add(mainHorizSplitPane, BorderLayout.CENTER);
		
		settingsPanel = new JPanel();
		mainHorizSplitPane.setLeftComponent(settingsPanel);
		settingsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("134px:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("28px"),
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel_2 = new JLabel("Presets");
		lblNewLabel_2.setFont(new Font("Sans Serif", Font.BOLD, 14));
		settingsPanel.add(lblNewLabel_2, "2, 2, 3, 1, fill, default");
		
		JLabel lblNewLabel_1 = new JLabel("Fractals");
		settingsPanel.add(lblNewLabel_1, "2, 4, 3, 1, fill, default");
		
		fractParamPresetsCB = new FractParamPresetsCombo();
		fractParamPresetsCB.addActionListener(this);
		settingsPanel.add(fractParamPresetsCB, "2, 6, 3, 1, fill, default");
		
		JLabel lblNewLabel = new JLabel("Color Schemes");
		settingsPanel.add(lblNewLabel, "2, 8, 3, 1, fill, default");
		
		colorPresetsCombo = new ColorPresetsCombo();
		settingsPanel.add(colorPresetsCombo, "2, 10, 3, 1, fill, default");
		
		JSeparator separator = new JSeparator();
		settingsPanel.add(separator, "2, 12, 3, 1, fill, default");
		
		JLabel lblFractalSettings = new JLabel("Fractal Settings");
		settingsPanel.add(lblFractalSettings, "2, 14, 3, 1, fill, default");
		
		JLabel lblPixelW = new JLabel("Pixel W:");
		settingsPanel.add(lblPixelW, "2, 16, right, default");
		
		picWidth = new JTextField();
		lblPixelW.setLabelFor(picWidth);
		settingsPanel.add(picWidth, "4, 16, fill, default");
		picWidth.setColumns(10);
		
		JLabel lblPixelH = new JLabel("Pixel H:");
		settingsPanel.add(lblPixelH, "2, 18, right, default");
		
		picHeight = new JTextField();
		settingsPanel.add(picHeight, "4, 18, fill, default");
		picHeight.setColumns(10);
		
		JLabel lblCenterCx = new JLabel("Center cx:");
		settingsPanel.add(lblCenterCx, "2, 20, right, default");
		
		centerCX = new JTextField();
		settingsPanel.add(centerCX, "4, 20, fill, default");
		centerCX.setColumns(10);
		
		JLabel lblCenterCy = new JLabel("Center cy:");
		settingsPanel.add(lblCenterCy, "2, 22, right, default");
		
		centerCY = new JTextField();
		settingsPanel.add(centerCY, "4, 22, fill, default");
		centerCY.setColumns(10);
		
		JLabel lblDiameterX = new JLabel("Diameter x:");
		settingsPanel.add(lblDiameterX, "2, 24, right, default");
		
		diameterCX = new JTextField();
		settingsPanel.add(diameterCX, "4, 24, fill, default");
		diameterCX.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		settingsPanel.add(separator_1, "2, 26, 3, 1, fill, default");
		
		JLabel lblNewLabel_3 = new JLabel("Calculation Settings");
		settingsPanel.add(lblNewLabel_3, "2, 28, 3, 1, fill, default");
		
		JLabel lblNewLabel_4 = new JLabel("max. Iters.:");
		settingsPanel.add(lblNewLabel_4, "2, 30, right, center");
		
		maxIters = new JTextField();
		settingsPanel.add(maxIters, "4, 30, fill, default");
		maxIters.setColumns(10);
		
		JLabel lblMaxz = new JLabel("max. |Z|^2:");
		settingsPanel.add(lblMaxz, "2, 32, right, default");
		
		maxBetragQuadrat = new JTextField();
		settingsPanel.add(maxBetragQuadrat, "4, 32, fill, default");
		maxBetragQuadrat.setColumns(10);
		
		JLabel lblFunction = new JLabel("Function:");
		settingsPanel.add(lblFunction, "2, 34, right, default");
		
		functionCB = new FractFunctionsCombo();
		settingsPanel.add(functionCB, "4, 34, fill, default");
		
		JLabel lblOfWorkers = new JLabel("# of Workers:");
		settingsPanel.add(lblOfWorkers, "2, 36, right, default");
		
		nrOfWorkers = new JTextField();
		settingsPanel.add(nrOfWorkers, "4, 36, fill, default");
		nrOfWorkers.setColumns(10);
		
		outputSplitPane = new JSplitPane();
		outputSplitPane.setOneTouchExpandable(true);
		outputSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainHorizSplitPane.setRightComponent(outputSplitPane);
		//getContentPane().add(outputSplitPane, BorderLayout.CENTER);
		
		outPanel = new FractOutPanel();
		//getContentPane().add(outPanel, BorderLayout.WEST);
		outPanel.addZoomListener(new IZoomListener() {
			
			@Override
			public void rubberBandZoom(int x, int y, int width, int height) {
				MainFrame.this.zoomByRubberband(x, y, width, height);
			}
			
			@Override
			public void clickZoom(int x, int y) {
				MainFrame.this.zoomByClick(x,y);
				
			}
		});
		
		outPanel.setPreferredSize(new Dimension(805, 605));
		
		outputSplitPane.setTopComponent(outPanel);
		
		legendPanel = new LegendPanel();
		outputSplitPane.setRightComponent(legendPanel);
		outputSplitPane.setResizeWeight(1.0d);
		
		
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnStartCalculation = new JButton("Start calculation");
		btnStartCalculation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startCalculation();
			}
		});
		toolBar.add(btnStartCalculation);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppManager.getInstance().popHistory();
				FractHistory h = AppManager.getInstance().getLastHistory();
				if (h != null) {
					MainFrame.this.setFractParam(h.fractParam);
					MainFrame.this.outPanel.drawImage(h.fractImage);
				} else btnBack.setEnabled(false);
			}
		});
		toolBar.add(btnBack);
		
		btnSaveToPng = new JButton("Save to PNG");
		btnSaveToPng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage img = (BufferedImage)outPanel.getFractalImage();
				if (img != null) {
					JFileChooser dialog = new JFileChooser();
					dialog.setFileFilter(new FileNameExtensionFilter("PNG Image","png"));
					int ret = dialog.showSaveDialog(MainFrame.this);
					if (ret == JFileChooser.APPROVE_OPTION) {
						File f = dialog.getSelectedFile();
						try {
							ImageIO.write(img, "png", f);
							JOptionPane.showMessageDialog(MainFrame.this, "Image saved: "+f.getAbsolutePath(),"Info",JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JOptionPane.showMessageDialog(MainFrame.this, "Ooops! Error occured! "+e1.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		toolBar.add(btnSaveToPng);
	}
	
	private FractParam getActualFractParam() {
		FractParam p = new FractParam();
		
		p.picWidth = Integer.parseInt(picWidth.getText());
		p.picHeight = Integer.parseInt(picHeight.getText());
		
		p.centerCX = Double.parseDouble(centerCX.getText());
		p.centerCY = Double.parseDouble(centerCY.getText());
		
		p.diameterCX = Double.parseDouble(diameterCX.getText());
		p.iterFunc = (IFractFunction)functionCB.getSelectedItem();
		p.maxIterations = Integer.parseInt(maxIters.getText());
		p.maxBetragQuadrat = Double.parseDouble(maxBetragQuadrat.getText());
		p.nrOfWorkers = Integer.parseInt(nrOfWorkers.getText());
		
		p.colorPreset = (ColorPreset)colorPresetsCombo.getSelectedItem();
		
		return p;
	}
	
	public void setFractParam(FractParam p) {
		picWidth.setText(Integer.toString(p.picWidth));
		picHeight.setText(Integer.toString(p.picHeight));
		
		centerCX.setText(Double.toString(p.centerCX));
		centerCY.setText(Double.toString(p.centerCY));
		
		diameterCX.setText(Double.toString(p.diameterCX));
		
		functionCB.setSelectedItem(p.iterFunc);
		
		maxIters.setText(Integer.toString(p.maxIterations));
		maxBetragQuadrat.setText(Double.toString(p.maxBetragQuadrat));
		nrOfWorkers.setText(Integer.toString(p.nrOfWorkers));
		
		colorPresetsCombo.setSelectedItem(p.colorPreset);
	}
	
	public void zoomByClick(int centerX, int centerY) {
		FractParam p = this.getActualFractParam();
		p.initFractParams();
		p.diameterCX = p.diameterCX * 0.5;
		p.maxIterations = new Double(p.maxIterations * 1.3).intValue();
		
		p.centerCX = p.min_cx + centerX * p.punkt_abstand;
		p.centerCY = p.min_cy + (p.picHeight - centerY) * p.punkt_abstand; // inverse y-axis on draw
		
		this.setFractParam(p);
		this.startCalculation();
	}
	
	public void zoomByRubberband(int left, int top, int width, int height) {
		FractParam p = this.getActualFractParam();
		p.initFractParams();
		
		int pixelCenterX = left + width / 2;
		int pixelCenterY = top + height / 2;
		
		p.diameterCX = width / (double)p.picWidth * p.diameterCX;
		
		double scaleFactor = (double)p.picWidth / width; // selected area is scaleFactor times smaller
		p.maxIterations = new Double(p.maxIterations * (Math.pow(1.3, Math.log(scaleFactor)/Math.log(2.0)))).intValue();
		//double scaleFactor = p.initialDiameterCX / p.diameterCX;
		//p.maxIterations = new Double(p.initialMaxIterations * (Math.pow(1.3, Math.log(scaleFactor)/Math.log(2.0)))).intValue();
		p.centerCX = p.min_cx + pixelCenterX * p.punkt_abstand;
		p.centerCY = p.min_cy + (p.picHeight - pixelCenterY) * p.punkt_abstand; // inverse y-axis on draw
		
		this.setFractParam(p);
		this.startCalculation();
	}
	
	public void startCalculation() {
		// set up GUI for waiting:
		
		btnStartCalculation.setEnabled(false);
		
		
		
		// Get all params
		FractParam p = getActualFractParam();
		
		progressDialog = new ProgressDialog(p.nrOfWorkers);
		
		// Start calc:
		FractCalcer sw = new FractCalcer(p,this);

		sw.execute();
		
		progressDialog.setVisible(true);
	}

	@Override
	public void start(FractCalcer worker) {
	}
	
	@Override
	public void progress(FractCalcer worker,FractCalcerProgressData progress) {
		//System.out.println(threadName + ": "+progress + " ("+total+")");
		if (progressDialog != null) {
			progressDialog.updateProgress(progress.threadNr, new Double(progress.threadProgress*100).intValue());
		}
	}
	
	@Override
	public void done(FractCalcer worker) {
		try {
			Image img = worker.get();
			outPanel.drawImage(img);
			legendPanel.updateInfo(worker.getFractParam(), worker.getPalette());
			
			// Create history entry:
			AppManager.getInstance().addHistory(img,this.getActualFractParam());
			btnBack.setEnabled(true);
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
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
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == this.fractParamPresetsCB) {
			this.setFractParam((FractParam)this.fractParamPresetsCB.getSelectedItem());
		}
		
	}
}
