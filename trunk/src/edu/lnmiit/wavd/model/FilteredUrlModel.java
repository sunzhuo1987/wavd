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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import EDU.oswego.cs.dl.util.concurrent.Sync;
import edu.lnmiit.wavd.util.MRUCache;

// TODO: Auto-generated Javadoc
/**
 * The Class FilteredUrlModel.
 */
public abstract class FilteredUrlModel extends AbstractUrlModel {

    /** The _url model. */
    protected UrlModel _urlModel;

    /** The _filtered urls. */
    private Set _filteredUrls = null;

    /** The _implicit urls. */
    private Set _implicitUrls = null;

    /** The _cache. */
    private MRUCache _cache = new MRUCache(16);

    /** The _listener list. */
    protected EventListenerList _listenerList = new EventListenerList();

    /** The _logger. */
    protected Logger _logger = Logger.getLogger(getClass().getName());

    /** The _updating. */
    private boolean _updating = false;

    /** The miss. */
    private int hit, miss = 0;

    /**
     * Instantiates a new filtered url model.
     * 
     * @param urlModel
     *            the url model
     */
    public FilteredUrlModel(UrlModel urlModel) {
        _logger.setLevel(Level.INFO);
        _urlModel = urlModel;
        try {
            _urlModel.readLock().acquire();
            updateFilteredUrls();
            _urlModel.addUrlListener(new Listener());
        } catch (InterruptedException ie) {
            _logger.warning("Interrupted waiting for the read lock! " + ie.getMessage());
        } finally {
            _urlModel.readLock().release();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.AbstractUrlModel#readLock()
     */
    public Sync readLock() {
        return _urlModel.readLock();
    }

    /**
     * Inits the filters.
     */
    protected void initFilters() {
        _filteredUrls = new HashSet();
        _implicitUrls = new HashSet();
    }

    /**
     * Should filter.
     * 
     * @param url
     *            the url
     * 
     * @return true, if successful
     */
    protected abstract boolean shouldFilter(HttpUrl url);

    /**
     * Checks if is filtered.
     * 
     * @param url
     *            the url
     * 
     * @return true, if is filtered
     */
    protected boolean isFiltered(HttpUrl url) {
        return _filteredUrls != null && _filteredUrls.contains(url);
    }

    /**
     * Sets the filtered.
     * 
     * @param url
     *            the url
     * @param filtered
     *            the filtered
     */
    protected void setFiltered(HttpUrl url, boolean filtered) {
        if (filtered) {
            _filteredUrls.add(url);
        } else {
            _filteredUrls.remove(url);
        }
    }

    /**
     * Checks if is implicit.
     * 
     * @param url
     *            the url
     * 
     * @return true, if is implicit
     */
    public boolean isImplicit(HttpUrl url) {
        return _implicitUrls.contains(url);
    }

    /**
     * Sets the implicit.
     * 
     * @param url
     *            the url
     * @param filtered
     *            the filtered
     */
    protected void setImplicit(HttpUrl url, boolean filtered) {
        if (_implicitUrls == null)
            _implicitUrls = new HashSet();
        if (filtered) {
            _implicitUrls.add(url);
        } else {
            _implicitUrls.remove(url);
        }
    }

    /**
     * Checks if is visible.
     * 
     * @param url
     *            the url
     * 
     * @return true, if is visible
     */
    private boolean isVisible(HttpUrl url) {
        return isImplicit(url) || !isFiltered(url);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.AbstractUrlModel#getIndexOf(edu.lnmiit.wavd.model
     * .HttpUrl)
     */
    public int getIndexOf(HttpUrl url) {
        int index = Collections.binarySearch(getFilteredChildren(url), url);
        return index < 0 ? -1 : index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.AbstractUrlModel#getChildAt(edu.lnmiit.wavd.model
     * .HttpUrl, int)
     */
    public HttpUrl getChildAt(HttpUrl url, int index) {
        return (HttpUrl) getFilteredChildren(url).get(index);
    }

    /**
     * Update filtered urls.
     */
    private void updateFilteredUrls() {
        initFilters();
        recurseTree(null);
    }

    /**
     * Gets the filtered children.
     * 
     * @param parent
     *            the parent
     * 
     * @return the filtered children
     */
    private ArrayList getFilteredChildren(HttpUrl parent) {
        ArrayList childList = (ArrayList) _cache.get(parent);
        if (childList != null) {
            hit++;
            return childList;
        }
        try {
            childList = new ArrayList();
            _urlModel.readLock().acquire();
            int count = _urlModel.getChildCount(parent);
            for (int i = 0; i < count; i++) {
                HttpUrl child = _urlModel.getChildAt(parent, i);
                if (isVisible(child))
                    childList.add(child);
            }
            if (count > 0) { // we are saving some real work here
                miss++;
                _logger.fine("Hit=" + hit + ", miss=" + miss + " parent = " + parent + " count=" + count);
                _cache.put(parent, childList);
            }
            return childList;
        } catch (InterruptedException ie) {
            _logger.warning("Interrupted waiting for the read lock! " + ie.getMessage());
        } finally {
            _urlModel.readLock().release();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.AbstractUrlModel#getChildCount(edu.lnmiit.wavd.
     * model.HttpUrl)
     */
    public int getChildCount(HttpUrl url) {
        return getFilteredChildren(url).size();
    }

    /**
     * Recurse tree.
     * 
     * @param parent
     *            the parent
     */
    private void recurseTree(HttpUrl parent) {
        int count = _urlModel.getChildCount(parent);
        for (int i = 0; i < count; i++) {
            HttpUrl url = _urlModel.getChildAt(parent, i);
            if (shouldFilter(url)) {
                setFiltered(url, true);
            } else {
                grow(url);
            }
            recurseTree(url);
        }
    }

    /*
     * adds url, and marks any previously filtered intermediate nodes as
     * implicit fires events for all node that becomes visible
     */
    /**
     * Grow.
     * 
     * @param url
     *            the url
     */
    private void grow(HttpUrl url) {
        HttpUrl[] path = url.getUrlHierarchy();
        for (int i = 0; i < path.length - 1; i++) {
            if (!isVisible(path[i])) {
                setImplicit(path[i], true);
                if (i == 0) { // update the root node
                    _cache.remove(null);
                } else {
                    _cache.remove(path[i - 1]);
                }
                if (!_updating)
                    fireUrlAdded(path[i], -1); // FIXME
            }
        }
        _cache.remove(url.getParentUrl());
        if (!_updating)
            fireUrlAdded(url, 0); // FIXME
    }

    /*
     * removes url and any implicit parents. Fires events for the all urls
     * removed
     */
    /**
     * Prune.
     * 
     * @param url
     *            the url
     */
    private void prune(HttpUrl url) {
        _cache.remove(url.getParentUrl());
        if (!_updating)
            fireUrlRemoved(url, -1); // FIXME
        HttpUrl[] path = url.getUrlHierarchy();
        for (int i = path.length - 2; i >= 0; i--) {
            if (isImplicit(path[i]) && getChildCount(path[i]) == 0) {
                setImplicit(path[i], false);
                if (i == 0) { // update the root node
                    _cache.remove(null);
                } else {
                    _cache.remove(path[i - 1]);
                }
                if (!_updating)
                    fireUrlRemoved(path[i], -1); // FIXME
            }
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        _cache.clear();
        _updating = true;
        updateFilteredUrls();
        _updating = false;
        fireUrlsChanged();
    }

    /**
     * The Class Listener.
     */
    private class Listener implements UrlListener {

        /**
         * Instantiates a new listener.
         */
        public Listener() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.model.UrlListener#urlsChanged()
         */
        public void urlsChanged() {
            reset();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.UrlListener#urlAdded(edu.lnmiit.wavd.model.
         * UrlEvent)
         */
        public void urlAdded(UrlEvent evt) {
            HttpUrl url = evt.getUrl();
            if (!shouldFilter(url)) {
                grow(url);
            } else {
                setFiltered(url, true);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.UrlListener#urlChanged(edu.lnmiit.wavd.model
         * .UrlEvent)
         */
        public void urlChanged(UrlEvent evt) {
            HttpUrl url = evt.getUrl();
            if (shouldFilter(url)) { // it is now filtered
                if (isVisible(url)) { // we could previously see it
                    if (getChildCount(url) > 0) { // it has children
                        setFiltered(url, true);
                        setImplicit(url, true);
                        if (!_updating)
                            fireUrlChanged(url, -1); // FIXME
                    } else { // it has no children, hide it and any implicit
                        // parents
                        setFiltered(url, true);
                        prune(url);
                    }
                } // else there is nothing to do to an already invisible node
            } else { // it is now not filtered
                if (!isVisible(url)) { // it was previously hidden
                    setFiltered(url, false);
                    grow(url);
                } else if (!_updating) {
                    fireUrlChanged(url, -1); // FIXME
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.UrlListener#urlRemoved(edu.lnmiit.wavd.model
         * .UrlEvent)
         */
        public void urlRemoved(UrlEvent evt) {
            HttpUrl url = evt.getUrl();
            if (isVisible(url)) {
                prune(url);
            } else {
                setFiltered(url, false);
            }
        }

    }

}
