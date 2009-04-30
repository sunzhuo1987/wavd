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

import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class NamedValue.
 */
public class NamedValue {

    /** The _name. */
    private String _name;

    /** The _value. */
    private String _value;

    /** The _logger. */
    private static Logger _logger = Logger.getLogger("org.owasp.webscarab.model.NamedValue");

    {
        _logger.setLevel(Level.INFO);
    }

    /**
     * Instantiates a new named value.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public NamedValue(String name, String value) {
        _name = name;
        _value = value;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return _name + "='" + _value + "'";
    }

    /**
     * Split named values.
     * 
     * @param source
     *            the source
     * @param pairSeparator
     *            the pair separator
     * @param nvSeparator
     *            the nv separator
     * 
     * @return the named value[]
     */
    public static NamedValue[] splitNamedValues(String source, String pairSeparator, String nvSeparator) {
        try {
            if (source == null)
                return new NamedValue[0];
            String[] pairs = source.split(pairSeparator);
            _logger.fine("Split \"" + source + "\" into " + pairs.length);
            NamedValue[] values = new NamedValue[pairs.length];
            for (int i = 0; i < pairs.length; i++) {
                String[] nv = pairs[i].split(nvSeparator, 2);
                if (nv.length == 2) {
                    values[i] = new NamedValue(nv[0], nv[1]);
                } else if (nv.length == 1) {
                    values[i] = new NamedValue(nv[0], "");
                } else {
                    values[i] = null;
                }
            }
            return values;
        } catch (ArrayIndexOutOfBoundsException aioob) {
            _logger.warning("Error splitting \"" + source + "\" using '" + pairSeparator + "' and '" + nvSeparator
                    + "'");
        }
        return new NamedValue[0];
    }

}
