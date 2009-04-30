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
 * ConversationPanel2.java
 *
 * Created on 10 June 2005, 12:55
 */

package edu.lnmiit.wavd.ui.swing;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;


import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.util.swing.ListComboBoxModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.border.TitledBorder;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class ConversationPanel.
 */
public class ConversationPanel extends javax.swing.JPanel {
    
    /** The _preferred size. */
    private static Dimension _preferredSize = null;
    
    /** The _preferred location. */
    private static Point _preferredLocation = null;
    
    /** The _model. */
    private ConversationModel _model = null;
    
    /** The _selected. */
    private ConversationID _selected = null;
    
    /** The _frame. */
    private JFrame _frame = null;
    
    /** The _title. */
    private String _title = null;
    
    /** The _request panel. */
    private RequestPanel _requestPanel;
    
    /** The _response panel. */
    private ResponsePanel _responsePanel;
    
    /**
     * Instantiates a new conversation panel.
     */
    public ConversationPanel() {
        initComponents();
        addPanels();
    }
    
    /**
     * Instantiates a new conversation panel.
     * 
     * @param model the model
     */
    public ConversationPanel(ConversationModel model) {
        _model = model;
        initComponents();
        addPanels();
        ConversationListModel clm = new ConversationListModel(model);
        conversationComboBox.setModel(new ListComboBoxModel(clm));
        conversationComboBox.setRenderer(new ConversationRenderer(model));
        add(toolBar, BorderLayout.NORTH);
        getActionMap().put("TOGGLELAYOUT", new AbstractAction() {
            private static final long serialVersionUID = 1558804946998494321L;
            
            public void actionPerformed(ActionEvent event) {
                if (conversationSplitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                    conversationSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                } else {
                    conversationSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                }
            }
        });
        InputMap inputMap = getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK),
                "TOGGLELAYOUT");
    }
    
    /**
     * Adds the panels.
     */
    private void addPanels() {
        _requestPanel = new RequestPanel();
        // _requestPanel.setBorder(new TitledBorder("Request"));
        conversationSplitPane.setTopComponent(_requestPanel);
        
        _responsePanel = new ResponsePanel();
        // _responsePanel.setBorder(new TitledBorder("Response"));
        conversationSplitPane.setBottomComponent(_responsePanel);
        
        String orientation = Preferences.getPreference("ConversationPanel.orientation");
        if (orientation != null) {
            try {
                conversationSplitPane.setOrientation(Integer.parseInt(orientation));
            } catch (NumberFormatException nfe) {}
        }
        String dividerLocation = Preferences.getPreference("ConversationPanel.dividerLocation");
        if (dividerLocation != null) {
            try {
                conversationSplitPane.setDividerLocation(Integer.parseInt(dividerLocation));
            } catch (NumberFormatException nfe) {}
        }
        conversationSplitPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
                    Preferences.setPreference("ConversationPanel.dividerLocation", e.getNewValue().toString());
                } else if (e.getPropertyName().equals(JSplitPane.ORIENTATION_PROPERTY)) {
                    Preferences.setPreference("ConversationPanel.orientation", e.getNewValue().toString());
                }
            }
        });
    }
    
    /**
     * Resize frame.
     */
    private void resizeFrame() {
        if (_preferredSize == null) {
            try {
                int width = Integer.parseInt(Preferences.getPreference("ConversationFrame.width","600"));
                int height = Integer.parseInt(Preferences.getPreference("ConversationFrame.height","500"));
                _preferredSize = new Dimension(width, height);
            } catch (Exception e) {
                _preferredSize = new Dimension(800, 600);
            }
        }
        if (_preferredLocation == null) {
            try {
                String value = Preferences.getPreference("ConversationFrame.x");
                if (value != null) {
                    int x = Integer.parseInt(value);
                    value = Preferences.getPreference("ConversationFrame.y");
                    int y = Integer.parseInt(value);
                    _preferredLocation = new Point(x,y);
                }
            } catch (Exception e) {
            }
        }
        if (_preferredLocation != null) _frame.setLocation(_preferredLocation);
        if (_preferredSize != null) _frame.setSize(_preferredSize);
        
        _frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                if (!_frame.isVisible()) return;
                _preferredLocation = _frame.getLocation();
                Preferences.setPreference("ConversationFrame.x", Integer.toString(_preferredLocation.x));
                Preferences.setPreference("ConversationFrame.y", Integer.toString(_preferredLocation.y));
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                if (!_frame.isVisible()) return;
                _preferredSize = _frame.getSize();
                Preferences.setPreference("ConversationFrame.width", Integer.toString(_preferredSize.width));
                Preferences.setPreference("ConversationFrame.height", Integer.toString(_preferredSize.height));
            }
        });
    }
    
    /**
     * Sets the request.
     * 
     * @param request the request
     * @param editable the editable
     */
    public void setRequest(Request request, boolean editable) {
        _requestPanel.setEditable(editable);
        _requestPanel.setRequest(request);
    }
    
    /**
     * Checks if is request modified.
     * 
     * @return true, if is request modified
     */
    public boolean isRequestModified() {
        return _requestPanel.isModified();
    }
    
    /**
     * Gets the request.
     * 
     * @return the request
     * 
     * @throws MalformedURLException the malformed url exception
     */
    public Request getRequest() throws MalformedURLException {
        return _requestPanel.getRequest();
    }
    
    /**
     * Sets the response.
     * 
     * @param response the response
     * @param editable the editable
     */
    public void setResponse(Response response, boolean editable) {
        _responsePanel.setEditable(editable);
        _responsePanel.setResponse(response);
    }
    
    /**
     * Checks if is response modified.
     * 
     * @return true, if is response modified
     */
    public boolean isResponseModified() {
        return _responsePanel.isModified();
    }
    
    /**
     * Gets the response.
     * 
     * @return the response
     */
    public Response getResponse() {
        return _responsePanel.getResponse();
    }
    
    /**
     * Sets the selected conversation.
     * 
     * @param id the new selected conversation
     */
    public void setSelectedConversation(ConversationID id) {
        _selected = id;
        conversationComboBox.setSelectedItem(_selected);
    }
    
    /**
     * Gets the selected conversation.
     * 
     * @return the selected conversation
     */
    public ConversationID getSelectedConversation() {
        return _selected;
    }
    
    /**
     * In frame.
     * 
     * @return the j frame
     */
    public JFrame inFrame() {
        ConversationID selected = getSelectedConversation();
        if (selected != null) {
            return inFrame("WebScarab - conversation " + selected);
        }
        return inFrame(null);
    }
    
    /**
     * In frame.
     * 
     * @param title the title
     * 
     * @return the j frame
     */
    public JFrame inFrame(String title) {
        _title = title;
        _frame = new JFrame();
        _frame.getContentPane().add(this);
        _frame.setTitle(_title);
        resizeFrame();
        return _frame;
    }
    
    /**
     * Inits the components.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        toolBar = new javax.swing.JToolBar();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        conversationComboBox = new javax.swing.JComboBox();
        conversationSplitPane = new javax.swing.JSplitPane();

        toolBar.setFloatable(false);
        previousButton.setMnemonic('P');
        previousButton.setText("Previous");
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        toolBar.add(previousButton);

        nextButton.setMnemonic('N');
        nextButton.setText("Next");
        nextButton.setMaximumSize(new java.awt.Dimension(65, 27));
        nextButton.setMinimumSize(new java.awt.Dimension(65, 27));
        nextButton.setPreferredSize(new java.awt.Dimension(65, 27));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        toolBar.add(nextButton);

        conversationComboBox.setMaximumSize(new java.awt.Dimension(600, 32767));
        conversationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conversationComboBoxActionPerformed(evt);
            }
        });

        toolBar.add(conversationComboBox);

        setLayout(new java.awt.BorderLayout());

        conversationSplitPane.setDividerLocation(100);
        conversationSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        conversationSplitPane.setResizeWeight(0.3);
        conversationSplitPane.setOneTouchExpandable(true);
        add(conversationSplitPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    /**
     * Conversation combo box action performed.
     * 
     * @param evt the evt
     */
    private void conversationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conversationComboBoxActionPerformed
        ConversationID id = (ConversationID) conversationComboBox.getSelectedItem();
        if (id == null) {
            setRequest(null, false);
            setResponse(null, false);
            if (_frame != null)
                _frame.setTitle("WebScarab - no conversation selected");
        } else {
            Request request = _model.getRequest(id);
            Response response = _model.getResponse(id);
            setRequest(request, false);
            setResponse(response, false);
            if (_frame != null)
                _frame.setTitle("WebScarab - conversation " + id);
        }
    }//GEN-LAST:event_conversationComboBoxActionPerformed
    
    /**
     * Next button action performed.
     * 
     * @param evt the evt
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        int index = conversationComboBox.getSelectedIndex();
        if (index+1<conversationComboBox.getModel().getSize())
            conversationComboBox.setSelectedIndex(index+1);
    }//GEN-LAST:event_nextButtonActionPerformed
    
    /**
     * Previous button action performed.
     * 
     * @param evt the evt
     */
    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        int index = conversationComboBox.getSelectedIndex();
        if (index>0)
            conversationComboBox.setSelectedIndex(index-1);
    }//GEN-LAST:event_previousButtonActionPerformed
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String args[]) {
        try {
            final edu.lnmiit.wavd.model.FrameworkModel model = new edu.lnmiit.wavd.model.FrameworkModel();
            model.setSession("FileSystem",new java.io.File("/tmp/l/1"),"");
            ConversationPanel cp = new ConversationPanel(model.getConversationModel());
            JFrame frame = cp.inFrame();
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    System.exit(0);
                }
            });
            
            frame.setVisible(true);
            cp.setSelectedConversation(new ConversationID(1));
        } catch (edu.lnmiit.wavd.model.StoreException se) {
            se.printStackTrace();
            System.exit(0);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The conversation combo box. */
    private javax.swing.JComboBox conversationComboBox;
    
    /** The conversation split pane. */
    private javax.swing.JSplitPane conversationSplitPane;
    
    /** The next button. */
    private javax.swing.JButton nextButton;
    
    /** The previous button. */
    private javax.swing.JButton previousButton;
    
    /** The tool bar. */
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
    
}
