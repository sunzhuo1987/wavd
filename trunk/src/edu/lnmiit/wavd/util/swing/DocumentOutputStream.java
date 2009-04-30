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
 * DocumentOutputStream.java
 *
 * Created on August 20, 2004, 6:50 PM
 */

package edu.lnmiit.wavd.util.swing;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentOutputStream.
 */
public class DocumentOutputStream extends OutputStream {

    /** The _doc. */
    PlainDocument _doc = new PlainDocument();

    /** The _max. */
    private int _max;

    /**
     * Instantiates a new document output stream.
     */
    public DocumentOutputStream() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Instantiates a new document output stream.
     * 
     * @param max
     *            the max
     */
    public DocumentOutputStream(int max) {
        _max = max;
    }

    /**
     * Gets the document.
     * 
     * @return the document
     */
    public Document getDocument() {
        return _doc;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException {
        try {
            makeSpace(1);
            _doc.insertString(_doc.getLength(), new String(new byte[] { (byte) (b & 0xFF) }, "ISO-8859-1"), null);
        } catch (BadLocationException ble) {
            throw new IOException(ble.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(byte[] buff) throws IOException {
        write(buff, 0, buff.length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte[] buff, int off, int length) throws IOException {
        try {
            makeSpace(length);
            _doc.insertString(_doc.getLength(), new String(buff, off, length, "ISO-8859-1"), null);
        } catch (BadLocationException ble) {
            throw new IOException(ble.getMessage());
        }
    }

}
