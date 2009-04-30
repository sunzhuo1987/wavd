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
 * ManualRequestPanel.java
 *
 * Created on September 15, 2003, 11:16 AM
 */

package edu.lnmiit.wavd.plugin.manualrequest.swing;

import java.net.MalformedURLException;




import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.plugin.manualrequest.ManualRequest;
import edu.lnmiit.wavd.plugin.manualrequest.ManualRequestModel;
import edu.lnmiit.wavd.plugin.manualrequest.ManualRequestUI;
import edu.lnmiit.wavd.ui.swing.ConversationListModel;
import edu.lnmiit.wavd.ui.swing.ConversationRenderer;
import edu.lnmiit.wavd.ui.swing.RequestPanel;
import edu.lnmiit.wavd.ui.swing.ResponsePanel;
import edu.lnmiit.wavd.ui.swing.SwingPluginUI;
import edu.lnmiit.wavd.util.swing.ColumnDataModel;
import edu.lnmiit.wavd.util.swing.ListComboBoxModel;
import edu.lnmiit.wavd.util.swing.SwingWorker;

import javax.swing.border.TitledBorder;
import javax.swing.SwingUtilities;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.ComboBoxModel;
import javax.swing.ListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ManualRequestPanel.
 */
public class ManualRequestPanel extends javax.swing.JPanel implements SwingPluginUI, ManualRequestUI {
    
    /** The _model. */
    private ManualRequestModel _model;
    
    /** The _manual request. */
    private ManualRequest _manualRequest;
    
    /** The _sw. */
    private SwingWorker _sw = null;
    
    /** The _busy. */
    private boolean _busy = false;
    
    /** The _request panel. */
    private final RequestPanel _requestPanel;
    
    /** The _response panel. */
    private final ResponsePanel _responsePanel;
    
    /** The _req updater. */
    private final RequestUpdater _reqUpdater = new RequestUpdater();
    
    /** The _resp updater. */
    private final ResponseUpdater _respUpdater = new ResponseUpdater();
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(this.getClass().getName());
    
    /**
     * Instantiates a new manual request panel.
     * 
     * @param manualRequest the manual request
     */
    public ManualRequestPanel(ManualRequest manualRequest) {
        initComponents();
        
        _manualRequest = manualRequest;
        _model = _manualRequest.getModel();
        
        Request request = new Request();
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        _requestPanel = new RequestPanel();
        _requestPanel.setEditable(true);
        _requestPanel.setRequest(request);
        _requestPanel.setBorder(new TitledBorder("Request"));
        conversationSplitPane.setLeftComponent(_requestPanel);
        
        _responsePanel = new ResponsePanel();
        _responsePanel.setEditable(false);
        _responsePanel.setResponse(null);
        _responsePanel.setBorder(new TitledBorder("Response"));
        conversationSplitPane.setRightComponent(_responsePanel);
        
        ListModel conversationList = new ConversationListModel(_model.getConversationModel());
        ComboBoxModel requestModel = new ListComboBoxModel(conversationList);
        requestComboBox.setModel(requestModel);
        requestComboBox.setRenderer(new ConversationRenderer(_model.getConversationModel()));
        
        requestComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                requestComboBoxActionPerformed(e);
            }
        });
        
        _manualRequest.setUI(this);
    }
    
    /**
     * Inits the components.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        requestComboBox = new javax.swing.JComboBox();
        conversationSplitPane = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        getCookieButton = new javax.swing.JButton();
        fetchResponseButton = new javax.swing.JButton();
        updateCookiesButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Previous Requests : ");
        jLabel1.setMinimumSize(new java.awt.Dimension(135, 15));
        jPanel1.add(jLabel1, java.awt.BorderLayout.WEST);

        jPanel1.add(requestComboBox, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        conversationSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        conversationSplitPane.setResizeWeight(0.5);
        conversationSplitPane.setOneTouchExpandable(true);
        add(conversationSplitPane, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout());

        getCookieButton.setText("Get Cookies");
        getCookieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getCookieButtonActionPerformed(evt);
            }
        });

        jPanel2.add(getCookieButton);

        fetchResponseButton.setText("Fetch Response");
        fetchResponseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fetchResponseButtonActionPerformed(evt);
            }
        });

        jPanel2.add(fetchResponseButton);

        updateCookiesButton.setText("Update CookieJar");
        updateCookiesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCookiesButtonActionPerformed(evt);
            }
        });

        jPanel2.add(updateCookiesButton);

        add(jPanel2, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents
    
    /**
     * Request combo box action performed.
     * 
     * @param evt the evt
     */
    private void requestComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        Object o = requestComboBox.getSelectedItem();
        if (o instanceof ConversationID) {
            ConversationID id = (ConversationID) o;
            Request request = _model.getConversationModel().getRequest(id);
            _manualRequest.setRequest(request);
        }
    }
    
    /**
     * Update cookies button action performed.
     * 
     * @param evt the evt
     */
    private void updateCookiesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCookiesButtonActionPerformed
        _manualRequest.updateCookies();
    }//GEN-LAST:event_updateCookiesButtonActionPerformed
    
    /**
     * Gets the cookie button action performed.
     * 
     * @param evt the evt
     * 
     * @return the cookie button action performed
     */
    private void getCookieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getCookieButtonActionPerformed
        try {
            Request request = _requestPanel.getRequest();
            if (request != null && request.getURL() != null) {
                _manualRequest.setRequest(request);
                _manualRequest.addRequestCookies();
            }
        } catch (MalformedURLException mue) {
            JOptionPane.showMessageDialog(this, new String[] {"The URL requested is malformed", mue.getMessage()}, "Malformed URL", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_getCookieButtonActionPerformed
    
    /**
     * Fetch response button action performed.
     * 
     * @param evt the evt
     */
    private void fetchResponseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fetchResponseButtonActionPerformed
        try {
            Request request = _requestPanel.getRequest();
            fetchResponse(request);
        } catch (MalformedURLException mue) {
            JOptionPane.showMessageDialog(this, new String[] {"The URL requested is malformed", mue.getMessage()}, "Malformed URL", JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_fetchResponseButtonActionPerformed
    
    /**
     * Fetch response.
     * 
     * @param request the request
     */
    private void fetchResponse(final Request request) {
        if (request != null) {
            _requestPanel.setEnabled(false);
            fetchResponseButton.setEnabled(false);
            updateCookiesButton.setEnabled(false);
            getCookieButton.setEnabled(false);
            _manualRequest.setRequest(request);
            new SwingWorker() {
                public Object construct() {
                    try {
                        _manualRequest.fetchResponse();
                        return null;
                    } catch (IOException ioe) {
                        return ioe;
                    }
                }
                
                //Runs on the event-dispatching thread.
                public void finished() {
                    Object obj = getValue();
                    if (obj != null) {
                        Exception e = (Exception) obj;
                        JOptionPane.showMessageDialog(null, new String[] {"Error fetching response: ", e.toString()}, "Error", JOptionPane.ERROR_MESSAGE);
                        _logger.severe("IOException fetching response: " + e);
                        e.printStackTrace();
                    }
                    fetchResponseButton.setEnabled(true);
                    updateCookiesButton.setEnabled(true);
                    getCookieButton.setEnabled(true);
                    _requestPanel.setEnabled(true);
                }
            }.start();
        } else {
            _logger.severe("Can't fetch a null request");
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getPanel()
     */
    public javax.swing.JPanel getPanel() {
        return this;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.PluginUI#getPluginName()
     */
    public String getPluginName() {
        return new String("Manual Request");
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getConversationActions()
     */
    public javax.swing.Action[] getConversationActions() {
        // could return two actions here: "load request in editor", and "replay request"
        return null;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getUrlActions()
     */
    public javax.swing.Action[] getUrlActions() {
        // doesn't really make sense at an URL level
        return null;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.manualrequest.ManualRequestUI#requestChanged(edu.lnmiit.wavd.model.Request)
     */
    public void requestChanged(Request request) {
        _reqUpdater.setRequest(request);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.manualrequest.ManualRequestUI#responseChanged(edu.lnmiit.wavd.model.Response)
     */
    public void responseChanged(Response response) {
        _respUpdater.setResponse(response);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    public void setEnabled(final boolean enabled) {
        if (SwingUtilities.isEventDispatchThread()) {
            fetchResponseButton.setEnabled(enabled);
            getCookieButton.setEnabled(enabled);
            updateCookiesButton.setEnabled(enabled);
            requestComboBox.setEnabled(enabled);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setEnabled(enabled);
                }
            });
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getConversationColumns()
     */
    public ColumnDataModel[] getConversationColumns() {
        return null;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getUrlColumns()
     */
    public ColumnDataModel[] getUrlColumns() {
        return null;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The conversation split pane. */
    private javax.swing.JSplitPane conversationSplitPane;
    
    /** The fetch response button. */
    private javax.swing.JButton fetchResponseButton;
    
    /** The get cookie button. */
    private javax.swing.JButton getCookieButton;
    
    /** The j label1. */
    private javax.swing.JLabel jLabel1;
    
    /** The j panel1. */
    private javax.swing.JPanel jPanel1;
    
    /** The j panel2. */
    private javax.swing.JPanel jPanel2;
    
    /** The request combo box. */
    private javax.swing.JComboBox requestComboBox;
    
    /** The update cookies button. */
    private javax.swing.JButton updateCookiesButton;
    // End of variables declaration//GEN-END:variables
    
    /**
     * The Class RequestUpdater.
     */
    private class RequestUpdater implements Runnable {
        
        /** The _req. */
        private Request _req;
        
        /**
         * Sets the request.
         * 
         * @param request the new request
         */
        public void setRequest(Request request) {
            _req = request;
            if (SwingUtilities.isEventDispatchThread()) {
                run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(this);
                } catch (Exception e) {
                    _logger.info("Exception " + e);
                }
            }
        }
        
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            _requestPanel.setEditable(true);
            _requestPanel.setRequest(_req);
        }
        
    }
    
    /**
     * The Class ResponseUpdater.
     */
    private class ResponseUpdater implements Runnable {
        
        /** The _resp. */
        private Response _resp;
        
        /**
         * Sets the response.
         * 
         * @param response the new response
         */
        public void setResponse(Response response) {
            _resp = response;
            if (SwingUtilities.isEventDispatchThread()) {
                run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(this);
                } catch (Exception e) {
                    _logger.info("Exception " + e);
                }
            }
        }
        
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            _responsePanel.setEditable(false);
            _responsePanel.setResponse(_resp);
        }
        
    }
    
}
