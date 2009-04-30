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
 * HTMLParser.java
 *
 * Created on July 21, 2004, 4:25 PM
 */

package edu.lnmiit.wavd.parser;

import java.util.logging.Logger;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class HTMLParser.
 */
public class HTMLParser implements ContentParser {

    /** The _logger. */
    private Logger _logger = Logger.getLogger(this.getClass().getName());

    /**
     * Instantiates a new hTML parser.
     */
    public HTMLParser() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.parser.ContentParser#parseMessage(edu.lnmiit.wavd.model
     * .HttpUrl, edu.lnmiit.wavd.model.Message)
     */
    public Object parseMessage(HttpUrl url, Message message) {
        String contentType = message.getHeader("Content-Type");
        if (contentType == null || !contentType.matches("text/html.*")) {
            return null;
        }
        byte[] content = message.getContent();
        if (content == null || content.length == 0) {
            return null;
        }
        Parser parser = Parser.createParser(new String(content), null);
        try {
            NodeList nodelist = parser.extractAllNodesThatMatch(new NodeFilter() {
                public boolean accept(Node node) {
                    return true;
                }
            });
            return nodelist;
        } catch (ParserException pe) {
            _logger.severe(pe.toString());
            return null;
        }
    }

}
