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
 * Message.java
 *
 * Created on May 12, 2003, 11:10 PM
 */

package edu.lnmiit.wavd.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.lnmiit.wavd.httpclient.ChunkedInputStream;
import edu.lnmiit.wavd.httpclient.ChunkedOutputStream;
import edu.lnmiit.wavd.httpclient.FixedLengthInputStream;


// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 */
public class Message {
    
    /** The _headers. */
    private ArrayList _headers = null;
    
    /** The N o_ headers. */
    private NamedValue[] NO_HEADERS = new NamedValue[0];
    
    /** The Constant NO_CONTENT. */
    private static final byte[] NO_CONTENT = new byte[0];
    
    /** The _content stream. */
    InputStream _contentStream = null;
    
    /** The _content. */
    ByteArrayOutputStream _content = null;
    
    /** The _chunked. */
    boolean _chunked = false;
    
    /** The _gzipped. */
    boolean _gzipped = false;
    
    /** The _length. */
    int _length = -1;
    
    /** The _logger. */
    protected Logger _logger = Logger.getLogger(this.getClass().getName());
    
    /**
     * Instantiates a new message.
     */
    public Message() {
    }
    
    /**
     * Read.
     * 
     * @param is the is
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void read(InputStream is) throws IOException {
        _headers = null;
        String previous = null;
        String line = null;
        do {
            line=readLine(is);
            _logger.finer("Header: " + line);
            if (line.startsWith(" ")) {
                if (previous == null) {
                    _logger.severe("Got a continuation header but had no previous header line");
                } else {
                    previous = previous.trim() + " " + line.trim();
                }
            } else {
                if (previous != null) {
                    String[] pair = previous.split(":", 2);
                    if (pair.length == 2) {
                        addHeader(new NamedValue(pair[0], pair[1].trim()));
                    } else {
                        _logger.warning("Error parsing header: '" + previous + "'");
                    }
                }
                previous = line;
            }
        } while (!line.equals(""));
        
        _contentStream = is;
        if (_chunked) {
            _contentStream = new ChunkedInputStream(_contentStream);
        } else if (_length > -1) {
            _contentStream = new FixedLengthInputStream(_contentStream, _length);
        }
    }
    
    /**
     * Write.
     * 
     * @param os the os
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void write(OutputStream os) throws IOException {
        write(os, "\r\n");
    }
    
    /**
     * Write.
     * 
     * @param os the os
     * @param crlf the crlf
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void write(OutputStream os, String crlf) throws IOException {
        if (_headers != null) {
            for (int i=0; i<_headers.size(); i++) {
                NamedValue nv = (NamedValue) _headers.get(i);
                os.write(new String(nv.getName() + ": " + nv.getValue() + crlf).getBytes());
                _logger.finest("Header: " + nv);
            }
        }
        os.write(crlf.getBytes());
        _logger.finer("wrote headers");
        if (_chunked) {
            os = new ChunkedOutputStream(os);
        }
        if (_contentStream != null) {
            _logger.finer("Flushing contentStream");
            flushContentStream(os);
            _logger.finer("Done flushing contentStream");
        } else if (_content != null && _content.size() > 0) {
            _logger.finer("Writing content bytes");
            os.write(_content.toByteArray());
            _logger.finest("Content: \n" + new String(_content.toByteArray()));
            _logger.finer("Done writing content bytes");
        }
        if (_chunked) {
            ((ChunkedOutputStream) os).writeTrailer();
        }
    }
    
    /**
     * Parses the.
     * 
     * @param buffer the buffer
     * 
     * @throws ParseException the parse exception
     */
    public void parse(StringBuffer buffer) throws ParseException {
        _headers = null;
        String previous = null;
        String line = null;
        do {
            line=getLine(buffer);
            if (line.startsWith(" ")) {
                if (previous == null) {
                    _logger.severe("Got a continuation header but had no previous header line");
                } else {
                    previous = previous.trim() + " " + line.trim();
                }
            } else {
                if (previous != null) {
                    String[] pair = previous.split(":", 2);
                    if (pair.length == 2) {
                        addHeader(new NamedValue(pair[0], pair[1].trim()));
                    } else {
                        _logger.warning("Error parsing header: '" + previous + "'");
                    }
                }
                previous = line;
            }
        } while (!line.equals(""));
        
        _content = new ByteArrayOutputStream();
        try {
            _content.write(buffer.toString().getBytes());
        } catch (IOException ioe) {} // can't fail
        String cl = getHeader("Content-length");
        if (cl != null) {
            setHeader(new NamedValue("Content-length", Integer.toString(_content.size())));
        }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return toString("\r\n");
    }
    
    /**
     * To string.
     * 
     * @param crlf the crlf
     * 
     * @return the string
     */
    public String toString(String crlf) {
        StringBuffer buff = new StringBuffer();
        if (_headers != null) {
            for (int i=0; i<_headers.size(); i++) {
                NamedValue nv = (NamedValue) _headers.get(i);
                if (nv.getName().equalsIgnoreCase("Transfer-Encoding") && nv.getValue().indexOf("chunked")>-1) {
                    buff.append("X-" + nv.getName() + ": " + nv.getValue() + crlf);
                } else if (nv.getName().equalsIgnoreCase("Content-Encoding") && nv.getValue().indexOf("gzip")>-1) {
                    buff.append("X-" + nv.getName() + ": " + nv.getValue() + crlf);
                } else {
                    buff.append(nv.getName() + ": " + nv.getValue() + crlf);
                }
            }
        }
        byte[] content = getContent();
        if (_chunked && content != null) {
            buff.append("Content-length: " + Integer.toString(content.length) + crlf);
        }
        buff.append(crlf);
        if (content != null) {
            try {
                buff.append(new String(content, "UTF-8"));
            } catch (UnsupportedEncodingException uee) {}; // must support UTF-8
        }
        return buff.toString();
    }
    
    /**
     * Update flags for header.
     * 
     * @param header the header
     */
    private void updateFlagsForHeader(NamedValue header) {
        if (header.getName().equalsIgnoreCase("Transfer-Encoding")) {
            if (header.getValue().indexOf("chunked")>-1) {
                _chunked = true;
            } else {
                _chunked = false;
            }
        } else if (header.getName().equalsIgnoreCase("Content-Encoding")) {
            if (header.getValue().indexOf("gzip")>-1) {
                _gzipped = true;
            } else {
                _gzipped = false;
            }
        } else if (header.getName().equalsIgnoreCase("Content-length")) {
            try {
                _length = Integer.parseInt(header.getValue().trim());
            } catch (NumberFormatException nfe) {
                _logger.warning("Error parsing the content-length '" + header.getValue() + "' : " + nfe);
            }
        }
    }
    
    /**
     * Sets the header.
     * 
     * @param name the name
     * @param value the value
     */
    public void setHeader(String name, String value) {
        setHeader(new NamedValue(name, value.trim()));
    }
    
    /**
     * Sets the header.
     * 
     * @param header the new header
     */
    public void setHeader(NamedValue header) {
        updateFlagsForHeader(header);
        if (_headers == null) {
            _headers = new ArrayList(1);
        } else {
            for (int i=0; i<_headers.size(); i++) {
                NamedValue nv = (NamedValue) _headers.get(i);
                if (nv.getName().equalsIgnoreCase(header.getName())) {
                    _headers.set(i,header);
                    return;
                }
            }
        }
        _headers.add(header);
    }
    
    /**
     * Adds the header.
     * 
     * @param name the name
     * @param value the value
     */
    public void addHeader(String name, String value) {
        addHeader(new NamedValue(name, value.trim()));
    }
    
    /**
     * Adds the header.
     * 
     * @param header the header
     */
    public void addHeader(NamedValue header) {
        updateFlagsForHeader(header);
        if (_headers == null) {
            _headers = new ArrayList(1);
        }
        _headers.add(header);
    }
    
    /**
     * Delete header.
     * 
     * @param name the name
     * 
     * @return the string
     */
    public String deleteHeader(String name) {
        if (_headers == null) {
            return null;
        }
        for (int i=0; i<_headers.size(); i++) {
            NamedValue nv = (NamedValue) _headers.get(i);
            if (nv.getName().equalsIgnoreCase(name)) {
                _headers.remove(i);
                updateFlagsForHeader(new NamedValue(name, ""));
                return nv.getValue();
            }
        }
        return null;
    }
    
    /**
     * Gets the header names.
     * 
     * @return the header names
     */
    public String[] getHeaderNames() {
        if (_headers == null || _headers.size() == 0) {
            return new String[0];
        }
        String[] names = new String[_headers.size()];
        for (int i=0; i<_headers.size(); i++) {
            NamedValue nv = (NamedValue) _headers.get(i);
            names[i] = nv.getName();
        }
        return names;
    }
    
    /**
     * Gets the header.
     * 
     * @param name the name
     * 
     * @return the header
     */
    public String getHeader(String name) {
        if (_headers == null) {
            return null;
        }
        for (int i=0; i<_headers.size(); i++) {
            NamedValue nv = (NamedValue) _headers.get(i);
            if (nv.getName().equalsIgnoreCase(name)) {
                return nv.getValue();
            }
        }
        return null;
    }
    
    /**
     * Gets the headers.
     * 
     * @param name the name
     * 
     * @return the headers
     */
    public String[] getHeaders(String name) {
        if (_headers == null) 
            return null;
        ArrayList values = new ArrayList();
        for (int i=0; i<_headers.size(); i++) {
            NamedValue nv = (NamedValue) _headers.get(i);
            if (nv.getName().equalsIgnoreCase(name)) {
                values.add(nv.getValue());
            }
        }
        if (values.size() == 0) 
            return null;
        return (String[]) values.toArray(new String[0]);
    }
    
    /**
     * Gets the headers.
     * 
     * @return the headers
     */
    public NamedValue[] getHeaders() {
        if (_headers == null || _headers.size() == 0) {
            return new NamedValue[0];
        }
        return (NamedValue[]) _headers.toArray(NO_HEADERS);
    }
    
    /**
     * Sets the headers.
     * 
     * @param headers the new headers
     */
    public void setHeaders(NamedValue[] headers) {
        if (_headers == null) {
            _headers = new ArrayList();
        } else {
            _headers.clear();
        }
        for (int i=0; i<headers.length; i++) {
            addHeader(headers[i]);
        }
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
    protected String readLine(InputStream is) throws IOException {
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
        _logger.finest(line.toString());
        return line.toString();
    }
    
    /**
     * Gets the line.
     * 
     * @param buffer the buffer
     * 
     * @return the line
     */
    protected String getLine(StringBuffer buffer) {
        int lf = buffer.indexOf("\n");
        if (lf > -1) {
            int cr = buffer.indexOf("\r");
            if (cr == -1 || cr > lf) {
                cr = lf;
            }
            String line = buffer.substring(0,cr);
            buffer.delete(0, lf+1);
            _logger.finest("line is '" + line + "'");
            return line;
        } else if (buffer.length() > 0) {
            String line = buffer.toString();
            buffer.setLength(0);
            _logger.finest("line is '" + line + "'");
            return line;
        } else {
            return "";
        }
    }
    
    /**
     * Gets the content.
     * 
     * @return the content
     */
    public byte[] getContent() {
        try {
            flushContentStream(null);
        } catch (IOException ioe) {
            _logger.info("IOException flushing the contentStream: " + ioe);
        }
        if (_content != null && _gzipped) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(_content.toByteArray()));
                byte[] buff = new byte[1024];
                int got;
                while ((got = gzis.read(buff))>-1) {
                    baos.write(buff, 0, got);
                }
                return baos.toByteArray();
            } catch (IOException ioe) {
                _logger.info("IOException unzipping content : " + ioe);
                return NO_CONTENT;
            }
        }
        if (_content != null) {
            return _content.toByteArray();
        } else {
            return NO_CONTENT;
        }
    }
    
    /**
     * Flush content stream.
     */
    public void flushContentStream() {
        try {
            flushContentStream(null);
        } catch (IOException ioe) {
            _logger.info("Exception flushing the contentStream " + ioe);
        }
    }
    
    /**
     * Flush content stream.
     * 
     * @param os the os
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void flushContentStream(OutputStream os) throws IOException {
        IOException ioe = null;
        if (_contentStream == null) return;
        if (_content == null) _content = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        _logger.finest("Reading initial bytes from contentStream " + _contentStream);
        int got = _contentStream.read(buf);
        _logger.finest("Got " + got + " bytes");
        while (got > 0) {
            _content.write(buf,0, got);
            if (os != null) {
                try {
                    os.write(buf,0,got);
                } catch (IOException e) {
                    _logger.info("IOException ioe writing to output stream : " + e);
                    _logger.info("Had seen " + (_content.size()-got) + " bytes, was writing " + got);
                    ioe = e;
                    os = null;
                }
            }
            got = _contentStream.read(buf);
            _logger.finest("Got " + got + " bytes");
        }
        _contentStream = null;
        if (ioe != null) throw ioe;
    }
    
    /**
     * Sets the no body.
     */
    public void setNoBody() {
        _content = null;
        _contentStream = null;
    }
    
    /**
     * Sets the content.
     * 
     * @param bytes the new content
     */
    public void setContent(byte[] bytes) {
        // discard whatever is pending in the content stream
        try {
            flushContentStream(null);
        } catch (IOException ioe) {
            _logger.info("IOException flushing the contentStream " + ioe);
        }
        if (_gzipped) {
            try {
                _content = new ByteArrayOutputStream();
                GZIPOutputStream gzos = new GZIPOutputStream(_content);
                gzos.write(bytes);
                gzos.close();
            } catch (IOException ioe) {
                _logger.info("IOException gzipping content : " + ioe);
            }
        } else {
            _content = new ByteArrayOutputStream();
            try {
                _content.write(bytes);
            } catch (IOException ioe) {} // can't fail
        }
        String cl = getHeader("Content-length");
        if (cl != null) {
            setHeader(new NamedValue("Content-length", Integer.toString(_content.size())));
        }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (! (obj instanceof Message)) return false;
        Message mess = (Message) obj;
        NamedValue[] myHeaders = getHeaders();
        NamedValue[] thoseHeaders = mess.getHeaders();
        if (myHeaders.length != thoseHeaders.length) return false;
        for (int i=0; i<myHeaders.length; i++) {
            if (!myHeaders[i].getName().equalsIgnoreCase(thoseHeaders[i].getName())) return false;
            if (!myHeaders[i].getValue().equals(thoseHeaders[i].getValue())) return false;
        }
        byte[] myContent = getContent();
        byte[] thatContent = mess.getContent();
        return Arrays.equals(myContent, thatContent);
    }
    
}
