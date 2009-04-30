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

package edu.lnmiit.wavd.util.swing.treetable;

import edu.lnmiit.wavd.util.swing.AbstractTreeModel;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractTreeTableModel.
 */
public abstract class AbstractTreeTableModel extends AbstractTreeModel implements TreeTableModel {

    //
    // Default implementations for methods in the TreeTableModel interface.
    //

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

    // Left to be implemented in the subclass:

    /*
     * public int getColumnCount() public String getColumnName(Object node, int
     * column) public Object getValueAt(Object node, int column)
     */
}
