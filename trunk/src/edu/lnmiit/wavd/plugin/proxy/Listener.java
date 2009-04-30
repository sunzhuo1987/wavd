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
 * Listener.java
 *
 * Created on August 30, 2003, 4:15 PM
 */

package edu.lnmiit.wavd.plugin.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.util.W32WinInet;

// TODO: Auto-generated Javadoc
/**
 * The Class Listener.
 */
public class Listener implements Runnable {

    /** The _proxy. */
    private Proxy _proxy;

    /** The _address. */
    private String _address;

    /** The _port. */
    private int _port;

    /** The _base. */
    private HttpUrl _base = null;

    /** The _simulator. */
    private NetworkSimulator _simulator = null;

    /** The _primary proxy. */
    private boolean _primaryProxy = false;

    /** The _serversocket. */
    private ServerSocket _serversocket = null;

    /** The _stop. */
    private boolean _stop = false;

    /** The _stopped. */
    private boolean _stopped = true;

    /** The _addr. */
    private InetAddress _addr;

    /** The _count. */
    private int _count = 1;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(this.getClass().getName());

    /**
     * Instantiates a new listener.
     * 
     * @param proxy
     *            the proxy
     * @param address
     *            the address
     * @param port
     *            the port
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Listener(Proxy proxy, String address, int port) throws IOException {
        _proxy = proxy;
        if (address == null) {
            address = "*";
        }
        _address = address;
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("port must be between 0 and 65536");
        }
        _port = port;
        if (_address.equals("") || _address.equals("*")) {
            _addr = null;
        } else {
            _addr = InetAddress.getByName(_address);
        }
        // make sure we can listen on the port
        _serversocket = new ServerSocket(_port, 5, _addr);
        _serversocket.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        _stop = false;
        _stopped = false;
        Socket sock;
        ConnectionHandler ch;
        Thread thread;
        if (_serversocket == null || _serversocket.isClosed()) {
            try {
                listen();
            } catch (IOException ioe) {
                _logger.severe("Can't listen at " + getKey() + ": " + ioe);
                _stopped = true;
                return;
            }
        }
        if (W32WinInet.isAvailable() && _primaryProxy)
            W32WinInet.interceptProxy("localhost", _port);
        while (!_stop) {
            try {
                sock = _serversocket.accept();
                sock.getInetAddress();
                ch = new ConnectionHandler(_proxy, sock, _base, _simulator);
                thread = new Thread(ch, Thread.currentThread().getName() + "-" + Integer.toString(_count++));
                thread.setDaemon(true);
                thread.start();
            } catch (IOException e) {
                if (!e.getMessage().equals("Accept timed out")) {
                    System.err.println("I/O error while waiting for a connection : " + e.getMessage());
                }
            }
        }
        _stopped = true;
        try {
            _serversocket.close();
        } catch (IOException ioe) {
            System.err.println("Error closing socket : " + ioe);
        }
        if (W32WinInet.isAvailable() && _primaryProxy)
            W32WinInet.revertProxy();
        _logger.info("Not listening on " + getKey());
    }

    /**
     * Listen.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void listen() throws IOException {
        _serversocket = new ServerSocket(_port, 5, _addr);

        _logger.info("Proxy listening on " + getKey());

        try {
            _serversocket.setSoTimeout(100);
        } catch (SocketException se) {
            _logger.warning("Error setting sockettimeout " + se);
            _logger.warning("It is likely that this listener will be unstoppable!");
        }
    }

    /**
     * Stop.
     * 
     * @return true, if successful
     */
    public boolean stop() {
        _stop = true;
        if (!_stopped) {
            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
                if (_stopped) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets the address.
     * 
     * @return the address
     */
    public String getAddress() {
        return _address;
    }

    /**
     * Gets the port.
     * 
     * @return the port
     */
    public int getPort() {
        return _port;
    }

    /**
     * Sets the base.
     * 
     * @param base
     *            the new base
     */
    public void setBase(HttpUrl base) {
        _base = base;
    }

    /**
     * Gets the base.
     * 
     * @return the base
     */
    public HttpUrl getBase() {
        return _base;
    }

    /**
     * Sets the simulator.
     * 
     * @param simulator
     *            the new simulator
     */
    public void setSimulator(NetworkSimulator simulator) {
        _simulator = simulator;
    }

    /**
     * Gets the simulator.
     * 
     * @return the simulator
     */
    public NetworkSimulator getSimulator() {
        return _simulator;
    }

    /**
     * Sets the primary proxy.
     * 
     * @param primary
     *            the new primary proxy
     */
    public void setPrimaryProxy(boolean primary) {
        _primaryProxy = primary;
    }

    /**
     * Checks if is primary proxy.
     * 
     * @return true, if is primary proxy
     */
    public boolean isPrimaryProxy() {
        return _primaryProxy;
    }

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        return _address + ":" + _port;
    }

}
