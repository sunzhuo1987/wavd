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
 * $Id: ManualEditPanel.java,v 1.8 2005/05/19 15:20:56 rogan Exp $
 * ProxyUI.java
 *
 * Created on February 17, 2003, 9:05 PM
 */

package edu.lnmiit.wavd.plugin.proxy.swing;



import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.plugin.proxy.ManualEdit;
import edu.lnmiit.wavd.plugin.proxy.ManualEditUI;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import javax.swing.ButtonModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ListModel;
import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class ManualEditPanel.
 */
public class ManualEditPanel extends javax.swing.JPanel implements ProxyPluginUI, ManualEditUI {
    
    /** The _manual edit. */
    private ManualEdit _manualEdit;
    
    /** The _response button model. */
    private ButtonModel _requestButtonModel, _responseButtonModel;
    
    /**
     * Instantiates a new manual edit panel.
     * 
     * @param manualEdit the manual edit
     */
    public ManualEditPanel(ManualEdit manualEdit) {
        _manualEdit = manualEdit;
        initComponents();
        _requestButtonModel = interceptRequestCheckBox.getModel();
        _requestButtonModel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                boolean enabled = interceptRequestCheckBox.isSelected();
                interceptIncludeRegexTextField.setEnabled(isEnabled() && enabled);
                interceptExcludeRegexTextField.setEnabled(isEnabled() && enabled);
                interceptMethodList.setEnabled(isEnabled() && enabled);
                _manualEdit.setInterceptRequest(enabled);
            }
        });
        _responseButtonModel = interceptResponseCheckBox.getModel();
        _responseButtonModel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                boolean enabled = interceptResponseCheckBox.isSelected();
                interceptResponseTextField.setEnabled(isEnabled() && enabled);
                _manualEdit.setInterceptResponse(enabled);
            }
        });
        configure();
        interceptMethodList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                interceptMethodListValueChanged(evt);
            }
        });
        _manualEdit.setUI(this);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.PluginUI#getPluginName()
     */
    public String getPluginName() {
        return new String("Manual Edit");
    }
    
    /**
     * Configure.
     */
    public void configure() {
        boolean enabled = _manualEdit.getInterceptRequest();
        interceptRequestCheckBox.setSelected(enabled);
        
        sensitiveCheckBox.setSelected(_manualEdit.isCaseSensitive());
        
        interceptIncludeRegexTextField.setEnabled(isEnabled() && enabled);
        interceptIncludeRegexTextField.setText(_manualEdit.getIncludeRegex());
        
        interceptExcludeRegexTextField.setEnabled(isEnabled() && enabled);
        interceptExcludeRegexTextField.setText(_manualEdit.getExcludeRegex());
        
        interceptMethodList.setEnabled(isEnabled() && enabled);
        String[] interceptMethods = _manualEdit.getInterceptMethods();
        interceptMethodList.setSelectedIndices(getIndices(interceptMethods,interceptMethodList.getModel()));
        
        enabled = _manualEdit.getInterceptResponse();
        interceptResponseTextField.setEnabled(isEnabled() && enabled);
        interceptResponseCheckBox.setSelected(_manualEdit.getInterceptResponse());
        interceptResponseTextField.setText(_manualEdit.getInterceptResponseRegex());
    }
    
    /**
     * Gets the indices.
     * 
     * @param items the items
     * @param model the model
     * 
     * @return the indices
     */
    private int[] getIndices(String[] items, ListModel model) {
        int[] indices = new int[items.length];
        for (int i=0; i< items.length; i++) {
            boolean found = false;
            for (int j=0; j<model.getSize(); j++) {
                if (items[i].equals(model.getElementAt(j))) {
                    indices[i] = j;
                    found = true;
                }
            }
            if (!found) {
                indices[i] = -1;
                System.err.println("Did not find item["+i+"] == '" + items[i] + "' in the list model");
            }
        }
        return indices;
    }
    
    /**
     * Inits the components.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        interceptrequestPanel = new javax.swing.JPanel();
        interceptRequestCheckBox = new javax.swing.JCheckBox();
        sensitiveCheckBox = new javax.swing.JCheckBox();
        interceptIncludeLabel = new javax.swing.JLabel();
        interceptIncludeRegexTextField = new javax.swing.JTextField();
        interceptExcludeLabel = new javax.swing.JLabel();
        interceptExcludeRegexTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        interceptMethodList = new javax.swing.JList();
        interceptResponsePanel = new javax.swing.JPanel();
        interceptResponseCheckBox = new javax.swing.JCheckBox();
        interceptResponseTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        interceptrequestPanel.setLayout(new java.awt.GridBagLayout());

        interceptRequestCheckBox.setText("Intercept requests : ");
        interceptRequestCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        interceptrequestPanel.add(interceptRequestCheckBox, gridBagConstraints);

        sensitiveCheckBox.setText("Case Sensitive Regular Expressions ?");
        sensitiveCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        sensitiveCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sensitiveCheckBoxActionPerformed(evt);
            }
        });

        interceptrequestPanel.add(sensitiveCheckBox, new java.awt.GridBagConstraints());

        interceptIncludeLabel.setText("Include Paths matching : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        interceptrequestPanel.add(interceptIncludeLabel, gridBagConstraints);

        interceptIncludeRegexTextField.setToolTipText("Use a regular expression to select which URLs to intercept. Leave blank to ignore.");
        interceptIncludeRegexTextField.setEnabled(false);
        interceptIncludeRegexTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interceptIncludeRegexTextFieldActionPerformed(evt);
            }
        });
        interceptIncludeRegexTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                interceptIncludeRegexTextFieldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        interceptrequestPanel.add(interceptIncludeRegexTextField, gridBagConstraints);

        interceptExcludeLabel.setText("Exclude paths matching : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        interceptrequestPanel.add(interceptExcludeLabel, gridBagConstraints);

        interceptExcludeRegexTextField.setToolTipText("Use a regular expression to select which URLs not to intercept. Leave blank to ignore.");
        interceptExcludeRegexTextField.setEnabled(false);
        interceptExcludeRegexTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interceptExcludeRegexTextFieldActionPerformed(evt);
            }
        });
        interceptExcludeRegexTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                interceptExcludeRegexTextFieldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        interceptrequestPanel.add(interceptExcludeRegexTextField, gridBagConstraints);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setViewportBorder(new javax.swing.border.TitledBorder("Methods"));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(100, 120));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(100, 120));
        jScrollPane3.setAutoscrolls(true);
        interceptMethodList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "GET", "POST", "HEAD", "PUT", "DELETE", "TRACE", "PROPFIND", "OPTIONS" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        interceptMethodList.setToolTipText("Select which request methods to intercept");
        interceptMethodList.setVisibleRowCount(0);
        interceptMethodList.setEnabled(false);
        jScrollPane3.setViewportView(interceptMethodList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        interceptrequestPanel.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(interceptrequestPanel, gridBagConstraints);

        interceptResponsePanel.setLayout(new java.awt.GridBagLayout());

        interceptResponseCheckBox.setText("Intercept responses : ");
        interceptResponseCheckBox.setToolTipText("");
        interceptResponseCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        interceptResponsePanel.add(interceptResponseCheckBox, gridBagConstraints);

        interceptResponseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interceptResponseTextFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        interceptResponsePanel.add(interceptResponseTextField, gridBagConstraints);

        jLabel1.setText("Only MIME-Types matching :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        interceptResponsePanel.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(interceptResponsePanel, gridBagConstraints);

    }//GEN-END:initComponents
    
    /**
     * Sensitive check box action performed.
     * 
     * @param evt the evt
     */
    private void sensitiveCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sensitiveCheckBoxActionPerformed
        _manualEdit.setCaseSensitive(sensitiveCheckBox.isSelected());
    }//GEN-LAST:event_sensitiveCheckBoxActionPerformed
    
    /**
     * Intercept response text field action performed.
     * 
     * @param evt the evt
     */
    private void interceptResponseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interceptResponseTextFieldActionPerformed
        _manualEdit.setInterceptResponseRegex(interceptResponseTextField.getText());
    }//GEN-LAST:event_interceptResponseTextFieldActionPerformed
    
    /**
     * Intercept exclude regex text field focus lost.
     * 
     * @param evt the evt
     */
    private void interceptExcludeRegexTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_interceptExcludeRegexTextFieldFocusLost
        _manualEdit.setExcludeRegex(interceptExcludeRegexTextField.getText());
    }//GEN-LAST:event_interceptExcludeRegexTextFieldFocusLost
    
    /**
     * Intercept exclude regex text field action performed.
     * 
     * @param evt the evt
     */
    private void interceptExcludeRegexTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interceptExcludeRegexTextFieldActionPerformed
        _manualEdit.setExcludeRegex(interceptExcludeRegexTextField.getText());
    }//GEN-LAST:event_interceptExcludeRegexTextFieldActionPerformed
    
    /**
     * Intercept include regex text field focus lost.
     * 
     * @param evt the evt
     */
    private void interceptIncludeRegexTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_interceptIncludeRegexTextFieldFocusLost
        _manualEdit.setIncludeRegex(interceptIncludeRegexTextField.getText());
    }//GEN-LAST:event_interceptIncludeRegexTextFieldFocusLost
    
    /**
     * Intercept include regex text field action performed.
     * 
     * @param evt the evt
     */
    private void interceptIncludeRegexTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interceptIncludeRegexTextFieldActionPerformed
        _manualEdit.setIncludeRegex(interceptIncludeRegexTextField.getText());
    }//GEN-LAST:event_interceptIncludeRegexTextFieldActionPerformed
            
    /**
     * Intercept method list value changed.
     * 
     * @param evt the evt
     */
    private void interceptMethodListValueChanged(ListSelectionEvent evt) {
        int[] indices = interceptMethodList.getSelectedIndices();
        String[] methods = new String[indices.length];
        ListModel lm = interceptMethodList.getModel();
        for (int i=0; i< indices.length; i++) {
            methods[i] = (String) lm.getElementAt(indices[i]);
        }
        _manualEdit.setInterceptMethods(methods);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.proxy.swing.ProxyPluginUI#getPanel()
     */
    public JPanel getPanel() {
        return this;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.proxy.ManualEditUI#editRequest(edu.lnmiit.wavd.model.Request)
     */
    public Request editRequest(Request request) {
        ManualEditFrame mef = new ManualEditFrame();
        mef.setTitle("Edit Request");
        mef.setInterceptModels(interceptRequestCheckBox.getModel(), interceptResponseCheckBox.getModel());
        return mef.editRequest(request);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.proxy.ManualEditUI#editResponse(edu.lnmiit.wavd.model.Request, edu.lnmiit.wavd.model.Response)
     */
    public Response editResponse(Request request, Response response) {
        ManualEditFrame mef = new ManualEditFrame();
        mef.setTitle("Edit Response");
        mef.setInterceptModels(interceptRequestCheckBox.getModel(), interceptResponseCheckBox.getModel());
        return mef.editResponse(request, response);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        interceptRequestCheckBox.setEnabled(enabled);
        interceptResponseCheckBox.setEnabled(enabled);
        interceptMethodList.setEnabled(enabled && interceptRequestCheckBox.isSelected());
        interceptIncludeRegexTextField.setEnabled(enabled && interceptRequestCheckBox.isSelected());
        interceptExcludeRegexTextField.setEnabled(enabled && interceptRequestCheckBox.isSelected());
        interceptResponseTextField.setEnabled(enabled && interceptResponseCheckBox.isSelected());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The intercept exclude label. */
    private javax.swing.JLabel interceptExcludeLabel;
    
    /** The intercept exclude regex text field. */
    private javax.swing.JTextField interceptExcludeRegexTextField;
    
    /** The intercept include label. */
    private javax.swing.JLabel interceptIncludeLabel;
    
    /** The intercept include regex text field. */
    private javax.swing.JTextField interceptIncludeRegexTextField;
    
    /** The intercept method list. */
    private javax.swing.JList interceptMethodList;
    
    /** The intercept request check box. */
    private javax.swing.JCheckBox interceptRequestCheckBox;
    
    /** The intercept response check box. */
    private javax.swing.JCheckBox interceptResponseCheckBox;
    
    /** The intercept response panel. */
    private javax.swing.JPanel interceptResponsePanel;
    
    /** The intercept response text field. */
    private javax.swing.JTextField interceptResponseTextField;
    
    /** The interceptrequest panel. */
    private javax.swing.JPanel interceptrequestPanel;
    
    /** The j label1. */
    private javax.swing.JLabel jLabel1;
    
    /** The j scroll pane3. */
    private javax.swing.JScrollPane jScrollPane3;
    
    /** The sensitive check box. */
    private javax.swing.JCheckBox sensitiveCheckBox;
    // End of variables declaration//GEN-END:variables
    
}
