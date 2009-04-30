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
package edu.lnmiit.wavd.da.core;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import no.geosoft.cc.ui.SplashScreen;


import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.plugin.Framework;
import edu.lnmiit.wavd.plugin.fuzz.Fuzzer;
import edu.lnmiit.wavd.plugin.fuzz.swing.FuzzerPanel;
import edu.lnmiit.wavd.plugin.manualrequest.ManualRequest;
import edu.lnmiit.wavd.plugin.manualrequest.swing.ManualRequestPanel;
import edu.lnmiit.wavd.plugin.proxy.BeanShell;
import edu.lnmiit.wavd.plugin.proxy.BrowserCache;
import edu.lnmiit.wavd.plugin.proxy.CookieTracker;
import edu.lnmiit.wavd.plugin.proxy.ManualEdit;
import edu.lnmiit.wavd.plugin.proxy.Proxy;
import edu.lnmiit.wavd.plugin.proxy.RevealHidden;
import edu.lnmiit.wavd.plugin.proxy.swing.BeanShellPanel;
import edu.lnmiit.wavd.plugin.proxy.swing.ManualEditPanel;
import edu.lnmiit.wavd.plugin.proxy.swing.MiscPanel;
import edu.lnmiit.wavd.plugin.proxy.swing.ProxyPanel;
import edu.lnmiit.wavd.ui.swing.Lite;
import edu.lnmiit.wavd.ui.swing.UIFramework;
import edu.lnmiit.wavd.util.TextFormatter;
import edu.lnmiit.wavd.util.swing.ExceptionHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {
    

    /**
     * Instantiates a new main.
     */
    private Main() {
    }
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        
        System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
        
        final SplashScreen splash = new SplashScreen("/edu/lnmiit/wavd/da/core/waat.gif");
        splash.open(30000);
        initLogging();
        
        try {
            Preferences.loadPreferences(null);
        } catch (IOException ioe) {
            System.err.println("Error loading preferences: " + ioe);
            System.exit(1);
        }
        
        Framework framework = new Framework();
        
        boolean lite = Boolean.valueOf(Preferences.getPreference("WebScarab.lite", "true")).booleanValue();
        
        if (args != null && args.length > 0) {
            if (args[0].equalsIgnoreCase("lite")) {
                lite = true;
                if (args.length>1) {
                    String[] trim = new String[args.length-1];
                    System.arraycopy(args, 1, trim, 0, args.length-1);
                    args = trim;
                } else {
                    args = new String[0];
                }
            }
        }
        
        if (! lite) {
            final UIFramework uif = new UIFramework(framework);
            ExceptionHandler.setParentComponent(uif);
            loadAllPlugins(framework, uif);
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        uif.setVisible(true);
                        uif.toFront();
                        uif.requestFocus();
                        splash.close();
                    }
                });
            } catch (Exception e) {
                System.err.println("Error loading GUI: " + e.getMessage());
                System.exit(1);
            }
            uif.run();
        } else {
            final Lite uif = new Lite(framework);
            ExceptionHandler.setParentComponent(uif);
            loadLitePlugins(framework, uif);
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        uif.setVisible(true);
                        uif.toFront();
                        uif.requestFocus();
                        splash.close();
                    }
                });
            } catch (Exception e) {
                System.err.println("Error loading GUI: " + e.getMessage());
                System.exit(1);
            }
            uif.run();
        }
        
        try {
            Preferences.savePreferences();
        } catch (IOException ioe) {
            System.err.println("Could not save preferences: " + ioe);
        }
        System.exit(0);
    }
    
    /**
     * Inits the logging.
     */
    private static void initLogging() {
        Logger logger = Logger.getLogger("org.owasp.webscarab");
        logger.setUseParentHandlers(false);
        Handler ch = new ConsoleHandler();
        ch.setFormatter(new TextFormatter());
        logger.addHandler(ch);
        ch.setLevel(Level.FINE);
    }
    
    /**
     * Load all plugins.
     * 
     * @param framework the framework
     * @param uif the uif
     */
    public static void loadAllPlugins(Framework framework, UIFramework uif) {
        Proxy proxy = new Proxy(framework);
        framework.addPlugin(proxy);
        ProxyPanel proxyPanel = new ProxyPanel(proxy);
        uif.addPlugin(proxyPanel);
        
        ManualEdit me = new ManualEdit();
        proxy.addPlugin(me);
        proxyPanel.addPlugin(new ManualEditPanel(me));
        BeanShell bs = new BeanShell(framework);
        proxy.addPlugin(bs);
        proxyPanel.addPlugin(new BeanShellPanel(bs));
        RevealHidden rh = new RevealHidden();
        proxy.addPlugin(rh);
        BrowserCache bc = new BrowserCache();
        proxy.addPlugin(bc);
        CookieTracker ct = new CookieTracker(framework);
        proxy.addPlugin(ct);
        proxyPanel.addPlugin(new MiscPanel(rh, bc, ct));
        
        ManualRequest manualRequest = new ManualRequest(framework);
        framework.addPlugin(manualRequest);
        uif.addPlugin(new ManualRequestPanel(manualRequest));
                
        Fuzzer fuzzer = new Fuzzer(framework);
        framework.addPlugin(fuzzer);
        FuzzerPanel fuzzerPanel = new FuzzerPanel(fuzzer);
        uif.addPlugin(fuzzerPanel);

    }
    
    /**
     * Load lite plugins.
     * 
     * @param framework the framework
     * @param uif the uif
     */
    public static void loadLitePlugins(Framework framework, Lite uif) {
        Proxy proxy = new Proxy(framework);
        framework.addPlugin(proxy);
        ManualEdit me = new ManualEdit();
        proxy.addPlugin(me);
        uif.addPanel("Intercept", new ManualEditPanel(me));
        
        RevealHidden rh = new RevealHidden();
        proxy.addPlugin(rh);
        uif.setRevealHiddean(rh);
        

      
    }
    
}
