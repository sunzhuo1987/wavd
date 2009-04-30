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
 * ProxyUI.java
 *
 * Created on July 20, 2004, 4:40 PM
 */

package edu.lnmiit.wavd.plugin.proxy;



import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.plugin.PluginUI;

// TODO: Auto-generated Javadoc
/**
 * The Interface ProxyUI.
 */
public interface ProxyUI extends PluginUI {
    
    /**
     * Proxy added.
     * 
     * @param key the key
     */
    void proxyAdded(String key);
    
    /**
     * Proxy started.
     * 
     * @param key the key
     */
    void proxyStarted(String key);
    
    /**
     * Proxy stopped.
     * 
     * @param key the key
     */
    void proxyStopped(String key);
    
    /**
     * Proxy removed.
     * 
     * @param key the key
     */
    void proxyRemoved(String key);
    
    /**
     * Requested.
     * 
     * @param id the id
     * @param method the method
     * @param url the url
     */
    void requested(ConversationID id, String method, HttpUrl url);
    
    /**
     * Received.
     * 
     * @param id the id
     * @param status the status
     */
    void received(ConversationID id, String status);
    
    /**
     * Aborted.
     * 
     * @param id the id
     * @param reason the reason
     */
    void aborted(ConversationID id, String reason);
    
}
