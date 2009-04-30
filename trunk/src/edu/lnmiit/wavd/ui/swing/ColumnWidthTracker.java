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

package edu.lnmiit.wavd.ui.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import edu.lnmiit.wavd.model.Preferences;

// TODO: Auto-generated Javadoc
/**
 * The Class ColumnWidthTracker.
 */
public class ColumnWidthTracker implements PropertyChangeListener, TableColumnModelListener {
    
    /** The _trackers. */
    private static HashMap _trackers = new HashMap();
    
    /** The _key. */
    private String _key;
    
    /** The _tracked. */
    private List _tracked = new LinkedList();
    
    /**
     * Gets the tracker.
     * 
     * @param key the key
     * 
     * @return the tracker
     */
    public static ColumnWidthTracker getTracker(String key) {
        ColumnWidthTracker tracker = (ColumnWidthTracker) _trackers.get(key);
        if (tracker == null) {
            tracker = new ColumnWidthTracker(key);
            _trackers.put(key, tracker);
        }
        return tracker;
    }
    
    /**
     * Instantiates a new column width tracker.
     * 
     * @param key the key
     */
    protected ColumnWidthTracker(String key) {
        _key = key;
    }
    
    /**
     * Adds the table.
     * 
     * @param table the table
     */
    public void addTable(JTable table) {
        TableColumnModel tcm = table.getColumnModel();
        for (int i=0; i<tcm.getColumnCount(); i++) {
            TableColumn tc = tcm.getColumn(i);
            addColumn(tc);
        }
        tcm.addColumnModelListener(this);
        _tracked.add(tcm);
    }
    
    /**
     * Removes the table.
     * 
     * @param table the table
     */
    public void removeTable(JTable table) {
        TableColumnModel tcm = table.getColumnModel();
        for (int i=0; i<tcm.getColumnCount(); i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.removePropertyChangeListener(this);
        }
        tcm.removeColumnModelListener(this);
        _tracked.remove(tcm);
    }
    
    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if (property == null || !"preferredWidth".equals(property)) return;
        if (! (evt.getSource() instanceof TableColumn)) return;
        TableColumn tc = (TableColumn) evt.getSource();
        String name = String.valueOf(tc.getHeaderValue());
        int width = tc.getPreferredWidth();
        Preferences.setPreference(_key + "." + name + ".width", String.valueOf(width));
        Iterator it = _tracked.iterator();
        while (it.hasNext()) {
            TableColumnModel tcm = (TableColumnModel) it.next();
            for (int i=0; i<tcm.getColumnCount(); i++) {
                TableColumn tc2 = tcm.getColumn(i);
                String name2 = String.valueOf(tc2.getHeaderValue());
                if (name.equals(name2) && tc != tc2 && tc2.getPreferredWidth() != width)
                    tc2.setPreferredWidth(width);
            }
        }
    }
    
    /**
     * Adds the column.
     * 
     * @param tc the tc
     */
    private void addColumn(TableColumn tc) {
        String name = String.valueOf(tc.getHeaderValue());
        String preferredWidth = Preferences.getPreference(_key + "." + name + ".width");
        if (preferredWidth != null) {
            try {
                int width = Integer.parseInt(preferredWidth);
                tc.setPreferredWidth(width);
            } catch (NumberFormatException nfe) {}
        }
        tc.addPropertyChangeListener(this);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.event.TableColumnModelListener#columnAdded(javax.swing.event.TableColumnModelEvent)
     */
    public void columnAdded(TableColumnModelEvent e) {
        int index = e.getToIndex();
        TableColumnModel tcm = (TableColumnModel) e.getSource();
        TableColumn tc = tcm.getColumn(index);
        addColumn(tc);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.event.TableColumnModelListener#columnMarginChanged(javax.swing.event.ChangeEvent)
     */
    public void columnMarginChanged(javax.swing.event.ChangeEvent e) {}
    
    /* (non-Javadoc)
     * @see javax.swing.event.TableColumnModelListener#columnMoved(javax.swing.event.TableColumnModelEvent)
     */
    public void columnMoved(TableColumnModelEvent e) {}
    
    /* (non-Javadoc)
     * @see javax.swing.event.TableColumnModelListener#columnRemoved(javax.swing.event.TableColumnModelEvent)
     */
    public void columnRemoved(TableColumnModelEvent e) {
        int index = e.getToIndex();
        TableColumnModel tcm = (TableColumnModel) e.getSource();
        if (index >= tcm.getColumnCount()) return;
        TableColumn tc = tcm.getColumn(index);
        tc.removePropertyChangeListener(this);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.event.TableColumnModelListener#columnSelectionChanged(javax.swing.event.ListSelectionEvent)
     */
    public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {}
    
}
