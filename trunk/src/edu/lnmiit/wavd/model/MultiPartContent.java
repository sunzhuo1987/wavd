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

package edu.lnmiit.wavd.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.io.UnsupportedEncodingException;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiPartContent.
 */
public class MultiPartContent {
    
    /** The _boundary. */
    private byte[] _boundary;
    
    /** The _parts. */
    private List _parts;
    
    /** The Constant CRLF. */
    private static final byte[] CRLF = new byte[] { '\r', '\n' };
    
    /**
     * Instantiates a new multi part content.
     * 
     * @param contentType the content type
     * @param content the content
     */
    public MultiPartContent(String contentType, byte[] content) {
        try {
            _parts = new ArrayList();
            if (contentType != null && contentType.trim().startsWith("multipart/form-data")) {
                int pos = contentType.indexOf("boundary=");
                int semi = contentType.indexOf(";", pos);
                if (semi < 0) semi = contentType.length();
                _boundary = ("--" + contentType.substring(pos+9,semi).trim()).getBytes("UTF-8");
            } else {
                _boundary = null;
            }
            if (_boundary != null) {
                int start = findBytes(content, _boundary, 0) + _boundary.length + CRLF.length;
                int end = findBytes(content, _boundary, start);
                while (end < content.length) {
                    Message message = new Message();
                    try {
                        message.read(new ByteArrayInputStream(content, start, end-start-CRLF.length));
                    } catch (IOException ioe) {
                        System.err.println("IOException on a ByteArrayInputStream should never happen! " + ioe);
                    }
                    _parts.add(message);
                    start = end + _boundary.length + CRLF.length;
                    end = findBytes(content, _boundary, start);
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("UTF-8 not supported?! " + e);
        }
    }
    
    /**
     * Verify boundary.
     * 
     * @return true, if successful
     */
    public boolean verifyBoundary() {
        return true;
    }
    
    /**
     * Gets the boundary.
     * 
     * @return the boundary
     */
    public String getBoundary() {
        try {
            return new String(_boundary, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("UTF-8 not supported?! " + e);
            return null;
        }
    }
    
    /**
     * Size.
     * 
     * @return the int
     */
    public int size() {
        return _parts.size();
    }
    
    /**
     * Sets the.
     * 
     * @param index the index
     * @param part the part
     * 
     * @return the message
     */
    public Message set(int index, Message part) {
        return (Message) _parts.set(index, part);
    }
    
    /**
     * Gets the.
     * 
     * @param index the index
     * 
     * @return the message
     */
    public Message get(int index) {
        return (Message) _parts.get(index);
    }
    
    /**
     * Removes the.
     * 
     * @param index the index
     * 
     * @return the message
     */
    public Message remove(int index) {
        return (Message) _parts.remove(index);
    }
    
    /**
     * Adds the.
     * 
     * @param index the index
     * @param part the part
     */
    public void add(int index, Message part) {
        _parts.add(index, part);
    }
    
    /**
     * Adds the.
     * 
     * @param part the part
     * 
     * @return true, if successful
     */
    public boolean add(Message part) {
        return _parts.add(part);
    }
    
    /**
     * Find bytes.
     * 
     * @param source the source
     * @param find the find
     * @param start the start
     * 
     * @return the int
     */
    private int findBytes(byte[] source, byte[] find, int start) {
        int matches = 0;
        int pos = start;
        while (pos<source.length && matches < find.length) {
            if (source[pos+matches] == find[matches]) {
                matches++;
            } else {
                matches = 0;
                pos++;
            }
        }
        return pos;
    }
    
    /**
     * Read line.
     * 
     * @param is the is
     * 
     * @return the string
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String readLine(InputStream is) throws IOException {
        if (is == null) {
            NullPointerException npe = new NullPointerException("InputStream may not be null!");
            npe.printStackTrace();
            throw npe;
        }
        StringBuffer line = new StringBuffer();
        int i;
        char c=0x00;
        i = is.read();
        if (i == -1) return null;
        while (i > -1 && i != 10 && i != 13) {
            // Convert the int to a char
            c = (char)(i & 0xFF);
            line = line.append(c);
            i = is.read();
        }
        if (i == 13) { // 10 is unix LF, but DOS does 13+10, so read the 10 if we got 13
            i = is.read();
        }
        // _logger.finest(line.toString());
        return line.toString();
    }
    
    /**
     * Gets the part name.
     * 
     * @param index the index
     * 
     * @return the part name
     */
    public String getPartName(int index) {
        Message part = (Message) _parts.get(index);
        String disposition = part.getHeader("Content-Disposition");
        int nameindex = disposition.indexOf("name=");
        int semi = disposition.indexOf(";", nameindex);
        if (semi<0) semi = disposition.length();
        String name = disposition.substring(nameindex+5,semi).trim();
        if (name.startsWith("\"") && name.endsWith("\"") || name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1,name.length()-1);
        }
        return name;
    }
    
    /**
     * Gets the bytes.
     * 
     * @return the bytes
     */
    public byte[] getBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(_boundary);
            baos.write(CRLF);
            Iterator it = _parts.iterator();
            while (it.hasNext()) {
                Message message = (Message) it.next();
                message.write(baos);
                baos.write(CRLF);
                baos.write(_boundary);
                baos.write(CRLF);
            }
            return baos.toByteArray();
        } catch (IOException ioe) {
            System.err.println("Shouldn't happen!! " + ioe);
        }
        return null;
    }
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        Request request = new Request();
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream("/home/rogan/csob/3/conversations/4-request");
            request.read(fis);
            MultiPartContent mpc = new MultiPartContent(request.getHeader("Content-Type"), request.getContent());
            System.out.println("Got " + mpc.size());
            Message message = mpc.get(0);
            System.out.println("First part is " + message.getHeader("Content-Disposition") + " = '" + new String(message.getContent()) + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
