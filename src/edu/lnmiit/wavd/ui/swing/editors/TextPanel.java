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
 * HexEditor.java
 *
 * Created on November 4, 2003, 8:23 AM
 */

package edu.lnmiit.wavd.ui.swing.editors;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.util.regex.PatternSyntaxException;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultHighlighter;

import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.util.CharsetUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class TextPanel.
 */
public class TextPanel extends javax.swing.JPanel implements ByteArrayEditor {

    /**
     * 
     */
    private static final long serialVersionUID = -2633864558038585241L;

    /** The _find visible. */
    private static boolean _findVisible = false;

    /** The _find. */
    private static String _find = "";

    /** The _case sensitive. */
    private static boolean _caseSensitive = false;

    /** The _start. */
    private int _start = 0;

    /** The _editable. */
    private boolean _editable = false;

    /** The _modified. */
    private boolean _modified = false;

    /** The _bytes. */
    private byte[] _bytes = null;

    /** The _charset. */
    private String _charset = null;

    /** The _text. */
    private String _text = null;

    /** The _dcl. */
    private DocumentChangeListener _dcl = new DocumentChangeListener();

    /** The searcher. */
    private RegexSearcher searcher;

    /**
     * Instantiates a new text panel.
     */
    public TextPanel() {
        initComponents();
        findCaseCheckBox.setSelected(_caseSensitive);

        setName("Text");

        searcher = new RegexSearcher(textTextArea, DefaultHighlighter.DefaultPainter);

        InputMap inputMap = getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        getActionMap().put("TOGGLEWRAP", new AbstractAction() {
            /**
	     * 
	     */
            private static final long serialVersionUID = 3180710491820185700L;

            public void actionPerformed(ActionEvent event) {
                textTextArea.setLineWrap(!textTextArea.getLineWrap());
            }
        });
        // Ctrl-W to toggle wordwrap
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK), "TOGGLEWRAP");

        getActionMap().put("TOGGLEFIND", new AbstractAction() {
            /**
	     * 
	     */
            private static final long serialVersionUID = 1667191933246150422L;

            public void actionPerformed(ActionEvent event) {
                _findVisible = !findPanel.isVisible();
                findPanel.setVisible(_findVisible);
                if (_findVisible) {
                    findTextField.requestFocusInWindow();
                }
            }
        });
        // Ctrl-F to toggle the find bar
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK), "TOGGLEFIND");

        findPanel.setVisible(_findVisible);
        findTextField.setText(_find);
        findTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent evt) {
                find();
            }

            public void removeUpdate(DocumentEvent evt) {
                find();
            }

            public void insertUpdate(DocumentEvent evt) {
                find();
            }

            private void find() {
                _find = findTextField.getText();
                _start = doFind(_find, 0, _caseSensitive) + 1;
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        textTextArea.setEditable(editable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setBytes(java.lang.String
     * , byte[])
     */
    public void setBytes(String contentType, byte[] bytes) {
        _bytes = bytes;
        if (bytes == null) {
            setText(contentType, "");
        } else {
            _charset = null;
            int ci = contentType.indexOf("charset");
            if (ci == -1) {
                _charset = CharsetUtils.getCharset(bytes);
            } else {
                _charset = contentType.substring(ci + 8);
            }
            try {
                setText(contentType, new String(bytes, _charset));
            } catch (UnsupportedEncodingException e) {
                setText(contentType, e.getMessage());
            }
        }
    }

    /**
     * Sets the text.
     * 
     * @param contentType
     *            the content type
     * @param text
     *            the text
     */
    public void setText(String contentType, String text) {
        String wrap = Preferences.getPreference("TextPanel.wrap", "false");
        if (wrap != null && wrap.equals("true"))
            textTextArea.setLineWrap(true);

        _text = text;
        textTextArea.getDocument().removeDocumentListener(_dcl);
        _modified = false;
        if (text != null)
            textTextArea.setText(text);
        textTextArea.setCaretPosition(0);
        if (_editable)
            textTextArea.getDocument().addDocumentListener(_dcl);
        _start = doFind(_find, 0, _caseSensitive) + 1;
    }

    /**
     * Gets the text.
     * 
     * @return the text
     */
    public String getText() {
        _text = textTextArea.getText();
        return _text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#isModified()
     */
    public boolean isModified() {
        return _editable && _modified;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        if (isModified()) {
            try {
                String text = getText();
                if (_charset == null)
                    _charset = CharsetUtils.getCharset(text.getBytes());
                if (_charset != null) {
                    _bytes = text.getBytes(_charset);
                } else {
                    _bytes = text.getBytes();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return _bytes;
    }

    /**
     * Do find.
     * 
     * @param pattern
     *            the pattern
     * @param start
     *            the start
     * @param caseSensitive
     *            the case sensitive
     * 
     * @return the int
     */
    private int doFind(String pattern, int start, boolean caseSensitive) {
        findMessageLabel.setText("");
        try {
            int pos = searcher.search(pattern, start, caseSensitive);
            if (start > 0 && pos == -1) {
                pos = searcher.search(pattern, 0, caseSensitive);
                if (pos > -1)
                    findMessageLabel.setText("Reached end of page, continued from top");
            }
            if (pos > -1) {
                textTextArea.setCaretPosition(pos);
            } else {
                findMessageLabel.setText("Not found");
            }
            return pos;
        } catch (PatternSyntaxException pse) {
            findMessageLabel.setText(pse.getMessage());
            return -1;
        }
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        textScrollPane = new javax.swing.JScrollPane();
        textTextArea = new javax.swing.JTextArea();
        findPanel = new javax.swing.JPanel();
        findLabel = new javax.swing.JLabel();
        findTextField = new javax.swing.JTextField();
        findNextButton = new javax.swing.JButton();
        findCaseCheckBox = new javax.swing.JCheckBox();
        findMessageLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        textTextArea.setEditable(false);
        textScrollPane.setViewportView(textTextArea);

        add(textScrollPane, java.awt.BorderLayout.CENTER);

        findPanel.setLayout(new java.awt.GridBagLayout());

        findLabel.setText("Find: ");
        findPanel.add(findLabel, new java.awt.GridBagConstraints());

        findTextField.setMinimumSize(new java.awt.Dimension(60, 19));
        findTextField.setPreferredSize(new java.awt.Dimension(80, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        findPanel.add(findTextField, gridBagConstraints);

        findNextButton.setMnemonic('N');
        findNextButton.setText("Next");
        findNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findNextButtonActionPerformed(evt);
            }
        });

        findPanel.add(findNextButton, new java.awt.GridBagConstraints());

        findCaseCheckBox.setMnemonic('M');
        findCaseCheckBox.setText("Match Case");
        findCaseCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findCaseCheckBoxActionPerformed(evt);
            }
        });

        findPanel.add(findCaseCheckBox, new java.awt.GridBagConstraints());

        findMessageLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        findPanel.add(findMessageLabel, gridBagConstraints);

        add(findPanel, java.awt.BorderLayout.SOUTH);

    }

    // </editor-fold>//GEN-END:initComponents

    /**
     * Find case check box action performed.
     * 
     * @param evt
     *            the evt
     */
    private void findCaseCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_findCaseCheckBoxActionPerformed
        _caseSensitive = findCaseCheckBox.isSelected();
        _start = doFind(_find, 0, _caseSensitive) + 1;
    }// GEN-LAST:event_findCaseCheckBoxActionPerformed

    /**
     * Find next button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void findNextButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_findNextButtonActionPerformed
        _start = doFind(_find, _start, _caseSensitive) + 1;
    }// GEN-LAST:event_findNextButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The find case check box. */
    private javax.swing.JCheckBox findCaseCheckBox;

    /** The find label. */
    private javax.swing.JLabel findLabel;

    /** The find message label. */
    private javax.swing.JLabel findMessageLabel;

    /** The find next button. */
    private javax.swing.JButton findNextButton;

    /** The find panel. */
    private javax.swing.JPanel findPanel;

    /** The find text field. */
    private javax.swing.JTextField findTextField;

    /** The text scroll pane. */
    private javax.swing.JScrollPane textScrollPane;

    /** The text text area. */
    private javax.swing.JTextArea textTextArea;

    // End of variables declaration//GEN-END:variables

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        javax.swing.JFrame top = new javax.swing.JFrame("Text Editor");
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        TextPanel tp = new TextPanel();
        top.getContentPane().add(tp);
        top.setBounds(100, 100, 600, 400);
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.FileInputStream fis = new java.io.FileInputStream("/etc/passwd");
            byte[] buff = new byte[1024];
            int got;
            while ((got = fis.read(buff)) > -1)
                baos.write(buff, 0, got);
            fis.close();
            baos.close();
            tp.setBytes("text", baos.toByteArray());
            // tp.setBytes("text",
            // "ABCDEFGHIJKLMNOP\nABCDEFGHIJKLMNOP".getBytes());
            tp.setEditable(true);
            top.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The listener interface for receiving documentChange events. The class
     * that is interested in processing a documentChange event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addDocumentChangeListener<code> method. When
     * the documentChange event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see DocumentChangeEvent
     */
    private class DocumentChangeListener implements DocumentListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.DocumentListener#changedUpdate(javax.swing.event
         * .DocumentEvent)
         */
        public void changedUpdate(DocumentEvent evt) {
            _modified = true;
            _text = null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.DocumentListener#removeUpdate(javax.swing.event
         * .DocumentEvent)
         */
        public void removeUpdate(DocumentEvent evt) {
            _modified = true;
            _text = null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.DocumentListener#insertUpdate(javax.swing.event
         * .DocumentEvent)
         */
        public void insertUpdate(DocumentEvent evt) {
            _modified = true;
            _text = null;
        }
    }

}
