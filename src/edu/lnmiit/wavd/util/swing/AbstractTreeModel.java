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

package edu.lnmiit.wavd.util.swing;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractTreeModel.
 */

public abstract class AbstractTreeModel implements TreeModel {

    /** The listeners. */
    protected EventListenerList listeners;

    /**
     * Instantiates a new abstract tree model.
     */
    protected AbstractTreeModel() {
        listeners = new EventListenerList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
     * java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
        for (int count = getChildCount(parent), i = 0; i < count; i++)
            if (getChild(parent, i).equals(child))
                return i;

        return -1;
    }

    /**
     * Fire new root.
     */
    protected void fireNewRoot() {
        Object[] pairs = listeners.getListenerList();

        Object root = getRoot();

        /*
         * Undocumented. I think it is the only reasonable/possible solution to
         * use use null as path if there is no root. TreeModels without root
         * aren't important anyway, since JTree doesn't support them (yet).
         */
        TreePath path = (root != null) ? new TreePath(root) : null;

        TreeModelEvent e = null;

        for (int i = pairs.length - 2; i >= 0; i -= 2) {
            if (pairs[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(this, path, null, null);

                ((TreeModelListener) pairs[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * Fire structure changed.
     */
    protected void fireStructureChanged() {
        fireTreeStructureChanged(new TreePath(getRoot()));
    }

    /**
     * Fire path leaf state changed.
     * 
     * @param path
     *            the path
     */
    protected void firePathLeafStateChanged(TreePath path) {
        fireTreeStructureChanged(path);
    }

    /**
     * Fire tree structure changed.
     * 
     * @param parentPath
     *            the parent path
     */
    protected void fireTreeStructureChanged(TreePath parentPath) {
        Object[] pairs = listeners.getListenerList();

        TreeModelEvent e = null;

        for (int i = pairs.length - 2; i >= 0; i -= 2) {
            if (pairs[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(this, parentPath, null, null);

                ((TreeModelListener) pairs[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * Fire path changed.
     * 
     * @param path
     *            the path
     */
    protected void firePathChanged(TreePath path) {
        Object node = path.getLastPathComponent();
        TreePath parentPath = path.getParentPath();

        if (parentPath == null)
            fireChildrenChanged(path, null, null);
        else {
            Object parent = parentPath.getLastPathComponent();

            fireChildChanged(parentPath, getIndexOfChild(parent, node), node);
        }
    }

    /**
     * Fire child added.
     * 
     * @param parentPath
     *            the parent path
     * @param index
     *            the index
     * @param child
     *            the child
     */
    protected void fireChildAdded(TreePath parentPath, int index, Object child) {
        fireChildrenAdded(parentPath, new int[] { index }, new Object[] { child });
    }

    /**
     * Fire child changed.
     * 
     * @param parentPath
     *            the parent path
     * @param index
     *            the index
     * @param child
     *            the child
     */
    protected void fireChildChanged(TreePath parentPath, int index, Object child) {
        fireChildrenChanged(parentPath, new int[] { index }, new Object[] { child });
    }

    /**
     * Fire child removed.
     * 
     * @param parentPath
     *            the parent path
     * @param index
     *            the index
     * @param child
     *            the child
     */
    protected void fireChildRemoved(TreePath parentPath, int index, Object child) {
        fireChildrenRemoved(parentPath, new int[] { index }, new Object[] { child });
    }

    /**
     * Fire children added.
     * 
     * @param parentPath
     *            the parent path
     * @param indices
     *            the indices
     * @param children
     *            the children
     */
    protected void fireChildrenAdded(TreePath parentPath, int[] indices, Object[] children) {
        Object[] pairs = listeners.getListenerList();

        TreeModelEvent e = null;

        for (int i = pairs.length - 2; i >= 0; i -= 2) {
            if (pairs[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(this, parentPath, indices, children);

                ((TreeModelListener) pairs[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /**
     * Fire children changed.
     * 
     * @param parentPath
     *            the parent path
     * @param indices
     *            the indices
     * @param children
     *            the children
     */
    protected void fireChildrenChanged(TreePath parentPath, int[] indices, Object[] children) {
        Object[] pairs = listeners.getListenerList();

        TreeModelEvent e = null;

        for (int i = pairs.length - 2; i >= 0; i -= 2) {
            if (pairs[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(this, parentPath, indices, children);

                ((TreeModelListener) pairs[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Fire children removed.
     * 
     * @param parentPath
     *            the parent path
     * @param indices
     *            the indices
     * @param children
     *            the children
     */
    protected void fireChildrenRemoved(TreePath parentPath, int[] indices, Object[] children) {
        Object[] pairs = listeners.getListenerList();

        TreeModelEvent e = null;

        for (int i = pairs.length - 2; i >= 0; i -= 2) {
            if (pairs[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(this, parentPath, indices, children);
                ((TreeModelListener) pairs[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    protected Object clone() throws CloneNotSupportedException {
        AbstractTreeModel clone = (AbstractTreeModel) super.clone();

        clone.listeners = new EventListenerList();

        return clone;
    }

    /*
     * (non-Javadoc)
     * 
     * @seejavax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.
     * TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(TreeModelListener.class, l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.
     * TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(TreeModelListener.class, l);
    }
}
