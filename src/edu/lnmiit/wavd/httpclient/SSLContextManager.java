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

package edu.lnmiit.wavd.httpclient;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import edu.lnmiit.wavd.util.Encoding;
import edu.lnmiit.wavd.util.NullComparator;

// TODO: Auto-generated Javadoc
/**
 * The Class SSLContextManager.
 */
public class SSLContextManager {
    
    /** The _context maps. */
    private Map _contextMaps = new TreeMap(new NullComparator());
    
    /** The _no client cert context. */
    private SSLContext _noClientCertContext;
    
    /** The _default key. */
    private String _defaultKey = null;
    
    /** The _alias passwords. */
    private Map _aliasPasswords = new HashMap();
    
    /** The _key stores. */
    private List _keyStores = new ArrayList();
    
    /** The _key store descriptions. */
    private Map _keyStoreDescriptions = new HashMap();
    
    /** The _trust all certs. */
    private static TrustManager[] _trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }
    };
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /**
     * Instantiates a new sSL context manager.
     */
    public SSLContextManager() {
        try {
            _noClientCertContext = SSLContext.getInstance("SSL");
            _noClientCertContext.init(null, _trustAllCerts, new SecureRandom());
        } catch (NoSuchAlgorithmException nsao) {
            _logger.severe("Could not get an instance of the SSL algorithm: " + nsao.getMessage());
        } catch (KeyManagementException kme) {
            _logger.severe("Error initialising the SSL Context: " + kme);
        }
        try {
            initMSCAPI();
        } catch (Exception e) {}
    }
    
    /**
     * Checks if is provider available.
     * 
     * @param type the type
     * 
     * @return true, if is provider available
     */
    public boolean isProviderAvailable(String type) {
        try {
            if (type.equals("PKCS11")) {
                Class.forName("sun.security.pkcs11.SunPKCS11");
            } else if (type.equals("msks")) {
                Class.forName("se.assembla.jce.provider.ms.MSProvider");
            }
        } catch (Throwable t) {
            return false;
        }
        return true;
    }
    
    /**
     * Checks if is provider loaded.
     * 
     * @param keyStoreType the key store type
     * 
     * @return true, if is provider loaded
     */
    private boolean isProviderLoaded(String keyStoreType) {
        return Security.getProvider(keyStoreType) != null ? true : false;
    }
    
    /**
     * Adds the key store.
     * 
     * @param ks the ks
     * @param description the description
     * 
     * @return the int
     */
    private int addKeyStore(KeyStore ks, String description) {
        int index = _keyStores.indexOf(ks);
        if (index == -1) {
            _keyStores.add(ks);
            index = _keyStores.size() - 1;
        }
        _keyStoreDescriptions.put(ks, description);
        return index;
    }
    
    /**
     * Gets the key store count.
     * 
     * @return the key store count
     */
    public int getKeyStoreCount() {
        return _keyStores.size();
    }
    
    /**
     * Gets the key store description.
     * 
     * @param keystoreIndex the keystore index
     * 
     * @return the key store description
     */
    public String getKeyStoreDescription(int keystoreIndex) {
        return (String) _keyStoreDescriptions.get(_keyStores.get(keystoreIndex));
    }
    
    /**
     * Gets the alias count.
     * 
     * @param keystoreIndex the keystore index
     * 
     * @return the alias count
     */
    public int getAliasCount(int keystoreIndex) {
        return getAliases((KeyStore) _keyStores.get(keystoreIndex)).length;
    }
    
    /**
     * Gets the alias at.
     * 
     * @param keystoreIndex the keystore index
     * @param aliasIndex the alias index
     * 
     * @return the alias at
     */
    public String getAliasAt(int keystoreIndex, int aliasIndex) {
        return getAliases((KeyStore) _keyStores.get(keystoreIndex))[aliasIndex];
    }
    
    /**
     * Gets the aliases.
     * 
     * @param ks the ks
     * 
     * @return the aliases
     */
    private String[] getAliases(KeyStore ks) {
        List aliases = new ArrayList();
        try {
            Enumeration en = ks.aliases();
            while (en.hasMoreElements()) {
                String alias = (String) en.nextElement();
                if (ks.isKeyEntry(alias))
                    aliases.add(alias);
            }
        } catch (KeyStoreException kse) {
            kse.printStackTrace();
        }
        return (String[]) aliases.toArray(new String[0]);
    }
    
    /**
     * Gets the certificate.
     * 
     * @param keystoreIndex the keystore index
     * @param aliasIndex the alias index
     * 
     * @return the certificate
     */
    public Certificate getCertificate(int keystoreIndex, int aliasIndex) {
        try {
            KeyStore ks = (KeyStore) _keyStores.get(keystoreIndex);
            String alias = getAliasAt(keystoreIndex, aliasIndex);
            return ks.getCertificate(alias);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Gets the finger print.
     * 
     * @param cert the cert
     * 
     * @return the finger print
     * 
     * @throws KeyStoreException the key store exception
     */
    public String getFingerPrint(Certificate cert) throws KeyStoreException {
        if (!(cert instanceof X509Certificate)) return null;
        StringBuffer buff = new StringBuffer();
        X509Certificate x509 = (X509Certificate) cert;
        try {
            String fingerprint = Encoding.hashMD5(cert.getEncoded());
            for (int i=0; i<fingerprint.length(); i+=2) {
                buff.append(fingerprint.substring(i, i+1)).append(":");
            }
            buff.deleteCharAt(buff.length()-1);
        } catch (CertificateEncodingException e) {
            throw new KeyStoreException(e.getMessage());
        }
        String dn = x509.getSubjectDN().getName();
        _logger.info("Fingerprint is " + buff.toString().toUpperCase());
        return buff.toString().toUpperCase() + " " + dn;
    }
    
    /**
     * Checks if is key unlocked.
     * 
     * @param keystoreIndex the keystore index
     * @param aliasIndex the alias index
     * 
     * @return true, if is key unlocked
     */
    public boolean isKeyUnlocked(int keystoreIndex, int aliasIndex) {
        KeyStore ks = (KeyStore) _keyStores.get(keystoreIndex);
        String alias = getAliasAt(keystoreIndex, aliasIndex);
        
        Map pwmap = (Map) _aliasPasswords.get(ks);
        if (pwmap == null) return false;
        return pwmap.containsKey(alias);
    }
    
    /**
     * Sets the default key.
     * 
     * @param fingerprint the new default key
     */
    public void setDefaultKey(String fingerprint) {
        _defaultKey = fingerprint;
    }
    
    /**
     * Gets the default key.
     * 
     * @return the default key
     */
    public String getDefaultKey() {
        return _defaultKey;
    }
    
    /**
     * Inits the mscapi.
     * 
     * @throws KeyStoreException the key store exception
     * @throws NoSuchProviderException the no such provider exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws CertificateException the certificate exception
     */
    private void initMSCAPI()
    throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException {
        try {
            if (!isProviderAvailable("msks")) return;
            
            Provider mscapi = (Provider) Class.forName("se.assembla.jce.provider.ms.MSProvider").newInstance();
            Security.addProvider(mscapi);
            
            // init the key store
            KeyStore ks = KeyStore.getInstance("msks", "assembla");
            ks.load(null, null);
            addKeyStore(ks, "Microsoft CAPI Store");
        } catch (Exception e) {
            System.err.println("Error instantiating the MSCAPI provider");
            e.printStackTrace();
        }
    }
    
    /**
     * Inits the pkc s11.
     * 
     * @param name the name
     * @param library the library
     * @param kspassword the kspassword
     * 
     * @return the int
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws KeyStoreException the key store exception
     * @throws CertificateException the certificate exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public int initPKCS11(String name, String library, String kspassword)
    throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        try {
            if (!isProviderAvailable("PKCS11")) return -1;
            
            // Set up a virtual config file
            StringBuffer cardConfig = new StringBuffer();
            cardConfig.append("name = ").append(name).append("\n");
            cardConfig.append("library = ").append(library).append("\n");
            InputStream is = new ByteArrayInputStream(cardConfig.toString().getBytes());
            
            // create the provider
            Class pkcs11Class = Class.forName("sun.security.pkcs11.SunPKCS11");
            Constructor c = pkcs11Class.getConstructor(new Class[] { InputStream.class });
            Provider pkcs11 = (Provider) c.newInstance(new Object[] { is });
            Security.addProvider(pkcs11);
            
            // init the key store
            KeyStore ks = KeyStore.getInstance("PKCS11");
            ks.load(null, kspassword == null ? null : kspassword.toCharArray());
            return addKeyStore(ks, "PKCS#11");
        } catch (Exception e) {
            System.err.println("Error instantiating the PKCS11 provider");
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Load pkc s12 certificate.
     * 
     * @param filename the filename
     * @param ksPassword the ks password
     * 
     * @return the int
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws KeyStoreException the key store exception
     * @throws CertificateException the certificate exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public int loadPKCS12Certificate(String filename, String ksPassword)
    throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        // Open the file
        InputStream is = new FileInputStream(filename);
        if (is == null)
            throw new FileNotFoundException(filename + " could not be found");
        
        // create the keystore
        
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(is, ksPassword == null ? null : ksPassword.toCharArray());
        return addKeyStore(ks, "PKCS#12 - " + filename);
    }
    
    /**
     * Save key.
     * 
     * @param ks the ks
     * @param alias the alias
     * @param keypassword the keypassword
     */
    private void saveKey(KeyStore ks, String alias, String keypassword) {
        Map pwmap = (Map) _aliasPasswords.get(ks);
        if (pwmap == null) {
            pwmap = new TreeMap(new NullComparator());
            _aliasPasswords.put(ks, pwmap);
        }
        pwmap.put(alias, keypassword);
    }
    
    
    /**
     * Unlock key.
     * 
     * @param keystoreIndex the keystore index
     * @param aliasIndex the alias index
     * @param keyPassword the key password
     * 
     * @throws KeyStoreException the key store exception
     * @throws KeyManagementException the key management exception
     */
    public void unlockKey(int keystoreIndex, int aliasIndex, String keyPassword) throws KeyStoreException, KeyManagementException {
        KeyStore ks = (KeyStore) _keyStores.get(keystoreIndex);
        String alias = getAliasAt(keystoreIndex, aliasIndex);
        
        AliasKeyManager akm = new AliasKeyManager(ks, alias, keyPassword);
        
        String fingerprint = getFingerPrint(getCertificate(keystoreIndex, aliasIndex));
        
        if (fingerprint == null) {
            _logger.severe("No fingerprint found");
            return;
        }
        
        SSLContext sc;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException nsao) {
            _logger.severe("Could not get an instance of the SSL algorithm: " + nsao.getMessage());
            return;
        }
        
        sc.init(new KeyManager[] { akm }, _trustAllCerts, new SecureRandom());
        
        String key = fingerprint;
        if (key.indexOf(" ")>0)
            key = key.substring(0, key.indexOf(" "));
        _contextMaps.put(key, sc);
    }
    
    /**
     * Invalidate sessions.
     */
    public void invalidateSessions() {
        invalidateSession(_noClientCertContext);
        Iterator it = _contextMaps.keySet().iterator();
        while (it.hasNext()) {
            invalidateSession((SSLContext)_contextMaps.get(it.next()));
        }
    }
    
    /**
     * Invalidate session.
     * 
     * @param sc the sc
     */
    private void invalidateSession(SSLContext sc) {
        SSLSessionContext sslsc = sc.getClientSessionContext();
        if (sslsc != null) {
            int timeout = sslsc.getSessionTimeout();
            // force sessions to be timed out
            sslsc.setSessionTimeout(1);
            sslsc.setSessionTimeout(timeout);
        }
        sslsc = sc.getServerSessionContext();
        if (sslsc != null) {
            int timeout = sslsc.getSessionTimeout();
            // force sessions to be timed out
            sslsc.setSessionTimeout(1);
            sslsc.setSessionTimeout(timeout);
        }
    }
    
    /**
     * Gets the sSL context.
     * 
     * @param fingerprint the fingerprint
     * 
     * @return the sSL context
     */
    public SSLContext getSSLContext(String fingerprint) {
        _logger.info("Requested SSLContext for " + fingerprint);
        
        if (fingerprint == null || fingerprint.equals("none"))
            return _noClientCertContext;
        if (fingerprint.indexOf(" ")>0)
            fingerprint = fingerprint.substring(0, fingerprint.indexOf(" "));
        return (SSLContext) _contextMaps.get(fingerprint);
    }
    
}
