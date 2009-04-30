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
 * HexEditor.java
 *
 * Created on November 4, 2003, 8:23 AM
 */

package edu.lnmiit.wavd.ui.swing.editors;

import javax.swing.table.DefaultTableCellRenderer;

import edu.lnmiit.wavd.model.Preferences;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Font;

import javax.swing.CellEditor;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.Event;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class HexPanel.
 */
public class HexPanel extends javax.swing.JPanel implements ByteArrayEditor {
    
    /** The _table model. */
    private HexTableModel _tableModel = null;
    
    /** The _editable. */
    private boolean _editable = false;
    
    /** The _columns. */
    private int _columns = 16;
    
    /** The _modified. */
    private boolean _modified = false;
    
    /** The _data. */
    private byte[] _data = null;
    
    /** The _original. */
    private byte[] _original = null;
    
    /**
     * Instantiates a new hex panel.
     */
    public HexPanel() {
        initComponents();
        setName("Hex");
        
        _tableModel = new HexTableModel(_columns);
        hexTable.setModel(_tableModel);
        hexTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        hexTable.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.putClientProperty("html.disable", Boolean.TRUE);
        hexTable.setDefaultRenderer(Object.class, renderer);
        TableColumnModel colModel = hexTable.getColumnModel();
        // FIXME : use FontMetrics to get the real width of the font
        for (int i=0; i<_columns+2; i++) {
            colModel.getColumn(i).setPreferredWidth(2*9);
            colModel.getColumn(i).setResizable(false);
        }
        colModel.getColumn(0).setPreferredWidth(8*9);
        colModel.getColumn(_columns+1).setPreferredWidth(_columns*9);
        InputMap im = getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK), "Save");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK), "Load");
        getActionMap().put("Save", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser jfc = new JFileChooser(Preferences.getPreference("WebScarab.DefaultDir"));
                jfc.setDialogTitle("Select a file to write the message content to");
                int returnVal = jfc.showSaveDialog(HexPanel.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileOutputStream fos = new FileOutputStream(jfc.getSelectedFile());
                        fos.write(_data);
                        fos.close();
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(HexPanel.this, "Error writing file: " + ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                Preferences.setPreference("WebScarab.DefaultDir", jfc.getCurrentDirectory().getAbsolutePath());
            }
        });
        getActionMap().put("Load", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                if (!_editable) return;
                JFileChooser jfc = new JFileChooser(Preferences.getPreference("WebScarab.DefaultDir"));
                jfc.setDialogTitle("Select a file to read the message content from");
                int returnVal = jfc.showOpenDialog(HexPanel.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileInputStream fis = new FileInputStream(jfc.getSelectedFile());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buff = new byte[2048];
                        int got;
                        while ((got = fis.read(buff))>0) {
                            baos.write(buff,0,got);
                        }
                        fis.close();
                        baos.close();
                        _data = baos.toByteArray();
                        _tableModel.fireTableDataChanged();
                        _modified = true;
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(HexPanel.this, "Error writing file: " + ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                Preferences.setPreference("WebScarab.DefaultDir", jfc.getCurrentDirectory().getAbsolutePath());
            }
        });
    }
    
    /**
     * Gets the content types.
     * 
     * @return the content types
     */
    public String[] getContentTypes() {
        return new String[] { ".*" };
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        hexTable.setBackground(editable ? Color.WHITE : Color.LIGHT_GRAY );
        // we could do things like make insert and delete buttons visible and invisible here
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setBytes(java.lang.String, byte[])
     */
    public void setBytes(String contentType, byte[] bytes) {
        _original = bytes;
        if (bytes == null) {
            _data = null;
        } else {
            _data = new byte[bytes.length];
            System.arraycopy(bytes, 0, _data, 0, bytes.length);
        }
        if (SwingUtilities.isEventDispatchThread()) {
            _tableModel.fireTableDataChanged();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    _tableModel.fireTableDataChanged();
                }
            });
        }
        _modified = false;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#isModified()
     */
    public boolean isModified() {
        if (hexTable.isEditing()) hexTable.getCellEditor().stopCellEditing();
        return _editable && _modified;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        if (_editable && isModified()) _original = _data;
        _modified = false;
        return _original;
    }
    
    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        hexTable = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(453, 20));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(22, 48));
        hexTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        hexTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(hexTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The hex table. */
    private javax.swing.JTable hexTable;
    
    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        javax.swing.JFrame top = new javax.swing.JFrame("Hex Editor");
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
        
        HexPanel hp = new HexPanel();
        top.getContentPane().add(hp);
        top.setBounds(100,100,600,400);
        try {
            hp.setBytes(null, new byte[] {
                0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f,
                0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f,
                0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f,
            });
            hp.setEditable(true);
            top.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * The Class HexTableModel.
     */
    private class HexTableModel extends AbstractTableModel {
        
        /** The _columns. */
        private int _columns = 8;
        
        /**
         * Instantiates a new hex table model.
         */
        public HexTableModel() {
        }
        
        /**
         * Instantiates a new hex table model.
         * 
         * @param columns the columns
         */
        public HexTableModel(int columns) {
            _columns = columns;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "Position";
            } else if (columnIndex-1 < _columns) {
                return Integer.toHexString(columnIndex-1).toUpperCase();
            } else {
                return "String";
            }
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return _columns + 2;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            if (_data == null || _data.length == 0) {
                return 0;
            }
            if (_data.length % _columns == 0) {
                return (int) (_data.length / _columns);
            } else {
                return (int) (_data.length / _columns) + 1;
            }
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return pad(Integer.toHexString(rowIndex * _columns).toUpperCase(), '0', 8);
            } else if (columnIndex-1 < _columns) {
                int position = rowIndex * _columns + columnIndex-1;
                if (position < _data.length) {
                    int i = _data[position];
                    if (i<0) { i = i + 256; }
                    return pad(Integer.toString(i, 16).toUpperCase(),'0',2);
                } else {
                    return "";
                }
            } else {
                int start = rowIndex * _columns;
                StringBuffer buff = new StringBuffer();
                for (int i=0; i < _columns; i++) {
                    int pos = start + i;
                    if (pos >= _data.length) { return buff.toString(); }
                    if (_data[pos] < 32 || _data[pos] > 126) {
                        buff.append(".");
                    } else {
                        buff.append((char) _data[pos]);
                    }
                }
                return buff.toString();
            }
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0 || columnIndex>_columns) {
                return false;
            }
            int position = rowIndex * _columns + columnIndex-1;
            if (position < _data.length) {
                return _editable;
            }
            return false;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
         */
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            int position = rowIndex * _columns + columnIndex-1;
            if (position >= _data.length) {
                System.out.println("Out of range");
                return;
            }
            if (aValue instanceof String) {
                try {
                    String s = (String) aValue;
                    _data[position] = new Integer(Integer.parseInt(s.trim(), 16)).byteValue();
                    fireTableCellUpdated(rowIndex, _columns + 1);
                    _modified = true;
                } catch (NumberFormatException nfe) {
                    System.out.println("Number format error : " + nfe);
                }
            } else {
                System.out.println("Value is a " + aValue.getClass().getName());
            }
        }
        
        /**
         * Pad.
         * 
         * @param initial the initial
         * @param padchar the padchar
         * @param length the length
         * 
         * @return the string
         */
        private String pad(String initial, char padchar, int length) {
            if (initial.length() >= length) {
                return initial;
            }
            StringBuffer buff = new StringBuffer(length);
            for (int i=0; i<length - initial.length(); i++) {
                buff.append(padchar);
            }
            buff.append(initial);
            return buff.toString();
        }
    }
    
}
