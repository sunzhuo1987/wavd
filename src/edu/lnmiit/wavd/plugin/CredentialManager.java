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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.lnmiit.wavd.httpclient.Authenticator;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.util.Encoding;

// TODO: Auto-generated Javadoc
/**
 * The Class CredentialManager.
 */
public class CredentialManager implements Authenticator {

    // contains Maps per host, indexed by Realm
    /** The _basic credentials. */
    private Map _basicCredentials = new TreeMap();

    /** The _domain credentials. */
    private Map _domainCredentials = new TreeMap();

    /** The _ui. */
    private CredentialManagerUI _ui = null;

    /**
     * Instantiates a new credential manager.
     */
    public CredentialManager() {
    }

    /**
     * Sets the uI.
     * 
     * @param ui
     *            the new uI
     */
    public void setUI(CredentialManagerUI ui) {
        _ui = ui;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.httpclient.Authenticator#getCredentials(edu.lnmiit.wavd
     * .model.HttpUrl, java.lang.String[])
     */
    public synchronized String getCredentials(HttpUrl url, String[] challenges) {
        String creds = getPreferredCredentials(url.getHost(), challenges);
        if (creds != null)
            return creds;
        boolean prompt = Boolean.valueOf(Preferences.getPreference("WebScarab.promptForCredentials", "false"))
                .booleanValue();
        if (prompt && _ui != null && challenges != null && challenges.length > 0) {
            boolean ask = false;
            for (int i = 0; i < challenges.length; i++)
                if (challenges[i].startsWith("Basic") || challenges[i].startsWith("NTLM")
                        || challenges[i].startsWith("Negotiate"))
                    ask = true;
            if (ask)
                _ui.requestCredentials(url.getHost(), challenges);
        }
        return getPreferredCredentials(url.getHost(), challenges);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.httpclient.Authenticator#getProxyCredentials(java.lang
     * .String, java.lang.String[])
     */
    public synchronized String getProxyCredentials(String hostname, String[] challenges) {
        String creds = getPreferredCredentials(hostname, challenges);
        if (creds != null)
            return creds;
        boolean prompt = Boolean.valueOf(Preferences.getPreference("WebScarab.promptForCredentials", "false"))
                .booleanValue();
        if (prompt && _ui != null && challenges != null && challenges.length > 0) {
            boolean ask = false;
            for (int i = 0; i < challenges.length; i++)
                if (challenges[i].startsWith("Basic") || challenges[i].startsWith("NTLM")
                        || challenges[i].startsWith("Negotiate"))
                    ask = true;
            if (ask)
                _ui.requestCredentials(hostname, challenges);
        }
        return getPreferredCredentials(hostname, challenges);
    }

    /**
     * Adds the basic credentials.
     * 
     * @param cred
     *            the cred
     */
    public void addBasicCredentials(BasicCredential cred) {
        if ((cred.getUsername() == null || cred.getUsername().equals(""))
                && (cred.getPassword() == null || cred.getPassword().equals("")))
            return;
        Map realms = (Map) _basicCredentials.get(cred.getHost());
        if (realms == null) {
            realms = new TreeMap();
            _basicCredentials.put(cred.getHost(), realms);
        }
        realms.put(cred.getRealm(), cred);
    }

    /**
     * Adds the domain credentials.
     * 
     * @param cred
     *            the cred
     */
    public void addDomainCredentials(DomainCredential cred) {
        if ((cred.getUsername() == null || cred.getUsername().equals(""))
                && (cred.getPassword() == null || cred.getPassword().equals("")))
            return;
        _domainCredentials.put(cred.getHost(), cred);
    }

    /**
     * Gets the basic credential count.
     * 
     * @return the basic credential count
     */
    public int getBasicCredentialCount() {
        return getAllBasicCredentials().length;
    }

    /**
     * Gets the basic credential at.
     * 
     * @param index
     *            the index
     * 
     * @return the basic credential at
     */
    public BasicCredential getBasicCredentialAt(int index) {
        return getAllBasicCredentials()[index];
    }

    /**
     * Delete basic credential at.
     * 
     * @param index
     *            the index
     */
    public void deleteBasicCredentialAt(int index) {
        int i = -1;
        Iterator hosts = _basicCredentials.keySet().iterator();
        while (hosts.hasNext()) {
            Map realms = (Map) _basicCredentials.get(hosts.next());
            Iterator realm = realms.keySet().iterator();
            while (realm.hasNext()) {
                Object key = realm.next();
                i++;
                if (i == index)
                    realms.remove(key);
            }
        }
    }

    /**
     * Gets the domain credential count.
     * 
     * @return the domain credential count
     */
    public int getDomainCredentialCount() {
        return _domainCredentials.entrySet().size();
    }

    /**
     * Gets the domain credential at.
     * 
     * @param index
     *            the index
     * 
     * @return the domain credential at
     */
    public DomainCredential getDomainCredentialAt(int index) {
        List all = new ArrayList();
        Iterator hosts = _domainCredentials.keySet().iterator();
        while (hosts.hasNext())
            all.add(_domainCredentials.get(hosts.next()));
        return (DomainCredential) all.toArray(new DomainCredential[0])[index];
    }

    /**
     * Delete domain credential at.
     * 
     * @param index
     *            the index
     */
    public void deleteDomainCredentialAt(int index) {
        int i = -1;
        Iterator hosts = _domainCredentials.keySet().iterator();
        while (hosts.hasNext()) {
            Object key = hosts.next();
            i++;
            if (i == index)
                _domainCredentials.remove(key);
        }
    }

    /**
     * Gets the all basic credentials.
     * 
     * @return the all basic credentials
     */
    private BasicCredential[] getAllBasicCredentials() {
        List all = new ArrayList();
        Iterator hosts = _basicCredentials.keySet().iterator();
        while (hosts.hasNext()) {
            Map realms = (Map) _basicCredentials.get(hosts.next());
            Iterator realm = realms.keySet().iterator();
            while (realm.hasNext())
                all.add(realms.get(realm.next()));
        }
        return (BasicCredential[]) all.toArray(new BasicCredential[0]);
    }

    /**
     * Gets the preferred credentials.
     * 
     * @param host
     *            the host
     * @param challenges
     *            the challenges
     * 
     * @return the preferred credentials
     */
    private String getPreferredCredentials(String host, String[] challenges) {
        // we don't do pre-emptive auth at all
        if (challenges == null || challenges.length == 0)
            return null;
        for (int i = 0; i < challenges.length; i++) {
            if (challenges[i].startsWith("Basic")) {
                String creds = getBasicCredentials(host, challenges[i]);
                if (creds != null)
                    return "Basic " + creds;
            }
        }
        for (int i = 0; i < challenges.length; i++) {
            if (challenges[i].startsWith("NTLM")) {
                String creds = getDomainCredentials(host);
                if (creds != null)
                    return "NTLM " + creds;
            }
        }
        for (int i = 0; i < challenges.length; i++) {
            if (challenges[i].startsWith("Negotiate")) {
                String creds = getDomainCredentials(host);
                if (creds != null)
                    return "Negotiate " + creds;
            }
        }
        return null;
    }

    /**
     * Gets the basic credentials.
     * 
     * @param host
     *            the host
     * @param challenge
     *            the challenge
     * 
     * @return the basic credentials
     */
    private String getBasicCredentials(String host, String challenge) {
        String realm = challenge.substring("Basic Realm=\"".length(), challenge.length() - 1);
        Map realms = (Map) _basicCredentials.get(host);
        if (realms == null)
            return null;
        BasicCredential cred = (BasicCredential) realms.get(realm);
        if (cred == null)
            return null;
        String encoded = cred.getUsername() + ":" + cred.getPassword();
        return Encoding.base64encode(encoded.getBytes());
    }

    /**
     * Gets the domain credentials.
     * 
     * @param host
     *            the host
     * 
     * @return the domain credentials
     */
    private String getDomainCredentials(String host) {
        DomainCredential cred = (DomainCredential) _domainCredentials.get(host);
        if (cred == null)
            return null;
        String encoded = cred.getDomain() + "\\" + cred.getUsername() + ":" + cred.getPassword();
        return Encoding.base64encode(encoded.getBytes());
    }

}
