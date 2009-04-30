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
 * ColumnDataModel.java
 *
 * Created on 06 December 2004, 04:47
 */

package edu.lnmiit.wavd.util.swing;

import javax.swing.event.EventListenerList;

// TODO: Auto-generated Javadoc
/**
 * The Class ColumnDataModel.
 */
public abstract class ColumnDataModel {

    /** The _listener list. */
    protected EventListenerList _listenerList = new EventListenerList();

    /**
     * Instantiates a new column data model.
     */
    protected ColumnDataModel() {
    }

    /**
     * Gets the column class.
     * 
     * @return the column class
     */
    public Class getColumnClass() {
        return Object.class;
    }

    /**
     * Gets the column name.
     * 
     * @return the column name
     */
    public abstract String getColumnName();

    /**
     * Gets the value.
     * 
     * @param key
     *            the key
     * 
     * @return the value
     */
    public abstract Object getValue(Object key);

    /**
     * Adds the column data listener.
     * 
     * @param l
     *            the l
     */
    public void addColumnDataListener(ColumnDataListener l) {
        _listenerList.add(ColumnDataListener.class, l);
    }

    /**
     * Removes the column data listener.
     * 
     * @param l
     *            the l
     */
    public void removeColumnDataListener(ColumnDataListener l) {
        _listenerList.remove(ColumnDataListener.class, l);
    }

    // Notify all listeners that have registered interest for
    // notification on this event type. The event instance
    // is lazily created using the parameters passed into
    // the fire method.

    /**
     * Fire value changed.
     * 
     * @param key
     *            the key
     */
    public void fireValueChanged(Object key) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        ColumnDataEvent columnEvent = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ColumnDataListener.class) {
                // Lazily create the event:
                if (columnEvent == null)
                    columnEvent = new ColumnDataEvent(this, key);
                ((ColumnDataListener) listeners[i + 1]).dataChanged(columnEvent);
            }
        }
    }

    /**
     * Fire values changed.
     */
    public void fireValuesChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        ColumnDataEvent columnEvent = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ColumnDataListener.class) {
                // Lazily create the event:
                if (columnEvent == null)
                    columnEvent = new ColumnDataEvent(this, null);
                ((ColumnDataListener) listeners[i + 1]).dataChanged(columnEvent);
            }
        }
    }

}
