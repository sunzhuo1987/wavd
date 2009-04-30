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
 * DocumentHandler.java
 *
 * Created on April 16, 2004, 5:03 PM
 */

package edu.lnmiit.wavd.util.swing;

import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentHandler.
 */
public class DocumentHandler extends Handler {

    /** The _doc. */
    private PlainDocument _doc;

    /** The _max. */
    private int _max = Integer.MAX_VALUE;

    /**
     * Instantiates a new document handler.
     */
    public DocumentHandler() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Instantiates a new document handler.
     * 
     * @param limit
     *            the limit
     */
    public DocumentHandler(int limit) {
        _max = limit;
        _doc = new PlainDocument();
    }

    /**
     * Gets the document.
     * 
     * @return the document
     */
    public Document getDocument() {
        return _doc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Handler#close()
     */
    public void close() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Handler#flush()
     */
    public void flush() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
     */
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        final String msg;
        try {
            msg = getFormatter().format(record);
        } catch (Exception ex) {
            // We don't want to throw an exception here, but we
            // report the exception to any registered ErrorManager.
            reportError(null, ex, ErrorManager.FORMAT_FAILURE);
            return;
        }
        try {
            makeSpace(msg.length());
            _doc.insertString(_doc.getLength(), msg, null);
            // cr++;
        } catch (Exception ex) {
            // We don't want to throw an exception here, but we
            // report the exception to any registered ErrorManager.
            reportError(null, ex, ErrorManager.WRITE_FAILURE);
        }
    }

    /**
     * Make space.
     * 
     * @param count
     *            the count
     */
    private void makeSpace(int count) {
        int length = _doc.getLength();
        if (length + count < _max)
            return;
        try {
            if (count > _max) {
                _doc.remove(0, length);
            } else {
                int min = length + count - _max;
                String remove = _doc.getText(min, Math.min(500, length - min));
                int cr = remove.indexOf("\n");
                if (cr < 0) {
                    min = min + remove.length();
                } else {
                    min = Math.min(min + cr + 1, length);
                }
                _doc.remove(0, min);
            }
        } catch (BadLocationException ble) {
            System.err.println("BLE! " + ble);
        }
    }

}
