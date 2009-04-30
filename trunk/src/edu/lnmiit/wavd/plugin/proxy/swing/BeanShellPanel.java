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
 * $Id: BeanShellPanel.java,v 1.5 2005/05/18 15:23:31 rogan Exp $
 * ProxyUI.java
 *
 * Created on February 17, 2003, 9:05 PM
 */

package edu.lnmiit.wavd.plugin.proxy.swing;

import java.io.PrintStream;
import java.util.logging.Logger;

import javax.swing.JPanel;

import bsh.EvalError;
import edu.lnmiit.wavd.plugin.proxy.BeanShell;
import edu.lnmiit.wavd.plugin.proxy.BeanShellUI;
import edu.lnmiit.wavd.util.swing.DocumentOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class BeanShellPanel.
 */
public class BeanShellPanel extends javax.swing.JPanel implements ProxyPluginUI, BeanShellUI {

    /**
     * 
     */
    private static final long serialVersionUID = -7439958087569766266L;

    /** The _bean shell. */
    private BeanShell _beanShell;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(this.getClass().getName());

    /** The _dos. */
    private DocumentOutputStream _dos;

    /** The _doc stream. */
    private PrintStream _docStream;

    /**
     * Instantiates a new bean shell panel.
     * 
     * @param beanShell
     *            the bean shell
     */
    public BeanShellPanel(BeanShell beanShell) {
        _beanShell = beanShell;

        initComponents();
        configure();

        _beanShell.setUI(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.PluginUI#getPluginName()
     */
    public String getPluginName() {
        return new String("Bean Shell");
    }

    /**
     * Configure.
     */
    public void configure() {
        boolean enabled = _beanShell.getEnabled();
        enableCheckBox.setSelected(enabled);

        scriptTextArea.setEnabled(enabled);
        scriptTextArea.setText(_beanShell.getScript());

        commitButton.setEnabled(enabled);

        _dos = new DocumentOutputStream(10240);
        _docStream = new PrintStream(_dos);
        logTextArea.setDocument(_dos.getDocument());
    }

    /**
     * Inits the components.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        scriptPanel = new javax.swing.JPanel();
        enableCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        scriptTextArea = new javax.swing.JTextArea();
        commitButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.8);
        scriptPanel.setLayout(new java.awt.GridBagLayout());

        enableCheckBox.setText("Enabled : ");
        enableCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        enableCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        scriptPanel.add(enableCheckBox, gridBagConstraints);

        scriptTextArea.setFont(new java.awt.Font("Monospaced", 0, 14));
        scriptTextArea.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jScrollPane1.setViewportView(scriptTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        scriptPanel.add(jScrollPane1, gridBagConstraints);

        commitButton.setText("Commit");
        commitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        scriptPanel.add(commitButton, gridBagConstraints);

        jSplitPane1.setLeftComponent(scriptPanel);

        jPanel1.setLayout(new java.awt.BorderLayout());

        logTextArea.setBackground(new java.awt.Color(204, 204, 204));
        logTextArea.setEditable(false);
        jScrollPane2.setViewportView(logTextArea);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);

    }// GEN-END:initComponents

    /**
     * Commit button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void commitButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_commitButtonActionPerformed
        try {
            _beanShell.setScript(scriptTextArea.getText());
        } catch (EvalError ee) {
            _logger.severe("Script error: " + ee);
        }
    }// GEN-LAST:event_commitButtonActionPerformed

    /**
     * Enable check box action performed.
     * 
     * @param evt
     *            the evt
     */
    private void enableCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_enableCheckBoxActionPerformed
        boolean enabled = enableCheckBox.isSelected();
        _beanShell.setEnabled(enabled);
        scriptTextArea.setEnabled(enabled);
        commitButton.setEnabled(enabled);
    }// GEN-LAST:event_enableCheckBoxActionPerformed

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.swing.ProxyPluginUI#getPanel()
     */
    public JPanel getPanel() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.BeanShellUI#getErr()
     */
    public PrintStream getErr() {
        return _docStream;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.BeanShellUI#getOut()
     */
    public PrintStream getOut() {
        return _docStream;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        commitButton.setEnabled(enabled);
        enableCheckBox.setEnabled(enabled);
        scriptTextArea.setEnabled(enabled && enableCheckBox.isSelected());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The commit button. */
    private javax.swing.JButton commitButton;

    /** The enable check box. */
    private javax.swing.JCheckBox enableCheckBox;

    /** The j panel1. */
    private javax.swing.JPanel jPanel1;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    /** The j scroll pane2. */
    private javax.swing.JScrollPane jScrollPane2;

    /** The j split pane1. */
    private javax.swing.JSplitPane jSplitPane1;

    /** The log text area. */
    private javax.swing.JTextArea logTextArea;

    /** The script panel. */
    private javax.swing.JPanel scriptPanel;

    /** The script text area. */
    private javax.swing.JTextArea scriptTextArea;
    // End of variables declaration//GEN-END:variables

}
