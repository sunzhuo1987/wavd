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

import java.util.EventObject;

// TODO: Auto-generated Javadoc
/**
 * The Class FrameworkEvent.
 */
public class FrameworkEvent extends EventObject {
    
    /** The _id. */
    private ConversationID _id = null;
    
    /** The _url. */
    private HttpUrl _url = null;
    
    /** The _cookie. */
    private Cookie _cookie = null;
    
    /** The _property. */
    private String _property = null;
    
    /**
     * Instantiates a new framework event.
     * 
     * @param source the source
     * @param id the id
     * @param property the property
     */
    public FrameworkEvent(Object source, ConversationID id, String property) {
        super(source);
        _id = id;
        _property = property;
    }
    
    /**
     * Instantiates a new framework event.
     * 
     * @param source the source
     * @param url the url
     * @param property the property
     */
    public FrameworkEvent(Object source, HttpUrl url, String property) {
        super(source);
        _url = url;
        _property = property;
    }
    
    /**
     * Instantiates a new framework event.
     * 
     * @param source the source
     * @param cookie the cookie
     */
    public FrameworkEvent(Object source, Cookie cookie) {
        super(source);
        _cookie = cookie;
    }
    
    /**
     * Gets the conversation id.
     * 
     * @return the conversation id
     */
    public ConversationID getConversationID() {
        return _id;
    }
    
    /**
     * Gets the url.
     * 
     * @return the url
     */
    public HttpUrl getUrl() {
        return _url;
    }
    
    /**
     * Gets the cookie.
     * 
     * @return the cookie
     */
    public Cookie getCookie() {
        return _cookie;
    }
    
    /**
     * Gets the property name.
     * 
     * @return the property name
     */
    public String getPropertyName() {
        return _property;
    }
    
}
