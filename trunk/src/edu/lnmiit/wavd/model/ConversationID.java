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
 * ConversationID.java
 *
 * Created on July 13, 2004, 3:59 PM
 */

package edu.lnmiit.wavd.model;

import java.text.ParseException;

// TODO: Auto-generated Javadoc
/**
 * The Class ConversationID.
 */
public class ConversationID implements Comparable {
    
    /** The _lock. */
    private static Object _lock = new Object();
    
    /** The _next. */
    private static int _next = 1;
    
    /** The _id. */
    private int _id;
    
    /**
     * Instantiates a new conversation id.
     */
    public ConversationID() {
        synchronized(_lock) {
            _id = _next++;
        }
    }
    
    /**
     * Instantiates a new conversation id.
     * 
     * @param id the id
     */
    public ConversationID(int id) {
        synchronized (_lock) {
            _id = id;
            if (_id >= _next) {
                _next = _id + 1;
            } else if (_id <= 0) {
                throw new IllegalArgumentException("Cannot use a negative ConversationID");
            } 
        }        
    }
    
    /**
     * Instantiates a new conversation id.
     * 
     * @param id the id
     */    
    public ConversationID(String id) {
        this(Integer.parseInt(id.trim()));
    }
    
    /**
     * Reset.
     */    
    public static void reset() {
        synchronized(_lock) {
            _next = 1;
        }
    }
    
    /**
     * Gets the iD.
     * 
     * @return the iD
     */
    protected int getID() {
        return _id;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */    
    public String toString() {
        return Integer.toString(_id);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */    
    public boolean equals(Object o) {
        if (o == null || ! (o instanceof ConversationID)) return false;
        return _id == ((ConversationID)o).getID();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */    
    public int hashCode() {
        return _id;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(T)
     */    
    public int compareTo(Object o) {
        if (o instanceof ConversationID) {
            int thatid = ((ConversationID)o).getID();
            return _id - thatid;
        }
        return 1;
    }
    
}
