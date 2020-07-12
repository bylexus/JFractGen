package ch.alexi.jfractgen.logic;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

import org.json.JSONException;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;

import ch.alexi.jfractgen.gui.AboutDialog;
import ch.alexi.jfractgen.gui.FileMenu;
import ch.alexi.jfractgen.gui.MainFrame;
import ch.alexi.jfractgen.models.ColorPreset;
import ch.alexi.jfractgen.models.FractCalcerResultData;
import ch.alexi.jfractgen.models.FractParam;
import ch.alexi.jfractgen.models.PresetsCollection;
import ch.alexi.jfractgen.models.UserPreferences;

/**
 * The AppManager offers some application-wide functions and data. Singleton.
 *
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in
 * Java/Swing.
 *
 * @author Alexander Schenkel, www.alexi.ch (c) 2012 Alexander Schenkel
 */
public class AppManager implements ApplicationListener {
    private static AppManager inst = new AppManager();
    private MainFrame mainFrame;
    private Stack<FractCalcerResultData> history;
    private PresetsCollection presets;
    private UserPreferences prefs;
    private ObjectFactory factory;

    private AppManager() {
        this.history = new Stack<FractCalcerResultData>();
        this.prefs = new UserPreferences();
        this.loadUserSettings();
    }

    public static AppManager getInstance() {
        return inst;
    }

    /**
     * Called directly from the Main class, the AppManager is responsible for
     * setting up and displaying the GUI. It also starts the first calculation.
     *
     * @return
     */
    public MainFrame createAndShowGUI() {
        // OS X? see http://simplericity.org/macify/
        Application app = new DefaultApplication();
        // app.addPreferencesMenuItem();
        // app.setEnabledPreferencesMenu(false);
        app.addApplicationListener(this);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JFractGen");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        UIManager.put("Label.font", new Font("Sans Serif", Font.PLAIN, 11));
        UIManager.put("ComboBox.font", new Font("Sans Serif", Font.PLAIN, 11));
        UIManager.put("CheckBox.font", new Font("Sans Serif", Font.PLAIN, 11));
        UIManager.put("TextField.font", new Font("Sans Serif", Font.PLAIN, 11));
        UIManager.put("Button.font", new Font("Sans Serif", Font.PLAIN, 11));

        if (mainFrame == null) {
            // Create and set up the window.
            mainFrame = new MainFrame("JFractGen");
            mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            mainFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    shutdown();
                }
            });

            if (!app.isMac()) {
                // Add menu entries for non-mac GUIs:
            }

            JMenuBar menuBar = mainFrame.getJMenuBar();
            if (menuBar == null) {
                menuBar = new JMenuBar();
                mainFrame.setJMenuBar(menuBar);
            }
            menuBar.add(new FileMenu());

            getPresets().addPresetChangeListener(mainFrame);

            mainFrame.pack();
            mainFrame.setVisible(true);
            mainFrame.setFractParam(this.getPresets().getFractalPresets().get(0));

            // Start the first calculation:
            mainFrame.startCalculation();
        }

        return mainFrame;
    }

    public MainFrame getMainFrame() {
        return this.mainFrame;
    }

    /**
     * Reads the presets configuration file (presets.json) and returns a JSONObject.
     *
     * @return All presets from the presets.json configuration file.
     */
    public PresetsCollection getPresets() {
        if (this.presets == null) {
            presets = new PresetsCollection();

            File presetFile = new File(getUserSettingsDir() + File.separator + "presets.json");
            if (presetFile.exists()) {
                System.out.println("Loading presets file from " + presetFile.getAbsolutePath());
                presets.loadFromJsonFile(presetFile);
            } else {
                // Read the system-delivered preset file, if the user
                // preset is not present:
                System.out.println("Loading default presets file");
                InputStream is = getClass().getResourceAsStream("/presets.json");
                if (is != null) {
                    presets.loadFromJsonStream(is);
                }
            }
        }

        return presets;
    }

    public void addFractalPreset(FractParam p) {
        this.getPresets().addFractalPreset(p);
        this.saveUserPresets();
    }

    /**
     * Removes the fractal preset given from the list of available ones, but only if
     * it is not the last one.
     *
     * @param p
     */
    public void removeUserFractalPreset(FractParam p) {
        if (this.getPresets().getFractalPresets().size() > 1) {
            this.getPresets().removeFractalPreset(p);
            this.saveUserPresets();
        }
    }

    public void removeUserColorPreset(ColorPreset p) {
        if (this.getPresets().getColorPresets().size() > 1) {
            this.getPresets().removeColorPreset(p);
            this.saveUserPresets();
        }
    }

    /**
     * Adds a fractal result data object to the history stack.
     *
     * @param data
     */
    public void addHistory(FractCalcerResultData data) {
        history.push(data);
        if (history.size() > getUserPrefs().getNrOfHistoryEntries()) {
            history.removeElementAt(0);
        }
    }

    /**
     * Pops a fractal result data object from the history stack, returning the data.
     *
     * @return
     */
    public FractCalcerResultData popHistory() {
        if (!this.history.isEmpty()) {
            FractCalcerResultData res = this.history.pop();
            System.gc();
            return res;
        } else
            return null;
    }

    /**
     * How many entries does the history actually have?
     *
     * @return
     */
    public int getHistoryCount() {
        return this.history.size();
    }

    public UserPreferences getUserPrefs() {
        return this.prefs;
    }

    protected void loadUserSettings() {
        File userSettingsFile = new File(getUserSettingsDir() + File.separator + "settings.xml");
        if (userSettingsFile.exists()) {
            getUserPrefs().loadPrefsFromFile(userSettingsFile);
        }

    }

    public String getUserSettingsDir() {
        return System.getProperty("user.home") + File.separator + ".jfractgen";
    }

    /**
     * Exits the application, and do necessary things before.
     */
    public void shutdown() {
        File userSettingsFile = new File(getUserSettingsDir() + File.separator + "settings.xml");
        System.out.println("User settings go to: " + userSettingsFile);
        userSettingsFile.mkdirs();
        userSettingsFile.delete();

        // save user preferences:
        this.getUserPrefs().savePrefsToFile(userSettingsFile);

        // save user presets:
        this.saveUserPresets();
        System.exit(0);

    }

    private void saveUserPresets() {
        File f = new File(getUserSettingsDir() + File.separator + "presets.json");
        BufferedWriter w;
        try {
            w = new BufferedWriter(new FileWriter(f));

            w.write(getPresets().getPresetsJsonObject().toString(4));
            // w.write(getPresetsJSONObject().put("fractalPresets",this.getFractParamPresets().getJSONArray()).toString(4));
            w.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Icon getIcon(String name) {
        URL imgUrl = getClass().getResource("/icons/" + name + ".png");
        if (imgUrl != null) {
            return new ImageIcon(imgUrl);
        } else {
            return null;
        }
    }

    public void handleAbout(ApplicationEvent arg0) {
        AboutDialog.display();
        arg0.setHandled(true);

    }

    public void handleOpenApplication(ApplicationEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void handleOpenFile(ApplicationEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void handlePreferences(ApplicationEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void handlePrintFile(ApplicationEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void handleQuit(ApplicationEvent arg0) {
        shutdown();

    }

    public void handleReOpenApplication(ApplicationEvent arg0) {
        // TODO Auto-generated method stub

    }

    public ObjectFactory getObjectFactory() {
        if (this.factory == null) {
            this.factory = new ObjectFactory();
        }
        return this.factory;
    }
}
