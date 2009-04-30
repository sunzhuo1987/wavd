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
 * Cookie.java
 *
 * Created on September 10, 2003, 11:01 PM
 */

package edu.lnmiit.wavd.model;

import java.util.Date;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Cookie.
 */

public class Cookie {

    /** The _date. */
    private Date _date = null;

    /** The _name. */
    private String _name = null;

    /** The _value. */
    private String _value = null;

    /** The _key. */
    private String _key = null;

    /** The _comment. */
    private String _comment = null;

    /** The _domain. */
    private String _domain = null;

    /** The _path. */
    private String _path = null;

    /** The _maxage. */
    private String _maxage = null;

    /** The _secure. */
    private boolean _secure = false;

    /** The _version. */
    private String _version = null;

    /** The _httponly. */
    private boolean _httponly = false;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new cookie.
     * 
     * @param date
     *            the date
     * @param url
     *            the url
     * @param setHeader
     *            the set header
     */
    public Cookie(Date date, HttpUrl url, String setHeader) {
        _date = date;
        _domain = url.getHost();
        _path = url.getPath();
        int index = _path.lastIndexOf("/");
        if (index > 0) {
            _path = _path.substring(0, index);
        } else {
            _path = "/";
        }
        parseHeader(setHeader);
        _key = _domain + _path + " " + _name;
    }

    /**
     * Instantiates a new cookie.
     * 
     * @param date
     *            the date
     * @param setHeader
     *            the set header
     */
    public Cookie(Date date, String setHeader) {
        _date = date;
        parseHeader(setHeader);
        _key = _domain + _path + " " + _name;
    }

    /**
     * Parses the header.
     * 
     * @param setHeader
     *            the set header
     */
    private void parseHeader(String setHeader) {
        if (setHeader == null) {
            throw new NullPointerException("You may not pass a null value for setHeader");
        }
        String[] parts = setHeader.split(" *; *");
        if (parts.length < 1) {
            throw new IllegalArgumentException("The setHeader must have at least one part to it!");
        }
        String[] av = parts[0].split("=", 2);
        if (av.length != 2) {
            throw new IllegalArgumentException("The header passed in must at least contain the name and value '"
                    + parts[0] + "'");
        }
        _name = av[0];
        _value = av[1];
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("secure")) {
                _secure = true;
            } else if (parts[i].equalsIgnoreCase("httponly")) {
                _httponly = true;
            } else {
                av = parts[i].split("=", 2);
                if (av.length != 2) {
                    _logger.warning("Unknown cookie attribute '" + parts[i] + "'");
                } else if (av[0].equalsIgnoreCase("Comment")) {
                    _comment = av[1];
                } else if (av[0].equalsIgnoreCase("Domain")) {
                    _domain = av[1];
                } else if (av[0].equalsIgnoreCase("Path")) {
                    _path = av[1];
                } else if (av[0].equalsIgnoreCase("Max-Age")) {
                    _maxage = av[1];
                } else if (av[0].equalsIgnoreCase("Version")) {
                    _version = av[1];
                }
            }
        }
    }

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        return _key;
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate() {
        return _date;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
        return _value;
    }

    /**
     * Gets the domain.
     * 
     * @return the domain
     */
    public String getDomain() {
        return _domain;
    }

    /**
     * Gets the max age.
     * 
     * @return the max age
     */
    public String getMaxAge() {
        return _maxage;
    }

    /**
     * Gets the path.
     * 
     * @return the path
     */
    public String getPath() {
        return _path;
    }

    /**
     * Gets the secure.
     * 
     * @return the secure
     */
    public boolean getSecure() {
        return _secure;
    }

    /**
     * Gets the hTTP only.
     * 
     * @return the hTTP only
     */
    public boolean getHTTPOnly() {
        return _httponly;
    }

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return _version;
    }

    /**
     * Gets the comment.
     * 
     * @return the comment
     */
    public String getComment() {
        return _comment;
    }

    /**
     * Sets the cookie.
     * 
     * @return the string
     */
    public String setCookie() {
        StringBuffer buf = new StringBuffer();
        buf.append(_name + "=" + _value);
        if (_comment != null) {
            buf.append("; Comment=" + _comment);
        }
        if (_domain != null) {
            buf.append("; Domain=" + _domain);
        }
        if (_maxage != null) {
            buf.append("; Max-Age=" + _maxage);
        }
        if (_path != null) {
            buf.append("; Path=" + _path);
        }
        if (_secure) {
            buf.append("; Secure");
        }
        if (_httponly) {
            buf.append("; httponly");
        }
        if (_version != null) {
            buf.append("; Version=" + _version);
        }
        return buf.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(_date.getTime()).append(" ");
        buff.append(setCookie());
        return buff.toString();
    }

}
