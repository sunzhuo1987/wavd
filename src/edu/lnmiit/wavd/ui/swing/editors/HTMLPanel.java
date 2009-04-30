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

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.ChangedCharSetException;

import java.util.logging.Logger;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

// TODO: Auto-generated Javadoc
/**
 * The Class HTMLPanel.
 */
public class HTMLPanel extends JPanel implements ByteArrayEditor {
    
    /** The _data. */
    private byte[] _data = new byte[0];
    
    /** The _search dialog. */
    private SearchDialog _searchDialog = null;
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /** The _bytes. */
    private byte[] _bytes = null;
    
    /**
     * Instantiates a new hTML panel.
     */
    public HTMLPanel() {
        initComponents();
        setName("HTML");
        
        htmlEditorPane.setEditable(false);
        // even though we override getStream in a custom editor pane,
        // if the HTML includes a Frame, the editor Kit creates a new
        // non-custom JEditorPane, which causes problems !!!
        htmlEditorPane.setEditorKit(new MyHTMLEditorKit());
        htmlEditorPane.addHyperlinkListener(new HTMLPanel.LinkToolTipListener());
    }
    
    /**
     * Gets the content types.
     * 
     * @return the content types
     */
    public String[] getContentTypes() {
        return new String[] { "text/html.*" };
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
        // We can't edit HTML directly. This panel is just a renderer
        // _editable = editable;
        // htmlEditorPane.setEditable(editable);
        // we could do things like make buttons visible and invisible here
    }

    /**
     * Gets the charset.
     * 
     * @param contentType the content type
     * @param bytes the bytes
     * 
     * @return the charset
     */
    private String getCharset(String contentType, byte[] bytes) {
        String[] charsets;
        nsDetector det = new nsDetector(nsPSMDetector.ALL);
        
        boolean isAscii = det.isAscii(bytes,bytes.length);
        // DoIt if non-ascii and not done yet.
        if (!isAscii)
            det.DoIt(bytes,bytes.length, false);
        charsets = det.getProbableCharsets();
        det.DataEnd();
        
        if (isAscii) return "ASCII";
        if (charsets.length == 0) return null;
        if (charsets.length == 1 && charsets[0].equals("nomatch")) return null;
        
        return charsets[0];
    }

    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setBytes(java.lang.String, byte[])
     */
    public void setBytes(String contentType, byte[] bytes) {
        _bytes = bytes;
        // htmlEditorPane.getDocument().putProperty("base","");
        if (bytes != null) {
            String charset = null;
            if (contentType.indexOf("charset") == -1) {
                charset = getCharset(contentType, bytes);
                contentType = contentType + "; charset="+charset;
            } else {
                charset = contentType.substring(contentType.indexOf("charset=")+8);
            }
            htmlEditorPane.setContentType(contentType);
            // FIXME: may need to reset style sheets, etc here. Not sure how to do that, though
            // Maybe this will work?
            htmlEditorPane.setDocument(JEditorPane.createEditorKitForContentType("text/html").createDefaultDocument());
            htmlEditorPane.putClientProperty("IgnoreCharsetDirective", Boolean.TRUE);
            htmlEditorPane.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
            
            try {
                if (charset != null) {
                    htmlEditorPane.setText(new String(bytes, charset));
                } else {
                    htmlEditorPane.setText(new String(bytes));
                }
            } catch (Exception e) {
                _logger.warning("Error setting HTML text: " + e);
                e.printStackTrace();
            }
        } else {
            htmlEditorPane.setText("");
        }
        htmlEditorPane.setCaretPosition(0);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#isModified()
     */
    public boolean isModified() {
        return false;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        return _bytes;
    }
    
    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        htmlScrollPane = new javax.swing.JScrollPane();
        htmlEditorPane = new NoNetEditorPane();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(400, 20));
        setPreferredSize(new java.awt.Dimension(400, 20));
        htmlScrollPane.setViewportView(htmlEditorPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(htmlScrollPane, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The html editor pane. */
    private javax.swing.JEditorPane htmlEditorPane;
    
    /** The html scroll pane. */
    private javax.swing.JScrollPane htmlScrollPane;
    // End of variables declaration//GEN-END:variables
    
    /**
     * The Class NoNetEditorPane.
     */
    private class NoNetEditorPane extends JEditorPane {
        
        /* (non-Javadoc)
         * @see javax.swing.JEditorPane#getStream(java.net.URL)
         */
        protected InputStream getStream(URL page) throws IOException {
            _logger.info("Rejecting request for " + page);
            throw new IOException("We do not support network traffic");
        }
    }
    
    /**
     * The listener interface for receiving linkToolTip events.
     * The class that is interested in processing a linkToolTip
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addLinkToolTipListener<code> method. When
     * the linkToolTip event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see LinkToolTipEvent
     */
    public class LinkToolTipListener implements HyperlinkListener {
        
        /**
         * Instantiates a new link tool tip listener.
         */
        public LinkToolTipListener() {
        }
        
        /* (non-Javadoc)
         * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
         */
        public void hyperlinkUpdate(HyperlinkEvent he) {
            EventType type = he.getEventType();
            if (type == EventType.ENTERED) {
                JEditorPane jep = (JEditorPane) he.getSource();
                URL url = he.getURL();
                if (url != null) {
                    jep.setToolTipText(url.toString());
                } else {
                    jep.setToolTipText(he.getDescription());
                }
            } else if (type == EventType.EXITED) {
                JEditorPane jep = (JEditorPane) he.getSource();
                jep.setToolTipText("");
            }
        }
    }
    
    
}

