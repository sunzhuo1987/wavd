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

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.table.AbstractTableModel;

import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.plugin.BasicCredential;
import edu.lnmiit.wavd.plugin.CredentialManager;
import edu.lnmiit.wavd.plugin.DomainCredential;

// TODO: Auto-generated Javadoc
/**
 * The Class CredentialManagerFrame.
 */
public class CredentialManagerFrame extends javax.swing.JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 4145587250305006300L;

    /** The _manager. */
    private CredentialManager _manager;

    /** The _btm. */
    private BasicTableModel _btm;

    /** The _dtm. */
    private DomainTableModel _dtm;

    /**
     * Instantiates a new credential manager frame.
     * 
     * @param manager
     *            the manager
     */
    public CredentialManagerFrame(CredentialManager manager) {
        initComponents();
        boolean prompt = Boolean.valueOf(Preferences.getPreference("WebScarab.promptForCredentials", "false"))
                .booleanValue();
        promptCheckBox.setSelected(prompt);
        addComponentListener(new Updater());
        _manager = manager;
        _btm = new BasicTableModel();
        _dtm = new DomainTableModel();
        basicTable.setModel(_btm);
        domainTable.setModel(_dtm);
    }

    /**
     * Update credentials.
     */
    private void updateCredentials() {
        _btm.fireTableDataChanged();
        _dtm.fireTableDataChanged();
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        basicTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        domainTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        promptCheckBox = new javax.swing.JCheckBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("WebScarab Credentials");
        jLabel1.setText("Basic Credentials");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel1, gridBagConstraints);

        basicTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null },
                { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] {
                "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane1.setViewportView(basicTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jLabel2.setText("Domain Credentials");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel2, gridBagConstraints);

        domainTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null },
                { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] {
                "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane2.setViewportView(domainTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane2, gridBagConstraints);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jPanel1.add(addButton);

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jPanel1.add(deleteButton);

        clearButton.setText("Clear all");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        jPanel1.add(clearButton);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        jPanel1.add(closeButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        getContentPane().add(jPanel1, gridBagConstraints);

        promptCheckBox.setText("Ask when required");
        promptCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        promptCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        promptCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                promptCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(promptCheckBox, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 400) / 2, (screenSize.height - 300) / 2, 400, 300);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Prompt check box action performed.
     * 
     * @param evt
     *            the evt
     */
    private void promptCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_promptCheckBoxActionPerformed
        Preferences.setPreference("WebScarab.promptForCredentials", Boolean.toString(promptCheckBox.isSelected()));
    }// GEN-LAST:event_promptCheckBoxActionPerformed

    /**
     * Clear button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_clearButtonActionPerformed
        for (int i = _manager.getBasicCredentialCount() - 1; i >= 0; i--) {
            _manager.deleteBasicCredentialAt(i);
        }
        for (int i = _manager.getDomainCredentialCount() - 1; i >= 0; i--) {
            _manager.deleteDomainCredentialAt(i);
        }
        updateCredentials();
    }// GEN-LAST:event_clearButtonActionPerformed

    /**
     * Close button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_closeButtonActionPerformed
        setVisible(false);
    }// GEN-LAST:event_closeButtonActionPerformed

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
        int basicSelection = basicTable.getSelectedRow();
        int domainSelection = domainTable.getSelectedRow();
        if (basicSelection > -1 && domainSelection > -1)
            return;
        if (basicSelection > -1)
            _manager.deleteBasicCredentialAt(basicSelection);
        if (domainSelection > -1)
            _manager.deleteDomainCredentialAt(domainSelection);
        updateCredentials();
    }// GEN-LAST:event_deleteButtonActionPerformed

    /**
     * Adds the button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
        // FIRST
        // :
        // event_addButtonActionPerformed
        new CredentialRequestDialog(this, true, _manager).requestCredentials(null, null);
        updateCredentials();
    }// GEN-LAST:event_addButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The add button. */
    private javax.swing.JButton addButton;

    /** The basic table. */
    private javax.swing.JTable basicTable;

    /** The clear button. */
    private javax.swing.JButton clearButton;

    /** The close button. */
    private javax.swing.JButton closeButton;

    /** The delete button. */
    private javax.swing.JButton deleteButton;

    /** The domain table. */
    private javax.swing.JTable domainTable;

    /** The j label1. */
    private javax.swing.JLabel jLabel1;

    /** The j label2. */
    private javax.swing.JLabel jLabel2;

    /** The j panel1. */
    private javax.swing.JPanel jPanel1;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    /** The j scroll pane2. */
    private javax.swing.JScrollPane jScrollPane2;

    /** The prompt check box. */
    private javax.swing.JCheckBox promptCheckBox;

    // End of variables declaration//GEN-END:variables

    /**
     * The Class BasicTableModel.
     */
    private class BasicTableModel extends AbstractTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = -1371252550390862859L;
        /** The _column names. */
        private String[] _columnNames = { "Host", "Realm", "Username" };

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int column) {
            return _columnNames[column];
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 3;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return _manager.getBasicCredentialCount();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int column) {
            BasicCredential bc = _manager.getBasicCredentialAt(row);
            switch (column) {
            case 0:
                return bc.getHost();
            case 1:
                return bc.getRealm();
            case 2:
                return bc.getUsername();
            }
            return null;
        }

    }

    /**
     * The Class DomainTableModel.
     */
    private class DomainTableModel extends AbstractTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = 2566332489156466203L;
        /** The _column names. */
        private String[] _columnNames = { "Host", "Domain", "Username" };

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int column) {
            return _columnNames[column];
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 3;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return _manager.getDomainCredentialCount();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int column) {
            DomainCredential dc = _manager.getDomainCredentialAt(row);
            switch (column) {
            case 0:
                return dc.getHost();
            case 1:
                return dc.getDomain();
            case 2:
                return dc.getUsername();
            }
            return null;
        }

    }

    /**
     * The Class Updater.
     */
    private class Updater extends ComponentAdapter {

        /*
         * (non-Javadoc)
         * 
         * @seejava.awt.event.ComponentAdapter#componentShown(java.awt.event.
         * ComponentEvent)
         */
        public void componentShown(ComponentEvent e) {
            updateCredentials();
        }
    }

}
