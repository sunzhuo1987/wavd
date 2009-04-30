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
 * $Id: Fuzzer.java,v 1.8 2005/09/21 22:06:33 rogan Exp $
 */

package edu.lnmiit.wavd.plugin.fuzz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import edu.lnmiit.wavd.httpclient.ConversationHandler;
import edu.lnmiit.wavd.httpclient.FetcherQueue;
import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.model.StoreException;
import edu.lnmiit.wavd.plugin.Framework;
import edu.lnmiit.wavd.plugin.Hook;
import edu.lnmiit.wavd.plugin.Plugin;
import edu.lnmiit.wavd.util.Encoding;

// TODO: Auto-generated Javadoc
/**
 * The Class Fuzzer.
 */
public class Fuzzer implements Plugin, ConversationHandler {

    /** The _model. */
    private FuzzerModel _model = null;

    /** The _framework. */
    private Framework _framework = null;

    /** The _fuzz factory. */
    private FuzzFactory _fuzzFactory = new FuzzFactory();

    /** The _fetcher queue. */
    private FetcherQueue _fetcherQueue = null;

    /** The _threads. */
    private int _threads = 4;

    /** The _run thread. */
    private Thread _runThread = null;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new fuzzer.
     * 
     * @param framework
     *            the framework
     */
    public Fuzzer(Framework framework) {
        _framework = framework;
        _model = new FuzzerModel(_framework.getModel());
        loadFuzzStrings();
        _fetcherQueue = new FetcherQueue("Fuzzer", this, _threads, 0);
    }

    /**
     * Load fuzz strings.
     */
    private void loadFuzzStrings() {
        int i = 0;
        String description;
        while ((description = Preferences.getPreference("Fuzz." + i + ".description")) != null) {
            String location = Preferences.getPreference("Fuzz." + i + ".location");
            if (location != null && !description.equals("")) {
                try {
                    URL url = new URL(location);
                    _fuzzFactory.loadFuzzStrings(description, url.openStream());
                } catch (IOException ioe) {
                    _logger
                            .warning("Error loading \"" + description + "\" from " + location + " : "
                                    + ioe.getMessage());
                }
            }
            i++;
        }
    }

    /**
     * Gets the fuzz factory.
     * 
     * @return the fuzz factory
     */
    public FuzzFactory getFuzzFactory() {
        return _fuzzFactory;
    }

    /**
     * Gets the model.
     * 
     * @return the model
     */
    public FuzzerModel getModel() {
        return _model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#getPluginName()
     */
    public String getPluginName() {
        return new String("Fuzzer");
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#run()
     */
    public void run() {
        _model.setStatus("Started");
        _model.setStopping(false);
        _runThread = Thread.currentThread();

        _model.setRunning(true);
        while (!_model.isStopping()) {
            // queue them as fast as they come, sleep a bit otherwise
            boolean submittedRequest = queueRequests();
            if (!submittedRequest) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
            }
        }
        _fetcherQueue.clearRequestQueue();
        _model.setRunning(false);
        _runThread = null;
        _model.setStatus("Stopped");
    }

    /**
     * Start fuzzing.
     */
    public void startFuzzing() {
        int count = _model.getFuzzParameterCount();
        if (count > 0 && _model.getFuzzUrl() != null) {
            _model.setBusyFuzzing(true);
        } else {
            _logger.warning("Can't fuzz if there are no parameters or URL");
        }
    }

    /**
     * Construct current fuzz request.
     * 
     * @return the request
     * 
     * @throws MalformedURLException
     *             the malformed url exception
     */
    private Request constructCurrentFuzzRequest() throws MalformedURLException {
        Request request = new Request();
        request.setMethod(_model.getFuzzMethod());
        request.setVersion(_model.getFuzzVersion());
        int count = _model.getFuzzHeaderCount();
        // _logger.info("Got headers: " + count);
        for (int i = 0; i < count; i++) {
            // _logger.info("Header is " + _model.getFuzzHeader(i));
            request.addHeader(_model.getFuzzHeader(i));
        }
        // if (request.getMethod().equals("POST")) {
        // request.setHeader("Content-Type",
        // "application/x-www-form-urlencoded");
        // }
        String url = _model.getFuzzUrl().toString();
        String path = null;
        String fragment = null;
        String query = null;
        String cookie = null;
        ByteArrayOutputStream content = null;
        count = _model.getFuzzParameterCount();
        for (int i = 0; i < count; i++) {
            Parameter parameter = _model.getFuzzParameter(i);
            Object value = _model.getFuzzParameterValue(i);
            String location = parameter.getLocation();
            if (location.equals(Parameter.LOCATION_PATH)) {
                if (path == null) {
                    path = (String) value;
                } else {
                    path = path + "/" + (value == null ? "" : (String) value);
                }
            } else if (location.equals(Parameter.LOCATION_FRAGMENT)) {
                String frag = parameter.getName();
                if (frag == null) {
                    frag = (String) value;
                } else if (value == null) {
                    frag = frag + "=" + Encoding.urlEncode((String) value);
                } else {
                    frag = null;
                }
                if (fragment == null) {
                    fragment = frag;
                } else if (frag != null) {
                    fragment = fragment + "&" + frag;
                }
            } else if (location.equals(Parameter.LOCATION_QUERY)) {
                String q = parameter.getName() + "=" + Encoding.urlEncode((String) value);
                if (query == null) {
                    query = q;
                } else {
                    query = query + "&" + q;
                }
            } else if (location.equals(Parameter.LOCATION_COOKIE)) {
                String c = parameter.getName() + "=" + (String) value;
                if (cookie == null) {
                    cookie = c;
                } else {
                    cookie = cookie + "; " + c;
                }
            } else if (location.equals(Parameter.LOCATION_BODY)) {
                // FIXME - Assumes this is normal form data
                String b = parameter.getName() + "=" + Encoding.urlEncode((String) value);
                if (content == null) {
                    content = new ByteArrayOutputStream();
                    try {
                        content.write(b.getBytes());
                    } catch (IOException ioe) {
                    }
                } else {
                    try {
                        content.write(("&" + b).getBytes());
                    } catch (IOException ioe) {
                    }
                }
            } else {
                _logger.severe("Skipping unknown parameter location " + location);
            }
        }
        if (path != null)
            url = url + "/" + path;
        if (fragment != null)
            url = url + ";" + fragment;
        if (query != null)
            url = url + "?" + query;
        request.setURL(new HttpUrl(url));
        if (cookie != null)
            request.addHeader("Cookie", cookie);
        if (content != null) {
            request.setHeader("Content-Length", Integer.toString(content.size()));
            request.setContent(content.toByteArray());
        } else if (request.getMethod().equals("POST")) {
            request.setHeader("Content-Length", "0");
        }
        return request;
    }

    /**
     * Pause fuzzing.
     */
    public void pauseFuzzing() {
        _model.setBusyFuzzing(false);
    }

    /**
     * Stop fuzzing.
     */
    public void stopFuzzing() {
        _model.setBusyFuzzing(false);
    }

    /**
     * Queue requests.
     * 
     * @return true, if successful
     */
    private boolean queueRequests() {
        if (!_model.isBusyFuzzing())
            return false;
        if (_fetcherQueue.getRequestsQueued() >= _threads)
            return false;
        try {
            Request request = constructCurrentFuzzRequest();
            _fetcherQueue.submit(request);
            if (!_model.incrementFuzzer()) {
                _model.setBusyFuzzing(false);
            }
        } catch (Exception e) {
            _model.setBusyFuzzing(false);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.httpclient.ConversationHandler#requestError(edu.lnmiit
     * .wavd.model.Request, java.io.IOException)
     */
    public void requestError(Request request, IOException ioe) {
        _logger.warning("Caught exception : " + ioe.getMessage());
        _model.setBusyFuzzing(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.httpclient.ConversationHandler#responseReceived(edu.lnmiit
     * .wavd.model.Response)
     */
    public void responseReceived(Response response) {
        if (response.getStatus().equals("400")) {
            _logger.warning("Bad request");
            _model.setBusyFuzzing(false);
            return;
        }
        Request request = response.getRequest();
        if (request == null) {
            _logger.warning("Got a null request from the response!");
            return;
        }
        ConversationID id = _framework.addConversation(request, response, "Fuzzer");
        _model.addConversation(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#stop()
     */
    public boolean stop() {
        _model.setStatus("Stopped");
        _model.setRunning(false);
        return !_model.isRunning();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#setSession(java.lang.String,
     * java.lang.Object, java.lang.String)
     */
    public void setSession(String type, Object store, String session) throws edu.lnmiit.wavd.model.StoreException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#flush()
     */
    public void flush() throws StoreException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#isBusy()
     */
    public boolean isBusy() {
        return _model.isBusy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#isRunning()
     */
    public boolean isRunning() {
        return _model.isRunning();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#getStatus()
     */
    public String getStatus() {
        return _model.getStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#isModified()
     */
    public boolean isModified() {
        return _model.isModified();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.plugin.Plugin#analyse(edu.lnmiit.wavd.model.ConversationID
     * , edu.lnmiit.wavd.model.Request, edu.lnmiit.wavd.model.Response,
     * java.lang.String)
     */
    public void analyse(ConversationID id, Request request, Response response, String origin) {
        Signature signature = new Signature(request);
        _model.addSignature(signature);
        if (!response.getStatus().equals("304")) {
            byte[] content = response.getContent();
            if (content == null)
                content = new byte[0];
            String checksum = Encoding.hashMD5(content);
            _model.addChecksum(signature.getUrl(), checksum);
        }
    }

    /**
     * Load template from conversation.
     * 
     * @param id
     *            the id
     */
    public void loadTemplateFromConversation(ConversationID id) {
        if (_model.isBusyFuzzing()) {
            stopFuzzing();
        }
        Request request = _framework.getModel().getRequest(id);
        HttpUrl url = request.getURL();
        if (url.getParameters() != null)
            url = url.getParentUrl();
        _model.setFuzzMethod(request.getMethod());
        _model.setFuzzUrl(url.toString());
        _model.setFuzzVersion(request.getVersion());
        while (_model.getFuzzHeaderCount() > 0) {
            _model.removeFuzzHeader(0);
        }
        while (_model.getFuzzParameterCount() > 0) {
            _model.removeFuzzParameter(0);
        }
        NamedValue[] headers = request.getHeaders();
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Cookie"))
                    continue;
                _model.addFuzzHeader(_model.getFuzzHeaderCount(), headers[i]);
            }
        }
        Parameter[] params = Parameter.getParameters(request);
        for (int i = 0; i < params.length; i++) {
            _model.addFuzzParameter(i, params[i], null, 0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#getScriptableObject()
     */
    public Object getScriptableObject() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.Plugin#getScriptingHooks()
     */
    public Hook[] getScriptingHooks() {
        return new Hook[0];
    }

}
