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
 * RevealHiddenPanel.java
 *
 * Created on July 25, 2003, 10:58 PM
 */

package edu.lnmiit.wavd.plugin.proxy.swing;

import edu.lnmiit.wavd.plugin.proxy.BrowserCache;
import edu.lnmiit.wavd.plugin.proxy.CookieTracker;
import edu.lnmiit.wavd.plugin.proxy.RevealHidden;

// TODO: Auto-generated Javadoc
/**
 * The Class MiscPanel.
 */
public class MiscPanel extends javax.swing.JPanel implements ProxyPluginUI {
    
    /** The _reveal hidden. */
    private RevealHidden _revealHidden;
    
    /** The _browsercache. */
    private BrowserCache _browsercache;
    
    /** The _cookie tracker. */
    private CookieTracker _cookieTracker;
    
    /**
     * Instantiates a new misc panel.
     * 
     * @param rh the rh
     * @param bc the bc
     * @param ct the ct
     */
    public MiscPanel(RevealHidden rh, BrowserCache bc, CookieTracker ct) {
        _revealHidden = rh;
        _browsercache = bc;
        _cookieTracker = ct;
        
        initComponents();
        configure();
    }

    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.proxy.swing.ProxyPluginUI#getPanel()
     */
    public javax.swing.JPanel getPanel() {
        return this;
    }    
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.PluginUI#getPluginName()
     */
    public String getPluginName() {
        return new String("Miscellaneous");
    }    
    
    /**
     * Configure.
     */
    private void configure() {
        interceptHiddenFieldCheckBox.setSelected(_revealHidden.getEnabled());
        browserCacheCheckBox.setSelected(_browsercache.getEnabled());
        injectRequestCookiesCheckBox.setSelected(_cookieTracker.getInjectRequests());
        readResponseCookiesCheckBox.setSelected(_cookieTracker.getReadResponses());
    }
    
    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        interceptHiddenFieldCheckBox = new javax.swing.JCheckBox();
        browserCacheCheckBox = new javax.swing.JCheckBox();
        spacerLabel = new javax.swing.JLabel();
        injectRequestCookiesCheckBox = new javax.swing.JCheckBox();
        readResponseCookiesCheckBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        interceptHiddenFieldCheckBox.setText("Reveal hidden fields in HTML pages : ");
        interceptHiddenFieldCheckBox.setToolTipText("Select this to change all hidden form fields to text fields. Looks at pages of type HTML and javascript.");
        interceptHiddenFieldCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        interceptHiddenFieldCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interceptHiddenFieldCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(interceptHiddenFieldCheckBox, gridBagConstraints);

        browserCacheCheckBox.setText("Prevent browser from caching content : ");
        browserCacheCheckBox.setToolTipText("");
        browserCacheCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        browserCacheCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browserCacheCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(browserCacheCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(spacerLabel, gridBagConstraints);

        injectRequestCookiesCheckBox.setText("Inject known cookies into requests");
        injectRequestCookiesCheckBox.setToolTipText("");
        injectRequestCookiesCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        injectRequestCookiesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                injectRequestCookiesCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(injectRequestCookiesCheckBox, gridBagConstraints);

        readResponseCookiesCheckBox.setText("Get cookies from responses");
        readResponseCookiesCheckBox.setToolTipText("");
        readResponseCookiesCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        readResponseCookiesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readResponseCookiesCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(readResponseCookiesCheckBox, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Read response cookies check box action performed.
     * 
     * @param evt the evt
     */
    private void readResponseCookiesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readResponseCookiesCheckBoxActionPerformed
        _cookieTracker.setReadResponses(readResponseCookiesCheckBox.isSelected());
    }//GEN-LAST:event_readResponseCookiesCheckBoxActionPerformed

    /**
     * Inject request cookies check box action performed.
     * 
     * @param evt the evt
     */
    private void injectRequestCookiesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_injectRequestCookiesCheckBoxActionPerformed
        _cookieTracker.setInjectRequests(injectRequestCookiesCheckBox.isSelected());
    }//GEN-LAST:event_injectRequestCookiesCheckBoxActionPerformed

    /**
     * Browser cache check box action performed.
     * 
     * @param evt the evt
     */
    private void browserCacheCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browserCacheCheckBoxActionPerformed
        _browsercache.setEnabled(browserCacheCheckBox.isSelected());
    }//GEN-LAST:event_browserCacheCheckBoxActionPerformed

    /**
     * Intercept hidden field check box action performed.
     * 
     * @param evt the evt
     */
    private void interceptHiddenFieldCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interceptHiddenFieldCheckBoxActionPerformed
        _revealHidden.setEnabled(interceptHiddenFieldCheckBox.isSelected());
    }//GEN-LAST:event_interceptHiddenFieldCheckBoxActionPerformed

    /* (non-Javadoc)
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        browserCacheCheckBox.setEnabled(enabled);
        injectRequestCookiesCheckBox.setEnabled(enabled);
        interceptHiddenFieldCheckBox.setEnabled(enabled);
        readResponseCookiesCheckBox.setEnabled(enabled);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The browser cache check box. */
    private javax.swing.JCheckBox browserCacheCheckBox;
    
    /** The inject request cookies check box. */
    private javax.swing.JCheckBox injectRequestCookiesCheckBox;
    
    /** The intercept hidden field check box. */
    private javax.swing.JCheckBox interceptHiddenFieldCheckBox;
    
    /** The read response cookies check box. */
    private javax.swing.JCheckBox readResponseCookiesCheckBox;
    
    /** The spacer label. */
    private javax.swing.JLabel spacerLabel;
    // End of variables declaration//GEN-END:variables
    
}
