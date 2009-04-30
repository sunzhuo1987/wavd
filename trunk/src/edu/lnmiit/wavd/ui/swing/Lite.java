/*
 * Copyright 2009 Udai Gupta, Hemant Purohit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * WebScarab.java
 *
 * Created on July 13, 2003, 7:11 PM
 */

package edu.lnmiit.wavd.ui.swing;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import edu.lnmiit.wavd.model.FileSystemStore;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.StoreException;
import edu.lnmiit.wavd.plugin.CredentialManager;
import edu.lnmiit.wavd.plugin.Framework;
import edu.lnmiit.wavd.plugin.FrameworkUI;
import edu.lnmiit.wavd.plugin.proxy.RevealHidden;
import edu.lnmiit.wavd.util.TempDir;
import edu.lnmiit.wavd.util.swing.HeapMonitor;
import edu.lnmiit.wavd.util.swing.SwingWorker;

// TODO: Auto-generated Javadoc
/**
 * The Class Lite.
 */
public class Lite extends JFrame implements FrameworkUI {
    
    /** The _framework. */
    private Framework _framework;
    
    /** The _model. */
    private FrameworkModel _model;
    
    /** The _plugins. */
    private ArrayList _plugins;
    
    /** The _summary panel. */
    private SummaryPanel _summaryPanel;
    
    /** The _transcoder. */
    private TranscoderFrame _transcoder = null;
    
    /** The _credential manager frame. */
    private CredentialManagerFrame _credentialManagerFrame = null;
    
    /** The _credential request dialog. */
    private CredentialRequestDialog _credentialRequestDialog = null;
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger("org.owasp.webscarab");
    
    // we use this to wait on the exit of the UI
    /** The _exit. */
    private Object _exit = new Object();
    
    /** The _temp dir. */
    private File _tempDir = null;
    
    /** The _reveal hidden. */
    private RevealHidden _revealHidden = null;
    
    /**
     * Instantiates a new lite.
     * 
     * @param framework the framework
     */
    public Lite(Framework framework) {
        _framework = framework;
        _model = framework.getModel();
        
        initComponents();
        getContentPane().add(new HeapMonitor(), BorderLayout.SOUTH);
        setPreferredSize();
        
        framework.setUI(this);
        
        _summaryPanel = new SummaryPanel(_model);
        tabbedPane.addTab("Summary", _summaryPanel);
        
        CredentialManager cm = _framework.getCredentialManager();
        _credentialManagerFrame = new CredentialManagerFrame(cm);
        _credentialRequestDialog = new CredentialRequestDialog(this, true, cm);
        cm.setUI(_credentialRequestDialog);
        
        initEditorViews();
        initHelp();
    }
    
    /**
     * Inits the help.
     */
    private void initHelp() {
        try {
            URL url = getClass().getResource("/help/jhelpset.hs");
            if (url == null) throw new NullPointerException("The help set could not be found");
            HelpSet helpSet = new HelpSet(null, url);
            HelpBroker helpBroker = helpSet.createHelpBroker();
            contentsMenuItem.addActionListener(new CSH.DisplayHelpFromSource(helpBroker));
            helpBroker.enableHelpKey(getRootPane(), "about", helpSet);        // for F1
        } catch (Throwable e) {
            final String[] message;
            if (e instanceof NullPointerException) {
                message = new String[] { "Help set not found" };
            } else if (e instanceof NoClassDefFoundError) {
                message = new String[] {"The JavaHelp libraries could not be found", "Please add jhall.jar to the extension directory of your Java Runtime environment"};
            } else {
                message = new String[] { "Unknown error: ",e.getClass().getName(), e.getMessage()};
            }
            for (int i=0; i<message.length; i++) {
                System.err.println(message[i]);
            }
            contentsMenuItem.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    JOptionPane.showMessageDialog(Lite.this, message, "Help is not available", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
    
    /**
     * Run.
     */
    public void run() {
        createTemporarySession();
        synchronized(_exit) {
            try {
                _exit.wait();
            } catch (InterruptedException ie) {
                _logger.info("Interrupted waiting for exit: " + ie);
            }
        }
    }
    
    /**
     * Delete tempdir.
     */
    private void deleteTempdir() {
        if (_tempDir != null) {
            TempDir.recursiveDelete(_tempDir);
            _tempDir = null;
        }
    }
    
    /**
     * Inits the editor views.
     */
    public void initEditorViews() {
        String wrap = Preferences.getPreference("TextPanel.wrap", "false");
        if (wrap != null && wrap.equals("true")) wrapTextCheckBoxMenuItem.setSelected(true);
    }
    
    /**
     * Sets the preferred size.
     */
    private void setPreferredSize() {
        try {
            int xpos = Integer.parseInt(Preferences.getPreference("WebScarab.position.x").trim());
            int ypos = Integer.parseInt(Preferences.getPreference("WebScarab.position.y").trim());
            int width = Integer.parseInt(Preferences.getPreference("WebScarab.size.x").trim());
            int height = Integer.parseInt(Preferences.getPreference("WebScarab.size.y").trim());
            setBounds(xpos,ypos,width,height);
        } catch (NumberFormatException nfe) {
            setSize(800,600);
            setExtendedState(MAXIMIZED_BOTH);
        } catch (NullPointerException npe) {
            setSize(800,600);
            setExtendedState(MAXIMIZED_BOTH);
        }
    }
    
    /**
     * Adds the plugin.
     * 
     * @param plugin the plugin
     */
    public void addPlugin(SwingPluginUI plugin) {
        addPluginEnhancements(plugin);
        addPanel(plugin.getPluginName(), plugin.getPanel());
    }
    
    /**
     * Adds the plugin enhancements.
     * 
     * @param plugin the plugin
     */
    public void addPluginEnhancements(SwingPluginUI plugin) {
        _summaryPanel.addUrlActions(plugin.getUrlActions());
        _summaryPanel.addUrlColumns(plugin.getUrlColumns());
        _summaryPanel.addConversationActions(plugin.getConversationActions());
        _summaryPanel.addConversationColumns(plugin.getConversationColumns());
    }
    
    /**
     * Adds the panel.
     * 
     * @param name the name
     * @param panel the panel
     */
    public void addPanel(final String name, final JPanel panel) {
        if (panel != null)
            tabbedPane.addTab(name, panel);
    }
    
    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        summaryInternalFrame = new javax.swing.JInternalFrame();
        tabbedPane = new javax.swing.JTabbedPane();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        editorMenu = new javax.swing.JMenu();
        wrapTextCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        toolsMenu = new javax.swing.JMenu();
        proxyMenuItem = new javax.swing.JMenuItem();
        credentialsMenuItem = new javax.swing.JMenuItem();
        transcoderMenuItem = new javax.swing.JMenuItem();
        hiddenCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        fullMenuItem = new javax.swing.JCheckBoxMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        summaryInternalFrame.setIconifiable(true);
        summaryInternalFrame.setMaximizable(true);
        summaryInternalFrame.setResizable(true);
        summaryInternalFrame.setTitle("Summary");
        summaryInternalFrame.setVisible(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("WAAT - Dynamic Analysis Module");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");
        newMenuItem.setMnemonic('N');
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(newMenuItem);

        openMenuItem.setMnemonic('O');
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(openMenuItem);

        saveMenuItem.setMnemonic('S');
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveMenuItem);

        exitMenuItem.setMnemonic('X');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);

        mainMenuBar.add(fileMenu);

        viewMenu.setMnemonic('V');
        viewMenu.setText("View");
        editorMenu.setMnemonic('E');
        editorMenu.setText("Content Editors");
        wrapTextCheckBoxMenuItem.setMnemonic('W');
        wrapTextCheckBoxMenuItem.setText("Wrap Text");
        wrapTextCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wrapTextCheckBoxMenuItemActionPerformed(evt);
            }
        });

        editorMenu.add(wrapTextCheckBoxMenuItem);

        viewMenu.add(editorMenu);

        mainMenuBar.add(viewMenu);

        toolsMenu.setMnemonic('T');
        toolsMenu.setText("Tools");
        proxyMenuItem.setMnemonic('P');
        proxyMenuItem.setText("Proxies");
        proxyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proxyMenuItemActionPerformed(evt);
            }
        });

        toolsMenu.add(proxyMenuItem);

        credentialsMenuItem.setText("Credentials");
        credentialsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                credentialsMenuItemActionPerformed(evt);
            }
        });

        toolsMenu.add(credentialsMenuItem);

        transcoderMenuItem.setMnemonic('T');
        transcoderMenuItem.setText("Transcoder");
        transcoderMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transcoderMenuItemActionPerformed(evt);
            }
        });

        toolsMenu.add(transcoderMenuItem);

        hiddenCheckBoxMenuItem.setText("Reveal Hidden Fields");
        hiddenCheckBoxMenuItem.setToolTipText("Reveals hidden fields in HTML documents");
        hiddenCheckBoxMenuItem.setEnabled(false);
        hiddenCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hiddenCheckBoxMenuItemActionPerformed(evt);
            }
        });

        toolsMenu.add(hiddenCheckBoxMenuItem);

        fullMenuItem.setText("Use full-featured interface");
        fullMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullMenuItemActionPerformed(evt);
            }
        });

        toolsMenu.add(fullMenuItem);

        mainMenuBar.add(toolsMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutMenuItem);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Full menu item action performed.
     * 
     * @param evt the evt
     */
    private void fullMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullMenuItemActionPerformed
        Preferences.setPreference("WebScarab.lite", Boolean.toString(!fullMenuItem.isSelected()));
        if (fullMenuItem.isSelected()) {
            JOptionPane.showMessageDialog(this, "Restart WebScarab in order to switch interfaces", "Restart required", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_fullMenuItemActionPerformed

    /**
     * Save menu item action performed.
     * 
     * @param evt the evt
     */
    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        if (_tempDir != null) {
            JFileChooser jfc = new JFileChooser(Preferences.getPreference("WebScarab.DefaultDir"));
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setDialogTitle("Select a directory to write the session into");
            int returnVal = jfc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File dir = jfc.getSelectedFile();
                if (FileSystemStore.isExistingSession(dir)) {
                    JOptionPane.showMessageDialog(null, new String[] {dir + " already contains a session ", }, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    new SwingWorker() {
                        public Object construct() {
                            try {
                                closeSession();
                                TempDir.recursiveCopy(_tempDir, dir);
                                TempDir.recursiveDelete(_tempDir);
                                _tempDir = null;
                                _framework.setSession("FileSystem", dir, "");
                                _framework.startPlugins();
                                return null;
                            } catch (StoreException se) {
                                return se;
                            } catch (IOException ioe) {
                                return ioe;
                            }
                        }
                        
                        public void finished() {
                            Object result = getValue();
                            if (result == null) return;
                            if (result instanceof Exception) {
                                Exception e = (Exception) result;
                                JOptionPane.showMessageDialog(null, new String[] {"Error saving Session : ", e.toString()}, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }.start();
                }
                Preferences.setPreference("WebScarab.DefaultDir", jfc.getCurrentDirectory().getAbsolutePath());
            }
        } else {
            try {
                if (_framework.isModified()) {
                    boolean running = _framework.isRunning();
                    if (running)
                        _framework.stopPlugins();
                    _framework.saveSessionData();
                    if (running)
                        _framework.startPlugins();
                }
            } catch (StoreException se) {
                JOptionPane.showMessageDialog(null, new String[] {"Error saving Session : ", se.toString()}, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed
    
    /**
     * Open existing session.
     */
    private void openExistingSession() {
        JFileChooser jfc = new JFileChooser(Preferences.getPreference("WebScarab.DefaultDir"));
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("Choose a directory that contains a previous session");
        int returnVal = jfc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File dir = jfc.getSelectedFile();
            if (!FileSystemStore.isExistingSession(dir)) {
                // FIXME to change this to prompt to create it if it does not already exist
                JOptionPane.showMessageDialog(null, new String[] {dir + " does not contain a session ", }, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                loadSession(dir);
            }
            Preferences.setPreference("WebScarab.DefaultDir", jfc.getCurrentDirectory().getAbsolutePath());
        }
    }
    
    /**
     * Open menu item action performed.
     * 
     * @param evt the evt
     */
    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        openExistingSession();
    }//GEN-LAST:event_openMenuItemActionPerformed
    
    /**
     * Creates the new session.
     */
    private void createNewSession() {
        JFileChooser jfc = new JFileChooser(Preferences.getPreference("WebScarab.DefaultDir"));
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("Select a directory to write the session into");
        int returnVal = jfc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File dir = jfc.getSelectedFile();
            if (FileSystemStore.isExistingSession(dir)) {
                // FIXME to change this to prompt to open it if it already exists
                JOptionPane.showMessageDialog(null, new String[] {dir + " already contains a session ", }, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                loadSession(dir);
            }
            Preferences.setPreference("WebScarab.defaultDirectory", jfc.getCurrentDirectory().getAbsolutePath());
        }
    }
    
    /**
     * New menu item action performed.
     * 
     * @param evt the evt
     */
    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        createNewSession();
    }//GEN-LAST:event_newMenuItemActionPerformed

    /**
     * Hidden check box menu item action performed.
     * 
     * @param evt the evt
     */
    private void hiddenCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hiddenCheckBoxMenuItemActionPerformed
        _revealHidden.setEnabled(hiddenCheckBoxMenuItem.isSelected());
    }//GEN-LAST:event_hiddenCheckBoxMenuItemActionPerformed

    /**
     * Form window closing.
     * 
     * @param evt the evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing
    
    /**
     * Sets the reveal hiddean.
     * 
     * @param revealHidden the new reveal hiddean
     */
    public void setRevealHiddean(RevealHidden revealHidden) {
        _revealHidden = revealHidden;
        hiddenCheckBoxMenuItem.setEnabled(_revealHidden != null);
        hiddenCheckBoxMenuItem.setSelected(_revealHidden != null && _revealHidden.getEnabled());
    }
        
    /**
     * Credentials menu item action performed.
     * 
     * @param evt the evt
     */
    private void credentialsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_credentialsMenuItemActionPerformed
        _credentialManagerFrame.setVisible(true);
    }//GEN-LAST:event_credentialsMenuItemActionPerformed
    
    /**
     * Wrap text check box menu item action performed.
     * 
     * @param evt the evt
     */
    private void wrapTextCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wrapTextCheckBoxMenuItemActionPerformed
        Preferences.setPreference("TextPanel.wrap", Boolean.toString(wrapTextCheckBoxMenuItem.isSelected()));
    }//GEN-LAST:event_wrapTextCheckBoxMenuItemActionPerformed
    
    /**
     * Load session.
     * 
     * @param sessionDir the session dir
     */
    private void loadSession(final File sessionDir) {
        new SwingWorker() {
            public Object construct() {
                try {
                    closeSession();
                    _framework.setSession("FileSystem", sessionDir, "");
                    _framework.startPlugins();
                    return null;
                } catch (StoreException se) {
                    return se;
                }
            }
            public void finished() {
                Object result = getValue();
                if (result == null) return;
                if (result instanceof StoreException) {
                    StoreException se = (StoreException) result;
                    JOptionPane.showMessageDialog(null, new String[] {"Error loading Session : ", se.toString()}, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.start();
    }
    
    /**
     * Creates the temporary session.
     */
    private void createTemporarySession() {
        try {
            _tempDir = TempDir.createTempDir("webscarab", ".tmp", null);
        } catch (IOException ioe) {
            _tempDir = null;
            JOptionPane.showMessageDialog(null, new String[] {"Error creating a temporary session : ", ioe.getMessage()}, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (_tempDir != null) {
            loadSession(_tempDir);
        }
    }
    
    /**
     * Close session.
     * 
     * @throws StoreException the store exception
     */
    private void closeSession() throws StoreException {
        if (_framework.isRunning()) {
            _framework.stopPlugins();
        }
        if (_framework.isModified()) {
            _framework.saveSessionData();
        }
    }
    
    /**
     * Form component resized.
     * 
     * @param evt the evt
     */
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (! isShowing()) return;
        Preferences.getPreferences().setProperty("WebScarab.size.x",Integer.toString(getWidth()));
        Preferences.getPreferences().setProperty("WebScarab.size.y",Integer.toString(getHeight()));
    }//GEN-LAST:event_formComponentResized
    
    /**
     * Form component moved.
     * 
     * @param evt the evt
     */
    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        if (! isShowing()) return;
        Preferences.getPreferences().setProperty("WebScarab.position.x",Integer.toString(getX()));
        Preferences.getPreferences().setProperty("WebScarab.position.y",Integer.toString(getY()));
    }//GEN-LAST:event_formComponentMoved
    
    /**
     * Transcoder menu item action performed.
     * 
     * @param evt the evt
     */
    private void transcoderMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transcoderMenuItemActionPerformed
        if (_transcoder == null) {
            _transcoder = new TranscoderFrame();
        }
        _transcoder.setVisible(true);
    }//GEN-LAST:event_transcoderMenuItemActionPerformed
    
    /**
     * Proxy menu item action performed.
     * 
     * @param evt the evt
     */
    private void proxyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proxyMenuItemActionPerformed
        new ProxyConfig(this, _framework).setVisible(true);
    }//GEN-LAST:event_proxyMenuItemActionPerformed
    
    /**
     * Exit menu item action performed.
     * 
     * @param evt the evt
     */
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        exit();
    }//GEN-LAST:event_exitMenuItemActionPerformed
    
    /**
     * About menu item action performed.
     * 
     * @param evt the evt
     */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        String[] message = new String[] {
            "OWASP WebScarab Lite - version " + _framework.getVersion(),
            " - part of the Open Web Application Security Project",
            "See http://www.owasp.org/software/webscarab.html",
            "", "Primary Developer : ",
            "         Rogan Dawes (rogan at dawes.za.net)",
            " ",
            "This is a stripped down version of the complete WebScarab tool",
            "To obtain access to the full functionality of WebScarab, ",
            "run WebScarab without the \"Lite\" parameter"
        };
        JOptionPane.showMessageDialog(this, message, "About WebScarab", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    
    /**
     * Exit.
     */    
    private void exit() {
        if (_framework.isRunning() && !_framework.stopPlugins()) {
            if (_framework.isModified()) {
                String[] status = _framework.getStatus();
                int count = status.length;
                String[] message = new String[count+2];
                System.arraycopy(status, 0, message, 0, count);
                message[count] = "";
                message[count+1] = "Force data save anyway?";
                int choice = JOptionPane.showOptionDialog(this, message, "Error - Plugins are busy", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
                if (choice != JOptionPane.YES_OPTION) return;
            }
        }
        if (_framework.isModified()) {
            try {
                _framework.saveSessionData();
            } catch (Exception e) {
                int choice = JOptionPane.showOptionDialog(this, new String[] {"Error saving session!", e.toString(), "Quit anyway?"}, "Error!", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
                if (choice != JOptionPane.YES_OPTION) return;
            }
        }
        synchronized(_exit) {
            _exit.notify();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The about menu item. */
    private javax.swing.JMenuItem aboutMenuItem;
    
    /** The contents menu item. */
    private javax.swing.JMenuItem contentsMenuItem;
    
    /** The credentials menu item. */
    private javax.swing.JMenuItem credentialsMenuItem;
    
    /** The editor menu. */
    private javax.swing.JMenu editorMenu;
    
    /** The exit menu item. */
    private javax.swing.JMenuItem exitMenuItem;
    
    /** The file menu. */
    private javax.swing.JMenu fileMenu;
    
    /** The full menu item. */
    private javax.swing.JCheckBoxMenuItem fullMenuItem;
    
    /** The help menu. */
    private javax.swing.JMenu helpMenu;
    
    /** The hidden check box menu item. */
    private javax.swing.JCheckBoxMenuItem hiddenCheckBoxMenuItem;
    
    /** The main menu bar. */
    private javax.swing.JMenuBar mainMenuBar;
    
    /** The new menu item. */
    private javax.swing.JMenuItem newMenuItem;
    
    /** The open menu item. */
    private javax.swing.JMenuItem openMenuItem;
    
    /** The proxy menu item. */
    private javax.swing.JMenuItem proxyMenuItem;
    
    /** The save menu item. */
    private javax.swing.JMenuItem saveMenuItem;
    
    /** The summary internal frame. */
    private javax.swing.JInternalFrame summaryInternalFrame;
    
    /** The tabbed pane. */
    private javax.swing.JTabbedPane tabbedPane;
    
    /** The tools menu. */
    private javax.swing.JMenu toolsMenu;
    
    /** The transcoder menu item. */
    private javax.swing.JMenuItem transcoderMenuItem;
    
    /** The view menu. */
    private javax.swing.JMenu viewMenu;
    
    /** The wrap text check box menu item. */
    private javax.swing.JCheckBoxMenuItem wrapTextCheckBoxMenuItem;
    // End of variables declaration//GEN-END:variables
    
}
