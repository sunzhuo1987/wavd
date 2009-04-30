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
package edu.lnmiit.wavd.util.swing.treetable;

import java.util.EventObject;

import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractCellEditor.
 */
public class AbstractCellEditor implements CellEditor {

    /** The listener list. */
    protected EventListenerList listenerList = new EventListenerList();

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject e) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#stopCellEditing()
     */
    public boolean stopCellEditing() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#cancelCellEditing()
     */
    public void cancelCellEditing() {
    }

    /*
     * (non-Javadoc)
     * 
     * @seejavax.swing.CellEditor#addCellEditorListener(javax.swing.event.
     * CellEditorListener)
     */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /*
     * (non-Javadoc)
     * 
     * @seejavax.swing.CellEditor#removeCellEditorListener(javax.swing.event.
     * CellEditorListener)
     */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /*
     * Notify all listeners that have registered interest for notification on
     * this event type.
     * 
     * @see EventListenerList
     */
    /**
     * Fire editing stopped.
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                ((CellEditorListener) listeners[i + 1]).editingStopped(new ChangeEvent(this));
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for notification on
     * this event type.
     * 
     * @see EventListenerList
     */
    /**
     * Fire editing canceled.
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                ((CellEditorListener) listeners[i + 1]).editingCanceled(new ChangeEvent(this));
            }
        }
    }
}
