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
 * ListenerTableModel.java
 *
 * Created on August 31, 2003, 11:59 PM
 */

package edu.lnmiit.wavd.plugin.proxy.swing;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import edu.lnmiit.wavd.plugin.proxy.Proxy;

// TODO: Auto-generated Javadoc
/**
 * The Class ListenerTableModel.
 */
public class ListenerTableModel extends AbstractTableModel {

    /**
     * 
     */
    private static final long serialVersionUID = 5809666321442821821L;

    /** The _proxy. */
    private Proxy _proxy;

    /** The _listeners. */
    private ArrayList _listeners = new ArrayList();

    /** The column names. */
    protected String[] columnNames = { "Address", "Port", "Base URL", "Simulated network", "Primary" };

    /** The column class. */
    protected Class[] columnClass = { String.class, Integer.class, String.class, String.class, Boolean.class };

    /**
     * Instantiates a new listener table model.
     * 
     * @param proxy
     *            the proxy
     */
    public ListenerTableModel(Proxy proxy) {
        _proxy = proxy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        if (column < columnNames.length) {
            return columnNames[column];
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
        return columnClass[column];
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public synchronized int getColumnCount() {
        return columnNames.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public synchronized int getRowCount() {
        return _listeners.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public synchronized Object getValueAt(int row, int column) {
        if (row < 0 || row >= _listeners.size()) {
            System.err.println("Attempt to get row " + row + ", column " + column + " : row does not exist!");
            return null;
        }
        String key = (String) _listeners.get(row);
        if (column <= columnNames.length) {
            switch (column) {
            case 0:
                return _proxy.getAddress(key);
            case 1:
                return new Integer(_proxy.getPort(key));
            case 2:
                return _proxy.getBase(key);
            case 3:
                return _proxy.getSimulator(key);
            case 4:
                return new Boolean(_proxy.isPrimaryProxy(key));
            default:
                return null;
            }
        } else {
            System.err.println("Attempt to get row " + row + ", column " + column + " : column does not exist!");
            return null;
        }
    }

    /**
     * Gets the key.
     * 
     * @param index
     *            the index
     * 
     * @return the key
     */
    public String getKey(int index) {
        return (String) _listeners.get(index);
    }

    /**
     * Proxy removed.
     * 
     * @param key
     *            the key
     */
    public void proxyRemoved(String key) {
        int index = _listeners.indexOf(key);
        if (index > -1) {
            _listeners.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    /**
     * Proxy added.
     * 
     * @param key
     *            the key
     */
    public void proxyAdded(String key) {
        int index = _listeners.indexOf(key);
        if (index == -1) {
            _listeners.add(key);
            fireTableRowsInserted(_listeners.size(), _listeners.size());
        }
    }

}
