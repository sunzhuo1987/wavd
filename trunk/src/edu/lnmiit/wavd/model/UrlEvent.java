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
 * The Class UrlEvent.
 */
public class UrlEvent extends EventObject {
    
    /** The _url. */
    private HttpUrl _url;
    
    /** The _position. */
    private int _position;
    
    /**
     * Instantiates a new url event.
     * 
     * @param source the source
     * @param url the url
     * @param position the position
     */
    public UrlEvent(Object source, HttpUrl url, int position) {
        super(source);
        _url = url;
        _position = position;
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
     * Gets the position.
     * 
     * @return the position
     */
    public int getPosition() {
        return _position;
    }
}
