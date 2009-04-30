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

package edu.lnmiit.wavd.util;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class DOMHandler.
 */
public class DOMHandler implements ContentHandler, LexicalHandler {
    
    /** The _document. */
    private Document _document = null;
    
    /** The _stack. */
    private Stack _stack = new Stack();
    
    /** The _last. */
    private Node _last = null;
    
    /** The _namespaces. */
    private List _namespaces = null;
    
    /** The Constant XMLNS_PREFIX. */
    public static final String XMLNS_PREFIX = "xmlns";
    
    /** The Constant XMLNS_STRING. */
    public static final String XMLNS_STRING = "xmlns:";
    
    /** The Constant XMLNS_URI. */
    public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    
    /**
     * Instantiates a new dOM handler.
     * 
     * @throws ParserConfigurationException the parser configuration exception
     */
    public DOMHandler() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        _document = builder.newDocument();
    }
    
    /**
     * Gets the document.
     * 
     * @return the document
     */
    public Document getDocument() {
        return _document;
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length) {
        Node last = (Node)_stack.peek();
        
        if (last != _document) {
            final String text = new String(ch, start, length);
            if (_last != null && _last.getNodeType() == Node.TEXT_NODE) {
                ((Text)_last).appendData(text);
            } else{
                _last = last.appendChild(_document.createTextNode(text));
            }
        }
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() {
        _stack.push(_document);
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() {
        _stack.pop();
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespace, String localName, String qName, Attributes attrs) {
        Element element = (Element)_document.createElementNS(namespace, qName);
        
        // Add namespace declarations first
        if (_namespaces != null) {
            for (int i = 0; i < _namespaces.size(); i++) {
                String prefix = (String) _namespaces.get(i++);
                
                if (prefix == null || prefix.equals("")) {
                    element.setAttributeNS(XMLNS_URI, XMLNS_PREFIX, (String) _namespaces.get(i));
                }
                else {
                    element.setAttributeNS(XMLNS_URI, XMLNS_STRING + prefix, (String) _namespaces.get(i));
                }
            }
            _namespaces.clear();
        }
        
        // Add attributes to element
        final int nattrs = attrs.getLength();
        for (int i = 0; i < nattrs; i++) {
            if (attrs.getLocalName(i) == null) {
                element.setAttribute(attrs.getQName(i), attrs.getValue(i));
            }
            else {
                element.setAttributeNS(attrs.getURI(i), attrs.getQName(i),
                attrs.getValue(i));
            }
        }
        
        // Append this new node onto current stack node
        Node last = (Node)_stack.peek();
        last.appendChild(element);
        
        // Push this node onto stack
        _stack.push(element);
        _last = null;
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String namespace, String localName, String qName) {
        _stack.pop();
        _last = null;
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
     */
    public void startPrefixMapping(String prefix, String uri) {
        if (_namespaces == null) {
            _namespaces = new ArrayList();
        }
        _namespaces.add(prefix);
        _namespaces.add(uri);
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix) {
        // do nothing
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] ch, int start, int length) {
        // do nothing
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
     */
    public void processingInstruction(String target, String data) {
        Node last = (Node)_stack.peek();
        ProcessingInstruction pi = _document.createProcessingInstruction(target, data);
        if (pi != null){
            last.appendChild(pi);
            _last = pi;
        }
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator locator) {
        // do nothing
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String name) {
        // do nothing
    }
    
    
    /* (non-Javadoc)
     * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
     */
    public void comment(char[] ch, int start, int length) {
        Node last = (Node)_stack.peek();
        Comment comment = _document.createComment(new String(ch,start,length));
        if (comment != null){
            last.appendChild(comment);
            _last = comment;
        }
    }
    
    // Lexical Handler methods- not implemented
    /* (non-Javadoc)
     * @see org.xml.sax.ext.LexicalHandler#startCDATA()
     */
    public void startCDATA() { }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ext.LexicalHandler#endCDATA()
     */
    public void endCDATA() { }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
     */
    public void startEntity(java.lang.String name) { }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ext.LexicalHandler#endDTD()
     */
    public void endDTD() { }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
     */
    public void endEntity(String name) { }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
     */
    public void startDTD(String name, String publicId, String systemId) throws SAXException { }
    
}
