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
 * ProxyConfig.java
 *
 * Created on August 28, 2003, 9:30 PM
 */

package edu.lnmiit.wavd.ui.swing;

//import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;

import edu.lnmiit.wavd.httpclient.HTTPClientFactory;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.plugin.Framework;
import edu.lnmiit.wavd.util.W32WinInet;

// TODO: Auto-generated Javadoc
/**
 * The Class ProxyConfig.
 */
public class ProxyConfig extends javax.swing.JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -4322937546231707187L;

    /** The _factory. */
    private HTTPClientFactory _factory = HTTPClientFactory.getInstance();

    /** The _framework. */
    private Framework _framework;

    /** The _logger. */
   // private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new proxy config.
     * 
     * @param parent
     *            the parent
     * @param framework
     *            the framework
     */
    public ProxyConfig(java.awt.Frame parent, Framework framework) {
        super(parent, true);
        _framework = framework;
        initComponents();

        httpProxyServerTextField.setText(_factory.getHttpProxy());
        httpProxyPortTextField.setText(Integer.toString(_factory.getHttpProxyPort()));

        httpsProxyServerTextField.setText(_factory.getHttpsProxy());
        httpsProxyPortTextField.setText(Integer.toString(_factory.getHttpsProxyPort()));

        String[] noproxies = _factory.getNoProxy();
        if (noproxies.length > 0) {
            StringBuffer buff = new StringBuffer();
            buff.append(noproxies[0]);
            for (int i = 1; i < noproxies.length; i++) {
                buff.append(", ").append(noproxies[i]);
            }
            noProxyTextArea.setText(buff.toString());
        } else {
            noProxyTextArea.setText("");
        }
        if (!W32WinInet.isAvailable()) {
            w32Button.getParent().remove(w32Button);
        }
    }

    /**
     * Inits the components.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel2 = new javax.swing.JLabel();
        httpProxyServerTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        httpProxyPortTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        httpsProxyServerTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        httpsProxyPortTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        noProxyTextArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        w32Button = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Config proxies");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel2.setText("HTTP  Proxy : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jLabel2, gridBagConstraints);

        httpProxyServerTextField.setMinimumSize(new java.awt.Dimension(250, 20));
        httpProxyServerTextField.setPreferredSize(new java.awt.Dimension(250, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(httpProxyServerTextField, gridBagConstraints);

        jLabel5.setText("Port :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jLabel5, gridBagConstraints);

        httpProxyPortTextField.setMinimumSize(new java.awt.Dimension(60, 20));
        httpProxyPortTextField.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(httpProxyPortTextField, gridBagConstraints);

        jLabel4.setText("HTTPS Proxy : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jLabel4, gridBagConstraints);

        httpsProxyServerTextField.setMinimumSize(new java.awt.Dimension(250, 20));
        httpsProxyServerTextField.setPreferredSize(new java.awt.Dimension(250, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(httpsProxyServerTextField, gridBagConstraints);

        jLabel3.setText("Port :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jLabel3, gridBagConstraints);

        httpsProxyPortTextField.setMinimumSize(new java.awt.Dimension(60, 20));
        httpsProxyPortTextField.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(httpsProxyPortTextField, gridBagConstraints);

        jLabel1.setText("No Proxy : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jLabel1, gridBagConstraints);

        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(250, 48));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 51));
        noProxyTextArea.setLineWrap(true);
        noProxyTextArea
                .setToolTipText("Enter a comma separated list of hosts that do not need to go through the proxy");
        noProxyTextArea.setMinimumSize(new java.awt.Dimension(250, 40));
        noProxyTextArea.setPreferredSize(new java.awt.Dimension(250, 40));
        jScrollPane1.setViewportView(noProxyTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        w32Button.setText("Get IE Settings");
        w32Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w32ButtonActionPerformed(evt);
            }
        });

        jPanel1.add(w32Button);

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        jPanel1.add(applyButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel1.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
    }// GEN-END:initComponents

    /**
     * W32 button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void w32ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
        // FIRST
        // :
        // event_w32ButtonActionPerformed
        if (W32WinInet.isAvailable()) {
            String server = W32WinInet.getHttpProxyServer();
            httpProxyServerTextField.setText(server == null ? "" : server);
            int port = W32WinInet.getHttpProxyPort();
            if (port < 1)
                port = 3128;
            httpProxyPortTextField.setText(Integer.toString(port));

            server = W32WinInet.getHttpsProxyServer();
            httpsProxyServerTextField.setText(server == null ? "" : server);
            port = W32WinInet.getHttpsProxyPort();
            if (port < 1)
                port = 3128;
            httpsProxyPortTextField.setText(Integer.toString(port));

            String bypass = W32WinInet.getNoProxy();
            if (bypass == null)
                bypass = "";
            noProxyTextArea.setText(bypass.replaceAll(";", ","));
        }
    }// GEN-LAST:event_w32ButtonActionPerformed

    /**
     * Apply button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_applyButtonActionPerformed
        String error = null;
        String httpserver = httpProxyServerTextField.getText().trim();
        int httpport = -1;
        try {
            String p = httpProxyPortTextField.getText().trim();
            if (httpserver.equals("") && p.equals(""))
                p = "3128";
            httpport = Integer.parseInt(p);
            if (httpport < 1 || httpport > 65535)
                error = "HTTP Proxy port must be between 0 and 65536";
        } catch (NumberFormatException nfe) {
            error = "Error parsing the HTTP Proxy port number";
        }
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String httpsserver = httpsProxyServerTextField.getText().trim();
        int httpsport = -1;
        try {
            String p = httpsProxyPortTextField.getText().trim();
            if (httpsserver.equals("") && p.equals(""))
                p = "3128";
            httpsport = Integer.parseInt(p);
            if (httpsport < 1 || httpsport > 65535)
                error = "HTTPS Proxy port must be between 0 and 65536";
        } catch (NumberFormatException nfe) {
            error = "Error parsing the HTTPS Proxy port number";
        }
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] noproxies = noProxyTextArea.getText().trim().split(" *, *");

        boolean running = _framework.isRunning();
        if (running) {
            if (_framework.isBusy()) {
                String[] status = _framework.getStatus();
                JOptionPane.showMessageDialog(this, status, "Error - plugins are busy", JOptionPane.ERROR_MESSAGE);
                return;
            }
            _framework.stopPlugins();
        }
        _factory.setHttpProxy(httpserver, httpport);
        _factory.setHttpsProxy(httpsserver, httpsport);
        _factory.setNoProxy(noproxies);
        if (running) {
            _framework.startPlugins();
        }

        Preferences.setPreference("WebScarab.httpProxy", httpserver + ":" + httpport);
        Preferences.setPreference("WebScarab.httpsProxy", httpsserver + ":" + httpsport);
        Preferences.setPreference("WebScarab.noProxy", noProxyTextArea.getText());

        setVisible(false);
        dispose();
    }// GEN-LAST:event_applyButtonActionPerformed

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
        setVisible(false);
        dispose();
    }// GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Close dialog.
     * 
     * @param evt
     *            the evt
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {// GEN-FIRST:
        // event_closeDialog
        setVisible(false);
        dispose();
    }// GEN-LAST:event_closeDialog

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The apply button. */
    private javax.swing.JButton applyButton;

    /** The cancel button. */
    private javax.swing.JButton cancelButton;

    /** The http proxy port text field. */
    private javax.swing.JTextField httpProxyPortTextField;

    /** The http proxy server text field. */
    private javax.swing.JTextField httpProxyServerTextField;

    /** The https proxy port text field. */
    private javax.swing.JTextField httpsProxyPortTextField;

    /** The https proxy server text field. */
    private javax.swing.JTextField httpsProxyServerTextField;

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

    /** The j panel1. */
    private javax.swing.JPanel jPanel1;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    /** The no proxy text area. */
    private javax.swing.JTextArea noProxyTextArea;

    /** The w32 button. */
    private javax.swing.JButton w32Button;
    // End of variables declaration//GEN-END:variables

}
