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

import edu.lnmiit.wavd.plugin.BasicCredential;
import edu.lnmiit.wavd.plugin.CredentialManager;
import edu.lnmiit.wavd.plugin.CredentialManagerUI;
import edu.lnmiit.wavd.plugin.DomainCredential;

// TODO: Auto-generated Javadoc
/**
 * The Class CredentialRequestDialog.
 */
public class CredentialRequestDialog extends javax.swing.JDialog implements CredentialManagerUI {

    /**
     * 
     */
    private static final long serialVersionUID = 3445551942626312411L;

    /** The _manager. */
    private CredentialManager _manager;

    /**
     * Instantiates a new credential request dialog.
     * 
     * @param parent
     *            the parent
     * @param modal
     *            the modal
     * @param manager
     *            the manager
     */
    public CredentialRequestDialog(java.awt.Frame parent, boolean modal, CredentialManager manager) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton(okButton);
        _manager = manager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.plugin.CredentialManagerUI#requestCredentials(java.lang
     * .String, java.lang.String[])
     */
    public void requestCredentials(final String host, final String[] challenges) {
        setup(host, challenges);
        getOwner().setVisible(true);
        setVisible(true);
        toFront();
    }

    /**
     * Setup.
     * 
     * @param host
     *            the host
     * @param challenges
     *            the challenges
     */
    private void setup(String host, String[] challenges) {
        basicRadioButton.setEnabled(false);
        domainRadioButton.setEnabled(false);
        hostTextField.setText("");
        domainTextField.setText("");
        realmTextArea.setText("");
        usernameTextField.setText("");
        passwordTextField.setText("");

        if (host != null)
            hostTextField.setText(host);
        if (challenges == null) {
            basicRadioButton.setEnabled(true);
            realmTextArea.setEditable(true);
            domainRadioButton.setEnabled(true);
        } else {
            for (int i = 0; i < challenges.length; i++) {
                if (challenges[i].startsWith("Basic ")) {
                    basicRadioButton.setEnabled(true);
                    String realm = challenges[i].substring("Basic realm=\"".length(), challenges[i].length() - 1);
                    realmTextArea.setText(realm);
                    realmTextArea.setEditable(false);
                } else if (challenges[i].startsWith("NTLM") || challenges[i].startsWith("Negotiate")) {
                    domainRadioButton.setEnabled(true);
                }
            }
        }
        if (basicRadioButton.isEnabled()) {
            basicRadioButton.doClick();
        } else if (domainRadioButton.isEnabled()) {
            domainRadioButton.doClick();
        }
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        methodButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        basicRadioButton = new javax.swing.JRadioButton();
        domainRadioButton = new javax.swing.JRadioButton();
        hostLabel = new javax.swing.JLabel();
        hostTextField = new javax.swing.JTextField();
        domainLabel = new javax.swing.JLabel();
        domainTextField = new javax.swing.JTextField();
        realmLabel = new javax.swing.JLabel();
        realmTextArea = new javax.swing.JTextArea();
        usernameLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordTextField = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Enter your credentials to access this web site");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        methodButtonGroup.add(basicRadioButton);
        basicRadioButton.setSelected(true);
        basicRadioButton.setText("Basic");
        basicRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                basicRadioButtonActionPerformed(evt);
            }
        });

        jPanel1.add(basicRadioButton);

        methodButtonGroup.add(domainRadioButton);
        domainRadioButton.setText("Domain");
        domainRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                domainRadioButtonActionPerformed(evt);
            }
        });

        jPanel1.add(domainRadioButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanel1, gridBagConstraints);

        hostLabel.setText("Host");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(hostLabel, gridBagConstraints);

        hostTextField.setMinimumSize(new java.awt.Dimension(200, 22));
        hostTextField.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(hostTextField, gridBagConstraints);

        domainLabel.setText("Domain");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(domainLabel, gridBagConstraints);

        domainTextField.setMinimumSize(new java.awt.Dimension(200, 22));
        domainTextField.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(domainTextField, gridBagConstraints);

        realmLabel.setText("Realm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(realmLabel, gridBagConstraints);

        realmTextArea.setEditable(false);
        realmTextArea.setMinimumSize(new java.awt.Dimension(200, 22));
        realmTextArea.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(realmTextArea, gridBagConstraints);

        usernameLabel.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(usernameLabel, gridBagConstraints);

        usernameTextField.setMinimumSize(new java.awt.Dimension(200, 22));
        usernameTextField.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(usernameTextField, gridBagConstraints);

        passwordLabel.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(passwordLabel, gridBagConstraints);

        passwordTextField.setMinimumSize(new java.awt.Dimension(200, 22));
        passwordTextField.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(passwordTextField, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel2.add(cancelButton);

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jPanel2.add(okButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanel2, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 419) / 2, (screenSize.height - 206) / 2, 419, 206);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Domain radio button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void domainRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_domainRadioButtonActionPerformed
        domainLabel.setVisible(true);
        domainTextField.setVisible(true);
        realmLabel.setVisible(false);
        realmTextArea.setVisible(false);
    }// GEN-LAST:event_domainRadioButtonActionPerformed

    /**
     * Basic radio button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void basicRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_basicRadioButtonActionPerformed
        domainLabel.setVisible(false);
        domainTextField.setVisible(false);
        realmLabel.setVisible(true);
        realmTextArea.setVisible(true);
    }// GEN-LAST:event_basicRadioButtonActionPerformed

    /**
     * Ok button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
        // FIRST
        // :
        // event_okButtonActionPerformed
        ok();
    }// GEN-LAST:event_okButtonActionPerformed

    /**
     * Cancel button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_cancelButtonActionPerformed
        cancel();
    }// GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Form window closed.
     * 
     * @param evt
     *            the evt
     */
    private void formWindowClosed(java.awt.event.WindowEvent evt) {// GEN-FIRST:
        // event_formWindowClosed
        cancel();
    }// GEN-LAST:event_formWindowClosed

    /**
     * Ok.
     */
    private void ok() {
        String host = hostTextField.getText();
        String domain = domainTextField.getText();
        String realm = realmTextArea.getText();
        String username = usernameTextField.getText();
        String password = new String(passwordTextField.getPassword());
        if (!(username.equals("") || password.equals(""))) {
            if (basicRadioButton.isSelected()) {
                BasicCredential bc = new BasicCredential(host, realm, username, password);
                _manager.addBasicCredentials(bc);
            } else if (domainRadioButton.isSelected()) {
                DomainCredential dc = new DomainCredential(host, domain, username, password);
                _manager.addDomainCredentials(dc);
            }
        }
        setVisible(false);
    }

    /**
     * Cancel.
     */
    private void cancel() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The basic radio button. */
    private javax.swing.JRadioButton basicRadioButton;

    /** The cancel button. */
    private javax.swing.JButton cancelButton;

    /** The domain label. */
    private javax.swing.JLabel domainLabel;

    /** The domain radio button. */
    private javax.swing.JRadioButton domainRadioButton;

    /** The domain text field. */
    private javax.swing.JTextField domainTextField;

    /** The host label. */
    private javax.swing.JLabel hostLabel;

    /** The host text field. */
    private javax.swing.JTextField hostTextField;

    /** The j panel1. */
    private javax.swing.JPanel jPanel1;

    /** The j panel2. */
    private javax.swing.JPanel jPanel2;

    /** The method button group. */
    private javax.swing.ButtonGroup methodButtonGroup;

    /** The ok button. */
    private javax.swing.JButton okButton;

    /** The password label. */
    private javax.swing.JLabel passwordLabel;

    /** The password text field. */
    private javax.swing.JPasswordField passwordTextField;

    /** The realm label. */
    private javax.swing.JLabel realmLabel;

    /** The realm text area. */
    private javax.swing.JTextArea realmTextArea;

    /** The username label. */
    private javax.swing.JLabel usernameLabel;

    /** The username text field. */
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables

}
