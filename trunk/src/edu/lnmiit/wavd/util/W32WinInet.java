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
package edu.lnmiit.wavd.util;

// TODO: Auto-generated Javadoc
/**
 * The Class W32WinInet.
 */
public class W32WinInet {

    /** The PROX y_ typ e_ direct. */    
    public static int PROXY_TYPE_DIRECT         = 0x00000001;   // direct to net
    
    /** The PROX y_ typ e_ proxy. */    
    public static int PROXY_TYPE_PROXY          = 0x00000002;   // via named proxy
    
    /** The PROX y_ typ e_ aut o_ prox y_ url. */    
    public static int PROXY_TYPE_AUTO_PROXY_URL = 0x00000004;   // autoproxy URL
    
    /** The PROX y_ typ e_ aut o_ detect. */    
    public static int PROXY_TYPE_AUTO_DETECT    = 0x00000008;   // use autoproxy detection

    /** The _available. */
    private static boolean _available = false;
    
    /** The _intercepted. */
    private static boolean _intercepted = false;
    
    /** The _per conn. */
    private static long _perConn = 0;
    
    /** The _proxy server. */
    private static String _proxyServer = null;
    
    /** The _proxy bypass. */
    private static String _proxyBypass = null;
    
    /**
     * Test library load.
     * 
     * @return the int
     */
    private native static int testLibraryLoad();
    
    /**
     * Gets the internet per conn flags.
     * 
     * @return the internet per conn flags
     */
    private native static long getInternetPerConnFlags();
    
    /**
     * Gets the auto discovery flags.
     * 
     * @return the auto discovery flags
     */
    private native static long getAutoDiscoveryFlags();

    /**
     * Gets the auto config url.
     * 
     * @return the auto config url
     */
    private native static String getAutoConfigUrl();

    /**
     * Gets the proxy server.
     * 
     * @return the proxy server
     */
    private native static String getProxyServer();

    /**
     * Gets the proxy bypass.
     * 
     * @return the proxy bypass
     */
    private native static String getProxyBypass();

    /**
     * Sets the proxy.
     * 
     * @param perConnFlags the per conn flags
     * @param proxyServer the proxy server
     * @param proxyBypass the proxy bypass
     * 
     * @return the int
     */
    private native static int setProxy(long perConnFlags, String proxyServer, String proxyBypass);

    static {
        try {
            System.loadLibrary("W32WinInet");
            if (testLibraryLoad() == 1) 
                _available = true;
        } catch (UnsatisfiedLinkError ule) {
            _available = false;
        }
    }
    
    /**
     * Checks if is available.
     * 
     * @return true, if is available
     */    
    public static boolean isAvailable() {
        return _available;
    }
    
    /**
     * Intercept proxy.
     * 
     * @param server the server
     * @param port the port
     * 
     * @return true, if successful
     */    
    public static boolean interceptProxy(String server, int port) {
        if (!isAvailable()) return false;
        if (! _intercepted) {
            _perConn = getInternetPerConnFlags();
            _proxyServer = getProxyServer();
            _proxyBypass = getProxyBypass();
        }
        int result = setProxy(PROXY_TYPE_PROXY, server + ":" + port, null);
        if (result != 0) {
            result = setProxy(_perConn, _proxyServer, _proxyBypass);
            return false;
        }
        _intercepted = true;
        return true;
    }
    
    /**
     * Revert proxy.
     */    
    public static void revertProxy() {
        if (! _intercepted) return;
        int result = setProxy(_perConn, _proxyServer, _proxyBypass);
        _intercepted = false;
    }
    
    /**
     * Gets the proxy.
     * 
     * @param type the type
     * 
     * @return the proxy
     */
    private static String getProxy(String type) {
        String proxy;
        if (_intercepted) {
            proxy = _proxyServer;
        } else {
            proxy = getProxyServer();
        }
        if (proxy == null) return null;
        String[] proxies;
        if (proxy.indexOf("=")>0) {
            proxies = proxy.split(";");
        } else {
            return proxy;
        }
        for (int i=0; i<proxies.length; i++) {
            if (proxies[i].startsWith(type+"=")) {
                return proxies[i].substring(proxies[i].indexOf("=")+1);
            }
        }
        return null;
    }
    
    /**
     * Gets the http proxy server.
     * 
     * @return the http proxy server
     */    
    public static String getHttpProxyServer() {
        String proxy = getProxy("http");
        if (proxy == null) return null;
        return proxy.substring(0, proxy.indexOf(":"));
    }
    
    /**
     * Gets the http proxy port.
     * 
     * @return the http proxy port
     */
    public static int getHttpProxyPort() {
        String proxy = getProxy("http");
        if (proxy == null) return -1;
        try {
            return Integer.parseInt(proxy.substring(proxy.indexOf(":")+1));
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
    
    /**
     * Gets the https proxy server.
     * 
     * @return the https proxy server
     */
    public static String getHttpsProxyServer() {
        String proxy = getProxy("https");
        if (proxy == null) return null;
        return proxy.substring(0, proxy.indexOf(":"));
    }
    
    /**
     * Gets the https proxy port.
     * 
     * @return the https proxy port
     */
    public static int getHttpsProxyPort() {
        String proxy = getProxy("https");
        if (proxy == null) return -1;
        try {
            return Integer.parseInt(proxy.substring(proxy.indexOf(":")+1));
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
    
    /**
     * Gets the no proxy.
     * 
     * @return the no proxy
     */    
    public static String getNoProxy() {
        String bypass;
        if (!_intercepted) {
            bypass = getProxyBypass();
        } else {
            bypass = _proxyBypass;
        }
        if (bypass == null) return null;
        return bypass;
    }
    
    /**
     * The main method.
     * 
     * @param args the arguments
     * 
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        if (!isAvailable()) {
            System.err.println("DLL not found, or wrong platform!");
            System.exit(1);
        }
        if (args.length == 0) {
            System.err.println("Please specify an address to set temporarily");
            System.err.println("e.g. W32WinInet localhost:3128");
        }
        long perConn = getInternetPerConnFlags();
        String proxyServer = getProxyServer();
        String proxyBypass = getProxyBypass();
        
        System.out.println("Current settings:");
        System.out.println("PerConnFlags: " + perConn);
        System.out.println("ProxyServer: " + proxyServer);
        System.out.println("ProxyBypass: " + proxyBypass);
        System.out.println();
        
        System.out.println("Changed to " + args[0] + ", result is : " + setProxy(PROXY_TYPE_PROXY, args[0], null));
        
        System.out.println("Settings are now:");
        System.out.println("PerConnFlags: " + getInternetPerConnFlags());
        System.out.println("ProxyServer: " + getProxyServer());
        System.out.println("ProxyBypass: " + getProxyBypass());
        
        System.out.print("Press enter to change them back: ");
        System.in.read();
        
        System.out.println("Result is : " + setProxy(perConn, proxyServer, proxyBypass));
        System.out.println();

        System.out.println("Current settings:");
        System.out.println("PerConnFlags: " + getInternetPerConnFlags());
        System.out.println("ProxyServer: " + getProxyServer());
        System.out.println("ProxyBypass: " + getProxyBypass());
        
    }
}

