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
 * TextFormatter.java
 *
 * Created on April 12, 2004, 6:37 PM
 */

package edu.lnmiit.wavd.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Date;
import java.text.SimpleDateFormat;

// TODO: Auto-generated Javadoc
/**
 * The Class TextFormatter.
 */
public class TextFormatter extends Formatter {
    
    /** The _sdf. */
    SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm:ss ");
    
    /**
     * Instantiates a new text formatter.
     */
    public TextFormatter() {
    }
    
    /* (non-Javadoc)
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public String format(LogRecord record) {
        StringBuffer buff = new StringBuffer(100);
        buff.append(_sdf.format(new Date(record.getMillis())));
        buff.append(Thread.currentThread().getName());
        String className = record.getSourceClassName();
        if (className.indexOf(".")>-1) { 
            className = className.substring(className.lastIndexOf(".")+1,className.length());
        }
        buff.append("(").append(className).append(".");
        buff.append(record.getSourceMethodName()).append("): ");
        buff.append(record.getMessage());
        if (record.getParameters() != null) {
            Object[] params = record.getParameters();
            buff.append(" { ").append(params[0]);
            for (int i=1; i<params.length; i++) {
                buff.append(", ").append(params[i]);
            }
            buff.append(" }");
        }
        buff.append("\n");
        return buff.toString();
    }
    
}
