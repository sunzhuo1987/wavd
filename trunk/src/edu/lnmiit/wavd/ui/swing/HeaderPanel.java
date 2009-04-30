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

import javax.swing.table.AbstractTableModel;

import edu.lnmiit.wavd.model.NamedValue;

// TODO: Auto-generated Javadoc
/**
 * The Class HeaderPanel.
 */
public class HeaderPanel extends javax.swing.JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -8873120037246334162L;

    /** The Constant NO_HEADERS. */
    private final static NamedValue[] NO_HEADERS = new NamedValue[0];

    /** The Constant _cwt. */
    private final static ColumnWidthTracker _cwt = ColumnWidthTracker.getTracker("Header");

    /** The _editable. */
    private boolean _editable = false;

    /** The _modified. */
    private boolean _modified = false;

    /** The _htm. */
    private HeaderTableModel _htm;

    /** The _headers. */
    private List _headers = new ArrayList();

    /**
     * Instantiates a new header panel.
     */
    public HeaderPanel() {
        _htm = new HeaderTableModel();
        initComponents();

        _cwt.addTable(headerTable);
    }

    /**
     * Sets the editable.
     * 
     * @param editable
     *            the new editable
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        if (editable) {
            add(buttonPanel, java.awt.BorderLayout.EAST);
            headerTable.setBackground(new java.awt.Color(255, 255, 255));
        } else {
            remove(buttonPanel);
            headerTable.setBackground(new java.awt.Color(204, 204, 204));
        }
        // revalidate();
    }

    /**
     * Checks if is editable.
     * 
     * @return true, if is editable
     */
    public boolean isEditable() {
        return _editable;
    }

    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    public boolean isModified() {
        if (headerTable.isEditing()) {
            headerTable.getCellEditor().stopCellEditing();
        }
        return _modified;
    }

    /**
     * Sets the headers.
     * 
     * @param headers
     *            the new headers
     */
    public void setHeaders(NamedValue[] headers) {
        _headers.clear();
        if (headers != null && headers.length > 0) {
            for (int i = 0; i < headers.length; i++)
                _headers.add(headers[i]);
        }
        _modified = false;
        _htm.fireTableDataChanged();
    }

    /**
     * Gets the headers.
     * 
     * @return the headers
     */
    public NamedValue[] getHeaders() {
        _modified = false;
        return (NamedValue[]) _headers.toArray(NO_HEADERS);
    }

    /**
     * Insert row.
     * 
     * @param row
     *            the row
     */
    public void insertRow(int row) {
        _headers.add(row, new NamedValue("Header", "value"));
        _modified = true;
        _htm.fireTableRowsInserted(row, row);
    }

    /**
     * Removes the row.
     * 
     * @param row
     *            the row
     */
    public void removeRow(int row) {
        _headers.remove(row);
        _modified = true;
        _htm.fireTableRowsDeleted(row, row);
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonPanel = new javax.swing.JPanel();
        insertButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        headerTable = new javax.swing.JTable();

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        insertButton.setText("Insert");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        buttonPanel.add(insertButton, gridBagConstraints);

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        buttonPanel.add(deleteButton, gridBagConstraints);

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 50));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(453, 103));
        headerTable.setModel(_htm);
        headerTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(headerTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Delete button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_deleteButtonActionPerformed
        int rowIndex = headerTable.getSelectedRow();
        if (rowIndex > -1) {
            removeRow(rowIndex);
        }
    }// GEN-LAST:event_deleteButtonActionPerformed

    /**
     * Insert button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_insertButtonActionPerformed
        int rowIndex = headerTable.getSelectedRow();
        if (rowIndex > -1) {
            insertRow(rowIndex);
        } else {
            insertRow(_htm.getRowCount());
        }
    }// GEN-LAST:event_insertButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The button panel. */
    private javax.swing.JPanel buttonPanel;

    /** The delete button. */
    private javax.swing.JButton deleteButton;

    /** The header table. */
    private javax.swing.JTable headerTable;

    /** The insert button. */
    private javax.swing.JButton insertButton;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    // End of variables declaration//GEN-END:variables

    /**
     * The Class HeaderTableModel.
     */
    private class HeaderTableModel extends AbstractTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = -2608943354913701810L;
        /** The _names. */
        private String[] _names = new String[] { "Header", "Value" };

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int column) {
            return _names[column];
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 2;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return _headers.size();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int column) {
            if (row > _headers.size() - 1)
                return "ERROR";
            NamedValue nv = (NamedValue) _headers.get(row);
            if (column == 0)
                return nv.getName();
            return nv.getValue();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
         * int, int)
         */
        public void setValueAt(Object aValue, int row, int col) {
            if (_editable && aValue instanceof String) {
                NamedValue nv = (NamedValue) _headers.get(row);
                if (col == 0) {
                    _headers.set(row, new NamedValue((String) aValue, nv.getValue()));
                } else {
                    _headers.set(row, new NamedValue(nv.getName(), (String) aValue));
                }
                _modified = true;
                fireTableCellUpdated(row, col);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable(int row, int column) {
            return _editable;
        }

    }

}
