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
 * MessagePanel.java
 *
 * Created on November 6, 2003, 8:43 AM
 */

package edu.lnmiit.wavd.ui.swing.editors;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.ui.swing.ColumnWidthTracker;
import edu.lnmiit.wavd.util.Encoding;

// TODO: Auto-generated Javadoc
/**
 * The Class UrlEncodedPanel.
 */
public class UrlEncodedPanel extends JPanel implements ByteArrayEditor {

    /**
     * 
     */
    private static final long serialVersionUID = 5175424404792486432L;

    /** The Constant _cwt. */
    private final static ColumnWidthTracker _cwt = ColumnWidthTracker.getTracker("UrlEncoded");

    /** The _editable. */
    private boolean _editable = false;

    /** The _modified. */
    private boolean _modified = false;

    /** The _table model. */
    private NamedValueTableModel _tableModel;

    /** The _data. */
    private String _data = null;

    /** The _values. */
    private List _values = new ArrayList();

    /**
     * Instantiates a new url encoded panel.
     */
    public UrlEncodedPanel() {
        initComponents();
        setName("URLEncoded");
        _tableModel = new NamedValueTableModel();
        headerTable.setModel(_tableModel);
        setEditable(_editable);
        _cwt.addTable(headerTable);
    }

    /**
     * Gets the content types.
     * 
     * @return the content types
     */
    public String[] getContentTypes() {
        return new String[] { "application/x-www-form-urlencoded" };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setBytes(java.lang.String
     * , byte[])
     */
    public void setBytes(String contentType, byte[] bytes) {
        _values.clear();
        if (bytes == null) {
            _data = null;
        } else {
            try {
                _data = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            NamedValue[] values = NamedValue.splitNamedValues(_data, "&", "=");
            String name, value;
            for (int i = 0; i < values.length; i++) {
                name = Encoding.urlDecode(values[i].getName());
                value = Encoding.urlDecode(values[i].getValue());
                values[i] = new NamedValue(name, value);
                _values.add(values[i]);
            }
        }
        _tableModel.fireTableDataChanged();
        _modified = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        if (_editable && isModified()) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < _values.size(); i++) {
                NamedValue value = (NamedValue) _values.get(i);
                if (value.getName() == null || value.getName().equals(""))
                    continue;
                if (i > 0)
                    buff.append("&");
                buff.append(Encoding.urlEncode(value.getName())).append("=");
                if (value.getValue() != null)
                    buff.append(Encoding.urlEncode(value.getValue()));
            }
            _data = buff.toString();
        }
        if (_data == null) {
            return new byte[0];
        } else {
            try {
                return _data.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                return new byte[0];
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        buttonPanel.setVisible(_editable);
        Color color;
        if (_editable) {
            color = Color.WHITE;
        } else {
            color = new Color(204, 204, 204);
        }
        headerTable.setBackground(color);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#isModified()
     */
    public boolean isModified() {
        if (headerTable.isEditing()) {
            headerTable.getCellEditor().stopCellEditing();
        }
        return _editable && _modified;
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        headerTable = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        insertButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(402, 50));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 50));
        headerTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(headerTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(buttonPanel, gridBagConstraints);

    }

    // </editor-fold>//GEN-END:initComponents

    /**
     * Insert row.
     * 
     * @param row
     *            the row
     */
    public void insertRow(int row) {
        _values.add(row, new NamedValue("Variable", "value"));
        _modified = true;
        _tableModel.fireTableRowsInserted(row, row);
    }

    /**
     * Removes the row.
     * 
     * @param row
     *            the row
     */
    public void removeRow(int row) {
        _values.remove(row);
        _modified = true;
        _tableModel.fireTableRowsDeleted(row, row);
    }

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
            insertRow(_tableModel.getRowCount());
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
     * The Class NamedValueTableModel.
     */
    private class NamedValueTableModel extends AbstractTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = -3821657345074234841L;
        /** The _names. */
        private String[] _names = new String[] { "Variable", "Value" };

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
            return _values.size();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int column) {
            if (row > _values.size() - 1)
                return "ERROR";
            NamedValue nv = (NamedValue) _values.get(row);
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
                NamedValue nv = (NamedValue) _values.get(row);
                if (col == 0) {
                    _values.set(row, new NamedValue((String) aValue, nv.getValue()));
                } else {
                    _values.set(row, new NamedValue(nv.getName(), (String) aValue));
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
