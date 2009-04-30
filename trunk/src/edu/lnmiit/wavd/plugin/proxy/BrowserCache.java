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
 * RevealHidden.java
 *
 * Created on July 13, 2003, 7:39 PM
 */

package edu.lnmiit.wavd.plugin.proxy;

import java.io.IOException;

import edu.lnmiit.wavd.httpclient.HTTPClient;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

// TODO: Auto-generated Javadoc
/**
 * The Class BrowserCache.
 */
public class BrowserCache extends ProxyPlugin {

    /** The _enabled. */
    private boolean _enabled = false;

    /**
     * Instantiates a new browser cache.
     */
    public BrowserCache() {
        parseProperties();
    }

    /**
     * Parses the properties.
     */
    public void parseProperties() {
        String prop = "BrowserCache.enabled";
        String value = Preferences.getPreference(prop, "false");
        _enabled = "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyPlugin#getPluginName()
     */
    public String getPluginName() {
        return new String("Browser Cache");
    }

    /**
     * Sets the enabled.
     * 
     * @param bool
     *            the new enabled
     */
    public void setEnabled(boolean bool) {
        _enabled = bool;
        String prop = "BrowserCache.enabled";
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
                // we could be smarter about this, and keep a record of the
                // pages that we
                // have seen so far, and only remove headers for those that we
                // have not?
                request.deleteHeader("ETag");
                request.deleteHeader("If-Modified-Since");
                request.deleteHeader("If-None-Match");
            }
            return _in.fetchResponse(request);
        }

    }

}
