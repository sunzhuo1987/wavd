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
 * SiteTreeModelAdapter.java
 *
 * Created on August 27, 2004, 4:19 AM
 */

package edu.lnmiit.wavd.ui.swing;

import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;


import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.UrlEvent;
import edu.lnmiit.wavd.model.UrlListener;
import edu.lnmiit.wavd.model.UrlModel;
import edu.lnmiit.wavd.util.swing.AbstractTreeModel;

import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class UrlTreeModelAdapter.
 */
public class UrlTreeModelAdapter extends AbstractTreeModel {
    
    /** The _model. */
    protected UrlModel _model;
    
    /** The _listener. */
    private Listener _listener = new Listener();
    
    /** The _logger. */
    protected Logger _logger = Logger.getLogger(getClass().getName());
    
    /** The _root. */
    private Object _root = new String("RooT");
    
    /**
     * Instantiates a new url tree model adapter.
     * 
     * @param model the model
     */
    public UrlTreeModelAdapter(UrlModel model) {
        _model = model;
        _model.addUrlListener(_listener);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public Object getRoot() {
        return _root;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index) {
        if (_model == null) throw new NullPointerException("Getting a child when the model is null!");
        if (parent == getRoot()) parent = null;
        return _model.getChildAt((HttpUrl) parent, index);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
        if (_model == null) return 0;
        if (parent == getRoot()) parent = null;
        return _model.getChildCount((HttpUrl) parent);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
        if (node == getRoot()) return false;
        HttpUrl url = (HttpUrl) node;
        if (url.getParameters() != null) return true;
        if (url.getPath().endsWith("/")) return false;
        return getChildCount(url) == 0;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        // we do not support editing
    }
    
    /**
     * Url tree path.
     * 
     * @param url the url
     * 
     * @return the tree path
     */
    protected TreePath urlTreePath(HttpUrl url) {
        Object root = getRoot();
        if (url == null || url == root) {
            return new TreePath(root);
        } else {
            Object[] urlPath = url.getUrlHierarchy();
            Object[] path = new Object[urlPath.length+1];
            path[0] = root;
            System.arraycopy(urlPath, 0, path, 1, urlPath.length);
            return new TreePath(path);
        }
    }
    
    /**
     * The Class Listener.
     */
    private class Listener implements UrlListener {
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.model.UrlListener#urlAdded(edu.lnmiit.wavd.model.UrlEvent)
         */
        public void urlAdded(final UrlEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                HttpUrl url = evt.getUrl();
                HttpUrl parent = url.getParentUrl();
                int index = getIndexOfChild(parent, url);
                fireChildAdded(urlTreePath(parent), index, url);
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            urlAdded(evt);
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception processing " + evt + " " + e);
                    e.getCause().printStackTrace();
                }
            }
        }
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.model.UrlListener#urlChanged(edu.lnmiit.wavd.model.UrlEvent)
         */
        public void urlChanged(final UrlEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                HttpUrl url = evt.getUrl();
                HttpUrl parent = url.getParentUrl();
                int index = getIndexOfChild(parent, url);
                fireChildChanged(urlTreePath(parent), index, url);
            } else {
                if (true) return;
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            urlChanged(evt);
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception processing " + evt + " " + e);
                    e.getCause().printStackTrace();
                }
            }
        }
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.model.UrlListener#urlRemoved(edu.lnmiit.wavd.model.UrlEvent)
         */
        public void urlRemoved(final UrlEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                HttpUrl url = evt.getUrl();
                HttpUrl parent = url.getParentUrl();
                int pos = 0;
                int count = getChildCount(parent);
                for (int i=0; i<count; i++) {
                    HttpUrl sibling = (HttpUrl) getChild(parent, i);
                    if (url.compareTo(sibling)<0) {
                        break;
                    } else {
                        pos++;
                    }
                }
                fireChildRemoved(urlTreePath(parent), pos, url);
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            urlRemoved(evt);
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception processing " + evt + " " + e);
                    e.getCause().printStackTrace();
                }
            }
        }
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.model.UrlListener#urlsChanged()
         */
        public void urlsChanged() {
            if (SwingUtilities.isEventDispatchThread()) {
                fireStructureChanged();
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            urlsChanged();
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception processing event: " + e);
                    e.getCause().printStackTrace();
                }
            }
        }
        
    }
}
