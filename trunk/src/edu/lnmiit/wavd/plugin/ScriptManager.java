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
import java.io.IOException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFEngine;

import java.util.Vector;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import edu.lnmiit.wavd.model.Preferences;

// TODO: Auto-generated Javadoc
/**
 * The Class ScriptManager.
 */
public class ScriptManager {
    
    /** The _bsf manager. */
    private BSFManager _bsfManager;
    
    /** The _hooks. */
    private TreeMap _hooks = new TreeMap();
    
    /** The _listeners. */
    private EventListenerList _listeners = new EventListenerList();
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /**
     * Instantiates a new script manager.
     * 
     * @param framework the framework
     */
    public ScriptManager(Framework framework) {
        try {
            _bsfManager = new BSFManager();
            _bsfManager.declareBean("framework", framework, framework.getClass());
            _bsfManager.declareBean("out", System.out, System.out.getClass());
            _bsfManager.declareBean("err", System.err, System.out.getClass());
        } catch (BSFException bsfe) {
            _logger.severe("Declaring a bean should not throw an exception! " + bsfe);
        }
    }
    
    /**
     * Adds the script listener.
     * 
     * @param listener the listener
     */
    public void addScriptListener(ScriptListener listener) {
        synchronized(_listeners) {
            _listeners.add(ScriptListener.class, listener);
        }
    }
    
    /**
     * Removes the script listener.
     * 
     * @param listener the listener
     */
    public void removeScriptListener(ScriptListener listener) {
        synchronized(_listeners) {
            _listeners.remove(ScriptListener.class, listener);
        }
    }
    
    /**
     * Register hooks.
     * 
     * @param pluginName the plugin name
     * @param hooks the hooks
     */
    public void registerHooks(String pluginName, Hook[] hooks) {
        if (hooks != null && hooks.length > 0) {
            _hooks.put(pluginName, hooks);
            for (int i=0; i<hooks.length; i++) {
                hooks[i].setBSFManager(_bsfManager);
                hooks[i].setScriptManager(this);
            }
            fireHooksChanged();
        }
    }
    
    /**
     * Gets the plugin count.
     * 
     * @return the plugin count
     */
    public int getPluginCount() {
        return _hooks.size();
    }
    
    /**
     * Gets the plugin.
     * 
     * @param i the i
     * 
     * @return the plugin
     */
    public String getPlugin(int i) {
        String[] plugins = (String[]) _hooks.keySet().toArray(new String[0]);
        return plugins[i];
    }
    
    /**
     * Gets the hook count.
     * 
     * @param plugin the plugin
     * 
     * @return the hook count
     */
    public int getHookCount(String plugin) {
        Hook[] hooks = (Hook[]) _hooks.get(plugin);
        if (hooks == null) return 0;
        return hooks.length;
    }
    
    /**
     * Gets the hook.
     * 
     * @param plugin the plugin
     * @param i the i
     * 
     * @return the hook
     */
    public Hook getHook(String plugin, int i) {
        Hook[] hooks = (Hook[]) _hooks.get(plugin);
        if (hooks == null) return null;
        return hooks[i];
    }
    
    /**
     * Adds the script.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     * @param position the position
     * 
     * @throws BSFException the BSF exception
     */
    public void addScript(String plugin, Hook hook, Script script, int position) throws BSFException {
        String language = BSFManager.getLangFromFilename(script.getFile().getName());
        if (language != null) {
            script.setLanguage(language);
            script.setEnabled(true);
            hook.addScript(script, position);
            fireScriptAdded(plugin, hook, script);
        }
    }
    
    /**
     * Adds the script.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     * 
     * @throws BSFException the BSF exception
     */
    public void addScript(String plugin, Hook hook, Script script) throws BSFException {
        addScript(plugin, hook, script, hook.getScriptCount());
    }
    
    /**
     * Sets the enabled.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     * @param enabled the enabled
     */
    public void setEnabled(String plugin, Hook hook, Script script, boolean enabled) {
        script.setEnabled(enabled);
        fireScriptChanged(plugin, hook, script);
    }
    
    /**
     * Removes the script.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     */
    public void removeScript(String plugin, Hook hook, Script script) {
        int count = hook.getScriptCount();
        for (int i=0; i<count; i++) {
            Script s = hook.getScript(i);
            if (s == script) {
                hook.removeScript(i);
                fireScriptRemoved(plugin, hook, script);
                return;
            }
        }
    }
    
    /**
     * Load scripts.
     */
    public void loadScripts() {
        Iterator hookIt = _hooks.entrySet().iterator();
        while (hookIt.hasNext()) {
            Map.Entry entry = (Map.Entry) hookIt.next();
            String plugin = (String) entry.getKey();
            Hook[] hooks = (Hook[]) entry.getValue();
            if (hooks != null) {
                for (int i=0; i<hooks.length; i++) {
                    for (int j=0; j<hooks[i].getScriptCount(); j++)
                        hooks[i].removeScript(j);
                    int j=0;
                    String scriptName = Preferences.getPreference(hooks[i].getName()+"."+j+".name");
                    while (scriptName != null) {
                        File f = new File(scriptName);
                        if (f.canRead()) {
                            try {
                                Script script = new Script(f);
                                String enabled = Preferences.getPreference(hooks[i].getName()+"."+j+".enabled", "false");
                                addScript(plugin, hooks[i], script);
                                setEnabled(plugin, hooks[i], script, Boolean.valueOf(enabled).booleanValue());
                            } catch (IOException ioe) {
                                _logger.warning("Error loading script '" + scriptName + "' : " + ioe.getLocalizedMessage());
                            } catch (BSFException bsfe) {
                                _logger.warning("Error loading script '" + scriptName + "' : " + bsfe.getLocalizedMessage());
                            }
                        }
                        j++;
                        scriptName = Preferences.getPreference(hooks[i].getName()+"."+j+".name");
                    }
                }
            }
        }
    }
    
    /**
     * Save scripts.
     */
    public void saveScripts() {
        Iterator hookIt = _hooks.entrySet().iterator();
        while (hookIt.hasNext()) {
            Map.Entry entry = (Map.Entry) hookIt.next();
            String plugin = (String) entry.getKey();
            Hook[] hooks = (Hook[]) entry.getValue();
            if (hooks != null) {
                for (int i=0; i<hooks.length; i++) {
                    for (int j=0; j<hooks[i].getScriptCount(); j++) {
                        Script script = hooks[i].getScript(j);
                        Preferences.setPreference(hooks[i].getName()+"."+j+".name", script.getFile().getAbsolutePath());
                        Preferences.setPreference(hooks[i].getName()+"."+j+".enabled", Boolean.toString(script.isEnabled()));
                    }
                    Preferences.remove(hooks[i].getName()+"."+hooks[i].getScriptCount()+".name");
                    Preferences.remove(hooks[i].getName()+"."+hooks[i].getScriptCount()+".enabled");
                }
            }
        }
    }
    
    /**
     * Fire hooks changed.
     */
    protected void fireHooksChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = _listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ScriptListener.class) {
                ((ScriptListener)listeners[i+1]).hooksChanged();
            }
        }
    }
    
    /**
     * Fire script added.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     */
    protected void fireScriptAdded(String plugin, Hook hook, Script script) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ScriptListener.class) {
                ((ScriptListener)listeners[i+1]).scriptAdded(plugin, hook, script);
            }
        }
    }
    
    /**
     * Fire script removed.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     */
    protected void fireScriptRemoved(String plugin, Hook hook, Script script) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ScriptListener.class) {
                ((ScriptListener)listeners[i+1]).scriptRemoved(plugin, hook, script);
            }
        }
    }
    
    /**
     * Fire script started.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     */
    protected void fireScriptStarted(String plugin, Hook hook, Script script) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ScriptListener.class) {
                ((ScriptListener)listeners[i+1]).scriptStarted(plugin, hook, script);
            }
        }
    }
    
    /**
     * Fire script ended.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     */
    protected void fireScriptEnded(String plugin, Hook hook, Script script) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ScriptListener.class) {
                ((ScriptListener)listeners[i+1]).scriptEnded(plugin, hook, script);
            }
        }
    }
    
    /**
     * Fire script changed.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     */
    protected void fireScriptChanged(String plugin, Hook hook, Script script) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ScriptListener.class) {
                ((ScriptListener)listeners[i+1]).scriptChanged(plugin, hook, script);
            }
        }
    }
    
    /**
     * Fire script error.
     * 
     * @param plugin the plugin
     * @param hook the hook
     * @param script the script
     * @param error the error
     */
    protected void fireScriptError(String plugin, Hook hook, Script script, Throwable error) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ScriptListener.class) {
                ((ScriptListener)listeners[i+1]).scriptError(plugin, hook, script, error);
            }
        }
    }
    
    /**
     * Fire hook ended.
     * 
     * @param plugin the plugin
     * @param hook the hook
     */
    protected void fireHookEnded(String plugin, Hook hook) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ScriptListener.class) {
                ((ScriptListener)listeners[i+1]).hookEnded(plugin, hook);
            }
        }
    }
    
}
