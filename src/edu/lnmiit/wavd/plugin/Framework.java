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
 * Framework.java
 *
 * Created on June 16, 2004, 8:57 AM
 */

package edu.lnmiit.wavd.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.logging.Logger;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


import edu.lnmiit.wavd.httpclient.HTTPClientFactory;
import edu.lnmiit.wavd.httpclient.SSLContextManager;
import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.model.StoreException;

// TODO: Auto-generated Javadoc
/**
 * The Class Framework.
 */
public class Framework {
    
    /** The _plugins. */
    private List _plugins = new ArrayList();
    
    /** The _analysis queue. */
    private List _analysisQueue = new LinkedList();
    
    /** The _model. */
    private FrameworkModel _model;
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /** The _version. */
    private String _version;
    
    /** The _ui. */
    private FrameworkUI _ui = null;
    
    /** The _script manager. */
    private ScriptManager _scriptManager;
    
    /** The _credential manager. */
    private CredentialManager _credentialManager;
    
    /** The _allow add conversation. */
    private AddConversationHook _allowAddConversation;
    
    /** The _analyse conversation. */
    private Hook _analyseConversation;
    
    /** The _queue thread. */
    private Thread _queueThread = null;
    
    /** The _qp. */
    private QueueProcessor _qp = null;
    
    /** The drop pattern. */
    private Pattern dropPattern = null;
    
    /**
     * Instantiates a new framework.
     */
    public Framework() {
        _model = new FrameworkModel();
        _scriptManager = new ScriptManager(this);
        _allowAddConversation = new AddConversationHook();
        _scriptManager.registerHooks("Framework", new Hook[] { _allowAddConversation });
        extractVersionFromManifest();
        _credentialManager = new CredentialManager();
        configureHTTPClient();
        String dropRegex = Preferences.getPreference("WebScarab.dropRegex", null);
        if (dropRegex != null) {
            try {
                dropPattern = Pattern.compile(dropRegex);
            } catch (PatternSyntaxException pse) {
                _logger.warning("Got an invalid regular expression for conversations to ignore: " + dropRegex + " results in " + pse.toString());
            }
        }
        _qp = new Framework.QueueProcessor();
        _queueThread = new Thread(_qp, "QueueProcessor");
        _queueThread.setDaemon(true);
        _queueThread.setPriority(Thread.MIN_PRIORITY);
        _queueThread.start();
    }
    
    /**
     * Gets the script manager.
     * 
     * @return the script manager
     */
    public ScriptManager getScriptManager() {
        return _scriptManager;
    }
    
    /**
     * Gets the credential manager.
     * 
     * @return the credential manager
     */
    public CredentialManager getCredentialManager() {
        return _credentialManager;
    }
    
    /**
     * Sets the uI.
     * 
     * @param ui the new uI
     */
    public void setUI(FrameworkUI ui) {
        _ui = ui;
    }
    
    /**
     * Sets the session.
     * 
     * @param type the type
     * @param store the store
     * @param session the session
     * 
     * @throws StoreException the store exception
     */
    public void setSession(String type, Object store, String session) throws StoreException {
        _model.setSession(type, store, session);
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            Plugin plugin = (Plugin) it.next();
            if (!plugin.isRunning()) {
                plugin.setSession(type, store, session);
            } else {
                _logger.warning(plugin.getPluginName() + " is running while we are setting the session");
            }
        }
    }
    
    /**
     * Gets the model.
     * 
     * @return the model
     */
    public FrameworkModel getModel() {
        return _model;
    }
    
    /**
     * Extract version from manifest.
     */
    private void extractVersionFromManifest() {
        Package pkg = Package.getPackage("org.owasp.webscarab");
        if (pkg != null) _version = pkg.getImplementationVersion();
        else _logger.severe("PKG is null");
        if (_version == null) _version = "unknown (local build?)";
    }
    
    /**
     * Adds the plugin.
     * 
     * @param plugin the plugin
     */
    public void addPlugin(Plugin plugin) {
        _plugins.add(plugin);
        Hook[] hooks = plugin.getScriptingHooks();
        _scriptManager.registerHooks(plugin.getPluginName(), hooks);
    }
    
    /**
     * Gets the plugin.
     * 
     * @param name the name
     * 
     * @return the plugin
     */
    public Plugin getPlugin(String name) {
        Plugin plugin = null;
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            plugin = (Plugin) it.next();
            if (plugin.getPluginName().equals(name)) return plugin;
        }
        return null;
    }
    
    /**
     * Start plugins.
     */
    public void startPlugins() {
        HTTPClientFactory.getInstance().getSSLContextManager().invalidateSessions();
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            Plugin plugin = (Plugin) it.next();
            if (!plugin.isRunning()) {
                Thread t = new Thread(plugin, plugin.getPluginName());
                t.setDaemon(true);
                t.start();
            } else {
                _logger.warning(plugin.getPluginName() + " was already running");
            }
        }
        _scriptManager.loadScripts();
    }
    
    /**
     * Checks if is busy.
     * 
     * @return true, if is busy
     */
    public boolean isBusy() {
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            Plugin plugin = (Plugin) it.next();
            if (plugin.isBusy()) return true;
        }
        return false;
    }
    
    /**
     * Checks if is running.
     * 
     * @return true, if is running
     */
    public boolean isRunning() {
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            Plugin plugin = (Plugin) it.next();
            if (plugin.isRunning()) return true;
        }
        return false;
    }
    
    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    public boolean isModified() {
        if (_model.isModified()) return true;
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            Plugin plugin = (Plugin) it.next();
            if (plugin.isModified()) return true;
        }
        return false;
    }
    
    /**
     * Gets the status.
     * 
     * @return the status
     */
    public String[] getStatus() {
        List status = new ArrayList();
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            Plugin plugin = (Plugin) it.next();
            status.add(plugin.getPluginName() + " : " + plugin.getStatus());
        }
        return (String[]) status.toArray(new String[0]);
    }
    
    /**
     * Stop plugins.
     * 
     * @return true, if successful
     */
    public boolean stopPlugins() {
        if (isBusy()) return false;
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            Plugin plugin = (Plugin) it.next();
            if (plugin.isRunning()) {
                // _logger.info("Stopping " + plugin.getPluginName());
                plugin.stop();
                // _logger.info("Done");
            } else {
                _logger.warning(plugin.getPluginName() + " was not running");
            }
        }
        _scriptManager.saveScripts();
        return true;
    }
    
    /**
     * Save session data.
     * 
     * @throws StoreException the store exception
     */
    public void saveSessionData() throws StoreException {
        StoreException storeException = null;
        if (_model.isModified()) {
            _logger.info("Flushing model");
            _model.flush();
            _logger.info("Done");
        }
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            Plugin plugin = (Plugin) it.next();
            if (plugin.isModified()) {
                try {
                    _logger.info("Flushing " + plugin.getPluginName());
                    plugin.flush();
                    _logger.info("Done");
                } catch (StoreException se) {
                    if (storeException == null) storeException = se;
                    _logger.severe("Error saving data for " + plugin.getPluginName() + ": " + se);
                }
            }
        }
        
        if (storeException != null) throw storeException;
    }
    
    /**
     * Gets the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return _version;
    }
    
    /**
     * Reserve conversation id.
     * 
     * @return the conversation id
     */
    public ConversationID reserveConversationID() {
        return _model.reserveConversationID();
    }
    
    /**
     * Adds the conversation.
     * 
     * @param id the id
     * @param request the request
     * @param response the response
     * @param origin the origin
     */
    public void addConversation(ConversationID id, Request request, Response response, String origin) {
        addConversation(id, new Date(), request, response, origin);
    }
    
    /**
     * Adds the conversation.
     * 
     * @param id the id
     * @param when the when
     * @param request the request
     * @param response the response
     * @param origin the origin
     */
    public void addConversation(ConversationID id, Date when, Request request, Response response, String origin) {
        ScriptableConversation conversation = new ScriptableConversation(request, response, origin);
        _allowAddConversation.runScripts(conversation);
        if (conversation.isCancelled()) return;
        if (dropPattern != null && dropPattern.matcher(request.getURL().toString()).matches()) {
            return;
        }
        _model.addConversation(id, when, request, response, origin);
        if (!conversation.shouldAnalyse()) return;
        synchronized(_analysisQueue) {
            _analysisQueue.add(id);
        }
    }
    
    /**
     * Adds the conversation.
     * 
     * @param request the request
     * @param response the response
     * @param origin the origin
     * 
     * @return the conversation id
     */
    public ConversationID addConversation(Request request, Response response, String origin) {
        ConversationID id = reserveConversationID();
        addConversation(id, new Date(), request, response, origin);
        return id;
    }
    
    /**
     * Configure http client.
     */
    private void configureHTTPClient() {
        HTTPClientFactory factory = HTTPClientFactory.getInstance();
        String prop = null;
        String value;
        int colon;
        try {
            // FIXME for some reason, we get "" instead of null for value,
            // and do not use our default value???
            prop = "WebScarab.httpProxy";
            value = Preferences.getPreference(prop);
            if (value == null || value.equals("")) value = ":3128";
            colon = value.indexOf(":");
            factory.setHttpProxy(value.substring(0,colon), Integer.parseInt(value.substring(colon+1).trim()));
            
            prop = "WebScarab.httpsProxy";
            value = Preferences.getPreference(prop);
            if (value == null || value.equals("")) value = ":3128";
            colon = value.indexOf(":");
            factory.setHttpsProxy(value.substring(0,colon), Integer.parseInt(value.substring(colon+1).trim()));
            
            prop = "WebScarab.noProxy";
            value = Preferences.getPreference(prop, "");
            if (value == null) value = "";
            factory.setNoProxy(value.split(" *, *"));
            
            int connectTimeout = 30000;
            prop = "HttpClient.connectTimeout";
            value = Preferences.getPreference(prop,"");
            if (value != null && !value.equals("")) {
                try {
                    connectTimeout = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {}
            }
            int readTimeout = 0;
            prop = "HttpClient.readTimeout";
            value = Preferences.getPreference(prop,"");
            if (value != null && !value.equals("")) {
                try {
                    readTimeout = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {}
            }
            factory.setTimeouts(connectTimeout, readTimeout);
            
        } catch (NumberFormatException nfe) {
            _logger.warning("Error parsing property " + prop + ": " + nfe);
        } catch (Exception e) {
            _logger.warning("Error configuring the HTTPClient property " + prop + ": " + e);
        }
        factory.setAuthenticator(_credentialManager);
    }
    
    /**
     * The Class QueueProcessor.
     */
    private class QueueProcessor implements Runnable {
        
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            while (true) {
                ConversationID id = null;
                synchronized (_analysisQueue) {
                    if (_analysisQueue.size()>0)
                        id = (ConversationID) _analysisQueue.remove(0);
                }
                if (id != null) {
                    Request request = _model.getRequest(id);
                    Response response = _model.getResponse(id);
                    String origin = _model.getConversationOrigin(id);
                    Iterator it = _plugins.iterator();
                    while (it.hasNext()) {
                        Plugin plugin = (Plugin) it.next();
                        if (plugin.isRunning()) {
                            try {
                                plugin.analyse(id, request, response, origin);
                            } catch (Exception e) {
                                _logger.warning(plugin.getPluginName() + " failed to process " + id + ": " + e);
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {}
                }
            }
        }
        
    }
    
    /**
     * The Class AddConversationHook.
     */
    private class AddConversationHook extends Hook {
        
        /**
         * Instantiates a new adds the conversation hook.
         */
        public AddConversationHook() {
            super("Add Conversation", 
            "Called when a new conversation is added to the framework.\n" +
            "Use conversation.setCancelled(boolean) and conversation.setAnalyse(boolean) " +
            "after deciding using conversation.getRequest() and conversation.getResponse()");
        }
        
        /**
         * Run scripts.
         * 
         * @param conversation the conversation
         */
        public void runScripts(ScriptableConversation conversation) {
            if (_bsfManager == null) return;
            synchronized(_bsfManager) {
                try {
                    _bsfManager.declareBean("conversation", conversation, conversation.getClass());
                    super.runScripts();
                    _bsfManager.undeclareBean("connection");
                } catch (Exception e) {
                    _logger.severe("Declaring or undeclaring a bean should not throw an exception! " + e);
                }
            }
        }
        
    }
    
}
