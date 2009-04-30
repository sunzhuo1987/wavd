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

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

// TODO: Auto-generated Javadoc
/**
 * The Class TreeUtil.
 */
public class TreeUtil {
    
    /**
     * Instantiates a new tree util.
     */
    private TreeUtil() {
    }
    
    /**
     * Expand all.
     * 
     * @param tree the tree
     * @param expand the expand
     */
    public static void expandAll(JTree tree, boolean expand) {
        TreeModel model = tree.getModel();
        
        // Traverse tree from root
        expandAll(tree, new TreePath(tree.getModel().getRoot()), expand);
    }
    
    /**
     * Expand all.
     * 
     * @param tree the tree
     * @param path the path
     * @param expand the expand
     */
    private static void expandAll(JTree tree, TreePath path, boolean expand) {
        Object parent = path.getLastPathComponent();
        int childCount = tree.getModel().getChildCount(parent);
        for (int i=0; i<childCount; i++) {
            Object child = tree.getModel().getChild(parent, i);
            TreePath childPath = path.pathByAddingChild(child);
            expandAll(tree, childPath, expand);
        }
        
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(path);
        } else {
            tree.collapsePath(path);
        }
    }
    
}
