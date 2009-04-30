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

import java.util.ArrayList;
import java.util.List;

import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.UrlModel;
import edu.lnmiit.wavd.util.swing.ColumnDataEvent;
import edu.lnmiit.wavd.util.swing.ColumnDataListener;
import edu.lnmiit.wavd.util.swing.ColumnDataModel;
import edu.lnmiit.wavd.util.swing.treetable.TreeTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class UrlTreeTableModelAdapter.
 */
public class UrlTreeTableModelAdapter extends UrlTreeModelAdapter implements TreeTableModel {

    /** The _columns. */
    private List _columns = new ArrayList();

    /** The _column listener. */
    private ColumnDataListener _columnListener;

    /**
     * Instantiates a new url tree table model adapter.
     * 
     * @param model
     *            the model
     */
    public UrlTreeTableModelAdapter(UrlModel model) {
        super(model);
        createListener();
    }

    /**
     * Creates the listener.
     */
    private void createListener() {
        _columnListener = new ColumnDataListener() {
            public void dataChanged(ColumnDataEvent cde) {
                Object column = cde.getSource();
                int col = _columns.indexOf(column);
                if (col < 0)
                    return;
                Object key = cde.getKey();
                if (key == null) {
                    fireStructureChanged();
                } else {
                    HttpUrl url = (HttpUrl) key;
                    firePathChanged(urlTreePath(url));
                }
            }
        };
    }

    /**
     * Adds the column.
     * 
     * @param column
     *            the column
     */
    public void addColumn(ColumnDataModel column) {
        _columns.add(column);
        column.addColumnDataListener(_columnListener);
        fireStructureChanged();
    }

    /**
     * Removes the column.
     * 
     * @param column
     *            the column
     */
    public void removeColumn(ColumnDataModel column) {
        int index = _columns.indexOf(column);
        if (index < 0)
            return;
        column.removeColumnDataListener(_columnListener);
        _columns.remove(index);
        fireStructureChanged();
    }

    //
    // The TreeTableNode interface.
    //

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getColumnCount()
     */
    public int getColumnCount() {
        return _columns.size() + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        if (column == 0)
            return "Url";
        return ((ColumnDataModel) _columns.get(column - 1)).getColumnName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
        if (column == 0)
            return TreeTableModel.class;
        return ((ColumnDataModel) _columns.get(column - 1)).getColumnClass();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getValueAt(java.lang
     * .Object, int)
     */
    public Object getValueAt(Object node, int column) {
        if (!(node instanceof HttpUrl))
            return null;
        HttpUrl url = (HttpUrl) node;
        if (column == 0)
            return url;
        return ((ColumnDataModel) _columns.get(column - 1)).getValue(url);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#isCellEditable(java
     * .lang.Object, int)
     */
    public boolean isCellEditable(Object node, int column) {
        return getColumnClass(column) == TreeTableModel.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#setValueAt(java.lang
     * .Object, java.lang.Object, int)
     */
    public void setValueAt(Object aValue, Object node, int column) {
    }

}
