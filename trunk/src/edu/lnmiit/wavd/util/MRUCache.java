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
 * CacheMap.java
 *
 * Created on July 15, 2004, 2:59 PM
 */

package edu.lnmiit.wavd.util;

import java.util.LinkedHashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class MRUCache.
 */
public class MRUCache extends LinkedHashMap {

    /**
     * 
     */
    private static final long serialVersionUID = 6078104689438659004L;
    /** The _max size. */
    private int _maxSize;

    /**
     * Instantiates a new mRU cache.
     * 
     * @param maxSize
     *            the max size
     */
    public MRUCache(int maxSize) {
        this(16, maxSize, 0.75f);
    }

    /**
     * Instantiates a new mRU cache.
     * 
     * @param initialCapacity
     *            the initial capacity
     * @param maxSize
     *            the max size
     */
    public MRUCache(int initialCapacity, int maxSize) {
        this(initialCapacity, maxSize, 0.75f);
    }

    /**
     * Instantiates a new mRU cache.
     * 
     * @param initialCapacity
     *            the initial capacity
     * @param maxSize
     *            the max size
     * @param loadFactor
     *            the load factor
     */
    public MRUCache(int initialCapacity, int maxSize, float loadFactor) {
        super(initialCapacity, loadFactor, true);
        _maxSize = maxSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
     */
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > _maxSize;
    }

}
