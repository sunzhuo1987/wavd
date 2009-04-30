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
 * RequestPanel.java
 *
 * Created on 02 June 2003, 03:09
 */

package edu.lnmiit.wavd.ui.swing;

import java.net.MalformedURLException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.ui.swing.editors.TextPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestPanel.
 */
public class RequestPanel extends javax.swing.JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -188167000106701807L;

    /** The _up to date. */
    private boolean[] _upToDate;

    /** The _editable. */
    private boolean _editable = false;

    /** The _modified. */
    private boolean _modified = false;

    /** The _selected. */
    private int _selected = 0;

    /** The _request. */
    private Request _request = null;

    /** The _message panel. */
    private MessagePanel _messagePanel;

    /** The _text panel. */
    private TextPanel _textPanel;

    /** The _preferred. */
    private static int _preferred = -1;

    /** The _reverting. */
    private boolean _reverting = false;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new request panel.
     */
    public RequestPanel() {
        initComponents();

        displayTabbedPane.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                try {
                    updateRequest(_selected);
                    _selected = displayTabbedPane.getSelectedIndex();
                    _preferred = _selected;
                    if (_selected >= 0) {
                        updatePanel(_selected);
                    }
                } catch (MalformedURLException mue) {
                    if (!_reverting) {
                        JOptionPane.showMessageDialog(RequestPanel.this, new String[] {
                                "The URL requested is malformed", mue.getMessage() }, "Malformed URL",
                                JOptionPane.ERROR_MESSAGE);
                        _reverting = true;
                        displayTabbedPane.setSelectedIndex(_selected);
                        _reverting = false;
                    }
                }
            }
        });

        _messagePanel = new MessagePanel();

        parsedPanel.remove(messagePanelPlaceHolder);
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        parsedPanel.add(_messagePanel, gridBagConstraints);

        _textPanel = new TextPanel();
        displayTabbedPane.add("Raw", _textPanel);

        _upToDate = new boolean[displayTabbedPane.getTabCount()];
        invalidatePanels();

        updateComponents(_editable);

        if (_preferred > -1 && _preferred < displayTabbedPane.getTabCount())
            displayTabbedPane.setSelectedIndex(_preferred);
    }

    /**
     * Invalidate panels.
     */
    private void invalidatePanels() {
        for (int i = 0; i < _upToDate.length; i++) {
            _upToDate[i] = false;
        }
    }

    /**
     * Update request.
     * 
     * @param panel
     *            the panel
     * 
     * @throws MalformedURLException
     *             the malformed url exception
     */
    private void updateRequest(int panel) throws MalformedURLException {
        if (!_editable || panel < 0) {
            return;
        }
        if (displayTabbedPane.getTitleAt(panel).equals("Parsed")) {// parsed
            // text
            if (_messagePanel.isModified()) {
                _request = (Request) _messagePanel.getMessage();
                _modified = true;
            }
            if (_request == null) {
                _request = new Request();
            }
            _request.setMethod(methodTextField.getText());
            String url = urlTextField.getText();
            if (!"".equals(url))
                _request.setURL(new HttpUrl(url));
            _request.setVersion(versionTextField.getText());
            // this is a bit of a hack. What we should really do is add a
            // listener
            // to the text fields, so we know when a change has been made. Until
            // then
            // this will do
            _modified = true;
        } else if (displayTabbedPane.getTitleAt(panel).equals("Raw")) { // raw
            // text
            if (_textPanel.isModified()) {
                try {
                    Request r = new Request();
                    String text = _textPanel.getText();
                    if (!"".equals(text))
                        r.parse(_textPanel.getText());
                    _request = r;
                } catch (Exception e) {
                    _logger.severe("Error parsing the rawTextArea, abandoning changes : " + e);
                }
                _modified = true;
            }
        }
        if (_modified)
            invalidatePanels();
        _upToDate[panel] = true;
    }

    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    public boolean isModified() {
        return _modified;
    }

    /**
     * Update panel.
     * 
     * @param panel
     *            the panel
     */
    private void updatePanel(int panel) {
        if (!_upToDate[panel]) {
            if (displayTabbedPane.getTitleAt(panel).equals("Parsed")) {// parsed
                // text
                _messagePanel.setMessage(_request);
                if (_request != null) {
                    methodTextField.setText(_request.getMethod());
                    if (_request.getURL() != null) {
                        urlTextField.setText(_request.getURL().toString());
                    } else {
                        urlTextField.setText("");
                    }
                    versionTextField.setText(_request.getVersion());
                } else {
                    methodTextField.setText("");
                    urlTextField.setText("");
                    versionTextField.setText("");
                }
            } else if (displayTabbedPane.getTitleAt(panel).equals("Raw")) { // raw
                // text
                if (_request != null && _request.getMethod() != null && _request.getURL() != null
                        && _request.getVersion() != null) {
                    _textPanel.setText(null, _request.toString("\n"));
                } else {
                    _textPanel.setText(null, "");
                }
            }
            _upToDate[panel] = true;
        }
    }

    /**
     * Update components.
     * 
     * @param editable
     *            the editable
     */
    private void updateComponents(boolean editable) {
        java.awt.Color color;
        if (editable) {
            color = new java.awt.Color(255, 255, 255);
        } else {
            color = new java.awt.Color(204, 204, 204);
        }
        methodTextField.setEditable(editable);
        urlTextField.setEditable(editable);
        versionTextField.setEditable(editable);
        methodTextField.setBackground(color);
        urlTextField.setBackground(color);
        versionTextField.setBackground(color);
    }

    /**
     * Sets the editable.
     * 
     * @param editable
     *            the new editable
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        _textPanel.setEditable(editable);
        updateComponents(editable);
        _messagePanel.setEditable(editable);
    }

    /**
     * Sets the request.
     * 
     * @param request
     *            the new request
     */
    public void setRequest(Request request) {
        _modified = false;
        if (request != null) {
            _request = new Request(request);
        } else {
            _request = null;
        }
        invalidatePanels();
        if (SwingUtilities.isEventDispatchThread()) {
            updatePanel(displayTabbedPane.getSelectedIndex());
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    updatePanel(displayTabbedPane.getSelectedIndex());
                }
            });
        }
    }

    /**
     * Gets the request.
     * 
     * @return the request
     * 
     * @throws MalformedURLException
     *             the malformed url exception
     */
    public Request getRequest() throws MalformedURLException {
        if (_editable) {
            int panel = displayTabbedPane.getSelectedIndex();
            updateRequest(panel);
        }
        return _request;
    }

    /**
     * Select panel.
     * 
     * @param title
     *            the title
     */
    public void selectPanel(String title) {
        for (int i = 0; i < displayTabbedPane.getTabCount(); i++) {
            String tab = displayTabbedPane.getTitleAt(i);
            int selected = displayTabbedPane.getSelectedIndex();
            if (tab != null && tab.equalsIgnoreCase(title) && i != selected) {
                displayTabbedPane.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * Inits the components.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        displayTabbedPane = new javax.swing.JTabbedPane();
        parsedPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        methodTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        urlTextField = new javax.swing.JTextField();
        messagePanelPlaceHolder = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        versionTextField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        parsedPanel.setLayout(new java.awt.GridBagLayout());

        jLabel3.setLabelFor(methodTextField);
        jLabel3.setText("Method");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        parsedPanel.add(jLabel3, gridBagConstraints);

        methodTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        parsedPanel.add(methodTextField, gridBagConstraints);

        jLabel4.setLabelFor(urlTextField);
        jLabel4.setText("URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        parsedPanel.add(jLabel4, gridBagConstraints);

        urlTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        parsedPanel.add(urlTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        parsedPanel.add(messagePanelPlaceHolder, gridBagConstraints);

        jLabel5.setLabelFor(urlTextField);
        jLabel5.setText("Version");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        parsedPanel.add(jLabel5, gridBagConstraints);

        versionTextField.setBackground(new java.awt.Color(204, 204, 204));
        versionTextField.setEditable(false);
        versionTextField.setText("HTTP/1.0");
        versionTextField.setMinimumSize(new java.awt.Dimension(65, 19));
        versionTextField.setPreferredSize(new java.awt.Dimension(65, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        parsedPanel.add(versionTextField, gridBagConstraints);

        displayTabbedPane.addTab("Parsed", parsedPanel);

        add(displayTabbedPane, java.awt.BorderLayout.CENTER);

    }// GEN-END:initComponents

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        final RequestPanel panel = new RequestPanel();
        javax.swing.JFrame top = new javax.swing.JFrame(panel.getName());
        top.getContentPane().setLayout(new java.awt.BorderLayout());
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
        javax.swing.JButton button = new javax.swing.JButton("GET");
        top.getContentPane().add(panel);
        top.getContentPane().add(button, java.awt.BorderLayout.SOUTH);
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    System.out.println(panel.getRequest());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // top.setBounds(100,100,600,400);
        top.pack();
        top.setVisible(true);

        Request request = new Request();
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream("l2/conversations/1-request");
            request.read(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        panel.setEditable(true);
        panel.setRequest(request);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The display tabbed pane. */
    private javax.swing.JTabbedPane displayTabbedPane;

    /** The j label3. */
    private javax.swing.JLabel jLabel3;

    /** The j label4. */
    private javax.swing.JLabel jLabel4;

    /** The j label5. */
    private javax.swing.JLabel jLabel5;

    /** The message panel place holder. */
    private javax.swing.JPanel messagePanelPlaceHolder;

    /** The method text field. */
    private javax.swing.JTextField methodTextField;

    /** The parsed panel. */
    private javax.swing.JPanel parsedPanel;

    /** The url text field. */
    private javax.swing.JTextField urlTextField;

    /** The version text field. */
    private javax.swing.JTextField versionTextField;
    // End of variables declaration//GEN-END:variables

}
