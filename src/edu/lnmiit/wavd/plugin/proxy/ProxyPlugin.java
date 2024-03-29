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
 * ProxyPlugin.java
 *
 * Created on July 10, 2003, 12:41 PM
 */

package edu.lnmiit.wavd.plugin.proxy;

import edu.lnmiit.wavd.httpclient.HTTPClient;
import edu.lnmiit.wavd.model.StoreException;

// TODO: Auto-generated Javadoc
/**
 * The Class ProxyPlugin.
 */
public abstract class ProxyPlugin {

    /**
     * Sets the session.
     * 
     * @param type
     *            the type
     * @param store
     *            the store
     * @param session
     *            the session
     */
    public void setSession(String type, Object store, String session) {
    }

    /**
     * Flush.
     * 
     * @throws StoreException
     *             the store exception
     */
    public void flush() throws StoreException {
    }

    /**
     * Gets the plugin name.
     * 
     * @return the plugin name
     */
    public abstract String getPluginName();

    /**
     * Gets the proxy plugin.
     * 
     * @param in
     *            the in
     * 
     * @return the proxy plugin
     */
    public abstract HTTPClient getProxyPlugin(HTTPClient in);

}
