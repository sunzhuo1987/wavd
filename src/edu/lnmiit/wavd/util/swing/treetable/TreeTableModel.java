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

import javax.swing.tree.TreeModel;

// TODO: Auto-generated Javadoc
/**
 * The Interface TreeTableModel.
 */
public interface TreeTableModel extends TreeModel
{
    
    /**
     * Gets the column count.
     * 
     * @return the column count
     */
    int getColumnCount();

    /**
     * Gets the column name.
     * 
     * @param column the column
     * 
     * @return the column name
     */
    String getColumnName(int column);

    /**
     * Gets the column class.
     * 
     * @param column the column
     * 
     * @return the column class
     */
    Class getColumnClass(int column);

    /**
     * Gets the value at.
     * 
     * @param node the node
     * @param column the column
     * 
     * @return the value at
     */
    Object getValueAt(Object node, int column);

    /**
     * Checks if is cell editable.
     * 
     * @param node the node
     * @param column the column
     * 
     * @return true, if is cell editable
     */
    boolean isCellEditable(Object node, int column);

    /**
     * Sets the value at.
     * 
     * @param aValue the a value
     * @param node the node
     * @param column the column
     */
    void setValueAt(Object aValue, Object node, int column);
}

