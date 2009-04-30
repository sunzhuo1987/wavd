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
 * ColumnDataListener.java
 *
 * Created on 06 December 2004, 04:54
 */

package edu.lnmiit.wavd.util.swing;

import java.util.EventListener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving columnData events.
 * The class that is interested in processing a columnData
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addColumnDataListener<code> method. When
 * the columnData event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ColumnDataEvent
 */
public interface ColumnDataListener extends EventListener {
    
    /**
     * Data changed.
     * 
     * @param cde the cde
     */
    public void dataChanged(ColumnDataEvent cde);
    
}
