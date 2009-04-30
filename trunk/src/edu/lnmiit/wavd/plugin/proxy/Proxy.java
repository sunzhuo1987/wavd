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
 * $Id: Proxy.java,v 1.24 2005/05/18 15:23:31 rogan Exp $
 */

package edu.lnmiit.wavd.plugin.proxy;

import java.io.IOException;

import java.lang.NumberFormatException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.logging.Logger;




import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.model.StoreException;
import edu.lnmiit.wavd.plugin.Framework;
import edu.lnmiit.wavd.plugin.Hook;
import edu.lnmiit.wavd.plugin.Plugin;
import edu.lnmiit.wavd.plugin.Script;
import edu.lnmiit.wavd.plugin.ScriptManager;

import java.net.MalformedURLException;
import java.net.InetAddress;

// TODO: Auto-generated Javadoc
/**
 * The Class Proxy.
 */
public class Proxy implements Plugin {
    
    /** The _running. */
    private boolean _running = false;
    
    /** The _framework. */
    private Framework _framework = null;
    
    /** The _ui. */
    private ProxyUI _ui = null;
    
    /** The _plugins. */
    private ArrayList _plugins = new ArrayList();
    
    /** The _listeners. */
    private TreeMap _listeners = new TreeMap();
    
    /** The _simulators. */
    private TreeMap _simulators = new TreeMap();
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /** The _status. */
    private String _status = "Stopped";
    
    /** The _pending. */
    private int _pending = 0;
    
    /** The _allow connection. */
    private Proxy.ConnectionHook _allowConnection = new ConnectionHook(
    "Allow connection", 
    "Called when a new connection is received from a browser\n" +
    "use connection.getAddress() and connection.closeConnection() to decide and react"
    );
    
    /** The _intercept request. */
    private Proxy.ConnectionHook _interceptRequest = new ConnectionHook(
    "Intercept request", 
    "Called when a new request has been submitted by the browser\n" +
    "use connection.getRequest() and connection.setRequest(request) to perform changes"
    );
    
    /** The _intercept response. */
    private Proxy.ConnectionHook _interceptResponse = new ConnectionHook(
    "Intercept response", 
    "Called when the request has been submitted to the server, and the response " + 
    "has been recieved.\n" +
    "use connection.getResponse() and connection.setResponse(response) to perform changes"
    );
    
    /**
     * Instantiates a new proxy.
     * 
     * @param framework the framework
     */
    public Proxy(Framework framework) {
        _framework = framework;
        createSimulators();
        createListeners();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#getScriptingHooks()
     */
    public Hook[] getScriptingHooks() {
        return new Hook[] { _allowConnection, _interceptRequest, _interceptResponse };
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#getScriptableObject()
     */
    public Object getScriptableObject() {
        return null;
    }
    
    /**
     * Allow client connection.
     * 
     * @param connection the connection
     */
    void allowClientConnection(ScriptableConnection connection) {
        _allowConnection.runScripts(connection);
    }
    
    /**
     * Intercept request.
     * 
     * @param connection the connection
     */
    void interceptRequest(ScriptableConnection connection) {
        _interceptRequest.runScripts(connection);
    }
    
    /**
     * Intercept response.
     * 
     * @param connection the connection
     */
    void interceptResponse(ScriptableConnection connection) {
        _interceptResponse.runScripts(connection);
    }
    
    /**
     * Sets the uI.
     * 
     * @param ui the new uI
     */
    public void setUI(ProxyUI ui) {
        _ui = ui;
        if (_ui != null) _ui.setEnabled(_running);
    }
    
    /**
     * Adds the plugin.
     * 
     * @param plugin the plugin
     */
    public void addPlugin(ProxyPlugin plugin) {
        _plugins.add(plugin);
    }
    
    /**
     * Gets the plugin.
     * 
     * @param name the name
     * 
     * @return the plugin
     */
    public ProxyPlugin getPlugin(String name) {
        ProxyPlugin plugin = null;
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            plugin = (ProxyPlugin) it.next();
            if (plugin.getPluginName().equals(name)) return plugin;
        }
        return null;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#getPluginName()
     */
    public String getPluginName() {
        return new String("Proxy");
    }
    
    /**
     * Gets the proxies.
     * 
     * @return the proxies
     */
    public String[] getProxies() {
        if (_listeners.size()==0) {
            return new String[0];
        }
        return (String[]) _listeners.keySet().toArray(new String[0]);
    }
    
    /**
     * Gets the address.
     * 
     * @param key the key
     * 
     * @return the address
     */
    public String getAddress(String key) {
        Listener l = (Listener) _listeners.get(key);
        if (l != null) {
            return l.getAddress();
        } else {
            return null;
        }
    }
    
    /**
     * Gets the port.
     * 
     * @param key the key
     * 
     * @return the port
     */
    public int getPort(String key) {
        Listener l = (Listener) _listeners.get(key);
        if (l != null) {
            return l.getPort();
        } else {
            return -1;
        }
    }
    
    /**
     * Gets the base.
     * 
     * @param key the key
     * 
     * @return the base
     */
    public HttpUrl getBase(String key) {
        Listener l = (Listener) _listeners.get(key);
        if (l != null) {
            return l.getBase();
        } else {
            return null;
        }
    }
    
    /**
     * Gets the simulators.
     * 
     * @return the simulators
     */
    public String[] getSimulators() {
        return (String[])_simulators.keySet().toArray(new String[0]);
    }
    
    /**
     * Gets the simulator.
     * 
     * @param key the key
     * 
     * @return the simulator
     */
    public String getSimulator(String key) {
        Listener l = (Listener) _listeners.get(key);
        if (l != null) {
            NetworkSimulator netsim = l.getSimulator();
            if (netsim != null) {
                return netsim.getName();
            } else {
                return "Unlimited";
            }
        } else {
            return "Unlimited";
        }
    }
    
    /**
     * Checks if is primary proxy.
     * 
     * @param key the key
     * 
     * @return true, if is primary proxy
     */
    public boolean isPrimaryProxy(String key) {
        Listener l = (Listener) _listeners.get(key);
        if (l != null) {
            return l.isPrimaryProxy();
        } else {
            return false;
        }
    }
    
    /**
     * Gets the plugins.
     * 
     * @return the plugins
     */
    protected ProxyPlugin[] getPlugins() {
        ProxyPlugin[] plugins = new ProxyPlugin[_plugins.size()];
        for (int i=0; i<_plugins.size(); i++) {
            plugins[i] = (ProxyPlugin) _plugins.get(i);
        }
        return plugins;
    }
    
    /**
     * Adds the listener.
     * 
     * @param address the address
     * @param port the port
     * @param base the base
     * @param simulator the simulator
     * @param primary the primary
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    
    public void addListener(String address, int port, HttpUrl base, String simulator, boolean primary) throws IOException {
        Listener l = createListener(address, port, base, simulator, primary);
        startListener(l);
        
        String key = l.getKey();
        Preferences.setPreference("Proxy.listener." + key + ".base", base == null ? "" : base.toString());
        Preferences.setPreference("Proxy.listener." + key + ".simulator", simulator);
        Preferences.setPreference("Proxy.listener." + key + ".primary", primary == true ? "yes" : "no");
        
        String value = null;
        Iterator i = _listeners.keySet().iterator();
        while (i.hasNext()) {
            key = (String) i.next();
            if (value == null) {
                value = key;
            } else {
                value = value + ", " + key;
            }
        }
        Preferences.setPreference("Proxy.listeners", value);
    }
    
    /**
     * Start listener.
     * 
     * @param l the l
     */
    private void startListener(Listener l) {
        Thread t = new Thread(l, "Listener-"+l.getKey());
        t.setDaemon(true);
        t.start();
        if (_ui != null) _ui.proxyStarted(l.getKey());
    }
    
    /**
     * Stop listener.
     * 
     * @param l the l
     * 
     * @return true, if successful
     */
    private boolean stopListener(Listener l) {
        boolean stopped = l.stop();
        if (stopped && _ui != null) _ui.proxyStopped(l.getKey());
        return stopped;
    }
    
    /**
     * Removes the listener.
     * 
     * @param key the key
     * 
     * @return true, if successful
     */
    public boolean removeListener(String key) {
        Listener l = (Listener) _listeners.get(key);
        if (l == null) return false;
        if (stopListener(l)) {
            _listeners.remove(key);
            if (_ui != null) _ui.proxyRemoved(key);
            Preferences.remove("Proxy.listener." + key + ".base");
            Preferences.remove("Proxy.listener." + key + ".simulator");
            Preferences.remove("Proxy.listener." + key + ".primary");
            String value = null;
            Iterator i = _listeners.keySet().iterator();
            while (i.hasNext()) {
                key = (String) i.next();
                if (value == null) {
                    value = key;
                } else {
                    value = value + ", " + key;
                }
            }
            if (value == null) {
                value = "";
            }
            Preferences.setPreference("Proxy.listeners", value);
            return true;
        } else {
            return false;
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#run()
     */
    public void run() {
        Iterator it = _listeners.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Listener l = (Listener) _listeners.get(key);
            startListener(l);
        }
        _running = true;
        if (_ui != null) _ui.setEnabled(_running);
        _status = "Started, Idle";
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#stop()
     */
    public boolean stop() {
        _running = false;
        Iterator it = _listeners.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Listener l = (Listener) _listeners.get(key);
            if (!stopListener(l)) {
                _logger.severe("Failed to stop Listener-" + l.getKey());
                _running = true;
            }
        }
        if (_ui != null) _ui.setEnabled(_running);
        _status = "Stopped";
        return ! _running;
    }
    
    /**
     * Got request.
     * 
     * @param request the request
     * 
     * @return the conversation id
     */
    protected ConversationID gotRequest(Request request) {
        ConversationID id = _framework.reserveConversationID();
        if (_ui != null) _ui.requested(id, request.getMethod(), request.getURL());
        _pending++;
        _status = "Started, " + _pending + " in progress";
        return id;
    }
    
    /**
     * Got response.
     * 
     * @param id the id
     * @param response the response
     */
    protected void gotResponse(ConversationID id, Response response) {
        if (_ui != null) _ui.received(id, response.getStatusLine());
        _framework.addConversation(id, response.getRequest(), response, getPluginName());
        _pending--;
        _status = "Started, " + (_pending>0? (_pending + " in progress") : "Idle");
    }
    
    /**
     * Failed response.
     * 
     * @param id the id
     * @param reason the reason
     */
    protected void failedResponse(ConversationID id, String reason) {
        if (_ui != null) _ui.aborted(id, reason);
        _pending--;
        _status = "Started, " + (_pending>0? (_pending + " in progress") : "Idle");
    }
    
    /**
     * Creates the simulators.
     */
    private void createSimulators() {
        _simulators.put("Unlimited", null);
        _simulators.put("T1", new NetworkSimulator("T1", 3, 1544000/10, 1544000/10));
        _simulators.put("DSL (384k down, 128k up)", new NetworkSimulator("DSL (384k down, 128k up)", 10, 128*1024/10, 384*1024/10));
        _simulators.put("Bonded ISDN", new NetworkSimulator("Bonded ISDN", 20, 128*1024/10, 128*1024/10));
        _simulators.put("ISDN", new NetworkSimulator("ISDN", 20, 64*1024/10, 64*1024/10));
        _simulators.put("56k modem", new NetworkSimulator("56k modem", 200, 33600/10, 56000/10));
        _simulators.put("28k modem", new NetworkSimulator("28k modem", 200, 28800/10));
    }
    
    /**
     * Creates the listeners.
     */
    private void createListeners() {
        String prop = "Proxy.listeners";
        String value = Preferences.getPreference(prop);
        if (value == null || value.trim().equals("")) {
            _logger.warning("No proxies configured!?");
            value = "127.0.0.1:8008";
        }
        String[] listeners = value.trim().split(" *,+ *");
        
        String addr;
        int port = 0;
        HttpUrl base;
        String simulator = null;
        boolean primary = false;
        
        for (int i=0; i<listeners.length; i++) {
            addr = listeners[i].substring(0, listeners[i].indexOf(":"));
            try {
                port = Integer.parseInt(listeners[i].substring(listeners[i].indexOf(":")+1).trim());
            } catch (NumberFormatException nfe) {
                System.err.println("Error parsing port for " + listeners[i] + ", skipping it!");
                continue;
            }
            prop = "Proxy.listener." + listeners[i] + ".base";
            value = Preferences.getPreference(prop, "");
            if (value.equals("")) {
                base = null;
            } else {
                try {
                    base = new HttpUrl(value);
                } catch (MalformedURLException mue) {
                    _logger.severe("Malformed 'base' parameter for listener '"+listeners[i]+"'");
                    break;
                }
            }
            
            prop = "Proxy.listener." + listeners[i] + ".simulator";
            value = Preferences.getPreference(prop, "Unlimited");
            
            if (!value.trim().equals("") && _simulators.containsKey(value)) {
                simulator = value;
            } else {
                _logger.warning("Unknown network simulator '" + value + "'");
            }
            
            prop = "Proxy.listener." + listeners[i] + ".primary";
            value = Preferences.getPreference(prop, "false");
            primary = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes");
            
            try {
                Listener l = createListener(addr, port, base, simulator, primary);
            } catch (IOException ioe) {
                _logger.severe("Error starting proxy (" + addr + ":" + port + " " + base + " " + ioe);
            }
        }
    }
    
    /**
     * Creates the listener.
     * 
     * @param address the address
     * @param port the port
     * @param base the base
     * @param simulator the simulator
     * @param primaryProxy the primary proxy
     * 
     * @return the listener
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private Listener createListener(String address, int port, HttpUrl base, String simulator, boolean primaryProxy) throws IOException {
        if (base != null && base.equals("")) {
            base = null;
        }
        if (simulator == null || simulator.trim().equals("") || !_simulators.containsKey(simulator)) {
            simulator = "Unlimited";
        }
        NetworkSimulator netsim = (NetworkSimulator) _simulators.get(simulator);
        
        Listener l = new Listener(this, address, port);
        l.setBase(base);
        l.setSimulator(netsim);
        l.setPrimaryProxy(primaryProxy);
        
        String key = l.getKey();
        _listeners.put(key, l);
        
        if (_ui != null) _ui.proxyAdded(key);
        
        return l;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#flush()
     */
    public void flush() throws StoreException {
        // we do not run our own store, but our plugins might
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            ProxyPlugin plugin = (ProxyPlugin) it.next();
            plugin.flush();
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#isBusy()
     */
    public boolean isBusy() {
        return _pending > 0;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#getStatus()
     */
    public String getStatus() {
        return _status;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#isModified()
     */
    public boolean isModified() {
        return false;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#analyse(edu.lnmiit.wavd.model.ConversationID, edu.lnmiit.wavd.model.Request, edu.lnmiit.wavd.model.Response, java.lang.String)
     */
    public void analyse(ConversationID id, Request request, Response response, String origin) {
        // we do no analysis
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#setSession(java.lang.String, java.lang.Object, java.lang.String)
     */
    public void setSession(String type, Object store, String session) throws StoreException {
        // we have no listeners to remove
        Iterator it = _plugins.iterator();
        while (it.hasNext()) {
            ProxyPlugin plugin = (ProxyPlugin) it.next();
            plugin.setSession(type, store, session);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#isRunning()
     */
    public boolean isRunning() {
        return _running;
    }
    
    /**
     * The Class ConnectionHook.
     */
    private class ConnectionHook extends Hook {
        
        /**
         * Instantiates a new connection hook.
         * 
         * @param name the name
         * @param description the description
         */
        public ConnectionHook(String name, String description) {
            super(name, description);
        }
        
        /**
         * Run scripts.
         * 
         * @param connection the connection
         */
        public void runScripts(ScriptableConnection connection) {
            if (_bsfManager == null) return;
            synchronized(_bsfManager) {
                try {
                    _bsfManager.declareBean("connection", connection, connection.getClass());
                    super.runScripts();
                    _bsfManager.undeclareBean("connection");
                } catch (Exception e) {
                    _logger.severe("Declaring or undeclaring a bean should not throw an exception! " + e);
                }
            }
        }
        
    }
    
}
