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
 * $Id: ManualRequest.java,v 1.16 2005/05/18 15:23:31 rogan Exp $
 */

package edu.lnmiit.wavd.plugin.manualrequest;




import edu.lnmiit.wavd.httpclient.HTTPClientFactory;
import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.Cookie;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.model.StoreException;
import edu.lnmiit.wavd.plugin.Framework;
import edu.lnmiit.wavd.plugin.Hook;
import edu.lnmiit.wavd.plugin.Plugin;

import java.io.IOException;

import java.util.Date;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ManualRequest.
 */
public class ManualRequest implements Plugin {
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(this.getClass().getName());
    
    /** The _ui. */
    private ManualRequestUI _ui = null;
    
    /** The _request. */
    private Request _request = null;
    
    /** The _response. */
    private Response _response = null;
    
    /** The _response date. */
    private Date _responseDate = null;
    
    /** The _framework. */
    private Framework _framework;
    
    /** The _model. */
    private ManualRequestModel _model;
    
    /**
     * Instantiates a new manual request.
     * 
     * @param framework the framework
     */
    public ManualRequest(Framework framework) {
        _framework = framework;
        _model = new ManualRequestModel(_framework.getModel());
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#getPluginName()
     */
    public String getPluginName() {
        return new String("Manual Request");
    }
    
    /**
     * Gets the model.
     * 
     * @return the model
     */
    public ManualRequestModel getModel() {
        return _model;
    }
    
    /**
     * Sets the uI.
     * 
     * @param ui the new uI
     */
    public void setUI(ManualRequestUI ui) {
        _ui = ui;
        if (_ui != null) _ui.setEnabled(_model.isRunning());
    }
    
    /**
     * Sets the request.
     * 
     * @param request the new request
     */
    public void setRequest(Request request) {
        _request = request;
        if (_ui != null) {
            _ui.responseChanged(null);
            _ui.requestChanged(request);
        }
    }
    
    /**
     * Fetch response.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public synchronized void fetchResponse() throws IOException {
        if (_request != null) {
            try {
                _model.setBusy(true);
                _model.setStatus("Started, Fetching response");
                _response = HTTPClientFactory.getInstance().fetchResponse(_request);
                if (_response != null) {
                    _responseDate = new Date();
                    _framework.addConversation(_request, _response, "Manual Request");
                    if (_ui != null) _ui.responseChanged(_response);
                }
            } finally {
                _model.setStatus("Started, Idle");
                _model.setBusy(false);
            }
        }
    }
    
    /**
     * Adds the request cookies.
     */
    public void addRequestCookies() {
        if (_request != null) {
            Cookie[] cookies = _model.getCookiesForUrl(_request.getURL());
            if (cookies.length>0) {
                StringBuffer buff = new StringBuffer();
                buff.append(cookies[0].getName()).append("=").append(cookies[0].getValue());
                for (int i=1; i<cookies.length; i++) {
                    buff.append("; ").append(cookies[i].getName()).append("=").append(cookies[i].getValue());
                }
                _request.setHeader(new NamedValue("Cookie", buff.toString()));
                if (_ui != null) _ui.requestChanged(_request);
            }
        }
    }
    
    /**
     * Update cookies.
     */
    public void updateCookies() {
        if (_response != null) {
            NamedValue[] headers = _response.getHeaders();
            for (int i=0; i<headers.length; i++) {
                if (headers[i].getName().equalsIgnoreCase("Set-Cookie") || headers[i].getName().equalsIgnoreCase("Set-Cookie2")) {
                    Cookie cookie = new Cookie(_responseDate, _request.getURL(), headers[i].getValue());
                    _model.addCookie(cookie);
                }
            }
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#run()
     */
    public void run() {
        _model.setRunning(true);
        // we do not run in our own thread, so we just return
        if (_ui != null) _ui.setEnabled(_model.isRunning());
        _model.setStatus("Started, Idle");
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#stop()
     */
    public boolean stop() {
        _model.setStopping(true);
        _model.setRunning(false);
        _model.setStopping(false);
        // nothing to stop
        if (_ui != null) _ui.setEnabled(_model.isRunning());
        _model.setStatus("Stopped");
        return ! _model.isRunning();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#flush()
     */
    public void flush() throws StoreException {
        // we do not manage our own store
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#isRunning()
     */
    public boolean isRunning() {
        return _model.isRunning();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#isBusy()
     */
    public boolean isBusy() {
        return _model.isBusy();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#getStatus()
     */
    public String getStatus() {
        return _model.getStatus();
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
        // we maintain no model of our own
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#getScriptableObject()
     */
    public Object getScriptableObject() {
        return null;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.Plugin#getScriptingHooks()
     */
    public Hook[] getScriptingHooks() {
        return new Hook[0];
    }
    
}
