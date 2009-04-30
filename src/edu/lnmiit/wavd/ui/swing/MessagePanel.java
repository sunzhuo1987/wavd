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
 * MessagePanel.java
 *
 * Created on November 6, 2003, 8:43 AM
 */

package edu.lnmiit.wavd.ui.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.lnmiit.wavd.model.Message;
import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.model.Preferences;

// TODO: Auto-generated Javadoc
/**
 * The Class MessagePanel.
 */
public class MessagePanel extends javax.swing.JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 7438730900530046567L;

    /** The _cp. */
    private ContentPanel _cp;

    /** The _hp. */
    private HeaderPanel _hp;

    /** The _message. */
    private Message _message = null;

    /** The _editable. */
    private boolean _editable = false;

    /**
     * Instantiates a new message panel.
     */
    public MessagePanel() {
        initComponents();
        setName("Message");

        _hp = new HeaderPanel();
        messageSplitPane.setTopComponent(_hp);
        _cp = new ContentPanel();
        messageSplitPane.setBottomComponent(_cp);
        String dividerLocation = Preferences.getPreference("MessagePanel.dividerLocation");
        if (dividerLocation != null) {
            try {
                messageSplitPane.setDividerLocation(Integer.parseInt(dividerLocation));
            } catch (NumberFormatException nfe) {
            }
        }
        messageSplitPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("dividerLocation")) {
                    Preferences.setPreference("MessagePanel.dividerLocation", e.getNewValue().toString());
                }
            }
        });
        setEditable(false);
        setMessage(null);
    }

    /**
     * Instantiates a new message panel.
     * 
     * @param orientation
     *            the orientation
     */
    public MessagePanel(int orientation) {
        this();
        messageSplitPane.setOrientation(orientation);
    }

    /**
     * Sets the editable.
     * 
     * @param editable
     *            the new editable
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        _hp.setEditable(editable);
        _cp.setEditable(editable);
    }

    /**
     * Sets the message.
     * 
     * @param message
     *            the new message
     */
    public void setMessage(Message message) {
        _message = message;

        if (message != null) {
            _hp.setHeaders(_message.getHeaders());
            byte[] content = message.getContent();
            _cp.setContentType(message.getHeader("Content-Type"));
            _cp.setContent(content);
        } else {
            _hp.setHeaders(null);
            _cp.setContentType(null);
            _cp.setContent(null);
        }
        revalidate();
    }

    /**
     * Gets the message.
     * 
     * @return the message
     */
    public Message getMessage() {
        if (_editable) {
            if (_hp.isModified() && _message != null) {
                _message.setHeaders(_hp.getHeaders());
            }
            if (_cp.isModified()) {
                _message.setContent(_cp.getContent());
                if (_message.getHeader("Content-Length") != null) {
                    _message
                            .setHeader(new NamedValue("Content-Length", Integer.toString(_message.getContent().length)));
                }
            }
        }
        return _message;
    }

    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    public boolean isModified() {
        return _editable && (_hp.isModified() || _cp.isModified());
    }

    /**
     * Inits the components.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        messageSplitPane = new javax.swing.JSplitPane();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(400, 200));
        messageSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        messageSplitPane.setResizeWeight(0.3);
        messageSplitPane.setContinuousLayout(true);
        messageSplitPane.setDoubleBuffered(true);
        messageSplitPane.setOneTouchExpandable(true);
        add(messageSplitPane, java.awt.BorderLayout.CENTER);

    }// GEN-END:initComponents

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        edu.lnmiit.wavd.model.Response response = new edu.lnmiit.wavd.model.Response();
        try {
            String resp = "/home/rogan/workspace/webscarab/test/data/index-resp";
            if (args.length == 1) {
                resp = args[0];
            }
            java.io.FileInputStream fis = new java.io.FileInputStream(resp);
            response.read(fis);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        javax.swing.JFrame top = new javax.swing.JFrame("Message Pane");
        top.getContentPane().setLayout(new java.awt.BorderLayout());
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        javax.swing.JButton button = new javax.swing.JButton("GET");
        final MessagePanel mp = new MessagePanel();
        top.getContentPane().add(mp);
        top.getContentPane().add(button, java.awt.BorderLayout.SOUTH);
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println(mp.getMessage());
            }
        });
        // top.setBounds(100,100,600,400);
        top.pack();
        top.setVisible(true);
        try {
            mp.setEditable(true);
            mp.setMessage(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The message split pane. */
    private javax.swing.JSplitPane messageSplitPane;
    // End of variables declaration//GEN-END:variables

}
