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
 * ManualEdit.java
 *
 * Created on July 10, 2003, 4:46 PM
 */

package edu.lnmiit.wavd.plugin.proxy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;
import edu.lnmiit.wavd.httpclient.HTTPClient;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.plugin.Framework;

// TODO: Auto-generated Javadoc
/**
 * The Class BeanShell.
 */
public class BeanShell extends ProxyPlugin {

    /** The _logger. */
    private Logger _logger = Logger.getLogger(this.getClass().getName());

    /** The _script file. */
    private String _scriptFile = "";

    /** The _bean script. */
    private String _beanScript;

    /** The _default script. */
    private String _defaultScript = "/* Please read the JavaDoc and/or the source to understand what methods are available */\n"
            + "\n"
            + "import org.owasp.webscarab.model.Request;\n"
            + "import org.owasp.webscarab.model.Response;\n"
            + "import org.owasp.webscarab.httpclient.HTTPClient;\n"
            + "import java.io.IOException;\n"
            + "\n"
            + "public Response fetchResponse(HTTPClient nextPlugin, Request request) throws IOException {\n"
            + "   response = nextPlugin.fetchResponse(request);\n" + "   return response;\n" + "}\n";

    /** The _interpreter. */
    private Interpreter _interpreter;

    /** The _framework. */
    private Framework _framework = null;

    /** The _enabled. */
    private boolean _enabled = false;

    /** The _ui. */
    private BeanShellUI _ui = null;

    /**
     * Instantiates a new bean shell.
     * 
     * @param framework
     *            the framework
     */
    public BeanShell(Framework framework) {
        _interpreter = new Interpreter();
        _framework = framework;
        try {
            _interpreter.set("framework", _framework);
        } catch (EvalError ee) {
            _logger.severe("Couldn't set framework: " + ee);
        }
        parseProperties();
    }

    /**
     * Sets the uI.
     * 
     * @param ui
     *            the new uI
     */
    public void setUI(BeanShellUI ui) {
        _ui = ui;
        PrintStream ps = _ui.getOut();
        if (ps != null)
            _interpreter.setOut(ps); // FIXME TODO Why is this not working?
        ps = _ui.getErr();
        if (ps != null)
            _interpreter.setErr(ps); // when err does seem to be working??
    }

    /**
     * Parses the properties.
     */
    private void parseProperties() {
        String prop = "BeanShell.scriptFile";
        String value = Preferences.getPreference(prop, "");
        _scriptFile = value;
        if (!_scriptFile.equals("")) {
            loadScriptFile(_scriptFile);
        } else {
            try {
                setScript(_defaultScript);
            } catch (EvalError ee) {
                _logger.severe("Invalid default script string " + ee);
            }
        }

        prop = "BeanShell.enabled";
        value = Preferences.getPreference(prop, "");
        setEnabled(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyPlugin#getPluginName()
     */
    public String getPluginName() {
        return new String("Bean Shell");
    }

    /**
     * Sets the enabled.
     * 
     * @param bool
     *            the new enabled
     */
    public void setEnabled(boolean bool) {
        _enabled = bool;
        String prop = "BeanShell.enabled";
        Preferences.setPreference(prop, Boolean.toString(bool));
    }

    /**
     * Gets the enabled.
     * 
     * @return the enabled
     */
    public boolean getEnabled() {
        return _enabled;
    }

    /**
     * Load script file.
     * 
     * @param filename
     *            the filename
     */
    private void loadScriptFile(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            setScript(sb.toString());
        } catch (Exception e) {
            _logger.severe("Error reading BeanShell script from '" + filename + "' : " + e);
            try {
                setScript(_defaultScript);
            } catch (EvalError ee) {
                _logger.severe("Invalid default script string: " + ee);
            }
        }
    }

    /**
     * Sets the script file.
     * 
     * @param filename
     *            the new script file
     * 
     * @throws EvalError
     *             the eval error
     */
    public void setScriptFile(String filename) throws EvalError {
        _scriptFile = filename;
        String prop = "BeanShell.scriptfile";
        Preferences.setPreference(prop, filename);
        if (!filename.equals("")) {
            loadScriptFile(filename);
        } else {
            setScript(_defaultScript);
        }
    }

    /**
     * Gets the script file.
     * 
     * @return the script file
     */
    public String getScriptFile() {
        return _scriptFile;
    }

    /**
     * Sets the script.
     * 
     * @param script
     *            the new script
     * 
     * @throws EvalError
     *             the eval error
     */
    public void setScript(String script) throws EvalError {
        _beanScript = script;
        _interpreter = new Interpreter();
        _interpreter.eval(_beanScript);
    }

    /**
     * Gets the script.
     * 
     * @return the script
     */
    public String getScript() {
        return _beanScript;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.plugin.proxy.ProxyPlugin#getProxyPlugin(edu.lnmiit.wavd
     * .httpclient.HTTPClient)
     */
    public HTTPClient getProxyPlugin(HTTPClient in) {
        return new Plugin(in);
    }

    /**
     * The Class Plugin.
     */
    private class Plugin implements HTTPClient {

        /** The _in. */
        private HTTPClient _in;

        /**
         * Instantiates a new plugin.
         * 
         * @param in
         *            the in
         */
        public Plugin(HTTPClient in) {
            _in = in;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.httpclient.HTTPClient#fetchResponse(edu.lnmiit.wavd
         * .model.Request)
         */
        public Response fetchResponse(Request request) throws IOException {
            if (_enabled) {
                try {
                    synchronized (_interpreter) {
                        _interpreter.unset("response");
                        _interpreter.set("nextClient", _in);
                        _interpreter.set("request", request);
                        try {
                            _interpreter.eval("Response response = fetchResponse(nextClient, request);");
                        } catch (TargetError te) {
                            if (te.getTarget() instanceof IOException) {
                                throw (IOException) te.getTarget();
                            }
                            throw te;
                        }
                        Response response = (Response) _interpreter.get("response");
                        _interpreter.unset("model");
                        _interpreter.unset("response");
                        _interpreter.unset("nextClient");
                        _interpreter.unset("request");
                        response.setHeader("X-BeanShell", "possibly modified");
                        return response;
                    }
                } catch (EvalError e) {
                    System.err.println("e is a " + e.getClass());
                    if (_ui != null)
                        _ui.getErr().println(e.toString());
                    throw new IOException("Error evaluating bean script : " + e);
                }
            } else {
                return _in.fetchResponse(request);
            }
        }
    }

}
