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

import javax.swing.event.TreeModelEvent;

import javax.swing.tree.TreePath;


// TODO: Auto-generated Javadoc
/**
 * The Class TreeTableModelEvent.
 */
public class TreeTableModelEvent extends TreeModelEvent{
    
    /** The column. */
    protected int column;
    
    /** The type. */
    protected int type = 0;
    
    /** The Constant INSERT. */
    public static final int INSERT =  1;
    
    /** The Constant UPDATE. */
    public static final int UPDATE =  0;
    
    /** The Constant DELETE. */
    public static final int DELETE = -1;
    
    /**
     * Instantiates a new tree table model event.
     * 
     * @param source the source
     * @param path the path
     * @param column the column
     */
    public TreeTableModelEvent(Object source, TreePath path, int column)
    {
	super(source, path);
	this.path = path;
	this.childIndices = new int[0];
        this.column = column;
    }
    
    /**
     * Instantiates a new tree table model event.
     * 
     * @param source the source
     * @param path the path
     * @param column the column
     * @param type the type
     */
    public TreeTableModelEvent(Object source, TreePath path, int column, int type)
    {
	this(source, path, column);
        this.type = type;
    }
    
    /**
     * Gets the column.
     * 
     * @return the column
     */
    public int getColumn() {
        return column;
    }
    
    /**
     * Gets the type.
     * 
     * @return the type
     */
    public int getType() {
        return type;
    }
    
}
