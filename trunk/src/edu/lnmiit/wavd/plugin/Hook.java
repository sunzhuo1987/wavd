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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

// TODO: Auto-generated Javadoc
/**
 * The Class Hook.
 */
public class Hook {

    /** The _name. */
    private String _name;

    /** The _description. */
    private String _description;

    /** The _scripts. */
    private List _scripts = new ArrayList();

    /** The _bsf manager. */
    protected BSFManager _bsfManager = null;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new hook.
     * 
     * @param name
     *            the name
     * @param description
     *            the description
     */
    public Hook(String name, String description) {
        _name = name;
        _description = description;
    }

    /**
     * Sets the bSF manager.
     * 
     * @param bsfManager
     *            the new bSF manager
     */
    public void setBSFManager(BSFManager bsfManager) {
        _bsfManager = bsfManager;
    }

    /**
     * Sets the script manager.
     * 
     * @param scriptManager
     *            the new script manager
     */
    public void setScriptManager(ScriptManager scriptManager) {
        // _scriptManager = scriptManager;
    }

    /**
     * Run scripts.
     */
    protected void runScripts() {
        if (_bsfManager == null)
            return;
        synchronized (_bsfManager) {
            for (int i = 0; i < _scripts.size(); i++) {
                Script script = (Script) _scripts.get(i);
                if (script.isEnabled()) {
                    // if (_scriptManager != null)
                    // _scriptManager.scriptStarted(this, script);
                    try {
                        _bsfManager.exec(script.getLanguage(), _name, 0, 0, script.getScript());
                    } catch (BSFException bsfe) {
                        _logger.warning("Script exception: " + bsfe);
                        // if (_scriptManager != null)
                        // _scriptManager.scriptError(this, script, bsfe);
                    }
                    // if (_scriptManager != null)
                    // _scriptManager.scriptEnded(this, script);
                }
            }
        }
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Gets the script count.
     * 
     * @return the script count
     */
    public int getScriptCount() {
        return _scripts.size();
    }

    /**
     * Gets the script.
     * 
     * @param i
     *            the i
     * 
     * @return the script
     */
    public Script getScript(int i) {
        return (Script) _scripts.get(i);
    }

    /**
     * Adds the script.
     * 
     * @param script
     *            the script
     */
    public void addScript(Script script) {
        _scripts.add(script);
    }

    /**
     * Adds the script.
     * 
     * @param script
     *            the script
     * @param position
     *            the position
     */
    public void addScript(Script script, int position) {
        _scripts.add(position, script);
    }

    /**
     * Removes the script.
     * 
     * @param position
     *            the position
     * 
     * @return the script
     */
    public Script removeScript(int position) {
        return (Script) _scripts.remove(position);
    }

}
