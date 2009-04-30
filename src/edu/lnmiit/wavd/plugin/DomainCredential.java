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
 * The Class DomainCredential.
 */
public class DomainCredential {
    
    /** The _host. */
    private String _host;
    
    /** The _domain. */
    private String _domain;
    
    /** The _username. */
    private String _username;
    
    /** The _password. */
    private String _password;
    
    /**
     * Instantiates a new domain credential.
     * 
     * @param host the host
     * @param domain the domain
     * @param username the username
     * @param password the password
     */
    public DomainCredential(String host, String domain, String username, String password) {
        _host = host;
        _domain = domain;
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
     * Gets the domain.
     * 
     * @return the domain
     */
    public String getDomain() {
        return _domain;
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
