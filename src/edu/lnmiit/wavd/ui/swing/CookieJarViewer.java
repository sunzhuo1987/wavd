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
 * CookieJarViewer.java
 *
 * Created on September 30, 2003, 10:08 PM
 */

package edu.lnmiit.wavd.ui.swing;



import edu.lnmiit.wavd.model.Cookie;
import edu.lnmiit.wavd.model.FrameworkEvent;
import edu.lnmiit.wavd.model.FrameworkListener;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.util.swing.TableSorter;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.Color;

import java.util.Date;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class CookieJarViewer.
 */
public class CookieJarViewer extends javax.swing.JFrame {
    
    /** The _model. */
    private FrameworkModel _model;
    
    /** The _cookie table model. */
    private CookieTableModel _cookieTableModel;
    
    /** The _detail table model. */
    private HistoricalCookieTableModel _detailTableModel;
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /** The _key. */
    private String _key = null;
    
    /**
     * Instantiates a new cookie jar viewer.
     * 
     * @param model the model
     */
    public CookieJarViewer(FrameworkModel model) {
        _model = model;
        initComponents();
        _cookieTableModel = new CookieTableModel(_model);
        cookieTable.setModel(new TableSorter(_cookieTableModel, cookieTable.getTableHeader()));
        _detailTableModel = new HistoricalCookieTableModel(_model);
        cookieDetailTable.setModel(new TableSorter(_detailTableModel, cookieDetailTable.getTableHeader()));
        cookieTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                if (evt.getValueIsAdjusting()) return;
                int row = cookieTable.getSelectedRow();
                if (row>-1) {
                    _key = _cookieTableModel.getKeyAt(row);
                } else {
                    _key = null;
                }
                _detailTableModel.setKey(_key);
            }
        });
        addDialog.pack();
    }
    
    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        addDialog = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        domainTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        valueTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cookieTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cookieDetailTable = new javax.swing.JTable();
        closeButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        addDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        addDialog.setTitle("Add Cookie");
        addDialog.setLocationRelativeTo(null);
        addDialog.setModal(true);
        addDialog.setName("Add Cookie");
        addDialog.setResizable(false);
        jLabel6.setText("Domain : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        addDialog.getContentPane().add(jLabel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        addDialog.getContentPane().add(domainTextField, gridBagConstraints);

        jLabel4.setText("Path : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        addDialog.getContentPane().add(jLabel4, gridBagConstraints);

        pathTextField.setMinimumSize(new java.awt.Dimension(200, 19));
        pathTextField.setPreferredSize(new java.awt.Dimension(400, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        addDialog.getContentPane().add(pathTextField, gridBagConstraints);

        jLabel5.setText("Name : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        addDialog.getContentPane().add(jLabel5, gridBagConstraints);

        nameTextField.setMinimumSize(new java.awt.Dimension(200, 19));
        nameTextField.setPreferredSize(new java.awt.Dimension(400, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        addDialog.getContentPane().add(nameTextField, gridBagConstraints);

        jLabel3.setText("Value : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        addDialog.getContentPane().add(jLabel3, gridBagConstraints);

        valueTextField.setMinimumSize(new java.awt.Dimension(200, 19));
        valueTextField.setPreferredSize(new java.awt.Dimension(400, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        addDialog.getContentPane().add(valueTextField, gridBagConstraints);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jPanel2.add(okButton);

        cancelButton.setText("Cancel");
        jPanel2.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        addDialog.getContentPane().add(jPanel2, gridBagConstraints);

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("WebScarab Cookies");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jLabel1.setText("Cookies");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel1, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 200));
        cookieTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(cookieTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jLabel2.setText("Previous values");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel2, gridBagConstraints);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 200));
        cookieDetailTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(cookieDetailTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jScrollPane2, gridBagConstraints);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(closeButton, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(addButton, gridBagConstraints);

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
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(deleteButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-600)/2, (screenSize.height-400)/2, 600, 400);
    }
    // </editor-fold>//GEN-END:initComponents

    /**
     * Ok button action performed.
     * 
     * @param evt the evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        String domain = domainTextField.getText();
        domainTextField.setBackground("".equals(domain)?Color.RED:Color.WHITE);
        String path = pathTextField.getText();
        if ("".equals(path)) path = "/";
        String name = nameTextField.getText();
        nameTextField.setBackground("".equals(name)?Color.RED:Color.WHITE);
        String value = valueTextField.getText();
        if ("".equals(domain) || "".equals(name)) return;
        Cookie cookie = new Cookie(new Date(), name + "=" + value + "; domain=" + domain + "; path="+path);
        _logger.info("Cookie is " + cookie);
        _model.addCookie(cookie);
        addDialog.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed
    
    /**
     * Delete button action performed.
     * 
     * @param evt the evt
     */
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int row = cookieDetailTable.getSelectedRow();
        if (row < cookieDetailTable.getRowCount() && row > -1) {
            Cookie cookie = _model.getCookieAt(_key, row);
            _model.removeCookie(cookie);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed
    
    /**
     * Adds the button action performed.
     * 
     * @param evt the evt
     */
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        addDialog.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed
    
    /**
     * Close button action performed.
     * 
     * @param evt the evt
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed
    
    /**
     * Exit form.
     * 
     * @param evt the evt
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        setVisible(false);
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The add button. */
    private javax.swing.JButton addButton;
    
    /** The add dialog. */
    private javax.swing.JDialog addDialog;
    
    /** The cancel button. */
    private javax.swing.JButton cancelButton;
    
    /** The close button. */
    private javax.swing.JButton closeButton;
    
    /** The cookie detail table. */
    private javax.swing.JTable cookieDetailTable;
    
    /** The cookie table. */
    private javax.swing.JTable cookieTable;
    
    /** The delete button. */
    private javax.swing.JButton deleteButton;
    
    /** The domain text field. */
    private javax.swing.JTextField domainTextField;
    
    /** The j label1. */
    private javax.swing.JLabel jLabel1;
    
    /** The j label2. */
    private javax.swing.JLabel jLabel2;
    
    /** The j label3. */
    private javax.swing.JLabel jLabel3;
    
    /** The j label4. */
    private javax.swing.JLabel jLabel4;
    
    /** The j label5. */
    private javax.swing.JLabel jLabel5;
    
    /** The j label6. */
    private javax.swing.JLabel jLabel6;
    
    /** The j panel1. */
    private javax.swing.JPanel jPanel1;
    
    /** The j panel2. */
    private javax.swing.JPanel jPanel2;
    
    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;
    
    /** The j scroll pane2. */
    private javax.swing.JScrollPane jScrollPane2;
    
    /** The name text field. */
    private javax.swing.JTextField nameTextField;
    
    /** The ok button. */
    private javax.swing.JButton okButton;
    
    /** The path text field. */
    private javax.swing.JTextField pathTextField;
    
    /** The value text field. */
    private javax.swing.JTextField valueTextField;
    // End of variables declaration//GEN-END:variables
    
    /**
     * The Class CookieTableModel.
     */
    private class CookieTableModel extends AbstractTableModel {
        
        /** The _model. */
        private FrameworkModel _model;
        
        /** The _listener. */
        private FrameworkListener _listener = new FrameworkListener() {
            public void cookieAdded(FrameworkEvent evt) {
                Cookie cookie = evt.getCookie();
                int row = _model.getIndexOfCookie(cookie);
                int count = _model.getCookieCount(cookie.getKey());
                if (count == 1) {
                    fireTableRowsInserted(row, row);
                } else {
                    fireTableRowsUpdated(row, row);
                }
            }
            
            public void cookieRemoved(FrameworkEvent evt) {
                Cookie cookie = evt.getCookie();
                int count = _model.getCookieCount(cookie.getKey());
                if (count == 0) {
                    fireTableDataChanged();
                } else {
                    int row = _model.getIndexOfCookie(cookie);
                    fireTableRowsUpdated(row, row);
                }
            }
            
            public void cookiesChanged() {
                fireTableDataChanged();
            }
            
            public void conversationPropertyChanged(FrameworkEvent evt) {}
            
            public void urlPropertyChanged(FrameworkEvent evt) {}
            
        };
        
        /** The _column names. */
        private String[] _columnNames = new String[] { "Domain", "Path", "Name", "Date", "Value", "Secure", "Max age", "Comment" };
        
        /** The _column class. */
        private Class[] _columnClass = new Class[] { String.class, String.class, String.class, Date.class, String.class, Boolean.class, String.class, String.class };
        
        /**
         * Instantiates a new cookie table model.
         * 
         * @param model the model
         */
        public CookieTableModel(FrameworkModel model) {
            this._model = model;
            this._model.addModelListener(_listener);
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return _columnNames.length;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            if (this._model == null) return 0;
            return this._model.getCookieCount();
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            String key = this._model.getCookieAt(rowIndex);
            Cookie cookie = this._model.getCurrentCookie(key);
            switch (columnIndex) {
                case 0: return cookie.getDomain();
                case 1: return cookie.getPath();
                case 2: return cookie.getName();
                case 3: return cookie.getDate();
                case 4: return cookie.getValue();
                case 5: return Boolean.valueOf(cookie.getSecure());
                case 6: return cookie.getMaxAge();
                case 7: return cookie.getComment();
            }
            return null;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int columnIndex) {
            return _columnNames[columnIndex];
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
         */
        public Class getColumnClass(int columnIndex) {
            return _columnClass[columnIndex];
        }
        
        /**
         * Gets the key at.
         * 
         * @param row the row
         * 
         * @return the key at
         */
        public String getKeyAt(int row) {
            return this._model.getCookieAt(row);
        }
        
    }
    
    /**
     * The Class HistoricalCookieTableModel.
     */
    private class HistoricalCookieTableModel extends AbstractTableModel {
        
        /** The _model. */
        private FrameworkModel _model = null;
        
        /** The _key. */
        private String _key = null;
        
        /** The _listener. */
        private FrameworkListener _listener = new FrameworkListener() {
            public void cookieAdded(FrameworkEvent evt) {
                Cookie cookie = evt.getCookie();
                if (_key == null || ! _key.equals(cookie.getKey())) return;
                int row = _model.getIndexOfCookie(_key, cookie);
                fireTableRowsInserted(row, row);
            }
            
            public void cookieRemoved(FrameworkEvent evt) {
                Cookie cookie = evt.getCookie();
                if (_key == null || ! _key.equals(cookie.getKey())) return;
                fireTableDataChanged();
            }
            
            public void cookiesChanged() {
                fireTableDataChanged();
            }
            
            public void conversationPropertyChanged(FrameworkEvent evt) {}
            
            public void urlPropertyChanged(FrameworkEvent evt) {}
            
        };
        
        /** The _column names. */
        private String[] _columnNames = new String[] { "Date", "Value", "Secure", "Max age", "Comment" };
        
        /** The _column class. */
        private Class[] _columnClass = new Class[] { Date.class, String.class, Boolean.class, String.class, String.class };
        
        /**
         * Instantiates a new historical cookie table model.
         * 
         * @param model the model
         */
        public HistoricalCookieTableModel(FrameworkModel model) {
            this._model = model;
            this._model.addModelListener(_listener);
        }
        
        /**
         * Sets the key.
         * 
         * @param key the new key
         */
        public void setKey(String key) {
            _key = key;
            fireTableDataChanged();
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            if (this._model == null) return 0;
            if (_key == null) return 0;
            return this._model.getCookieCount(_key);
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int column) {
            Cookie cookie = this._model.getCookieAt(_key, row);
            switch (column) {
                case 0: return cookie.getDate();
                case 1: return cookie.getValue();
                case 2: return Boolean.valueOf(cookie.getSecure());
                case 3: return cookie.getMaxAge();
                case 4: return cookie.getComment();
            }
            return null;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return _columnNames.length;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int columnIndex) {
            return _columnNames[columnIndex];
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
         */
        public Class getColumnClass(int columnIndex) {
            return _columnClass[columnIndex];
        }
        
    }
}
