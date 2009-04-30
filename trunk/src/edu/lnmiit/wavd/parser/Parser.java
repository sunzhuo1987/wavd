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
 * Parser.java
 *
 * Created on September 7, 2004, 10:36 PM
 */

package edu.lnmiit.wavd.parser;



import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.Message;
import edu.lnmiit.wavd.util.MRUCache;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Parser.
 */
public class Parser {
    
    /** The _parsers. */
    private static List _parsers = new ArrayList();
    
    /** The _logger. */
    private static Logger _logger = Logger.getLogger("org.owasp.webscarab.parser.Parser");
    
    // we cache the 8 most recent messages and their parsed versions
    /** The _cache. */
    private static MRUCache _cache = new MRUCache(8);
    
    static {
        _parsers.add(new HTMLParser());
    }
    
    /**
     * Instantiates a new parser.
     */
    private Parser() {
    }
    
    /**
     * Parses the.
     * 
     * @param url the url
     * @param message the message
     * 
     * @return the object
     */    
    public static Object parse(HttpUrl url, Message message) {
        if (_cache.containsKey(message)) {
            return _cache.get(message);
        }
        Iterator it = _parsers.iterator();
        Object parsed = null;
        ContentParser parser;
        while(it.hasNext()) {
            parser = (ContentParser) it.next();
            parsed = parser.parseMessage(url, message);
            if (parsed != null) break;
        }
        _cache.put(message, parsed);
        return parsed;
    }
    
}
