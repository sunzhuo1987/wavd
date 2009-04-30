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
import java.util.Date;


import edu.lnmiit.wavd.httpclient.HTTPClient;
import edu.lnmiit.wavd.model.Cookie;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.plugin.Framework;

// TODO: Auto-generated Javadoc
/**
 * The Class CookieTracker.
 */
public class CookieTracker extends ProxyPlugin {
    
    /** The _model. */
    private FrameworkModel _model = null;
    
    /** The _inject requests. */
    private boolean _injectRequests = false;
    
    /** The _read responses. */
    private boolean _readResponses = false;
    
    /**
     * Instantiates a new cookie tracker.
     * 
     * @param framework the framework
     */
    public CookieTracker(Framework framework) {
        _model = framework.getModel();
        parseProperties();
    }
    
    /**
     * Parses the properties.
     */
    public void parseProperties() {
        String prop = "CookieTracker.injectRequests";
        String value = Preferences.getPreference(prop, "false");
        _injectRequests = ("true".equalsIgnoreCase( value ) || "yes".equalsIgnoreCase( value ));
        prop = "CookieTracker.readResponses";
        value = Preferences.getPreference(prop, "true");
        _readResponses = ("true".equalsIgnoreCase( value ) || "yes".equalsIgnoreCase( value ));
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyPlugin#getPluginName()
     */
    public String getPluginName() {
        return new String("Cookie Tracker");
    }
    
    /**
     * Sets the inject requests.
     * 
     * @param bool the new inject requests
     */
    public void setInjectRequests(boolean bool) {
        _injectRequests = bool;
        String prop = "CookieTracker.injectRequests";
        Preferences.setPreference(prop,Boolean.toString(bool));
    }

    /**
     * Gets the inject requests.
     * 
     * @return the inject requests
     */
    public boolean getInjectRequests() {
        return _injectRequests;
    }
    
    /**
     * Sets the read responses.
     * 
     * @param bool the new read responses
     */
    public void setReadResponses(boolean bool) {
        _readResponses = bool;
        String prop = "CookieTracker.readResponses";
        Preferences.setPreference(prop,Boolean.toString(bool));
    }

    /**
     * Gets the read responses.
     * 
     * @return the read responses
     */
    public boolean getReadResponses() {
        return _readResponses;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyPlugin#getProxyPlugin(edu.lnmiit.wavd.httpclient.HTTPClient)
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
         * @param in the in
         */
        public Plugin(HTTPClient in) {
            _in = in;
        }
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.httpclient.HTTPClient#fetchResponse(edu.lnmiit.wavd.model.Request)
         */
        public Response fetchResponse(Request request) throws IOException {
            if (_injectRequests) {
                // FIXME we should do something about any existing cookies that are in the Request
                // they could have been set via JavaScript, or some such!
                Cookie[] cookies = _model.getCookiesForUrl(request.getURL());
                if (cookies.length>0) {
                    StringBuffer buff = new StringBuffer();
                    buff.append(cookies[0].getName()).append("=").append(cookies[0].getValue());
                    for (int i=1; i<cookies.length; i++) {
                        buff.append("; ").append(cookies[i].getName()).append("=").append(cookies[i].getValue());
                    }
                    request.setHeader("Cookie", buff.toString());
                }
            }
            Response response = _in.fetchResponse(request);
            if (_readResponses && response != null) {
                NamedValue[] headers = response.getHeaders();
                for (int i=0; i<headers.length; i++) {
                    if (headers[i].getName().equalsIgnoreCase("Set-Cookie") || headers[i].getName().equalsIgnoreCase("Set-Cookie2")) {
                        Cookie cookie = new Cookie(new Date(), request.getURL(), headers[i].getValue());
                        _model.addCookie(cookie);
                    }
                }
            }
            return response;
        }
        
    }
    
}
