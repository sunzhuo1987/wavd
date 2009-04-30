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


package javax.xml.namespace;

import java.io.Serializable;

import javax.xml.XMLConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class QName.
 */
public class QName implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4418622981026545151L;
    
    /** The namespace uri. */
    private final String namespaceURI;
    
    /** The local part. */
    private final String localPart;
    
    /** The prefix. */
    private final String prefix;
    
    /** The q name. */
    private transient String qName;
    
    /** The hash code. */
    transient int hashCode = -1;
    
    /**
     * Instantiates a new q name.
     * 
     * @param namespaceURI the namespace uri
     * @param localPart the local part
     */
    public QName(String namespaceURI, String localPart) {
        this(namespaceURI, localPart, null);
    }
    
    /**
     * Instantiates a new q name.
     * 
     * @param namespaceURI the namespace uri
     * @param localPart the local part
     * @param prefix the prefix
     */
    public QName(String namespaceURI, String localPart, String prefix) {
        if (namespaceURI == null)
            namespaceURI = XMLConstants.NULL_NS_URI;
        if (localPart == null)
            throw new IllegalArgumentException();
        if (prefix == null)
            prefix = XMLConstants.DEFAULT_NS_PREFIX;
        else {
            if (XMLConstants.XML_NS_PREFIX.equals(prefix))
                namespaceURI = XMLConstants.XML_NS_URI;
            else if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix))
                namespaceURI = XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        }
        this.namespaceURI = namespaceURI;
        this.localPart = localPart;
        this.prefix = prefix;
    }
    
    /**
     * Instantiates a new q name.
     * 
     * @param localPart the local part
     */
    public QName(String localPart) {
        this(null, localPart, null);
    }
    
    /**
     * Gets the namespace uri.
     * 
     * @return the namespace uri
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }
    
    /**
     * Gets the local part.
     * 
     * @return the local part
     */
    public String getLocalPart() {
        return localPart;
    }
    
    /**
     * Gets the prefix.
     * 
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(Object obj) {
        if (obj instanceof QName) {
            QName qname = (QName) obj;
            return qname.getLocalPart().equals(localPart) &&
                    qname.getNamespaceURI().equals(namespaceURI);
        }
        return false;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode() {
        if (hashCode == -1)
            hashCode = localPart.hashCode() ^ namespaceURI.hashCode();
        return hashCode;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public synchronized String toString() {
        if (qName == null) {
            StringBuffer buf = new StringBuffer();
            if (namespaceURI.length() > 0) {
                buf.append('{');
                buf.append(namespaceURI);
                buf.append('}');
            }
            if (prefix.length() > 0) {
                buf.append(prefix);
                buf.append(':');
            }
            buf.append(localPart);
            qName = buf.toString();
        }
        return qName;
    }
    
    /**
     * Value of.
     * 
     * @param qNameAsString the q name as string
     * 
     * @return the q name
     */
    public static QName valueOf(String qNameAsString) {
        String namespaceUri = "", prefix = null;
        int start = qNameAsString.indexOf('{');
        int end = qNameAsString.indexOf('}');
        if (start != -1) {
            if (end < start)
                throw new IllegalArgumentException(qNameAsString);
            namespaceUri = qNameAsString.substring(start + 1, end);
            qNameAsString = qNameAsString.substring(end + 1);
        }
        start = qNameAsString.indexOf(':');
        if (start != -1) {
            prefix = qNameAsString.substring(0, start);
            qNameAsString = qNameAsString.substring(start + 1);
        }
        return new QName(namespaceUri, qNameAsString, prefix);
    }
    
}
