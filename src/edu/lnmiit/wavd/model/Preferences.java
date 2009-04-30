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
 * Preferences.java
 *
 * Created on September 15, 2003, 7:19 AM
 */

package edu.lnmiit.wavd.model;

import java.util.Properties;
import java.util.logging.Logger;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class Preferences.
 */
public class Preferences {
    
    /** The _props. */
    static Properties _props = new Properties();
    
    /** The _logger. */
    private static Logger _logger = Logger.getLogger("org.owasp.webscarab.model.Preferences");
    
    /** The _location. */
    private static String _location = null;
    
    /**
     * Instantiates a new preferences.
     */
    private Preferences() {
    }
    
    /**
     * Gets the preferences.
     * 
     * @return the preferences
     */
    public static Properties getPreferences() {
        return _props;
    }
    
    /**
     * Load preferences.
     * 
     * @param file the file
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void loadPreferences(String file) throws IOException {
        // If we are given a filename to load, use it, otherwise
        // look for a props file in the user's home directory
        // if the file does not exist, use the standard defaults
        
        if (file == null) {
            String sep = System.getProperty("file.separator");
            String home = System.getProperty("user.home");
            _location = home + sep + "WebScarab.properties";
        } else {
            _location = file;
        }
        
        try {
            Properties props = new Properties(System.getProperties());
            InputStream is = new FileInputStream(_location);
            props.load(is);
            _props = props;
        } catch (FileNotFoundException fnfe) {
            // we'll just use the defaults
        }
    }
    
    /**
     * Save preferences.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void savePreferences() throws IOException {
        FileOutputStream fos = new FileOutputStream(_location);
        _props.store(fos,"WebScarab Properties");
        fos.close();
    }
    
    /**
     * Sets the preference.
     * 
     * @param key the key
     * @param value the value
     */
    public static void setPreference(String key, String value) {
        _props.setProperty(key, value);
    }
    
    /**
     * Gets the preference.
     * 
     * @param key the key
     * 
     * @return the preference
     */
    public static String getPreference(String key) {
        return _props.getProperty(key);
    }
    
    /**
     * Gets the preference.
     * 
     * @param key the key
     * @param defaultValue the default value
     * 
     * @return the preference
     */
    public static String getPreference(String key, String defaultValue) {
        return _props.getProperty(key, defaultValue);
    }
    
    /**
     * Removes the.
     * 
     * @param key the key
     * 
     * @return the string
     */
    public static String remove(String key) {
        return (String) _props.remove(key);
    }
    
}
