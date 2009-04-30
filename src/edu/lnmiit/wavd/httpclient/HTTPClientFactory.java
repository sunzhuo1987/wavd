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
 * HttpClientFactory.java
 *
 * Created on August 19, 2004, 11:22 PM
 */

package edu.lnmiit.wavd.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating HTTPClient objects.
 */
public class HTTPClientFactory {

    /** The _instance. */
    private static HTTPClientFactory _instance = new HTTPClientFactory();

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /** The _http proxy. */
    private String _httpProxy = "";

    /** The _http proxy port. */
    private int _httpProxyPort = 80;

    /** The _https proxy. */
    private String _httpsProxy = "";

    /** The _https proxy port. */
    private int _httpsProxyPort = 80;

    /** The _no proxy. */
    private String[] _noProxy = new String[0];

    /** The _connect timeout. */
    private int _connectTimeout = 30000;

    /** The _read timeout. */
    private int _readTimeout = 0;

    /** The _ssl context manager. */
    private SSLContextManager _sslContextManager = null;

    /** The _authenticator. */
    private Authenticator _authenticator = null;

    /** The _client list. */
    private List _clientList = new ArrayList();

    /** The _available clients. */
    private List _availableClients = new ArrayList();

    /**
     * Instantiates a new hTTP client factory.
     */
    protected HTTPClientFactory() {
        _sslContextManager = new SSLContextManager();
    }

    /**
     * Gets the single instance of HTTPClientFactory.
     * 
     * @return single instance of HTTPClientFactory
     */
    public static HTTPClientFactory getInstance() {
        return _instance;
    }

    /**
     * Gets the sSL context manager.
     * 
     * @return the sSL context manager
     */
    public SSLContextManager getSSLContextManager() {
        return _sslContextManager;
    }

    /**
     * Sets the http proxy.
     * 
     * @param proxy
     *            the proxy
     * @param port
     *            the port
     */
    public void setHttpProxy(String proxy, int port) {
        if (proxy == null)
            proxy = "";
        _httpProxy = proxy;
        if (port < 1 || port > 65535)
            throw new IllegalArgumentException("Port is out of range: " + port);
        _httpProxyPort = port;
    }

    /**
     * Gets the http proxy.
     * 
     * @return the http proxy
     */
    public String getHttpProxy() {
        return _httpProxy;
    }

    /**
     * Gets the http proxy port.
     * 
     * @return the http proxy port
     */
    public int getHttpProxyPort() {
        return _httpProxyPort;
    }

    /**
     * Sets the https proxy.
     * 
     * @param proxy
     *            the proxy
     * @param port
     *            the port
     */
    public void setHttpsProxy(String proxy, int port) {
        if (proxy == null)
            proxy = "";
        _httpsProxy = proxy;
        if (port < 1 || port > 65535)
            throw new IllegalArgumentException("Port is out of range: " + port);
        _httpsProxyPort = port;
    }

    /**
     * Gets the https proxy.
     * 
     * @return the https proxy
     */
    public String getHttpsProxy() {
        return _httpsProxy;
    }

    /**
     * Gets the https proxy port.
     * 
     * @return the https proxy port
     */
    public int getHttpsProxyPort() {
        return _httpsProxyPort;
    }

    /**
     * Sets the no proxy.
     * 
     * @param noProxy
     *            the new no proxy
     */
    public void setNoProxy(String[] noProxy) {
        _noProxy = noProxy;
        if (_noProxy == null)
            _noProxy = new String[0];
    }

    /**
     * Gets the no proxy.
     * 
     * @return the no proxy
     */
    public String[] getNoProxy() {
        return _noProxy;
    }

    /**
     * Sets the timeouts.
     * 
     * @param connectTimeout
     *            the connect timeout
     * @param readTimeout
     *            the read timeout
     */
    public void setTimeouts(int connectTimeout, int readTimeout) {
        _connectTimeout = connectTimeout;
        _readTimeout = readTimeout;
    }

    /**
     * Sets the authenticator.
     * 
     * @param authenticator
     *            the new authenticator
     */
    public void setAuthenticator(Authenticator authenticator) {
        _authenticator = authenticator;
    }

    /**
     * Gets the authenticator.
     * 
     * @return the authenticator
     */
    public Authenticator getAuthenticator() {
        return _authenticator;
    }

    /**
     * Gets the hTTP client.
     * 
     * @return the hTTP client
     */
    public HTTPClient getHTTPClient() {
        URLFetcher uf = new URLFetcher();
        uf.setHttpProxy(_httpProxy, _httpProxyPort);
        uf.setHttpsProxy(_httpsProxy, _httpsProxyPort);
        uf.setNoProxy(_noProxy);
        uf.setSSLContextManager(_sslContextManager);
        uf.setTimeouts(_connectTimeout, _readTimeout);
        uf.setAuthenticator(_authenticator);
        return uf;
    }

    /**
     * Fetch response.
     * 
     * @param request
     *            the request
     * 
     * @return the response
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Response fetchResponse(Request request) throws IOException {
        HTTPClient hc = null;
        synchronized (_availableClients) {
            if (_availableClients.size() > 0) {
                hc = (HTTPClient) _availableClients.remove(0);
            } else {
                _logger.info("Creating a new Fetcher");
                hc = getHTTPClient();
                _clientList.add(hc);
            }
        }
        Response response = null;
        IOException ioe = null;
        try {
            response = hc.fetchResponse(request);
        } catch (IOException e) {
            ioe = e;
        }
        synchronized (_availableClients) {
            _availableClients.add(hc);
        }
        if (ioe != null)
            throw ioe;
        return response;
    }

}
