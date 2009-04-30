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

package edu.lnmiit.wavd.plugin;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.X509KeyManager;

// TODO: Auto-generated Javadoc
/**
 * The Class SSLKeyManager.
 */
public class SSLKeyManager implements X509KeyManager {

    /** The Constant KEY_PROPERTY. */
    public final static String KEY_PROPERTY = "KEYS";

    /** The Constant SELECTED_KEY. */
    public final static String SELECTED_KEY = "SELECTED KEY";

    /** The Constant SEP. */
    private static final String SEP = " -:- ";

    /** The _preferred store. */
    private String _preferredStore = null;

    /** The _preferred alias. */
    private String _preferredAlias = null;

    /** The _preferred key manager. */
    private X509KeyManager _preferredKeyManager = null;

    /** The _stores. */
    private Map _stores = new TreeMap();

    /** The _managers. */
    private Map _managers = new TreeMap();

    /** The _change support. */
    private PropertyChangeSupport _changeSupport = new PropertyChangeSupport(this);

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new sSL key manager.
     */
    public SSLKeyManager() {
        _logger.setLevel(Level.FINEST);
        if (System.getProperty("os.name", "").toLowerCase().indexOf("windows") > -1) {
            Provider provider;
            try {
                provider = (Provider) Class.forName("se.assembla.jce.provider.ms.MSProvider").newInstance();
            } catch (Throwable t) {
                return;
            }
            try {
                Security.insertProviderAt(provider, 2);
                KeyStore ks = KeyStore.getInstance("msks", "assembla");
                ks.load(null, null);
                addKeyStore("Microsoft CAPI store", ks, null);
            } catch (Exception e) {
                _logger.info("Microsoft CAPI interface not available: " + e);
            }
        }
    }

    /**
     * Adds the pkc s12 key store.
     * 
     * @param filename
     *            the filename
     * @param keyStorePassword
     *            the key store password
     * @param keyPassword
     *            the key password
     * 
     * @return the string
     * 
     * @throws KeyStoreException
     *             the key store exception
     * @throws UnrecoverableKeyException
     *             the unrecoverable key exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws CertificateException
     *             the certificate exception
     */
    public synchronized String addPKCS12KeyStore(String filename, String keyStorePassword, String keyPassword)
            throws KeyStoreException, UnrecoverableKeyException, IOException, CertificateException {
        if (keyStorePassword == null)
            keyStorePassword = "";
        if (keyPassword == null)
            keyPassword = keyStorePassword;

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(filename), keyStorePassword.toCharArray());
            String description = "PKCS#12: " + filename;
            addKeyStore(description, ks, keyPassword.toCharArray());

            return description;
        } catch (NoSuchAlgorithmException nsae) {
            _logger.severe("No SunX509 suport: " + nsae);
            return null;
        }
    }

    /**
     * Adds the key store.
     * 
     * @param description
     *            the description
     * @param ks
     *            the ks
     * @param password
     *            the password
     * 
     * @throws KeyStoreException
     *             the key store exception
     * @throws UnrecoverableKeyException
     *             the unrecoverable key exception
     */
    public synchronized void addKeyStore(String description, KeyStore ks, char[] password) throws KeyStoreException,
            UnrecoverableKeyException {
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);
            KeyManager km = kmf.getKeyManagers()[0];
            if (!(km instanceof X509KeyManager))
                throw new KeyStoreException("KeyManager for " + description + "is not X509!");
            _stores.put(description, ks);
            _managers.put(description, km);
        } catch (NoSuchAlgorithmException nsae) {
            _logger.severe("This should never happen! SunX509 algorithm not found: " + nsae.getMessage());
        }
        _changeSupport.firePropertyChange(KEY_PROPERTY, null, null);
    }

    /**
     * Gets the key store descriptions.
     * 
     * @return the key store descriptions
     */
    public String[] getKeyStoreDescriptions() {
        return (String[]) _stores.keySet().toArray(new String[0]);
    }

    /**
     * Removes the key store.
     * 
     * @param description
     *            the description
     */
    public synchronized void removeKeyStore(String description) {
        _stores.remove(description);
        _changeSupport.firePropertyChange(KEY_PROPERTY, null, null);
    }

    /**
     * Adds the property change listener.
     * 
     * @param listener
     *            the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes the property change listener.
     * 
     * @param listener
     *            the listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Gets the aliases.
     * 
     * @param description
     *            the description
     * 
     * @return the aliases
     */
    public synchronized String[] getAliases(String description) {
        KeyStore ks = (KeyStore) _stores.get(description);
        if (ks == null) {
            return null;
        }
        List aliases = new ArrayList();
        try {
            Enumeration e = ks.aliases();
            while (e.hasMoreElements()) {
                aliases.add(e.nextElement());
            }
        } catch (KeyStoreException kse) {
            _logger.severe("Error enumerating aliases: " + kse.getMessage());
        }
        return (String[]) aliases.toArray(new String[0]);
    }

    /**
     * Sets the preferred alias.
     * 
     * @param description
     *            the description
     * @param alias
     *            the alias
     * 
     * @return true, if successful
     */
    public synchronized boolean setPreferredAlias(String description, String alias) {
        String old = String.valueOf(_preferredStore) + SEP + String.valueOf(_preferredAlias);
        if (description != null && alias != null) {
            KeyStore ks = (KeyStore) _stores.get(description);
            try {
                if (ks.isKeyEntry(alias)) {
                    _preferredKeyManager = (X509KeyManager) _managers.get(description);
                    _preferredStore = description;
                    _preferredAlias = alias;
                    String now = String.valueOf(_preferredStore) + SEP + String.valueOf(_preferredAlias);
                    if (!now.equals(old))
                        _changeSupport.firePropertyChange(SELECTED_KEY, null, null);
                    return true;
                }
            } catch (KeyStoreException kse) {
                _logger.severe("Unexpected KeyStore exception: " + kse.getMessage());
            }
        }
        _preferredKeyManager = null;
        _preferredStore = null;
        _preferredAlias = null;
        String now = String.valueOf(_preferredStore) + SEP + String.valueOf(_preferredAlias);
        if (!now.equals(old))
            _changeSupport.firePropertyChange(SELECTED_KEY, null, null);
        return false;
    }

    /**
     * Gets the preferred store.
     * 
     * @return the preferred store
     */
    public String getPreferredStore() {
        return _preferredStore;
    }

    /**
     * Gets the preferred alias.
     * 
     * @return the preferred alias
     */
    public String getPreferredAlias() {
        return _preferredAlias;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509KeyManager#chooseClientAlias(java.lang.String[],
     * java.security.Principal[], java.net.Socket)
     */
    public synchronized String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        _logger.entering(getClass().getName(), "chooseClientAlias");
        if (_preferredStore != null && _preferredAlias != null)
            return _preferredStore + SEP + _preferredAlias;
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509KeyManager#chooseServerAlias(java.lang.String,
     * java.security.Principal[], java.net.Socket)
     */
    public synchronized String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        if (_preferredKeyManager != null)
            return _preferredKeyManager.chooseServerAlias(keyType, issuers, socket);

        Iterator it = _managers.keySet().iterator();
        while (it.hasNext()) {
            String source = (String) it.next();
            X509KeyManager km = (X509KeyManager) _managers.get(source);
            String alias = km.chooseServerAlias(keyType, issuers, socket);
            if (alias != null)
                return source + SEP + alias;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509KeyManager#getCertificateChain(java.lang.String)
     */
    public synchronized X509Certificate[] getCertificateChain(String alias) {
        String[] parts = alias.split(SEP, 2);
        String description = parts[0];
        alias = parts[1];
        X509KeyManager km = (X509KeyManager) _managers.get(description);
        return km.getCertificateChain(alias);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509KeyManager#getClientAliases(java.lang.String,
     * java.security.Principal[])
     */
    public synchronized String[] getClientAliases(String keyType, Principal[] issuers) {
        if (_preferredKeyManager != null)
            return _preferredKeyManager.getClientAliases(keyType, issuers);

        List allAliases = new ArrayList();
        Iterator it = _managers.keySet().iterator();
        while (it.hasNext()) {
            String source = (String) it.next();
            X509KeyManager km = (X509KeyManager) _managers.get(source);
            String[] aliases = km.getClientAliases(keyType, issuers);
            if (aliases != null) {
                for (int i = 0; i < aliases.length; i++) {
                    allAliases.add(source + SEP + aliases[i]);
                }
            }
        }
        return (String[]) allAliases.toArray(new String[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509KeyManager#getPrivateKey(java.lang.String)
     */
    public synchronized PrivateKey getPrivateKey(String alias) {
        String[] parts = alias.split(SEP, 2);
        String description = parts[0];
        alias = parts[1];
        X509KeyManager km = (X509KeyManager) _managers.get(description);
        return km.getPrivateKey(alias);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509KeyManager#getServerAliases(java.lang.String,
     * java.security.Principal[])
     */
    public synchronized String[] getServerAliases(String keyType, Principal[] issuers) {
        if (_preferredKeyManager != null)
            return _preferredKeyManager.getServerAliases(keyType, issuers);

        List allAliases = new ArrayList();
        Iterator it = _managers.keySet().iterator();
        while (it.hasNext()) {
            String source = (String) it.next();
            X509KeyManager km = (X509KeyManager) _managers.get(source);
            String[] aliases = km.getServerAliases(keyType, issuers);
            if (aliases != null) {
                for (int i = 0; i < aliases.length; i++) {
                    allAliases.add(source + SEP + aliases[i]);
                }
            }
        }
        return (String[]) allAliases.toArray(new String[0]);
    }

}
