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

package edu.lnmiit.wavd.plugin.fuzz;

import java.net.MalformedURLException;

import edu.lnmiit.wavd.model.Cookie;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.model.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.net.MalformedURLException;

// TODO: Auto-generated Javadoc
/**
 * The Class Signature.
 */
public class Signature {
    
    /** The NONE. */
    private static Parameter[] NONE = new Parameter[0];
    
    /** The _method. */
    private String _method;
    
    /** The _url. */
    private HttpUrl _url;
    
    /** The _content type. */
    private String _contentType;
    
    /** The _parameters. */
    private Parameter[] _parameters;
    
    /**
     * Instantiates a new signature.
     * 
     * @param request the request
     */
    public Signature(Request request) {
        _method = request.getMethod();
        _url = request.getURL();
        if (_url.getParameters() != null) _url = _url.getParentUrl();
        _contentType = request.getHeader("Content-Type");
        _parameters = Parameter.getParameters(request);
    }
    
    /**
     * Instantiates a new signature.
     * 
     * @param signature the signature
     * 
     * @throws MalformedURLException the malformed url exception
     */
    public Signature(String signature) throws MalformedURLException {
        String[] parts = signature.split(" ");
        _method = parts[0];
        _url = new HttpUrl(parts[1]);
        _contentType = parts[2].substring(1, parts[2].length()-1);
        if (_contentType.equals("null")) 
            _contentType = null;
        List parameters = new ArrayList();
        for (int i=3; i<parts.length; i++) {
            int colon = parts[i].indexOf(":");
            String location = parts[i].substring(0, colon);
            int left = parts[i].indexOf('(', colon);
            String name = parts[i].substring(colon+1, left);
            String type = parts[i].substring(left+1, parts[i].length()-1);
            Parameter param = new Parameter(location, name, type, "");
            parameters.add(param);
        }
        _parameters = (Parameter[]) parameters.toArray(Parameter.NO_PARAMS);
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
     * Gets the url.
     * 
     * @return the url
     */
    public HttpUrl getUrl() {
        return _url;
    }
    
    /**
     * Gets the content type.
     * 
     * @return the content type
     */
    public String getContentType() {
        return _contentType;
    }
    
    /**
     * Gets the parameters.
     * 
     * @return the parameters
     */
    public Parameter[] getParameters() {
        return _parameters;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(_method).append(" ").append(_url).append(" ");
        buff.append("(").append(_contentType).append(")");
        for (int i=0; i<_parameters.length; i++) {
            buff.append(" ").append(_parameters[i].getLocation()).append(":").append(_parameters[i].getName());
            buff.append("(").append(_parameters[i].getType()).append(")");
        }
        return buff.toString();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Signature)) return false;
        Signature that = (Signature) obj;
        if (!_method.equals(that._method)) return false;
        if (!_url.equals(that._url)) return false;
        if (_contentType == null && that._contentType != null) return false;
        if (_contentType != null && that._contentType == null) return false;
        if (_contentType != null && !_contentType.equals(that._contentType)) return false;
        if (_parameters.length != that._parameters.length) return false;
        for (int i=0; i<_parameters.length; i++) {
            if (! _parameters[i].equals(that._parameters[i])) return false;
        }
        return true;
    }
    
}
