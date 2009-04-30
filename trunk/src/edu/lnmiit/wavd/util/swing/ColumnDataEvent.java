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
 * ColumnDataEvent.java
 *
 * Created on 06 December 2004, 05:23
 */

package edu.lnmiit.wavd.util.swing;

import java.util.EventObject;

// TODO: Auto-generated Javadoc
/**
 * The Class ColumnDataEvent.
 */
public class ColumnDataEvent extends EventObject {

    /**
     * 
     */
    private static final long serialVersionUID = -8954100946557053876L;
    /** The _key. */
    private Object _key;

    /**
     * Instantiates a new column data event.
     * 
     * @param source
     *            the source
     * @param key
     *            the key
     */
    public ColumnDataEvent(Object source, Object key) {
        super(source);
        _key = key;
    }

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public Object getKey() {
        return _key;
    }

}
