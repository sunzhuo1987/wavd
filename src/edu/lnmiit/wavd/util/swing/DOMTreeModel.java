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


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.lnmiit.wavd.util.swing.AbstractTreeModel;

import javax.swing.tree.TreePath;

// TODO: Auto-generated Javadoc
/**
 * The Class DOMTreeModel.
 */
public class DOMTreeModel extends AbstractTreeModel {

    /** The _root. */
    private Node _root;
    
    /**
     * Instantiates a new dOM tree model.
     * 
     * @param root the root
     */
    public DOMTreeModel(Node root) {
        _root = root;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public Object getRoot() {
        return _root;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
        NodeList nodes = ((Node) parent).getChildNodes();
        return nodes.getLength();
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index) {
        NodeList nodes = ((Node) parent).getChildNodes();
        return nodes.item(index);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
        return ((Node)node).getNodeType() != Node.ELEMENT_NODE;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        // we do not support editing
    }

}

