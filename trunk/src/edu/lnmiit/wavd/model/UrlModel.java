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

// TODO: Auto-generated Javadoc
/**
 * The Interface UrlModel.
 */
public interface UrlModel {

    /**
     * Gets the child count.
     * 
     * @param parent the parent
     * 
     * @return the child count
     */
    int getChildCount(HttpUrl parent);
    
    /**
     * Gets the child at.
     * 
     * @param parent the parent
     * @param index the index
     * 
     * @return the child at
     */
    HttpUrl getChildAt(HttpUrl parent, int index);
    
    /**
     * Gets the index of.
     * 
     * @param url the url
     * 
     * @return the index of
     */
    int getIndexOf(HttpUrl url);
    
    /**
     * Read lock.
     * 
     * @return the sync
     */
    Sync readLock();
    
    /**
     * Adds the url listener.
     * 
     * @param listener the listener
     */
    void addUrlListener(UrlListener listener);
    
    /**
     * Removes the url listener.
     * 
     * @param listener the listener
     */
    void removeUrlListener(UrlListener listener);
    
}
