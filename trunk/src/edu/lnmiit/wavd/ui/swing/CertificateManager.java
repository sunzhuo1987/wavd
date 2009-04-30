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

import java.io.File;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;

import edu.lnmiit.wavd.httpclient.HTTPClientFactory;
import edu.lnmiit.wavd.httpclient.SSLContextManager;

// TODO: Auto-generated Javadoc
/**
 * The Class CertificateManager.
 */
public class CertificateManager extends javax.swing.JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5239475002021657917L;

    /** The _sslcm. */
    private SSLContextManager _sslcm = HTTPClientFactory.getInstance().getSSLContextManager();

    /** The _keystore list model. */
    private DefaultListModel _keystoreListModel;

    /** The _alias table model. */
    private AliasTableModel _aliasTableModel;

    /**
     * Instantiates a new certificate manager.
     */
    public CertificateManager() {
        initComponents();
        if (!_sslcm.isProviderAvailable("PKCS11"))
            keystoreTabbedPane.setEnabledAt(1, false);

        _keystoreListModel = new DefaultListModel();
        updateKeystoreList();
        keyStoreList.setModel(_keystoreListModel);
        keyStoreList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        keyStoreList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                int keystore = keyStoreList.getSelectedIndex();
                try {
                    _aliasTableModel.setKeystore(keystore);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, new String[] { "Error accessing key store: ", e.toString() },
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
        _aliasTableModel = new AliasTableModel();
        aliasTable.setModel(_aliasTableModel);
        aliasTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                _sslcm.setDefaultKey(null);
                int keystore = keyStoreList.getSelectedIndex();
                int alias = aliasTable.getSelectedRow();
                if (alias > -1) {
                    try {
                        Certificate cert = _sslcm.getCertificate(keystore, alias);
                        certTextArea.setText(cert.toString());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                new String[] { "Error accessing key store: ", e.toString() }, "Error",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                } else {
                    certTextArea.setText("");
                }
            }
        });
        // stupid netbeans does not pack or resize child dialogs
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        addKeystoreDialog.setBounds((screenSize.width - 450) / 2, (screenSize.height - 190) / 2, 450, 190);
        // and the designer keeps resizing!
        setBounds((screenSize.width - 600) / 2, (screenSize.height - 400) / 2, 600, 400);

        // set default buttons
        getRootPane().setDefaultButton(closeButton);
        addKeystoreDialog.getRootPane().setDefaultButton(keystoreOkButton);
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        int result = JOptionPane.showConfirmDialog(this, askPasswordField, "Enter password",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return new String(askPasswordField.getPassword());
        } else
            return null;
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        addKeystoreDialog = new javax.swing.JDialog(this);
        keystoreTabbedPane = new javax.swing.JTabbedPane();
        pkcs12Panel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pkcs12FileTextField = new javax.swing.JTextField();
        pkcs12BrowseButton = new javax.swing.JButton();
        pkcs12PasswordField = new javax.swing.JPasswordField();
        pkcs11Panel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        pkcs11NameTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        pkcs11LibraryTextField = new javax.swing.JTextField();
        pkcs11BrowseButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        pkcs11PasswordField = new javax.swing.JPasswordField();
        buttonPanel = new javax.swing.JPanel();
        keystoreCancelButton = new javax.swing.JButton();
        keystoreOkButton = new javax.swing.JButton();
        askPasswordField = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        keyStoreList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        aliasTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        addKeystoreButton = new javax.swing.JButton();
        setButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        certTextArea = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        currentCertTextField = new javax.swing.JTextField();

        addKeystoreDialog.setTitle("Add Key Store");
        addKeystoreDialog.setModal(true);
        addKeystoreDialog.setResizable(false);
        pkcs12Panel.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        pkcs12Panel.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        pkcs12Panel.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pkcs12Panel.add(pkcs12FileTextField, gridBagConstraints);

        pkcs12BrowseButton.setText("Browse");
        pkcs12BrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pkcs12BrowseButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        pkcs12Panel.add(pkcs12BrowseButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pkcs12Panel.add(pkcs12PasswordField, gridBagConstraints);

        keystoreTabbedPane.addTab("PKCS#12", pkcs12Panel);

        pkcs11Panel.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        pkcs11Panel.add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pkcs11Panel.add(pkcs11NameTextField, gridBagConstraints);

        jLabel6.setText("Library");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        pkcs11Panel.add(jLabel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pkcs11Panel.add(pkcs11LibraryTextField, gridBagConstraints);

        pkcs11BrowseButton.setText("Browse");
        pkcs11BrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pkcs11BrowseButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        pkcs11Panel.add(pkcs11BrowseButton, gridBagConstraints);

        jLabel7.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        pkcs11Panel.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pkcs11Panel.add(pkcs11PasswordField, gridBagConstraints);

        keystoreTabbedPane.addTab("PKCS#11", pkcs11Panel);

        addKeystoreDialog.getContentPane().add(keystoreTabbedPane, java.awt.BorderLayout.CENTER);

        keystoreCancelButton.setText("Cancel");
        keystoreCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keystoreCancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(keystoreCancelButton);

        keystoreOkButton.setText("Ok");
        keystoreOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keystoreOkButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(keystoreOkButton);

        addKeystoreDialog.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Certificate Manager");
        jScrollPane1.setMinimumSize(new java.awt.Dimension(250, 22));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 176));
        jScrollPane1.setViewportView(keyStoreList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jLabel1.setText("Key Store");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText("Certificates");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel2, gridBagConstraints);

        aliasTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null },
                { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] {
                "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane2.setViewportView(aliasTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(jScrollPane2, gridBagConstraints);

        addKeystoreButton.setText("Add Key Store");
        addKeystoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addKeystoreButtonActionPerformed(evt);
            }
        });

        jPanel1.add(addKeystoreButton);

        setButton.setText("Activate Selected Alias");
        setButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonActionPerformed(evt);
            }
        });

        jPanel1.add(setButton);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        jPanel1.add(closeButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        getContentPane().add(jPanel1, gridBagConstraints);

        certTextArea.setEditable(false);
        jScrollPane3.setViewportView(certTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane3, gridBagConstraints);

        jLabel8.setText("Certificate Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel8, gridBagConstraints);

        jLabel9.setText("Active key");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(currentCertTextField, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 600) / 2, (screenSize.height - 400) / 2, 600, 400);
    }

    // </editor-fold>//GEN-END:initComponents

    /**
     * Sets the button action performed.
     * 
     * @param evt
     *            the new button action performed
     */
    private void setButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
        // FIRST
        // :
        // event_setButtonActionPerformed
        int ks = keyStoreList.getSelectedIndex();
        int alias = aliasTable.getSelectedRow();
        String fingerprint = "";
        if (ks > -1 && alias > -1) {
            if (!_sslcm.isKeyUnlocked(ks, alias)) {
                String password = getPassword();
                try {
                    _sslcm.unlockKey(ks, alias, password);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, new String[] { "Error accessing key store: ", e.toString() },
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            Certificate cert = _sslcm.getCertificate(ks, alias);
            try {
                fingerprint = _sslcm.getFingerPrint(cert);
            } catch (KeyStoreException kse) {
                JOptionPane.showMessageDialog(null, new String[] { "Error calculating key fingerprint: ",
                        kse.toString() }, "Error", JOptionPane.ERROR_MESSAGE);
                fingerprint = "";
            }
        }
        currentCertTextField.setText(fingerprint);
        _sslcm.setDefaultKey(fingerprint);
    }// GEN-LAST:event_setButtonActionPerformed

    /**
     * Pkcs11 browse button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void pkcs11BrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
                                                                                    // -
                                                                                    // FIRST
                                                                                    // :
        // event_pkcs11BrowseButtonActionPerformed
        File library = chooseFile("Select the native PKCS#11 library", null);
        if (library != null)
            pkcs11LibraryTextField.setText(library.getAbsolutePath());
    }// GEN-LAST:event_pkcs11BrowseButtonActionPerformed

    /**
     * Pkcs12 browse button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void pkcs12BrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
                                                                                    // -
                                                                                    // FIRST
                                                                                    // :
        // event_pkcs12BrowseButtonActionPerformed
        FileFilter filter = new FileFilter() {
            public String getDescription() {
                return "*.p12, *.pfx";
            }

            public boolean accept(File file) {
                String name = file.getName();
                if (file.isDirectory())
                    return true;
                return name.endsWith(".p12") || name.endsWith(".pfx");
            }
        };

        File file = chooseFile("Select a PKCS#12 certificate", filter);
        if (file != null)
            pkcs12FileTextField.setText(file.getAbsolutePath());
    }// GEN-LAST:event_pkcs12BrowseButtonActionPerformed

    /**
     * Choose file.
     * 
     * @param message
     *            the message
     * @param filter
     *            the filter
     * 
     * @return the file
     */
    private File chooseFile(String message, FileFilter filter) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(message);
        jfc.setFileFilter(filter);
        int result = jfc.showOpenDialog(addKeystoreDialog);
        if (result == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        }
        return null;
    }

    /**
     * Keystore ok button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void keystoreOkButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_keystoreOkButtonActionPerformed
        try {
            int tab = keystoreTabbedPane.getSelectedIndex();
            if (tab == 0) { // PKCS#12
                String file = pkcs12FileTextField.getText();
                if (file.equals(""))
                    return;
                String kspass = new String(pkcs12PasswordField.getPassword());
                if (kspass.equals(""))
                    kspass = null;
                int ksIndex = _sslcm.loadPKCS12Certificate(file, kspass);
                _keystoreListModel.insertElementAt(_sslcm.getKeyStoreDescription(ksIndex), ksIndex);
            } else if (tab == 1) { // PKCS#11
                String name = pkcs11NameTextField.getText();
                if (name.equals(""))
                    return;
                String library = pkcs11LibraryTextField.getText();
                if (library.equals(""))
                    return;
                String kspass = new String(pkcs11PasswordField.getPassword());
                if (kspass.equals(""))
                    kspass = null;
                int ksIndex = _sslcm.initPKCS11(name, library, kspass);
                _keystoreListModel.insertElementAt(_sslcm.getKeyStoreDescription(ksIndex), ksIndex);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, new String[] { "Error loading Key Store: ", e.toString() }, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        addKeystoreDialog.setVisible(false);
    }// GEN-LAST:event_keystoreOkButtonActionPerformed

    /**
     * Update keystore list.
     */
    private void updateKeystoreList() {
        _keystoreListModel.removeAllElements();
        for (int i = 0; i < _sslcm.getKeyStoreCount(); i++) {
            _keystoreListModel.addElement(_sslcm.getKeyStoreDescription(i));
        }
    }

    /**
     * Keystore cancel button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void keystoreCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
                                                                                      // -
                                                                                      // FIRST
                                                                                      // :
        // event_keystoreCancelButtonActionPerformed
        addKeystoreDialog.setVisible(false);
    }// GEN-LAST:event_keystoreCancelButtonActionPerformed

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
     * Adds the keystore button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void addKeystoreButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_addKeystoreButtonActionPerformed
        addKeystoreDialog.setVisible(true);
    }// GEN-LAST:event_addKeystoreButtonActionPerformed

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CertificateManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The add keystore button. */
    private javax.swing.JButton addKeystoreButton;

    /** The add keystore dialog. */
    private javax.swing.JDialog addKeystoreDialog;

    /** The alias table. */
    private javax.swing.JTable aliasTable;

    /** The ask password field. */
    private javax.swing.JPasswordField askPasswordField;

    /** The button panel. */
    private javax.swing.JPanel buttonPanel;

    /** The cert text area. */
    private javax.swing.JTextArea certTextArea;

    /** The close button. */
    private javax.swing.JButton closeButton;

    /** The current cert text field. */
    private javax.swing.JTextField currentCertTextField;

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

    /** The j label7. */
    private javax.swing.JLabel jLabel7;

    /** The j label8. */
    private javax.swing.JLabel jLabel8;

    /** The j label9. */
    private javax.swing.JLabel jLabel9;

    /** The j panel1. */
    private javax.swing.JPanel jPanel1;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    /** The j scroll pane2. */
    private javax.swing.JScrollPane jScrollPane2;

    /** The j scroll pane3. */
    private javax.swing.JScrollPane jScrollPane3;

    /** The key store list. */
    private javax.swing.JList keyStoreList;

    /** The keystore cancel button. */
    private javax.swing.JButton keystoreCancelButton;

    /** The keystore ok button. */
    private javax.swing.JButton keystoreOkButton;

    /** The keystore tabbed pane. */
    private javax.swing.JTabbedPane keystoreTabbedPane;

    /** The pkcs11 browse button. */
    private javax.swing.JButton pkcs11BrowseButton;

    /** The pkcs11 library text field. */
    private javax.swing.JTextField pkcs11LibraryTextField;

    /** The pkcs11 name text field. */
    private javax.swing.JTextField pkcs11NameTextField;

    /** The pkcs11 panel. */
    private javax.swing.JPanel pkcs11Panel;

    /** The pkcs11 password field. */
    private javax.swing.JPasswordField pkcs11PasswordField;

    /** The pkcs12 browse button. */
    private javax.swing.JButton pkcs12BrowseButton;

    /** The pkcs12 file text field. */
    private javax.swing.JTextField pkcs12FileTextField;

    /** The pkcs12 panel. */
    private javax.swing.JPanel pkcs12Panel;

    /** The pkcs12 password field. */
    private javax.swing.JPasswordField pkcs12PasswordField;

    /** The set button. */
    private javax.swing.JButton setButton;

    // End of variables declaration//GEN-END:variables

    /**
     * The Class AliasTableModel.
     */
    private class AliasTableModel extends AbstractTableModel {

        /**
		 * 
		 */
        private static final long serialVersionUID = -393890827363260717L;

        /** The _ks. */
        private int _ks = -1;

        /** The _aliases. */
        private List _aliases = new ArrayList();

        /**
         * Sets the keystore.
         * 
         * @param ks
         *            the new keystore
         */
        public void setKeystore(int ks) {
            _ks = ks;
            _aliases.clear();
            if (_ks > -1) {
                for (int i = 0; i < _sslcm.getAliasCount(ks); i++)
                    _aliases.add(_sslcm.getAliasAt(ks, i));
            }
            fireTableDataChanged();
        }

        /**
         * Gets the alias.
         * 
         * @param row
         *            the row
         * 
         * @return the alias
         */
        public String getAlias(int row) {
            return (String) _aliases.get(row);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 1;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return _aliases.size();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int col) {
            return _aliases.get(row);
        }

    }

}
