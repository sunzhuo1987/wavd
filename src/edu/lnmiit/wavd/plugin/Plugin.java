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
 * WebScarabPlugin.java
 *
 * Created on July 10, 2003, 12:21 PM
 */

package edu.lnmiit.wavd.plugin;

import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.model.StoreException;

// TODO: Auto-generated Javadoc
/**
 * The Interface Plugin.
 */
public interface Plugin extends Runnable {
    
    /**
     * Gets the plugin name.
     * 
     * @return the plugin name
     */    
    String getPluginName();
    
    /**
     * Sets the session.
     * 
     * @param type the type
     * @param store the store
     * @param session the session
     * 
     * @throws StoreException the store exception
     */    
    void setSession(String type, Object store, String session) throws StoreException;
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    void run();
    
    /**
     * Checks if is running.
     * 
     * @return true, if is running
     */
    boolean isRunning();
    
    /**
     * Checks if is busy.
     * 
     * @return true, if is busy
     */
    boolean isBusy();
    
    /**
     * Gets the status.
     * 
     * @return the status
     */
    String getStatus();
    
    /**
     * Stop.
     * 
     * @return true, if successful
     */
    boolean stop();
    
    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    boolean isModified();
    
    /**
     * Flush.
     * 
     * @throws StoreException the store exception
     */    
    void flush() throws StoreException;
    
    /**
     * Analyse.
     * 
     * @param id the id
     * @param request the request
     * @param response the response
     * @param origin the origin
     */
    void analyse(ConversationID id, Request request, Response response, String origin);
    
    /**
     * Gets the scripting hooks.
     * 
     * @return the scripting hooks
     */
    Hook[] getScriptingHooks();
    
    /**
     * Gets the scriptable object.
     * 
     * @return the scriptable object
     */
    Object getScriptableObject();
    
}
