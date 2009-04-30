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

import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.X509KeyManager;

// TODO: Auto-generated Javadoc
/**
 * The Class AliasKeyManager.
 */
public class AliasKeyManager implements X509KeyManager {
    
    /** The _ks. */
    private KeyStore _ks;
    
    /** The _alias. */
    private String _alias;
    
    /** The _key password. */
    private String _keyPassword;
    
    /**
     * Instantiates a new alias key manager.
     * 
     * @param ks the ks
     * @param alias the alias
     * @param keyPassword the key password
     */
    public AliasKeyManager(KeyStore ks, String alias, String keyPassword) {
        _ks = ks;
        _alias = alias;
        _keyPassword = keyPassword;
    }
    
    /* (non-Javadoc)
     * @see javax.net.ssl.X509KeyManager#chooseClientAlias(java.lang.String[], java.security.Principal[], java.net.Socket)
     */
    public String chooseClientAlias(String[] str, Principal[] principal, Socket socket) {
        return _alias;
    }

    /* (non-Javadoc)
     * @see javax.net.ssl.X509KeyManager#chooseServerAlias(java.lang.String, java.security.Principal[], java.net.Socket)
     */
    public String chooseServerAlias(String str, Principal[] principal, Socket socket) {
        return _alias;
    }

    /* (non-Javadoc)
     * @see javax.net.ssl.X509KeyManager#getCertificateChain(java.lang.String)
     */
    public X509Certificate[] getCertificateChain(String alias) {
        try {
            Certificate[] certs = _ks.getCertificateChain(alias);
            if (certs == null) return null;
            X509Certificate[] x509certs = new X509Certificate[certs.length];
            for (int i=0; i<certs.length; i++) {
                x509certs[i]=(X509Certificate) certs[i];
            }
            return x509certs;
        } catch (KeyStoreException kse) {
            kse.printStackTrace();
            return null;
        }
    }

    /* (non-Javadoc)
     * @see javax.net.ssl.X509KeyManager#getClientAliases(java.lang.String, java.security.Principal[])
     */
    public String[] getClientAliases(String str, Principal[] principal) {
        return new String[] { _alias };
    }

    /* (non-Javadoc)
     * @see javax.net.ssl.X509KeyManager#getPrivateKey(java.lang.String)
     */
    public PrivateKey getPrivateKey(String alias) {
        try {
            return (PrivateKey) _ks.getKey(alias, _keyPassword.toCharArray());
        } catch (KeyStoreException kse) {
            kse.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException nsao) {
            nsao.printStackTrace();
            return null;
        } catch (UnrecoverableKeyException uke) {
            uke.printStackTrace();
            return null;
        }
    }

    /* (non-Javadoc)
     * @see javax.net.ssl.X509KeyManager#getServerAliases(java.lang.String, java.security.Principal[])
     */
    public String[] getServerAliases(String str, Principal[] principal) {
        return new String[] { _alias };
    }
    
}
