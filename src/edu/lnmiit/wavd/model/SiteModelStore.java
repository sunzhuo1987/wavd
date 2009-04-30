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
 * SiteModelStore.java
 *
 * Created on August 23, 2003, 2:38 PM
 */

package edu.lnmiit.wavd.model;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiteModelStore.
 */
public interface SiteModelStore {
    
    /**
     * Adds the conversation.
     * 
     * @param id the id
     * @param when the when
     * @param request the request
     * @param response the response
     * 
     * @return the int
     */    
    int addConversation(ConversationID id, Date when, Request request, Response response);
    
    /**
     * Sets the conversation property.
     * 
     * @param id the id
     * @param property the property
     * @param value the value
     */    
    void setConversationProperty(ConversationID id, String property, String value);
    
    /**
     * Adds the conversation property.
     * 
     * @param id the id
     * @param property the property
     * @param value the value
     * 
     * @return true, if successful
     */    
    boolean addConversationProperty(ConversationID id, String property, String value);
    
    /**
     * Gets the conversation properties.
     * 
     * @param id the id
     * @param property the property
     * 
     * @return the conversation properties
     */    
    String[] getConversationProperties(ConversationID id, String property);
    
    /**
     * Gets the index of conversation.
     * 
     * @param url the url
     * @param id the id
     * 
     * @return the index of conversation
     */    
    int getIndexOfConversation(HttpUrl url, ConversationID id);
    
    /**
     * Gets the conversation count.
     * 
     * @param url the url
     * 
     * @return the conversation count
     */    
    int getConversationCount(HttpUrl url);
    
    /**
     * Gets the conversation at.
     * 
     * @param url the url
     * @param index the index
     * 
     * @return the conversation at
     */    
    ConversationID getConversationAt(HttpUrl url, int index);
    
    
    /**
     * Adds the url.
     * 
     * @param url the url
     */    
    void addUrl(HttpUrl url);
    
    /**
     * Checks if is known url.
     * 
     * @param url the url
     * 
     * @return true, if is known url
     */    
    boolean isKnownUrl(HttpUrl url);
    
    /**
     * Sets the url property.
     * 
     * @param url the url
     * @param property the property
     * @param value the value
     */
    void setUrlProperty(HttpUrl url, String property, String value);
    
    /**
     * Adds the url property.
     * 
     * @param url the url
     * @param property the property
     * @param value the value
     * 
     * @return true, if successful
     */
    boolean addUrlProperty(HttpUrl url, String property, String value);
    
    /**
     * Gets the url properties.
     * 
     * @param url the url
     * @param property the property
     * 
     * @return the url properties
     */
    String[] getUrlProperties(HttpUrl url, String property);
    
    /**
     * Gets the child count.
     * 
     * @param url the url
     * 
     * @return the child count
     */
    public int getChildCount(HttpUrl url);
    
    /**
     * Gets the child at.
     * 
     * @param url the url
     * @param index the index
     * 
     * @return the child at
     */
    public HttpUrl getChildAt(HttpUrl url, int index);
    
    /**
     * Gets the index of.
     * 
     * @param url the url
     * 
     * @return the index of
     */
    public int getIndexOf(HttpUrl url);
    
    /**
     * Sets the request.
     * 
     * @param id the id
     * @param request the request
     */    
    void setRequest(ConversationID id, Request request);
    
    /**
     * Gets the request.
     * 
     * @param id the id
     * 
     * @return the request
     */    
    Request getRequest(ConversationID id);
    
    /**
     * Sets the response.
     * 
     * @param id the id
     * @param response the response
     */    
    void setResponse(ConversationID id, Response response);
    
    /**
     * Gets the response.
     * 
     * @param id the id
     * 
     * @return the response
     */    
    Response getResponse(ConversationID id);
    
    
    /**
     * Gets the cookie count.
     * 
     * @return the cookie count
     */    
    int getCookieCount();
    
    /**
     * Gets the cookie count.
     * 
     * @param key the key
     * 
     * @return the cookie count
     */    
    int getCookieCount(String key);
    
    /**
     * Gets the cookie at.
     * 
     * @param index the index
     * 
     * @return the cookie at
     */    
    String getCookieAt(int index);
    
    /**
     * Gets the cookie at.
     * 
     * @param key the key
     * @param index the index
     * 
     * @return the cookie at
     */    
    Cookie getCookieAt(String key, int index);
    
    /**
     * Gets the current cookie.
     * 
     * @param key the key
     * 
     * @return the current cookie
     */    
    Cookie getCurrentCookie(String key);
    
    /**
     * Gets the index of cookie.
     * 
     * @param cookie the cookie
     * 
     * @return the index of cookie
     */    
    int getIndexOfCookie(Cookie cookie);
    
    /**
     * Gets the index of cookie.
     * 
     * @param key the key
     * @param cookie the cookie
     * 
     * @return the index of cookie
     */    
    int getIndexOfCookie(String key, Cookie cookie);
    
    /**
     * Adds the cookie.
     * 
     * @param cookie the cookie
     * 
     * @return true, if successful
     */    
    boolean addCookie(Cookie cookie);
    
    /**
     * Removes the cookie.
     * 
     * @param cookie the cookie
     * 
     * @return true, if successful
     */
    boolean removeCookie(Cookie cookie);
    
    /**
     * Flush.
     * 
     * @throws StoreException the store exception
     */    
    void flush() throws StoreException;
}
