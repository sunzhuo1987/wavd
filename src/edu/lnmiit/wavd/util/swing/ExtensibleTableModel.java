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
 * ExtensibleTableModel.java
 *
 * Created on September 24, 2004, 8:10 AM
 */

package edu.lnmiit.wavd.util.swing;

import javax.swing.table.AbstractTableModel;

import java.util.List;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class ExtensibleTableModel.
 */
public abstract class ExtensibleTableModel extends AbstractTableModel {
    
    /** The _columns. */
    private List _columns = new ArrayList();
    
    /** The _column listener. */
    private ColumnDataListener _columnListener;
    
    /**
     * Instantiates a new extensible table model.
     */
    public ExtensibleTableModel() {
        _columnListener = new ColumnDataListener() {
            public void dataChanged(ColumnDataEvent cde) {
                Object column = cde.getSource();
                int col = _columns.indexOf(column);
                if (col < 0) return;
                Object key = cde.getKey();
                if (key == null) {
                    fireTableStructureChanged();
                } else {
                    int row = indexOfKey(key);
                    if (row > -1) {
                        fireTableCellUpdated(row, col);
                    }
                }
            }
        };
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public abstract int getRowCount();
    
    /**
     * Gets the key at.
     * 
     * @param row the row
     * 
     * @return the key at
     */
    public abstract Object getKeyAt(int row);
    
    /**
     * Index of key.
     * 
     * @param key the key
     * 
     * @return the int
     */
    public abstract int indexOfKey(Object key);
    
    /**
     * Adds the column.
     * 
     * @param column the column
     */
    public void addColumn(ColumnDataModel column) {
        _columns.add(column);
        column.addColumnDataListener(_columnListener);
        fireTableStructureChanged();
    }
    
    /**
     * Removes the column.
     * 
     * @param column the column
     */
    public void removeColumn(ColumnDataModel column) {
        int index = _columns.indexOf(column);
        if (index < 0) return;
        column.removeColumnDataListener(_columnListener);
        _columns.remove(index);
        fireTableStructureChanged();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return _columns.size();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        return ((ColumnDataModel) _columns.get(column)).getColumnName();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
        return ((ColumnDataModel) _columns.get(column)).getColumnClass();
    }
    
    /**
     * Gets the value at.
     * 
     * @param key the key
     * @param column the column
     * 
     * @return the value at
     */
    protected Object getValueAt(Object key, int column) {
        return ((ColumnDataModel) _columns.get(column)).getValue(key);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column) {
        Object key = getKeyAt(row);
        return getValueAt(key, column);
    }
    
}
