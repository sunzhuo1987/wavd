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
 * Request.java
 *
 * Created on May 12, 2003, 11:12 PM
 */

package edu.lnmiit.wavd.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import java.text.ParseException;
import java.util.logging.Level;

// TODO: Auto-generated Javadoc
/**
 * The Class Request.
 */
public class Request extends Message {
    
    /** The _method. */
    private String _method = "GET";
    
    /** The _url. */
    private HttpUrl _url = null;
    
    /** The _version. */
    private String _version = "HTTP/1.0";
    
    /**
     * Instantiates a new request.
     */
    public Request() {
    }
    
    /**
     * Instantiates a new request.
     * 
     * @param req the req
     */    
    public Request(Request req) {
        _method = req.getMethod();
        _url = req.getURL();
        _version = req.getVersion();
        setHeaders(req.getHeaders());
        setContent(req.getContent());
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.Message#read(java.io.InputStream)
     */
    public void read(InputStream is) throws IOException {
        read(is, null);
    }
    
    /**
     * Read.
     * 
     * @param is the is
     * @param base the base
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */    
    public void read(InputStream is, HttpUrl base) throws IOException {
        String line = null;
        _logger.finer("Base: " + base);
        try {
            line = readLine(is);
            _logger.finest("Request: " + line);
        } catch (SocketTimeoutException ste) {
            // System.err.println("Read timed out. Closing connection");
            return;
        }
        if (line == null || line.equals("")) {
            // System.err.println("Client closed connection!");
            return;
        }
        String[] parts = line.split(" ");
        if (parts.length == 2 || parts.length == 3) {
            setMethod(parts[0]);
            if (getMethod().equalsIgnoreCase("CONNECT")) {
                setURL(new HttpUrl("https://" + parts[1] + "/"));
            } else {
                // supports creating an absolute url from a relative one
                setURL(new HttpUrl(base, parts[1]));
            }
        } else {
            throw new IOException("Invalid request line reading from the InputStream '"+line+"'");
        }
        if (parts.length == 3) {
            setVersion(parts[2]);
        } else {
            setVersion("HTTP/0.9");
        }
        // Read the rest of the message headers and to the start of the body
        super.read(is);
        if (_method.equals("CONNECT") || _method.equals("GET") || _method.equals("HEAD") || _method.equals("TRACE")) {
            // These methods cannot include a message body
            setNoBody();
        }
    }
    
    /**
     * Parses the.
     * 
     * @param string the string
     * 
     * @throws ParseException the parse exception
     */    
    public void parse(String string) throws ParseException {
        parse(new StringBuffer(string));
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.Message#parse(java.lang.StringBuffer)
     */    
    public void parse(StringBuffer buff) throws ParseException {
        String line = null;
        line = getLine(buff);
        String[] parts = line.split(" ");
        if (parts.length == 2 || parts.length == 3) {
            setMethod(parts[0]);
            try {
                if (getMethod().equalsIgnoreCase("CONNECT")) {
                    setURL(new HttpUrl("https://" + parts[1] + "/"));
                } else {
                    setURL(new HttpUrl(parts[1]));
                }
            } catch (MalformedURLException mue) {
                throw new ParseException("Malformed URL '" + parts[1] + "' : " + mue, parts[0].length()+1);
            }
        } else {
            throw new ParseException("Invalid request line '" + line + "'", 0);
        }
        if (parts.length == 3) {
            setVersion(parts[2]);
        } else {
            setVersion("HTTP/0.9");
        }
        // Read the rest of the message headers and body
        super.parse(buff);
        if (_method.equals("CONNECT") || _method.equals("GET") || _method.equals("HEAD") || _method.equals("TRACE")) {
            // These methods cannot include a message body
            setNoBody();
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.Message#write(java.io.OutputStream)
     */    
    public void write(OutputStream os) throws IOException {
        write(os,"\r\n");
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.Message#write(java.io.OutputStream, java.lang.String)
     */    
    public void write(OutputStream os, String crlf) throws IOException {
        if (_method == null || _url == null || _version == null) {
            System.err.println("Uninitialised Request!");
            return;
        }
        os = new BufferedOutputStream(os);
        String requestLine = _method+" "+_url+" " + _version + crlf;
        os.write(requestLine.getBytes());
        _logger.finer("Request: " + requestLine);
        super.write(os, crlf);
        os.flush();
    }
    
    /**
     * Write direct.
     * 
     * @param os the os
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */    
    public void writeDirect(OutputStream os) throws IOException {
        writeDirect(os, "\r\n");
    }
    
    /**
     * Write direct.
     * 
     * @param os the os
     * @param crlf the crlf
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */    
    public void writeDirect(OutputStream os, String crlf) throws IOException {
        if (_method == null || _url == null || _version == null) {
            System.err.println("Uninitialised Request!");
            return;
        }
        os = new BufferedOutputStream(os);
        String requestLine = _method + " " + _url.direct() + " " + _version;
        os.write((requestLine+crlf).getBytes());
        _logger.finer("Request: " + requestLine);
        super.write(os, crlf);
        os.flush();
    }
    
    /**
     * Sets the method.
     * 
     * @param method the new method
     */    
    public void setMethod(String method) {
        _method = method.toUpperCase();
    }
    
    /**
     * Gets the method.
     * 
     * @return the method
     */    
    public String getMethod() {
        return _method;
    }
    
    /**
     * Sets the uRL.
     * 
     * @param url the new uRL
     */    
    public void setURL(HttpUrl url) {
        _url = url;
    }
    
    /**
     * Gets the uRL.
     * 
     * @return the uRL
     */    
    public HttpUrl getURL() {
        return _url;
    }
    
    /**
     * Sets the version.
     * 
     * @param version the new version
     */    
    public void setVersion(String version) {
        _version = version.toUpperCase();
    }
    
    /**
     * Gets the version.
     * 
     * @return the version
     */    
    public String getVersion() {
        return _version;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.Message#toString()
     */    
    public String toString() {
        return toString("\r\n");
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.Message#toString(java.lang.String)
     */    
    public String toString(String crlf) {
        if (_method == null || _url == null || _version == null) {
            return "Unitialised Request!";
        }
        StringBuffer buff = new StringBuffer();
        buff.append(_method).append(" ");
        buff.append(_url).append(" ");
        buff.append(_version).append(crlf);
        buff.append(super.toString(crlf));
        return buff.toString();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.Message#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Request)) return false;
        Request req = (Request)obj;
        if (!getMethod().equals(req.getMethod())) return false;
        if (!getURL().equals(req.getURL())) return false;
        if (!getVersion().equals(req.getVersion())) return false;
        return super.equals(req);
    }
    
}
