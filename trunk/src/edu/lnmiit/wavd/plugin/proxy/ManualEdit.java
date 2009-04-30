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

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import edu.lnmiit.wavd.httpclient.HTTPClient;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

// TODO: Auto-generated Javadoc
/**
 * The Class ManualEdit.
 */
public class ManualEdit extends ProxyPlugin {

    /** The INCLUDE. */
    private static String INCLUDE = ".*";

    /** The EXCLUDE. */
    private static String EXCLUDE = ".*\\.(gif|jpg|png|css|js|ico|swf|axd.*)$";

    /** The CONTENT. */
    private static String CONTENT = "text/.*";

    /** The _include regex. */
    private String _includeRegex = "";

    /** The _exclude regex. */
    private String _excludeRegex = "";

    /** The _intercept methods. */
    private String[] _interceptMethods = null;

    /** The _intercept request. */
    private boolean _interceptRequest = false;

    /** The _intercept response. */
    private boolean _interceptResponse = false;

    /** The _intercept response regex. */
    private String _interceptResponseRegex = "";

    /** The _case sensitive. */
    private boolean _caseSensitive = false;

    /** The _ui. */
    private ManualEditUI _ui = null;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new manual edit.
     */
    public ManualEdit() {
        parseProperties();
    }

    /**
     * Parses the properties.
     */
    public void parseProperties() {
        String prop = "ManualEdit.includeRegex";
        String value = Preferences.getPreference(prop, INCLUDE);
        _includeRegex = value;

        prop = "ManualEdit.excludeRegex";
        value = Preferences.getPreference(prop, EXCLUDE);
        _excludeRegex = value;

        prop = "ManualEdit.interceptMethods";
        value = Preferences.getPreference(prop, "GET, POST");
        _interceptMethods = value.split(" *, *");

        prop = "ManualEdit.interceptRequest";
        value = Preferences.getPreference(prop, "false");
        _interceptRequest = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes");

        prop = "ManualEdit.interceptResponse";
        value = Preferences.getPreference(prop, "false");
        _interceptResponse = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes");

        prop = "ManualEdit.interceptResponseRegex";
        value = Preferences.getPreference(prop, CONTENT);
        _interceptResponseRegex = value;

        prop = "ManualEdit.caseSensitive";
        value = Preferences.getPreference(prop, "false");
        _caseSensitive = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes");

    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyPlugin#getPluginName()
     */
    public String getPluginName() {
        return new String("Manual Edit");
    }

    /**
     * Sets the uI.
     * 
     * @param ui
     *            the new uI
     */
    public void setUI(ManualEditUI ui) {
        _ui = ui;
    }

    /**
     * Sets the include regex.
     * 
     * @param regex
     *            the new include regex
     */
    public void setIncludeRegex(String regex) {
        _includeRegex = regex;
        String prop = "ManualEdit.includeRegex";
        Preferences.setPreference(prop, regex);
    }

    /**
     * Gets the include regex.
     * 
     * @return the include regex
     */
    public String getIncludeRegex() {
        return _includeRegex;
    }

    /**
     * Sets the exclude regex.
     * 
     * @param regex
     *            the new exclude regex
     */
    public void setExcludeRegex(String regex) {
        _excludeRegex = regex;
        String prop = "ManualEdit.excludeRegex";
        Preferences.setPreference(prop, regex);
    }

    /**
     * Gets the exclude regex.
     * 
     * @return the exclude regex
     */
    public String getExcludeRegex() {
        return _excludeRegex;
    }

    /**
     * Sets the intercept methods.
     * 
     * @param methods
     *            the new intercept methods
     */
    public void setInterceptMethods(String[] methods) {
        _interceptMethods = methods;
        String value = "";
        if (methods.length > 0) {
            value = methods[0];
            for (int i = 1; i < methods.length; i++) {
                value = value + ", " + methods[i];
            }
        }
        String prop = "ManualEdit.interceptMethods";
        Preferences.setPreference(prop, value);
    }

    /**
     * Gets the intercept methods.
     * 
     * @return the intercept methods
     */
    public String[] getInterceptMethods() {
        return _interceptMethods;
    }

    /**
     * Sets the intercept request.
     * 
     * @param bool
     *            the new intercept request
     */
    public void setInterceptRequest(boolean bool) {
        _interceptRequest = bool;
        String prop = "ManualEdit.interceptRequest";
        Preferences.setPreference(prop, Boolean.toString(bool));
    }

    /**
     * Gets the intercept request.
     * 
     * @return the intercept request
     */
    public boolean getInterceptRequest() {
        return _interceptRequest;
    }

    /**
     * Sets the intercept response.
     * 
     * @param bool
     *            the new intercept response
     */
    public void setInterceptResponse(boolean bool) {
        _interceptResponse = bool;
        String prop = "ManualEdit.interceptResponse";
        Preferences.setPreference(prop, Boolean.toString(bool));
    }

    /**
     * Gets the intercept response.
     * 
     * @return the intercept response
     */
    public boolean getInterceptResponse() {
        return _interceptResponse;
    }

    /**
     * Sets the intercept response regex.
     * 
     * @param regex
     *            the new intercept response regex
     */
    public void setInterceptResponseRegex(String regex) {
        _interceptResponseRegex = regex;
        Preferences.setPreference("ManualEdit.interceptResponseRegex", regex);
    }

    /**
     * Gets the intercept response regex.
     * 
     * @return the intercept response regex
     */
    public String getInterceptResponseRegex() {
        return _interceptResponseRegex;
    }

    /**
     * Sets the case sensitive.
     * 
     * @param bool
     *            the new case sensitive
     */
    public void setCaseSensitive(boolean bool) {
        _caseSensitive = bool;
        String prop = "ManualEdit.caseSensitive";
        Preferences.setPreference(prop, Boolean.toString(bool));
    }

    /**
     * Checks if is case sensitive.
     * 
     * @return true, if is case sensitive
     */
    public boolean isCaseSensitive() {
        return _caseSensitive;
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

        /** The _exclude. */
        private Pattern _exclude;

        /** The _include. */
        private Pattern _include;

        /** The _content. */
        private Pattern _content;

        /**
         * Instantiates a new plugin.
         * 
         * @param in
         *            the in
         */
        public Plugin(HTTPClient in) {
            _in = in;

            int flags = _caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
            try {
                _include = Pattern.compile(_includeRegex, flags);
                _exclude = Pattern.compile(_excludeRegex, flags);
                _content = Pattern.compile(_interceptResponseRegex, flags);
            } catch (PatternSyntaxException pse) {
                _logger.warning("Regex pattern is invalid, using ALL default patterns! " + pse.getMessage());
                try {
                    _include = Pattern.compile(INCLUDE);
                    _exclude = Pattern.compile(EXCLUDE);
                    _content = Pattern.compile(CONTENT);
                } catch (PatternSyntaxException pse2) {
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.httpclient.HTTPClient#fetchResponse(edu.lnmiit.wavd
         * .model.Request)
         */
        public Response fetchResponse(Request request) throws IOException {
            if (_interceptRequest) {
                String url = request.getURL().toString();
                Matcher include = _include.matcher(url);
                Matcher exclude = _exclude.matcher(url);
                if (!exclude.matches() && include.matches()) {
                    String method = request.getMethod();
                    for (int i = 0; i < _interceptMethods.length; i++) {
                        if (method.equals(_interceptMethods[i])) {
                            if (_ui != null) {
                                request = _ui.editRequest(request);
                                if (request == null)
                                    throw new IOException("Request aborted in Manual Edit");
                            }
                        }
                    }
                }
            }
            Response response = _in.fetchResponse(request);
            if (_interceptResponse) {
                String contentType = response.getHeader("Content-Type");
                if (contentType == null || !_content.matcher(contentType).matches()) {
                    return response;
                }
                if (_ui != null) {
                    request = response.getRequest();
                    response = _ui.editResponse(request, response);
                    if (response == null)
                        throw new IOException("Response aborted in Manual Edit");
                    if (response.getRequest() == null)
                        response.setRequest(request);
                    response.addHeader("X-ManualEdit", "possibly modified");
                }
            }
            return response;
        }

    }

}
