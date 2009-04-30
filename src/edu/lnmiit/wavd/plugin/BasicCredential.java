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

// TODO: Auto-generated Javadoc
/**
 * The Class BasicCredential.
 */
public class BasicCredential {

    /** The _host. */
    private String _host;

    /** The _realm. */
    private String _realm;

    /** The _username. */
    private String _username;

    /** The _password. */
    private String _password;

    /**
     * Instantiates a new basic credential.
     * 
     * @param host
     *            the host
     * @param realm
     *            the realm
     * @param username
     *            the username
     * @param password
     *            the password
     */
    public BasicCredential(String host, String realm, String username, String password) {
        _host = host;
        _realm = realm;
        _username = username;
        _password = password;
    }

    /**
     * Gets the host.
     * 
     * @return the host
     */
    public String getHost() {
        return _host;
    }

    /**
     * Gets the realm.
     * 
     * @return the realm
     */
    public String getRealm() {
        return _realm;
    }

    /**
     * Gets the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return _username;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return _password;
    }

}
