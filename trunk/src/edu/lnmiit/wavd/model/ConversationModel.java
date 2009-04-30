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

package edu.lnmiit.wavd.model;

import EDU.oswego.cs.dl.util.concurrent.Sync;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Interface ConversationModel.
 */
public interface ConversationModel {
    
    /**
     * Gets the conversation count.
     * 
     * @return the conversation count
     */
    int getConversationCount();
    
    /**
     * Gets the conversation at.
     * 
     * @param index the index
     * 
     * @return the conversation at
     */
    ConversationID getConversationAt(int index);
    
    /**
     * Gets the index of conversation.
     * 
     * @param id the id
     * 
     * @return the index of conversation
     */
    int getIndexOfConversation(ConversationID id);
    
    /**
     * Read lock.
     * 
     * @return the sync
     */
    Sync readLock();
    
    /**
     * Gets the conversation origin.
     * 
     * @param id the id
     * 
     * @return the conversation origin
     */
    String getConversationOrigin(ConversationID id);
    
    /**
     * Gets the conversation date.
     * 
     * @param id the id
     * 
     * @return the conversation date
     */
    Date getConversationDate(ConversationID id);
    
    /**
     * Gets the request method.
     * 
     * @param id the id
     * 
     * @return the request method
     */
    String getRequestMethod(ConversationID id);
    
    /**
     * Gets the request url.
     * 
     * @param id the id
     * 
     * @return the request url
     */
    HttpUrl getRequestUrl(ConversationID id);
    
    /**
     * Gets the response status.
     * 
     * @param id the id
     * 
     * @return the response status
     */
    String getResponseStatus(ConversationID id);
    
    /**
     * Gets the request.
     * 
     * @param id the id
     * 
     * @return the request
     */
    Request getRequest(ConversationID id);
    
    /**
     * Gets the response.
     * 
     * @param id the id
     * 
     * @return the response
     */
    Response getResponse(ConversationID id);
    
    /**
     * Adds the conversation listener.
     * 
     * @param listener the listener
     */
    void addConversationListener(ConversationListener listener);
    
    /**
     * Removes the conversation listener.
     * 
     * @param listener the listener
     */
    void removeConversationListener(ConversationListener listener);
    
}
