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
 * Response.java
 *
 * Created on May 12, 2003, 11:18 PM
 */

package edu.lnmiit.wavd.model;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;

// TODO: Auto-generated Javadoc
/**
 * The Class Response.
 */
public class Response extends Message {

    /** The version. */
    private String version = null;

    /** The status. */
    private String status = null;

    /** The message. */
    private String message = null;

    /** The _request. */
    private Request _request = null;

    /**
     * Instantiates a new response.
     */
    public Response() {
        setVersion("HTTP/1.0");
    }

    /**
     * Instantiates a new response.
     * 
     * @param resp
     *            the resp
     */
    public Response(Response resp) {
        this.version = resp.getVersion();
        this.status = resp.getStatus();
        this.message = resp.getMessage();
        setHeaders(resp.getHeaders());
        setContent(resp.getContent());
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.Message#read(java.io.InputStream)
     */
    public void read(InputStream is) throws IOException {
        String line = readLine(is);
        if (line == null) {
            throw new IOException("No data received from the server");
        }
        String[] parts = line.split(" ", 3);
        if (parts.length >= 2) {
            setVersion(parts[0]);
            setStatus(parts[1]);
        } else {
            throw new IOException("Invalid response line read from the server: \"" + line + "\"");
        }
        if (parts.length == 3) {
            setMessage(parts[2]);
        } else {
            setMessage("");
        }
        super.read(is);
        if (status.equals("304") || status.equals("204")) {
            // These messages MUST NOT include a body
            setNoBody();
        }
    }

    /**
     * Parses the.
     * 
     * @param string
     *            the string
     * 
     * @throws ParseException
     *             the parse exception
     */
    public void parse(String string) throws ParseException {
        parse(new StringBuffer(string));
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.Message#parse(java.lang.StringBuffer)
     */
    public void parse(StringBuffer buff) throws ParseException {
        String line = getLine(buff);
        String[] parts = line.split(" ", 3);
        if (parts.length >= 2) {
            setVersion(parts[0]);
            setStatus(parts[1]);
        }
        if (parts.length == 3) {
            setMessage(parts[2]);
        } else {
            setMessage("");
        }
        super.parse(buff);
        if (status.equals("304") || status.equals("204")) {
            // These messages MUST NOT include a body
            setNoBody();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.Message#write(java.io.OutputStream)
     */
    public void write(OutputStream os) throws IOException {
        write(os, "\r\n");
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.Message#write(java.io.OutputStream,
     * java.lang.String)
     */
    public void write(OutputStream os, String crlf) throws IOException {
        os = new BufferedOutputStream(os);
        os.write(new String(version + " " + getStatusLine() + crlf).getBytes());
        super.write(os, crlf);
        os.flush();
    }

    /**
     * Sets the version.
     * 
     * @param version
     *            the new version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the status.
     * 
     * @param status
     *            the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the status.
     * 
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the message.
     * 
     * @param message
     *            the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the status line.
     * 
     * @return the status line
     */
    public String getStatusLine() {
        return status + " " + message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.Message#toString()
     */
    public String toString() {
        return toString("\r\n");
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.Message#toString(java.lang.String)
     */
    public String toString(String crlf) {
        StringBuffer buff = new StringBuffer();
        buff.append(version + " " + getStatusLine() + crlf);
        buff.append(super.toString(crlf));
        return buff.toString();
    }

    /**
     * Sets the request.
     * 
     * @param request
     *            the new request
     */
    public void setRequest(Request request) {
        _request = request;
    }

    /**
     * Gets the request.
     * 
     * @return the request
     */
    public Request getRequest() {
        return _request;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.Message#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Response))
            return false;
        Response resp = (Response) obj;
        if (!getVersion().equals(resp.getVersion()))
            return false;
        if (!getStatusLine().equals(resp.getStatusLine()))
            return false;
        return super.equals(obj);
    }

}
