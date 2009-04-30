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
 * SiteModel.java
 *
 * Created on July 13, 2004, 3:58 PM
 */

package edu.lnmiit.wavd.model;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import EDU.oswego.cs.dl.util.concurrent.Sync;
import edu.lnmiit.wavd.util.MRUCache;
import edu.lnmiit.wavd.util.ReentrantReaderPreferenceReadWriteLock;

// TODO: Auto-generated Javadoc
/**
 * The Class FrameworkModel.
 */
public class FrameworkModel {

    /** The _rwl. */
    private ReentrantReaderPreferenceReadWriteLock _rwl = new ReentrantReaderPreferenceReadWriteLock();

    /** The Constant NO_COOKIES. */
    private static final Cookie[] NO_COOKIES = new Cookie[0];

    /** The _listener list. */
    private EventListenerList _listenerList = new EventListenerList();

    // keeps a fairly small cache of recently used HttpUrl objects
    /** The _url cache. */
    private Map _urlCache = new MRUCache(200);

    /** The _store. */
    private SiteModelStore _store = null;

    /** The _url model. */
    private FrameworkUrlModel _urlModel;

    /** The _conversation model. */
    private FrameworkConversationModel _conversationModel;

    /** The _modified. */
    private boolean _modified = false;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new framework model.
     */
    public FrameworkModel() {
        _logger.setLevel(Level.INFO);
        _conversationModel = new FrameworkConversationModel(this);
        _urlModel = new FrameworkUrlModel();
    }

    /**
     * Sets the session.
     * 
     * @param type
     *            the type
     * @param store
     *            the store
     * @param session
     *            the session
     * 
     * @throws StoreException
     *             the store exception
     */
    public void setSession(String type, Object store, String session) throws StoreException {
        try {
            _rwl.writeLock().acquire();
            if (type.equals("FileSystem") && store instanceof File) {
                try {
                    _store = new FileSystemStore((File) store);
                } catch (Exception e) {
                    throw new StoreException("Error initialising session : " + e.getMessage());
                }
            } else {
                _rwl.writeLock().release();
                throw new StoreException("Unknown store type " + type + " and store " + store);
            }
            _rwl.readLock().acquire(); // downgrade
            _rwl.writeLock().release();
            _urlModel.fireUrlsChanged();
            _conversationModel.fireConversationsChanged();
            fireCookiesChanged();
            _rwl.readLock().release();
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }

    }

    /**
     * Read lock.
     * 
     * @return the sync
     */
    public Sync readLock() {
        return _rwl.readLock();
    }

    /**
     * Gets the url model.
     * 
     * @return the url model
     */
    public UrlModel getUrlModel() {
        return _urlModel;
    }

    /**
     * Gets the conversation model.
     * 
     * @return the conversation model
     */
    public ConversationModel getConversationModel() {
        return _conversationModel;
    }

    /**
     * Flush.
     * 
     * @throws StoreException
     *             the store exception
     */
    public void flush() throws StoreException {
        if (_modified) {
            try {
                _rwl.readLock().acquire();
                try {
                    _store.flush();
                    _modified = false;
                } finally {
                    _rwl.readLock().release();
                }
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
            }
        }
    }

    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    public boolean isModified() {
        return _modified;
    }

    /**
     * Reserve conversation id.
     * 
     * @return the conversation id
     */
    public ConversationID reserveConversationID() {
        return new ConversationID();
    }

    /**
     * Adds the conversation.
     * 
     * @param id
     *            the id
     * @param when
     *            the when
     * @param request
     *            the request
     * @param response
     *            the response
     * @param origin
     *            the origin
     */
    public void addConversation(ConversationID id, Date when, Request request, Response response, String origin) {
        try {
            HttpUrl url = request.getURL();
            addUrl(url); // fires appropriate events
            _rwl.writeLock().acquire();
            int index = _store.addConversation(id, when, request, response);
            _store.setConversationProperty(id, "ORIGIN", origin);
            _rwl.readLock().acquire();
            _rwl.writeLock().release();
            _conversationModel.fireConversationAdded(id, index); // FIXME
            _rwl.readLock().release();
            addUrlProperty(url, "METHODS", request.getMethod());
            addUrlProperty(url, "STATUS", response.getStatusLine());
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }
        _modified = true;
    }

    /**
     * Gets the conversation origin.
     * 
     * @param id
     *            the id
     * 
     * @return the conversation origin
     */
    public String getConversationOrigin(ConversationID id) {
        return getConversationProperty(id, "ORIGIN");
    }

    /**
     * Gets the conversation date.
     * 
     * @param id
     *            the id
     * 
     * @return the conversation date
     */
    public Date getConversationDate(ConversationID id) {
        try {
            _rwl.readLock().acquire();
            try {
                String when = getConversationProperty(id, "WHEN");
                if (when == null)
                    return null;
                try {
                    long time = Long.parseLong(when);
                    return new Date(time);
                } catch (NumberFormatException nfe) {
                    System.err.println("NumberFormatException parsing date for Conversation " + id + ": " + nfe);
                    return null;
                }
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Gets the request url.
     * 
     * @param conversation
     *            the conversation
     * 
     * @return the request url
     */

    public HttpUrl getRequestUrl(ConversationID conversation) {
        try {
            _rwl.readLock().acquire();
            try {
                // this allows us to reuse HttpUrl objects
                if (_urlCache.containsKey(conversation))
                    return (HttpUrl) _urlCache.get(conversation);

                String url = getConversationProperty(conversation, "URL");
                try {
                    HttpUrl httpUrl = new HttpUrl(url);
                    _urlCache.put(conversation, httpUrl);
                    return httpUrl;
                } catch (MalformedURLException mue) {
                    System.err.println("Malformed URL for Conversation " + conversation + ": " + mue);
                    return null;
                }
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Sets the conversation property.
     * 
     * @param conversation
     *            the conversation
     * @param property
     *            the property
     * @param value
     *            the value
     */
    public void setConversationProperty(ConversationID conversation, String property, String value) {
        try {
            _rwl.writeLock().acquire();
            _store.setConversationProperty(conversation, property, value);
            _rwl.readLock().acquire(); // downgrade
            _rwl.writeLock().release();
            _conversationModel.fireConversationChanged(conversation, 0); // FIXME
            fireConversationPropertyChanged(conversation, property);
            _rwl.readLock().release();
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }
        _modified = true;
    }

    /**
     * Adds the conversation property.
     * 
     * @param conversation
     *            the conversation
     * @param property
     *            the property
     * @param value
     *            the value
     * 
     * @return true, if successful
     */
    public boolean addConversationProperty(ConversationID conversation, String property, String value) {
        boolean change = false;
        try {
            _rwl.writeLock().acquire();
            change = _store.addConversationProperty(conversation, property, value);
            _rwl.readLock().acquire(); // downgrade to read lock
            _rwl.writeLock().release();
            if (change) {
                _conversationModel.fireConversationChanged(conversation, 0); // FIXME
                fireConversationPropertyChanged(conversation, property);
            }
            _rwl.readLock().release();
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }
        _modified = _modified || change;
        return change;
    }

    /**
     * Gets the conversation property.
     * 
     * @param conversation
     *            the conversation
     * @param property
     *            the property
     * 
     * @return the conversation property
     */
    public String getConversationProperty(ConversationID conversation, String property) {
        String[] values = getConversationProperties(conversation, property);
        if (values == null || values.length == 0)
            return null;
        if (values.length == 1)
            return values[0];
        StringBuffer value = new StringBuffer(values[0]);
        for (int i = 1; i < values.length; i++)
            value.append(", ").append(values[i]);
        return value.toString();
    }

    /**
     * Gets the request method.
     * 
     * @param id
     *            the id
     * 
     * @return the request method
     */
    public String getRequestMethod(ConversationID id) {
        return getConversationProperty(id, "METHOD");
    }

    /**
     * Gets the response status.
     * 
     * @param id
     *            the id
     * 
     * @return the response status
     */
    public String getResponseStatus(ConversationID id) {
        return getConversationProperty(id, "STATUS");
    }

    /**
     * Gets the conversation properties.
     * 
     * @param conversation
     *            the conversation
     * @param property
     *            the property
     * 
     * @return the conversation properties
     */
    public String[] getConversationProperties(ConversationID conversation, String property) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getConversationProperties(conversation, property);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Adds the url.
     * 
     * @param url
     *            the url
     */
    private void addUrl(HttpUrl url) {
        try {
            _rwl.readLock().acquire();
            try {
                if (!_store.isKnownUrl(url)) {
                    HttpUrl[] path = url.getUrlHierarchy();
                    for (int i = 0; i < path.length; i++) {
                        if (!_store.isKnownUrl(path[i])) {
                            _rwl.readLock().release(); // must give it up before
                            // writing
                            // XXX We could be vulnerable to a race condition
                            // here
                            // we should check again to make sure that it does
                            // not exist
                            // AFTER we get our writelock

                            // FIXME There is something very strange going on
                            // here
                            // sometimes we deadlock if we just do a straight
                            // acquire
                            // but there does not seem to be anything competing
                            // for the lock.
                            // This works, but it feels like a kluge! FIXME!!!
                            // _rwl.writeLock().acquire();
                            while (!_rwl.writeLock().attempt(5000)) {
                                _logger.severe("Timed out waiting for write lock, trying again");
                                _rwl.debug();
                            }
                            if (!_store.isKnownUrl(path[i])) {
                                _store.addUrl(path[i]);
                                _rwl.readLock().acquire(); // downgrade without
                                // giving up lock
                                _rwl.writeLock().release();
                                _urlModel.fireUrlAdded(path[i], 0); // FIXME
                                _modified = true;
                            } else { // modified by some other thread?! Go
                                // through the motions . . .
                                _rwl.readLock().acquire();
                                _rwl.writeLock().release();
                            }
                        }
                    }
                }
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }
    }

    /**
     * Sets the url property.
     * 
     * @param url
     *            the url
     * @param property
     *            the property
     * @param value
     *            the value
     */
    public void setUrlProperty(HttpUrl url, String property, String value) {
        addUrl(url);
        try {
            _rwl.writeLock().acquire();
            _store.setUrlProperty(url, property, value);
            _rwl.readLock().acquire(); // downgrade write to read
            _rwl.writeLock().release();
            _urlModel.fireUrlChanged(url, 0); // FIXME
            fireUrlPropertyChanged(url, property);
            _rwl.readLock().release();
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }
        _modified = true;
    }

    /**
     * Adds the url property.
     * 
     * @param url
     *            the url
     * @param property
     *            the property
     * @param value
     *            the value
     * 
     * @return true, if successful
     */
    public boolean addUrlProperty(HttpUrl url, String property, String value) {
        boolean change = false;
        addUrl(url);
        try {
            _rwl.writeLock().acquire();
            change = _store.addUrlProperty(url, property, value);
            _rwl.readLock().acquire();
            _rwl.writeLock().release();
            if (change) {
                _urlModel.fireUrlChanged(url, 0);
                fireUrlPropertyChanged(url, property);
            }
            _rwl.readLock().release();
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }
        _modified = _modified || change;
        return change;
    }

    /**
     * Gets the url properties.
     * 
     * @param url
     *            the url
     * @param property
     *            the property
     * 
     * @return the url properties
     */
    public String[] getUrlProperties(HttpUrl url, String property) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getUrlProperties(url, property);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Gets the url property.
     * 
     * @param url
     *            the url
     * @param property
     *            the property
     * 
     * @return the url property
     */
    public String getUrlProperty(HttpUrl url, String property) {
        String[] values = getUrlProperties(url, property);
        if (values == null || values.length == 0)
            return null;
        if (values.length == 1)
            return values[0];
        StringBuffer value = new StringBuffer(30);
        value.append(values[0]);
        for (int i = 1; i < values.length; i++)
            value.append(", ").append(values[i]);
        return value.toString();
    }

    /**
     * Gets the request.
     * 
     * @param conversation
     *            the conversation
     * 
     * @return the request
     */
    public Request getRequest(ConversationID conversation) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getRequest(conversation);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Gets the response.
     * 
     * @param conversation
     *            the conversation
     * 
     * @return the response
     */
    public Response getResponse(ConversationID conversation) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getResponse(conversation);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Adds the model listener.
     * 
     * @param listener
     *            the listener
     */
    public void addModelListener(FrameworkListener listener) {
        synchronized (_listenerList) {
            _listenerList.add(FrameworkListener.class, listener);
        }
    }

    /**
     * Removes the model listener.
     * 
     * @param listener
     *            the listener
     */
    public void removeModelListener(FrameworkListener listener) {
        synchronized (_listenerList) {
            _listenerList.remove(FrameworkListener.class, listener);
        }
    }

    /**
     * Gets the cookie count.
     * 
     * @return the cookie count
     */
    public int getCookieCount() {
        if (_store == null)
            return 0;
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getCookieCount();
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return 0;
        }
    }

    /**
     * Gets the cookie count.
     * 
     * @param key
     *            the key
     * 
     * @return the cookie count
     */
    public int getCookieCount(String key) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getCookieCount(key);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return 0;
        }
    }

    /**
     * Gets the cookie at.
     * 
     * @param index
     *            the index
     * 
     * @return the cookie at
     */
    public String getCookieAt(int index) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getCookieAt(index);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Gets the cookie at.
     * 
     * @param key
     *            the key
     * @param index
     *            the index
     * 
     * @return the cookie at
     */
    public Cookie getCookieAt(String key, int index) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getCookieAt(key, index);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Gets the index of cookie.
     * 
     * @param cookie
     *            the cookie
     * 
     * @return the index of cookie
     */
    public int getIndexOfCookie(Cookie cookie) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getIndexOfCookie(cookie);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return 0;
        }
    }

    /**
     * Gets the index of cookie.
     * 
     * @param key
     *            the key
     * @param cookie
     *            the cookie
     * 
     * @return the index of cookie
     */
    public int getIndexOfCookie(String key, Cookie cookie) {
        try {
            _rwl.readLock().acquire();
            try {
                return _store.getIndexOfCookie(key, cookie);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return 0;
        }
    }

    /**
     * Gets the current cookie.
     * 
     * @param key
     *            the key
     * 
     * @return the current cookie
     */
    public Cookie getCurrentCookie(String key) {
        try {
            _rwl.readLock().acquire();
            try {
                int count = _store.getCookieCount(key);
                return _store.getCookieAt(key, count - 1);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return null;
        }
    }

    /**
     * Adds the cookie.
     * 
     * @param cookie
     *            the cookie
     */
    public void addCookie(Cookie cookie) {
        try {
            _rwl.writeLock().acquire();
            boolean added = _store.addCookie(cookie);
            if (!added) { // we already had the cookie
                _rwl.writeLock().release();
            } else {
                _modified = true;
                _rwl.readLock().acquire();
                _rwl.writeLock().release();
                fireCookieAdded(cookie);
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }
    }

    /**
     * Removes the cookie.
     * 
     * @param cookie
     *            the cookie
     */
    public void removeCookie(Cookie cookie) {
        try {
            _rwl.writeLock().acquire();
            boolean deleted = _store.removeCookie(cookie);
            if (deleted) {
                _modified = true;
                _rwl.readLock().acquire();
                _rwl.writeLock().release();
                fireCookieRemoved(cookie);
                _rwl.readLock().release();
            } else {
                _rwl.writeLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
        }
    }

    /**
     * Gets the cookies for url.
     * 
     * @param url
     *            the url
     * 
     * @return the cookies for url
     */
    public Cookie[] getCookiesForUrl(HttpUrl url) {
        try {
            _rwl.readLock().acquire();
            try {
                List cookies = new ArrayList();

                String host = url.getHost();
                String path = url.getPath();

                int size = getCookieCount();
                for (int i = 0; i < size; i++) {
                    String key = getCookieAt(i);
                    Cookie cookie = getCurrentCookie(key);
                    String domain = cookie.getDomain();
                    if (host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain))) {
                        if (path.startsWith(cookie.getPath())) {
                            cookies.add(cookie);
                        }
                    }
                }
                return (Cookie[]) cookies.toArray(NO_COOKIES);
            } finally {
                _rwl.readLock().release();
            }
        } catch (InterruptedException ie) {
            _logger.severe("Interrupted! " + ie);
            return NO_COOKIES;
        }
    }

    /**
     * Fire cookie added.
     * 
     * @param cookie
     *            the cookie
     */
    protected void fireCookieAdded(Cookie cookie) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FrameworkEvent evt = new FrameworkEvent(this, cookie);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FrameworkListener.class) {
                try {
                    ((FrameworkListener) listeners[i + 1]).cookieAdded(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire cookie removed.
     * 
     * @param cookie
     *            the cookie
     */
    protected void fireCookieRemoved(Cookie cookie) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FrameworkEvent evt = new FrameworkEvent(this, cookie);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FrameworkListener.class) {
                try {
                    ((FrameworkListener) listeners[i + 1]).cookieRemoved(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire cookies changed.
     */
    protected void fireCookiesChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FrameworkListener.class) {
                try {
                    ((FrameworkListener) listeners[i + 1]).cookiesChanged();
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire conversation property changed.
     * 
     * @param id
     *            the id
     * @param property
     *            the property
     */
    protected void fireConversationPropertyChanged(ConversationID id, String property) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FrameworkEvent evt = new FrameworkEvent(this, id, property);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FrameworkListener.class) {
                try {
                    ((FrameworkListener) listeners[i + 1]).conversationPropertyChanged(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire url property changed.
     * 
     * @param url
     *            the url
     * @param property
     *            the property
     */
    protected void fireUrlPropertyChanged(HttpUrl url, String property) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FrameworkEvent evt = new FrameworkEvent(this, url, property);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FrameworkListener.class) {
                try {
                    ((FrameworkListener) listeners[i + 1]).urlPropertyChanged(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * The Class FrameworkUrlModel.
     */
    private class FrameworkUrlModel extends AbstractUrlModel {

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.model.AbstractUrlModel#readLock()
         */
        public Sync readLock() {
            return _rwl.readLock();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractUrlModel#getChildCount(edu.lnmiit.wavd
         * .model.HttpUrl)
         */
        public int getChildCount(HttpUrl parent) {
            if (_store == null)
                return 0;
            try {
                readLock().acquire();
                try {
                    return _store.getChildCount(parent);
                } finally {
                    readLock().release();
                }
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
                return 0;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractUrlModel#getIndexOf(edu.lnmiit.wavd
         * .model.HttpUrl)
         */
        public int getIndexOf(HttpUrl url) {
            try {
                readLock().acquire();
                try {
                    return _store.getIndexOf(url);
                } finally {
                    readLock().release();
                }
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
                return -1;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractUrlModel#getChildAt(edu.lnmiit.wavd
         * .model.HttpUrl, int)
         */
        public HttpUrl getChildAt(HttpUrl parent, int index) {
            try {
                readLock().acquire();
                try {
                    return _store.getChildAt(parent, index);
                } finally {
                    readLock().release();
                }
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
                return null;
            }
        }

    }

    /**
     * The Class FrameworkConversationModel.
     */
    private class FrameworkConversationModel extends AbstractConversationModel {

        /**
         * Instantiates a new framework conversation model.
         * 
         * @param model
         *            the model
         */
        public FrameworkConversationModel(FrameworkModel model) {
            super(model);
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.model.AbstractConversationModel#readLock()
         */
        public Sync readLock() {
            return _rwl.readLock();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractConversationModel#getConversationAt
         * (int)
         */
        public ConversationID getConversationAt(int index) {
            try {
                readLock().acquire();
                try {
                    return _store.getConversationAt(null, index);
                } finally {
                    readLock().release();
                }
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
                return null;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractConversationModel#getConversationCount
         * ()
         */
        public int getConversationCount() {
            if (_store == null)
                return 0;
            try {
                readLock().acquire();
                try {
                    return _store.getConversationCount(null);
                } finally {
                    readLock().release();
                }
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
                return 0;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractConversationModel#getIndexOfConversation
         * (edu.lnmiit.wavd.model.ConversationID)
         */
        public int getIndexOfConversation(ConversationID id) {
            try {
                readLock().acquire();
                try {
                    return _store.getIndexOfConversation(null, id);
                } finally {
                    readLock().release();
                }
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
                return 0;
            }
        }

    }

}
