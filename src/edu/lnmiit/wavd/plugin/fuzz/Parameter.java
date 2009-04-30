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

import java.util.ArrayList;
import java.util.List;

import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.model.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class Parameter.
 */
public class Parameter {

    /** The Constant NO_PARAMS. */
    public static final Parameter[] NO_PARAMS = new Parameter[0];

    /** The Constant LOCATION_PATH. */
    public static final String LOCATION_PATH = "Path";

    /** The Constant LOCATION_FRAGMENT. */
    public static final String LOCATION_FRAGMENT = "Fragment";

    /** The Constant LOCATION_QUERY. */
    public static final String LOCATION_QUERY = "Query";

    /** The Constant LOCATION_COOKIE. */
    public static final String LOCATION_COOKIE = "Cookie";

    /** The Constant LOCATION_BODY. */
    public static final String LOCATION_BODY = "Body";

    /** The _location. */
    private String _location;

    /** The _name. */
    private String _name;

    /** The _type. */
    private String _type;

    /** The _value. */
    private Object _value;

    /**
     * Gets the parameter locations.
     * 
     * @return the parameter locations
     */
    public static String[] getParameterLocations() {
        return new String[] { LOCATION_PATH, LOCATION_FRAGMENT, LOCATION_QUERY, LOCATION_COOKIE, LOCATION_BODY, };
    }

    /**
     * Instantiates a new parameter.
     * 
     * @param location
     *            the location
     * @param name
     *            the name
     * @param type
     *            the type
     * @param value
     *            the value
     */
    public Parameter(String location, String name, String type, Object value) {
        _location = location;
        _name = name;
        _type = type;
        if (value == null)
            throw new NullPointerException("Value may not be null");
        _value = value;
    }

    /**
     * Gets the location.
     * 
     * @return the location
     */
    public String getLocation() {
        return _location;
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
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return _type;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public Object getValue() {
        return _value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return _location + ":" + _name + "(" + _type + ") = " + _value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Parameter))
            return false;
        Parameter that = (Parameter) obj;
        return (_location.equals(that._location) && _name.equals(that._name) && _type.equals(that._type) && _value
                .equals(that._value));
    }

    /**
     * Gets the parameters.
     * 
     * @param request
     *            the request
     * 
     * @return the parameters
     */
    public static Parameter[] getParameters(Request request) {
        List parameters = new ArrayList();
        String method = request.getMethod();
        HttpUrl url = request.getURL();

        String query = url.getQuery();
        String fragments = url.getFragment();
        if (url.getParameters() != null)
            url = url.getParentUrl();
        String contentType = request.getHeader("Content-Type");

        if (fragments != null) {
            NamedValue[] values = NamedValue.splitNamedValues(fragments, "&", "=");
            for (int i = 0; i < values.length; i++) {
                parameters.add(new Parameter(Parameter.LOCATION_FRAGMENT, values[i].getName(), "STRING", values[i]
                        .getValue()));
            }
        }
        if (query != null) {
            NamedValue[] values = NamedValue.splitNamedValues(query, "&", "=");
            for (int i = 0; i < values.length; i++) {
                parameters.add(new Parameter(Parameter.LOCATION_QUERY, values[i].getName(), "STRING", values[i]
                        .getValue()));
            }
        }
        NamedValue[] headers = request.getHeaders();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].getName().equals("Cookie")) {
                NamedValue[] cookies = NamedValue.splitNamedValues(headers[i].getValue(), "; *", "=");
                for (int j = 0; j < cookies.length; j++) {
                    parameters.add(new Parameter(Parameter.LOCATION_COOKIE, cookies[j].getName(), "STRING", cookies[j]
                            .getValue()));
                }
            }
        }
        if (method.equals("POST")) {
            if (contentType != null) {
                Parameter[] body = getParamsFromContent(contentType, request.getContent());
                for (int i = 0; i < body.length; i++) {
                    parameters.add(body[i]);
                }
            }
        }
        return (Parameter[]) parameters.toArray(NO_PARAMS);
    }

    /**
     * Gets the params from content.
     * 
     * @param contentType
     *            the content type
     * @param content
     *            the content
     * 
     * @return the params from content
     */
    public static Parameter[] getParamsFromContent(String contentType, byte[] content) {
        if (contentType.equals("application/x-www-form-urlencoded")) {
            String body = new String(content);
            NamedValue[] nv = NamedValue.splitNamedValues(body, "&", "=");
            Parameter[] params = new Parameter[nv.length];
            for (int i = 0; i < nv.length; i++) {
                params[i] = new Parameter(Parameter.LOCATION_BODY, nv[i].getName(), "STRING", nv[i].getValue());
            }
            return params;
        }
        // FIXME do Multi-part here, too
        return new Parameter[0];
    }

}
