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

import EDU.oswego.cs.dl.util.concurrent.Sync;

import javax.swing.event.EventListenerList;

import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractUrlModel.
 */
public abstract class AbstractUrlModel implements UrlModel {
    
    /** The _listener list. */
    private EventListenerList _listenerList = new EventListenerList();
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /**
     * Instantiates a new abstract url model.
     */
    public AbstractUrlModel() {
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.UrlModel#getChildCount(edu.lnmiit.wavd.model.HttpUrl)
     */
    public abstract int getChildCount(HttpUrl parent);
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.UrlModel#getIndexOf(edu.lnmiit.wavd.model.HttpUrl)
     */
    public abstract int getIndexOf(HttpUrl url);
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.UrlModel#getChildAt(edu.lnmiit.wavd.model.HttpUrl, int)
     */
    public abstract HttpUrl getChildAt(HttpUrl parent, int index);
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.UrlModel#readLock()
     */
    public abstract Sync readLock();
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.UrlModel#addUrlListener(edu.lnmiit.wavd.model.UrlListener)
     */
    public void addUrlListener(UrlListener listener) {
        synchronized(_listenerList) {
            _listenerList.add(UrlListener.class, listener);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.UrlModel#removeUrlListener(edu.lnmiit.wavd.model.UrlListener)
     */
    public void removeUrlListener(UrlListener listener) {
        synchronized(_listenerList) {
            _listenerList.remove(UrlListener.class, listener);
        }
    }
    
    /**
     * Fire url added.
     * 
     * @param url the url
     * @param position the position
     */
    protected void fireUrlAdded(HttpUrl url, int position) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        UrlEvent evt = new UrlEvent(this, url, position);
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==UrlListener.class) {
                try {
                    ((UrlListener)listeners[i+1]).urlAdded(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Fire url removed.
     * 
     * @param url the url
     * @param position the position
     */
    protected void fireUrlRemoved(HttpUrl url, int position) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        UrlEvent evt = new UrlEvent(this, url, position);
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==UrlListener.class) {
                try {
                    ((UrlListener)listeners[i+1]).urlRemoved(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }
    
    /**
     * Fire url changed.
     * 
     * @param url the url
     * @param position the position
     */
    protected void fireUrlChanged(HttpUrl url, int position) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        UrlEvent evt = new UrlEvent(this, url, position);
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==UrlListener.class) {
                try {
                    ((UrlListener)listeners[i+1]).urlChanged(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Fire urls changed.
     */
    protected void fireUrlsChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==UrlListener.class) {
                try {
                    ((UrlListener)listeners[i+1]).urlsChanged();
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                    e.printStackTrace();
                }
            }
        }
    }
    
}
