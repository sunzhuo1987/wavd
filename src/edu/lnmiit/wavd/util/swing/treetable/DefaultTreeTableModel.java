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
 * DefaultTreeTableModel.java
 *
 * Created on 15 November 2003, 06:22
 */

package edu.lnmiit.wavd.util.swing.treetable;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultTreeTableModel.
 */
public class DefaultTreeTableModel extends DefaultTreeModel implements TreeTableModel {

    /**
     * 
     */
    private static final long serialVersionUID = -341000032122166852L;

    /**
     * Instantiates a new default tree table model.
     * 
     * @param root
     *            the root
     */
    public DefaultTreeTableModel(TreeNode root) {
        this(root, false);
    }

    /**
     * Instantiates a new default tree table model.
     * 
     * @param root
     *            the root
     * @param asksAllowsChildren
     *            the asks allows children
     */
    public DefaultTreeTableModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
    }

    // TreeTable specific methods

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        return "A";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
        return column == 0 ? TreeTableModel.class : Object.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getValueAt(java.lang
     * .Object, int)
     */
    public Object getValueAt(Object node, int column) {
        return column == 0 ? node : "Override getValueAt!!";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#isCellEditable(java
     * .lang.Object, int)
     */
    public boolean isCellEditable(Object node, int column) {
        return getColumnClass(column) == TreeTableModel.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#setValueAt(java.lang
     * .Object, java.lang.Object, int)
     */
    public void setValueAt(Object aValue, Object node, int column) {
    }

}
