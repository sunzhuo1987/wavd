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

package edu.lnmiit.wavd.plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Script.
 */
public class Script {
    
    /** The _file. */
    private File _file;
    
    /** The _script. */
    private String _script;
    
    /** The _last modified. */
    private long _lastModified;
    
    /** The _enabled. */
    private boolean _enabled;
    
    /** The _language. */
    private String _language = null;
    
    /**
     * Instantiates a new script.
     * 
     * @param file the file
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Script(File file) throws IOException {
        _file = file;
        reload();
        _enabled = false;
    }
    
    /**
     * Reload.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void reload() throws IOException {
        FileReader fr = null;
        try {
            fr = new FileReader(_file);
            int got;
            char[] buff = new char[1024];
            StringBuffer script = new StringBuffer();
            while ((got=fr.read(buff))>0) {
                script.append(buff,0,got);
            }
            _script = script.toString();
            _lastModified = _file.lastModified();
        } catch (IOException ioe) {
            _enabled = false;
            _script = "";
            throw ioe;
        } finally {
            if (fr!=null) fr.close();
        }
    }
    
    /**
     * Checks if is enabled.
     * 
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return _enabled;
    }
    
    /**
     * Sets the enabled.
     * 
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled) {
        _enabled = enabled;
    }
    
    /**
     * Gets the file.
     * 
     * @return the file
     */
    public File getFile() {
        return _file;
    }
    
    /**
     * Gets the script.
     * 
     * @return the script
     */
    public String getScript() {
        return _script;
    }
    
    /**
     * Sets the script.
     * 
     * @param script the new script
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setScript(String script) throws IOException {
        _script = script;
        FileWriter fw = null;
        try { 
            fw = new FileWriter(_file);
            fw.write(_script);
        } catch (IOException ioe) {
            _script = null;
            _lastModified = -1;
            _language = null;
            _enabled = false;
            throw ioe;
        } finally {
            if (fw != null) fw.close();
        }
    }
    
    /**
     * Gets the last modified.
     * 
     * @return the last modified
     */
    public long getLastModified() {
        return _lastModified;
    }
    
    /**
     * Gets the language.
     * 
     * @return the language
     */
    public String getLanguage() {
        return _language;
    }
    
    /**
     * Sets the language.
     * 
     * @param language the new language
     */
    public void setLanguage(String language) {
        _language = language;
    }
}
