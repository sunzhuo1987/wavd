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
 * FileSystemStore.java
 *
 * Created on August 23, 2003, 4:17 PM
 */

package edu.lnmiit.wavd.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
// import java.text.ParseException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.logging.Level;

import edu.lnmiit.wavd.util.MRUCache;

// TODO: Auto-generated Javadoc
/**
 * The Class FileSystemStore.
 */
public class FileSystemStore implements SiteModelStore {
    
    /** The Constant NO_CHILDREN. */
    private static final HttpUrl[] NO_CHILDREN = new HttpUrl[0];
    
    /** The _dir. */
    private File _dir;
    
    /** The _conversation dir. */
    private File _conversationDir;
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /** The _conversations. */
    private List _conversations = new ArrayList();
    
    /** The _conversation properties. */
    private SortedMap _conversationProperties = new TreeMap(new NullComparator());
    
    /** The _url properties. */
    private SortedMap _urlProperties = new TreeMap(new NullComparator());
    
    /** The _url conversations. */
    private SortedMap _urlConversations = new TreeMap(new NullComparator());
    
    /** The _urls. */
    private SortedMap _urls = new TreeMap(new NullComparator());
    
    /** The _request cache. */
    private Map _requestCache = new MRUCache(16);
    
    /** The _response cache. */
    private Map _responseCache = new MRUCache(16);
    
    /** The _url cache. */
    private Map _urlCache = new MRUCache(32);
    
    /** The _cookies. */
    private SortedMap _cookies = new TreeMap();
    
    /**
     * Checks if is existing session.
     * 
     * @param dir the dir
     * 
     * @return true, if is existing session
     */
    public static boolean isExistingSession(File dir) {
        File f = new File(dir, "conversations");
        return f.exists() && f.isDirectory();
    }
    
    /**
     * Instantiates a new file system store.
     * 
     * @param dir the dir
     * 
     * @throws StoreException the store exception
     */
    public FileSystemStore(File dir) throws StoreException {
        _logger.setLevel(Level.FINE);
        if (dir == null) {
            throw new StoreException("Cannot create a new FileSystemStore with a null directory!");
        } else {
            _dir = dir;
        }
        _conversationDir = new File(_dir, "conversations");
        if (_conversationDir.exists()) {
            _logger.fine("Loading session from " + _dir);
            load();
            _logger.fine("Finished loading session from " + _dir);
        } else {
            create();
        }
    }
    
    /**
     * Load.
     * 
     * @throws StoreException the store exception
     */
    private void load() throws StoreException {
        _logger.fine("Loading conversations");
        loadConversationProperties();
        _logger.fine("Loading urls");
        loadUrlProperties();
        _logger.fine("Loading cookies");
        loadCookies();
        _logger.fine("Done!");
    }
    
    /**
     * Load conversation properties.
     * 
     * @throws StoreException the store exception
     */
    private void loadConversationProperties() throws StoreException {
        ConversationID.reset();
        try {
            File f = new File(_dir, "conversationlog");
            if (!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            int linecount = 0;
            String line;
            Map map = null;
            ConversationID id = null;
            while ((line = br.readLine()) != null) {
                linecount++;
                if (line.startsWith("### Conversation :")) {
                    String cid = line.substring(line.indexOf(":")+2);
                    try {
                        id = new ConversationID(cid);
                        map = new HashMap();
                        _conversations.add(id);
                        _conversationProperties.put(id, map);
                    } catch (NumberFormatException nfe) {
                        throw new StoreException("Malformed conversation ID (" + cid +") parsing conversation log");
                    }
                } else if (line.equals("")) {
                    try {
                        HttpUrl url = new HttpUrl((String) map.get("URL"));
                        addConversationForUrl(url, id);
                    } catch (MalformedURLException mue) {
                        throw new StoreException("Malformed URL reading conversation " + id);
                    }
                    id = null;
                    map = null;
                } else {
                    if (map == null) throw new StoreException("Malformed conversation log at line " + linecount);
                    String property = line.substring(0, line.indexOf(":"));
                    String value = line.substring(line.indexOf(":")+2);
                    addProperty(map, property, value);
                }
            }
        } catch (IOException ioe) {
            throw new StoreException("Exception loading conversationlog: " + ioe);
        }
    }
    
    /**
     * Load url properties.
     * 
     * @throws StoreException the store exception
     */
    private void loadUrlProperties() throws StoreException {
        try {
            File f = new File(_dir, "urlinfo");
            if (!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            int linecount = 0;
            String line;
            Map map = null;
            HttpUrl url = null;
            while ((line = br.readLine()) != null) {
                linecount++;
                if (line.startsWith("### URL :")) {
                    String urlstr = line.substring(line.indexOf(":")+2);
                    try {
                        url = new HttpUrl(urlstr);
                        addUrl(url);
                        map = (Map) _urlProperties.get(url);
                    } catch (MalformedURLException mue) {
                        throw new StoreException("Malformed URL " + urlstr + " at line " + linecount + " in urlinfo");
                    }
                } else if (line.equals("")) {
                    url = null;
                    map = null;
                } else {
                    if (map == null) throw new StoreException("Malformed url info at line " + linecount);
                    String property = line.substring(0, line.indexOf(":"));
                    String value = line.substring(line.indexOf(":")+2);
                    addProperty(map, property, value);
                }
            }
        } catch (IOException ioe) {
            throw new StoreException("Exception loading url info : " + ioe);
        }
    }
    
    /**
     * Creates the.
     * 
     * @throws StoreException the store exception
     */
    private void create() throws StoreException {
        // create the empty directory structure
        if (!_dir.exists() && !_dir.mkdirs()) {
            throw new StoreException("Couldn't create directory " + _dir);
        } else if (!_dir.isDirectory()) {
            throw new StoreException(_dir + " exists, and is not a directory!");
        }
        
        _conversationDir = new File(_dir, "conversations");
        if (!_conversationDir.exists() && !_conversationDir.mkdirs()) {
            throw new StoreException("Couldn't create directory " + _conversationDir);
        } else if (!_conversationDir.isDirectory()) {
            throw new StoreException(_conversationDir + " exists, and is not a directory!");
        }
        
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#addConversation(edu.lnmiit.wavd.model.ConversationID, java.util.Date, edu.lnmiit.wavd.model.Request, edu.lnmiit.wavd.model.Response)
     */
    
    /**
     * adds a new conversation
     * @param id the id of the new conversation
     * @param when the date the conversation was created
     * @param request the request to add
     * @param response the response to add
     */
    public int addConversation(ConversationID id, Date when, Request request, Response response) {
        setRequest(id, request);
        setResponse(id, response);
        Map map = new HashMap();
        _conversationProperties.put(id, map);
        setConversationProperty(id, "METHOD", request.getMethod());
        setConversationProperty(id, "URL", request.getURL().toString());
        setConversationProperty(id, "STATUS", response.getStatusLine());
        setConversationProperty(id, "WHEN", Long.toString(when.getTime()));
        
        addConversationForUrl(request.getURL(), id);
        int index = Collections.binarySearch(_conversations, id);
        if (index<0) {
            index = -index -1;
            _conversations.add(index, id);
        }
        return index;
    }
    
    /**
     * Adds the conversation for url.
     * 
     * @param url the url
     * @param id the id
     */
    private void addConversationForUrl(HttpUrl url, ConversationID id) {
        List clist = (List) _urlConversations.get(url);
        if (clist == null) {
            clist = new ArrayList();
            _urlConversations.put(url, clist);
        }
        int index = Collections.binarySearch(clist, id);
        if (index < 0)
            clist.add(-index-1, id);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#setConversationProperty(edu.lnmiit.wavd.model.ConversationID, java.lang.String, java.lang.String)
     */
    public void setConversationProperty(ConversationID id, String property, String value) {
        Map map = (Map) _conversationProperties.get(id);
        if (map == null) throw new NullPointerException("No conversation Map for " + id);
        map.put(property, value);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#addConversationProperty(edu.lnmiit.wavd.model.ConversationID, java.lang.String, java.lang.String)
     */
    public boolean addConversationProperty(ConversationID id, String property, String value) {
        Map map = (Map) _conversationProperties.get(id);
        if (map == null) throw new NullPointerException("No conversation Map for " + id);
        return addProperty(map, property, value);
    }
    
    /**
     * Adds the property.
     * 
     * @param map the map
     * @param property the property
     * @param value the value
     * 
     * @return true, if successful
     */
    private boolean addProperty(Map map, String property, String value) {
        Object previous = map.get(property);
        if (previous == null) {
            map.put(property, value);
            return true;
        } else if (previous instanceof String) {
            if (previous.equals(value)) return false;
            String[] newval = new String[2];
            newval[0] = (String) previous;
            newval[1] = value;
            map.put(property, newval);
            return true;
        } else {
            String[] old = (String[]) previous;
            for (int i=0; i<old.length; i++)
                if (old[i].equals(value))
                    return false;
            String[] newval = new String[old.length + 1];
            System.arraycopy(old, 0, newval, 0, old.length);
            newval[old.length] = value;
            map.put(property, newval);
            return true;
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getConversationProperties(edu.lnmiit.wavd.model.ConversationID, java.lang.String)
     */
    public String[] getConversationProperties(ConversationID id, String property) {
        Map map = (Map) _conversationProperties.get(id);
        if (map == null) throw new NullPointerException("No conversation Map for " + id);
        return getProperties(map, property);
    }
    
    /**
     * Gets the properties.
     * 
     * @param map the map
     * @param property the property
     * 
     * @return the properties
     */
    private String[] getProperties(Map map, String property) {
        Object value = map.get(property);
        if (value == null) {
            return new String[0];
        } else if (value instanceof String[]) {
            String[] values = (String[]) value;
            if (values.length == 0) return values;
            String[] copy = new String[values.length];
            System.arraycopy(values, 0, copy, 0, values.length);
            return copy;
        } else {
            String[] values = new String[] {(String) value};
            return values;
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#addUrl(edu.lnmiit.wavd.model.HttpUrl)
     */
    public void addUrl(HttpUrl url) {
        if (_urlProperties.get(url) != null) throw new IllegalStateException("Adding an URL that is already there " + url);
        Map map = new HashMap();
        _urlProperties.put(url, map);
        
        HttpUrl parent = url.getParentUrl();
        _urlCache.remove(parent);
        SortedSet childSet = (SortedSet) _urls.get(parent);
        if (childSet == null) {
            childSet = new TreeSet();
            _urls.put(parent, childSet);
        }
        childSet.add(url);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#isKnownUrl(edu.lnmiit.wavd.model.HttpUrl)
     */
    public boolean isKnownUrl(HttpUrl url) {
        return _urlProperties.containsKey(url);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#setUrlProperty(edu.lnmiit.wavd.model.HttpUrl, java.lang.String, java.lang.String)
     */
    public void setUrlProperty(HttpUrl url, String property, String value) {
        Map map = (Map) _urlProperties.get(url);
        if (map == null) throw new NullPointerException("No URL Map for " + url);
        map.put(property, value);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#addUrlProperty(edu.lnmiit.wavd.model.HttpUrl, java.lang.String, java.lang.String)
     */
    public boolean addUrlProperty(HttpUrl url, String property, String value) {
        Map map = (Map) _urlProperties.get(url);
        if (map == null) throw new NullPointerException("No URL Map for " + url);
        return addProperty(map, property, value);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getUrlProperties(edu.lnmiit.wavd.model.HttpUrl, java.lang.String)
     */
    public String[] getUrlProperties(HttpUrl url, String property) {
        Map map = (Map) _urlProperties.get(url);
        if (map == null) return new String[0];
        return getProperties(map, property);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getChildCount(edu.lnmiit.wavd.model.HttpUrl)
     */
    public int getChildCount(HttpUrl url) {
        SortedSet childSet = (SortedSet) _urls.get(url);
        if (childSet == null) return 0;
        return childSet.size();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getChildAt(edu.lnmiit.wavd.model.HttpUrl, int)
     */
    public HttpUrl getChildAt(HttpUrl url, int index) {
        HttpUrl[] children = (HttpUrl[]) _urlCache.get(url);
        if (children == null) {
            SortedSet childSet = (SortedSet) _urls.get(url);
            if (childSet == null)
                throw new IndexOutOfBoundsException(url + " has no children");
            if (index >= childSet.size())
                throw new IndexOutOfBoundsException(url + " has only " + childSet.size() + " children, not " + index);
            children = ((HttpUrl[]) childSet.toArray(NO_CHILDREN));
            _urlCache.put(url, children);
        }
        return children[index];
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getIndexOf(edu.lnmiit.wavd.model.HttpUrl)
     */
    public int getIndexOf(HttpUrl url) {
        HttpUrl parent = url.getParentUrl();
        HttpUrl[] children = (HttpUrl[]) _urlCache.get(parent);
        if (children == null) {
            SortedSet childSet = (SortedSet) _urls.get(parent);
            if (childSet == null)
                throw new IndexOutOfBoundsException(url + " has no children");
            children = ((HttpUrl[]) childSet.toArray(NO_CHILDREN));
            _urlCache.put(parent, children);
        }
        return Arrays.binarySearch(children, url);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getConversationCount(edu.lnmiit.wavd.model.HttpUrl)
     */
    public int getConversationCount(HttpUrl url) {
        if (url == null) return _conversationProperties.size();
        List list = (List) _urlConversations.get(url);
        if (list == null) return 0;
        return list.size();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getConversationAt(edu.lnmiit.wavd.model.HttpUrl, int)
     */
    public ConversationID getConversationAt(HttpUrl url, int index) {
        List list;
        if (url == null) {
            list = new ArrayList(_conversationProperties.keySet());
        } else {
            list = (List) _urlConversations.get(url);
        }
        if (list == null) throw new NullPointerException(url + " does not have any conversations");
        if (list.size() < index) throw new ArrayIndexOutOfBoundsException(url + " does not have " + index + " conversations");
        return (ConversationID) list.get(index);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getIndexOfConversation(edu.lnmiit.wavd.model.HttpUrl, edu.lnmiit.wavd.model.ConversationID)
     */
    public int getIndexOfConversation(HttpUrl url, ConversationID id) {
        List list;
        if (url == null) {
            list = _conversations;
        } else {
            list = (List) _urlConversations.get(url);
        }
        if (list == null) throw new NullPointerException(url + " has no conversations");
        int index =  Collections.binarySearch(list, id);
        return index;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#setRequest(edu.lnmiit.wavd.model.ConversationID, edu.lnmiit.wavd.model.Request)
     */
    public void setRequest(ConversationID id, Request request) {
        // write the request to the disk using the requests own id
        if (request == null) {
            return;
        }
        _requestCache.put(id, request);
        try {
            File f = new File(_conversationDir, id + "-request");
            FileOutputStream fos = new FileOutputStream(f);
            request.write(fos);
            fos.close();
        } catch (IOException ioe) {
            _logger.severe("IOException writing request(" +id + ") : " + ioe);
        }
    }
    
    /*
     * retrieves the request associated with the specified conversation id
     */
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getRequest(edu.lnmiit.wavd.model.ConversationID)
     */
    public Request getRequest(ConversationID id) {
        Object o = _requestCache.get(id);
        if (o != null) return (Request) o;
        
        File f = new File(_conversationDir, id + "-request");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException fnfe) {
            return null;
        }
        Request r = new Request();
        try {
            r.read(fis);
            r.getContent();
            fis.close();
            return r;
        } catch (IOException ioe) {
            _logger.severe("IOException reading request(" +id + ") : " + ioe);
            return null;
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#setResponse(edu.lnmiit.wavd.model.ConversationID, edu.lnmiit.wavd.model.Response)
     */
    public void setResponse(ConversationID id, Response response) {
        // write the request to the disk using the requests own id
        if (response == null) {
            return;
        }
        _responseCache.put(id, response);
        try {
            File f = new File(_conversationDir, id + "-response");
            FileOutputStream fos = new FileOutputStream(f);
            response.write(fos);
            fos.close();
        } catch (IOException ioe) {
            _logger.severe("IOException writing response(" +id + ") : " + ioe);
        }
    }
    
    /*
     * retrieves the response associated with the specified conversation id
     */
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getResponse(edu.lnmiit.wavd.model.ConversationID)
     */
    public Response getResponse(ConversationID id) {
        Object o = _responseCache.get(id);
        if (o != null) return (Response) o;
        
        File f = new File(_conversationDir, id + "-response");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException fnfe) {
            return null;
        }
        Response r = new Response();
        try {
            r.read(fis);
            r.getContent();
            fis.close();
            return r;
        } catch (IOException ioe) {
            _logger.severe("IOException reading response(" +id + ") : " + ioe);
            return null;
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#flush()
     */
    public void flush() throws StoreException {
        flushConversationProperties();
        flushUrlProperties();
        flushCookies();
    }
    
    /**
     * Flush conversation properties.
     * 
     * @throws StoreException the store exception
     */
    private void flushConversationProperties() throws StoreException {
        try {
            File f = new File(_dir, "conversationlog");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            Iterator it = _conversationProperties.keySet().iterator();
            ConversationID id;
            Map map;
            while (it.hasNext()) {
                id = (ConversationID) it.next();
                map = (Map) _conversationProperties.get(id);
                bw.write("### Conversation : " + id + "\n");
                Iterator props = map.keySet().iterator();
                while(props.hasNext()) {
                    String property = (String) props.next();
                    String[] values = getProperties(map,  property);
                    if (values != null && values.length > 0) {
                        for (int i=0; i< values.length; i++) {
                            bw.write(property + ": " + values[i] + "\n");
                        }
                    }
                }
                bw.write("\n");
            }
            bw.close();
        } catch (IOException ioe) {
            throw new StoreException("Error writing conversation properties: " + ioe);
        }
    }
    
    /**
     * Flush url properties.
     * 
     * @throws StoreException the store exception
     */
    private void flushUrlProperties() throws StoreException {
        try {
            File f = new File(_dir, "urlinfo");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            Iterator it = _urlProperties.keySet().iterator();
            HttpUrl url;
            Map map;
            while (it.hasNext()) {
                url = (HttpUrl) it.next();
                map = (Map) _urlProperties.get(url);
                bw.write("### URL : " + url + "\n");
                Iterator props = map.keySet().iterator();
                while(props.hasNext()) {
                    String property = (String) props.next();
                    String[] values = getProperties(map,  property);
                    if (values != null && values.length > 0) {
                        for (int i=0; i< values.length; i++) {
                            bw.write(property + ": " + values[i] + "\n");
                        }
                    }
                }
                bw.write("\n");
            }
            bw.close();
        } catch (IOException ioe) {
            throw new StoreException("Error writing url properties: " + ioe);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getCookieCount()
     */
    public int getCookieCount() {
        return _cookies.size();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getCookieCount(java.lang.String)
     */
    public int getCookieCount(String key) {
        List list = (List) _cookies.get(key);
        if (list == null) return 0;
        return list.size();
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getCookieAt(int)
     */
    public String getCookieAt(int index) {
        return (String) new ArrayList(_cookies.keySet()).get(index);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getCookieAt(java.lang.String, int)
     */
    public Cookie getCookieAt(String key, int index) {
        List list = (List) _cookies.get(key);
        if (list == null) throw new NullPointerException("No such cookie! " + key);
        return (Cookie) list.get(index);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getCurrentCookie(java.lang.String)
     */
    public Cookie getCurrentCookie(String key) {
        List list = (List) _cookies.get(key);
        if (list == null) throw new NullPointerException("No such cookie! " + key);
        return (Cookie) list.get(list.size()-1);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getIndexOfCookie(edu.lnmiit.wavd.model.Cookie)
     */
    public int getIndexOfCookie(Cookie cookie) {
        return new ArrayList(_cookies.keySet()).indexOf(cookie.getKey());
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#getIndexOfCookie(java.lang.String, edu.lnmiit.wavd.model.Cookie)
     */
    public int getIndexOfCookie(String key, Cookie cookie) {
        List list = (List) _cookies.get(key);
        if (list == null) throw new NullPointerException("No such cookie! " + key);
        return list.indexOf(cookie);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#addCookie(edu.lnmiit.wavd.model.Cookie)
     */
    public boolean addCookie(Cookie cookie) {
        String key = cookie.getKey();
        List list = (List) _cookies.get(key);
        if (list == null) {
            list = new ArrayList();
            _cookies.put(key, list);
        }
        if (list.indexOf(cookie) > -1) return false;
        list.add(cookie);
        return true;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.SiteModelStore#removeCookie(edu.lnmiit.wavd.model.Cookie)
     */
    public boolean removeCookie(Cookie cookie) {
        String key = cookie.getKey();
        List list = (List) _cookies.get(key);
        if (list == null) return false;
        boolean deleted = list.remove(cookie);
        if (list.size() == 0) _cookies.remove(key);
        return deleted;
    }
    
    /**
     * Load cookies.
     * 
     * @throws StoreException the store exception
     */
    private void loadCookies() throws StoreException {
        _cookies.clear();
        try {
            File f = new File(_dir, "cookies");
            if (!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            int linecount = 0;
            String line;
            List list = null;
            String name = null;
            Cookie cookie = null;
            while ((line = br.readLine()) != null) {
                linecount++;
                if (line.startsWith("### Cookie :")) {
                    name = line.substring(line.indexOf(":")+2);
                    list = new ArrayList();
                    _cookies.put(name, list);
                } else if (line.equals("")) {
                    name = null;
                    list = null;
                } else {
                    if (list == null) throw new StoreException("Malformed cookie log at line " + linecount);
                    int pos = line.indexOf(" ");
                    try {
                        long time = Long.parseLong(line.substring(0, pos));
                        cookie = new Cookie(new Date(time), line.substring(pos+1));
                        list.add(cookie);
                    } catch (Exception e) {
                        throw new StoreException("Malformed cookie log at line " + linecount + " : " + e);
                    }
                }
            }
        } catch (IOException ioe) {
            throw new StoreException("Exception loading conversationlog: " + ioe);
        }
    }
    
    /**
     * Flush cookies.
     * 
     * @throws StoreException the store exception
     */
    private void flushCookies() throws StoreException {
        try {
            File f = new File(_dir, "cookies");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            Iterator it = _cookies.keySet().iterator();
            String name;
            List list;
            while (it.hasNext()) {
                name = (String) it.next();
                list = (List) _cookies.get(name);
                bw.write("### Cookie : " + name + "\n");
                Iterator cookies = list.iterator();
                while(cookies.hasNext()) {
                    Cookie cookie = (Cookie) cookies.next();
                    bw.write(cookie.toString() + "\n");
                }
                bw.write("\n");
            }
            bw.close();
        } catch (IOException ioe) {
            throw new StoreException("Error writing cookies: " + ioe);
        }
    }
    
    /**
     * The Class NullComparator.
     */
    private class NullComparator implements Comparator {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(T, T)
         */
        public int compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return 0;
            if (o1 == null && o2 != null) return 1;
            if (o1 != null && o2 == null) return -1;
            if (o1 instanceof Comparable) return ((Comparable)o1).compareTo(o2);
            throw new ClassCastException("Incomparable objects " + o1.getClass().getName() + " and " + o2.getClass().getName());
        }
        
    }
    
}
