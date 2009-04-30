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
 * TranscoderFrame.java
 *
 * Created on 29 May 2003, 03:18
 */

package edu.lnmiit.wavd.ui.swing;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import edu.lnmiit.wavd.util.Encoding;

// TODO: Auto-generated Javadoc
/**
 * The Class TranscoderFrame.
 */

public class TranscoderFrame extends javax.swing.JFrame implements ClipboardOwner {

    /**
     * 
     */
    private static final long serialVersionUID = -6283636461865769751L;

    /** The clipboard. */
    //private Clipboard clipboard = getToolkit().getSystemClipboard();

    /** The undo. */
    protected UndoManager undo = new UndoManager();

    /** The undo action. */
    protected UndoAction undoAction;

    /** The redo action. */
    protected RedoAction redoAction;

    /** The actions. */
    private Hashtable actions;

    /**
     * Instantiates a new transcoder frame.
     */
    public TranscoderFrame() {
        initComponents();
        createActionTable(textPane);

        undo.setLimit(10);
        undoAction = new UndoAction();
        editMenu.add(undoAction);
        redoAction = new RedoAction();
        editMenu.add(redoAction);

        editMenu.addSeparator();

        // These actions come from the default editor kit.
        // Get the ones we want and stick them in the menu.
        editMenu.add(getActionByName(DefaultEditorKit.cutAction));
        editMenu.add(getActionByName(DefaultEditorKit.copyAction));
        editMenu.add(getActionByName(DefaultEditorKit.pasteAction));

        editMenu.addSeparator();

        editMenu.add(getActionByName(DefaultEditorKit.selectAllAction));

        final Document d = textPane.getDocument();
        d.addUndoableEditListener(new MyUndoableEditListener());
        d.addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent evt) {
                update();
            }

            public void insertUpdate(DocumentEvent evt) {
                update();
            }

            public void removeUpdate(DocumentEvent evt) {
                update();
            }

            private void update() {
                countLabel.setText("Characters: " + d.getLength());
            }
        });
    }

    // The following two methods allow us to find an
    // action provided by the editor kit by its name.
    /**
     * Creates the action table.
     * 
     * @param textComponent
     *            the text component
     */
    private void createActionTable(JTextComponent textComponent) {
        actions = new Hashtable();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
    }

    /**
     * Gets the action by name.
     * 
     * @param name
     *            the name
     * 
     * @return the action by name
     */
    private Action getActionByName(String name) {
        return (Action) (actions.get(name));
    }

    /**
     * Inits the components.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        urlEncodeButton = new javax.swing.JButton();
        urlDecodeButton = new javax.swing.JButton();
        base64EncodeButton = new javax.swing.JButton();
        base64DecodeButton = new javax.swing.JButton();
        md5hashButton = new javax.swing.JButton();
        sha1hashButton = new javax.swing.JButton();
        countLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        editMenu = new javax.swing.JMenu();

        setTitle("Transcoder");
        textPane.setFont(new java.awt.Font("Monospaced", 0, 12));
        jScrollPane1.setViewportView(textPane);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        urlEncodeButton.setText("URL encode");
        urlEncodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlEncodeButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(urlEncodeButton, gridBagConstraints);

        urlDecodeButton.setText("URL decode");
        urlDecodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlDecodeButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(urlDecodeButton, gridBagConstraints);

        base64EncodeButton.setText("Base64 encode");
        base64EncodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                base64EncodeButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(base64EncodeButton, gridBagConstraints);

        base64DecodeButton.setText("Base64 decode");
        base64DecodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                base64DecodeButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(base64DecodeButton, gridBagConstraints);

        md5hashButton.setText("MD5 hash");
        md5hashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                md5hashButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(md5hashButton, gridBagConstraints);

        sha1hashButton.setText("SHA-1 hash");
        sha1hashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha1hashButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(sha1hashButton, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        countLabel.setText("Characters: 0");
        getContentPane().add(countLabel, java.awt.BorderLayout.NORTH);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 600) / 2, (screenSize.height - 400) / 2, 600, 400);
    }// GEN-END:initComponents

    /**
     * Sha1hash button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void sha1hashButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_sha1hashButtonActionPerformed
        if (textPane.getSelectionEnd() == textPane.getSelectionStart())
            textPane.select(0, textPane.getText().length());
        textPane.replaceSelection(Encoding.hashSHA(textPane.getSelectedText()));
    }// GEN-LAST:event_sha1hashButtonActionPerformed

    /**
     * Md5hash button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void md5hashButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_md5hashButtonActionPerformed
        if (textPane.getSelectionEnd() == textPane.getSelectionStart())
            textPane.select(0, textPane.getText().length());
        textPane.replaceSelection(Encoding.hashMD5(textPane.getSelectedText()));
    }// GEN-LAST:event_md5hashButtonActionPerformed

    /**
     * Base64 decode button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void base64DecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
                                                                                    // -
                                                                                    // FIRST
                                                                                    // :
        // event_base64DecodeButtonActionPerformed
        try {
            if (textPane.getSelectionEnd() == textPane.getSelectionStart())
                textPane.select(0, textPane.getText().length());
            textPane.replaceSelection(new String(Encoding.base64decode(textPane.getSelectedText())));
        } catch (Throwable t) {
            Runtime.getRuntime().gc();
            t.printStackTrace();
            textPane.setText("Exception! " + t.toString());
        }
    }// GEN-LAST:event_base64DecodeButtonActionPerformed

    /**
     * Url decode button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void urlDecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_urlDecodeButtonActionPerformed
        if (textPane.getSelectionEnd() == textPane.getSelectionStart())
            textPane.select(0, textPane.getText().length());
        textPane.replaceSelection(urlDecode(textPane.getSelectedText()));
    }// GEN-LAST:event_urlDecodeButtonActionPerformed

    /**
     * Url encode button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void urlEncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_urlEncodeButtonActionPerformed
        if (textPane.getSelectionEnd() == textPane.getSelectionStart())
            textPane.select(0, textPane.getText().length());
        textPane.replaceSelection(urlEncode(textPane.getSelectedText()));
    }// GEN-LAST:event_urlEncodeButtonActionPerformed

    /**
     * Base64 encode button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void base64EncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
                                                                                    // -
                                                                                    // FIRST
                                                                                    // :
        // event_base64EncodeButtonActionPerformed
        try {
            if (textPane.getSelectionEnd() == textPane.getSelectionStart())
                textPane.select(0, textPane.getText().length());
            textPane.replaceSelection(Encoding.base64encode(textPane.getSelectedText().getBytes()));
        } catch (Throwable t) {
            Runtime.getRuntime().gc();
            t.printStackTrace();
            textPane.setText("Exception! " + t.toString());
        }
    }// GEN-LAST:event_base64EncodeButtonActionPerformed

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer
     * .Clipboard, java.awt.datatransfer.Transferable)
     */
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    /**
     * Url decode.
     * 
     * @param str
     *            the str
     * 
     * @return the string
     */
    /*
     * public static String unicodeDecode( String str ) { // FIXME: TOTALLY
     * EXPERIMENTAL try { ByteBuffer bbuf = ByteBuffer.allocate( str.length() );
     * bbuf.put( str.getBytes() ); Charset charset = Charset.forName(
     * "ISO-8859-1" ); CharsetDecoder decoder = charset.newDecoder(); CharBuffer
     * cbuf = decoder.decode( bbuf ); return ( cbuf.toString() ); } catch (
     * Exception e ) { return ( "Encoding problem" ); } }
     */

    /**
     * Description of the Method
     * 
     *@param str
     *            Description of the Parameter
     *@return Description of the Return Value
     */
    /*
     * public static String unicodeEncode( String str ) { // FIXME: TOTALLY
     * EXPERIMENTAL try { Charset charset = Charset.forName( "ISO-8859-1" );
     * CharsetEncoder encoder = charset.newEncoder(); ByteBuffer bbuf =
     * encoder.encode( CharBuffer.wrap( str ) ); return ( new String(
     * bbuf.array() ) ); } catch ( Exception e ) { return ( "Encoding problem"
     * ); } }
     */

    /**
     * Description of the Method
     * 
     *@param str
     *            Description of the Parameter
     *@return Description of the Return Value
     */
    public static String urlDecode(String str) {
        try {
            return (URLDecoder.decode(str, "utf-8"));
        } catch (Exception e) {
            return ("Decoding error");
        }
    }

    /**
     * Url encode.
     * 
     * @param str
     *            the str
     * 
     * @return the string
     */
    public static String urlEncode(String str) {
        try {
            return (URLEncoder.encode(str, "utf-8"));
        } catch (Exception e) {
            return ("Encoding error");
        }
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        TranscoderFrame tf = new TranscoderFrame();
        tf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
        tf.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The base64 decode button. */
    private javax.swing.JButton base64DecodeButton;

    /** The base64 encode button. */
    private javax.swing.JButton base64EncodeButton;

    /** The count label. */
    private javax.swing.JLabel countLabel;

    /** The edit menu. */
    private javax.swing.JMenu editMenu;

    /** The j menu bar1. */
    private javax.swing.JMenuBar jMenuBar1;

    /** The j panel1. */
    private javax.swing.JPanel jPanel1;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    /** The md5hash button. */
    private javax.swing.JButton md5hashButton;

    /** The sha1hash button. */
    private javax.swing.JButton sha1hashButton;

    /** The text pane. */
    private javax.swing.JTextPane textPane;

    /** The url decode button. */
    private javax.swing.JButton urlDecodeButton;

    /** The url encode button. */
    private javax.swing.JButton urlEncodeButton;

    // End of variables declaration//GEN-END:variables

    /**
     * The Class UndoAction.
     */
    class UndoAction extends AbstractAction {

        /**
	 * 
	 */
        private static final long serialVersionUID = 8331802147120743791L;

        /**
         * Instantiates a new undo action.
         */
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        /**
         * Update undo state.
         */
        protected void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    /**
     * The Class RedoAction.
     */
    class RedoAction extends AbstractAction {

        /**
	 * 
	 */
        private static final long serialVersionUID = -3807244619724663122L;

        /**
         * Instantiates a new redo action.
         */
        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        /**
         * Update redo state.
         */
        protected void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    // This one listens for edits that can be undone.
    /**
     * The listener interface for receiving myUndoableEdit events. The class
     * that is interested in processing a myUndoableEdit event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addMyUndoableEditListener<code> method. When
     * the myUndoableEdit event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see MyUndoableEditEvent
     */
    protected class MyUndoableEditListener implements UndoableEditListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.UndoableEditListener#undoableEditHappened(javax
         * .swing.event.UndoableEditEvent)
         */
        public void undoableEditHappened(UndoableEditEvent e) {
            // Remember the edit and update the menus.
            undo.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }

}
