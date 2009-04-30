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

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

// TODO: Auto-generated Javadoc
/**
 * The Class TreeTableModelAdapter.
 */
public class TreeTableModelAdapter extends AbstractTableModel
{
    
    /** The tree. */
    JTree tree;
    
    /** The tree table model. */
    TreeTableModel treeTableModel;

    /**
     * Instantiates a new tree table model adapter.
     * 
     * @param treeTableModel the tree table model
     * @param tree the tree
     */
    public TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree) {
        this.tree = tree;
        this.treeTableModel = treeTableModel;

	tree.addTreeExpansionListener(new TreeExpansionListener() {
	    // Don't use fireTableRowsInserted() here; the selection model
	    // would get updated twice. 
	    public void treeExpanded(TreeExpansionEvent event) {  
	      fireTableDataChanged(); 
	    }
            public void treeCollapsed(TreeExpansionEvent event) {  
	      fireTableDataChanged(); 
	    }
	});

	// Install a TreeModelListener that can update the table when
	// tree changes. We use delayedFireTableDataChanged as we can
	// not be guaranteed the tree will have finished processing
	// the event before us.
        //
        // FIXME we are ignoring the above warning, and trying to do the
        // relevant calculations directly. This may break something
        // but I guess we won't know if we don't try!
	treeTableModel.addTreeModelListener(new TreeModelListener() {
	    public void treeNodesChanged(TreeModelEvent e) {
                int row = TreeTableModelAdapter.this.tree.getRowForPath(e.getTreePath());
                if (row < 0) return; // parent is not visible
                
                // This is painful! Why does the relevant TreePath constructor have to be protected?!
                Object[] children = e.getChildren();
                Object[] path = e.getTreePath().getPath();
                Object[] childPath = new Object[path.length+1];
                System.arraycopy(path, 0, childPath, 0, path.length);
                
                childPath[childPath.length - 1] = children[0];
                TreePath firstChildChanged = new TreePath(childPath);
                int firstRow = TreeTableModelAdapter.this.tree.getRowForPath(firstChildChanged);
                
                childPath[childPath.length - 1] = children[children.length-1];
                TreePath lastChildChanged = new TreePath(childPath);
                int lastRow = TreeTableModelAdapter.this.tree.getRowForPath(lastChildChanged);
                
                if (firstRow * lastRow < 0) System.err.println("First row is " + firstRow + " and last row is " + lastRow);
                if (firstRow < 0 || lastRow < 0) return;
                
                if (e instanceof TreeTableModelEvent && firstRow == lastRow) {
                    int column = ((TreeTableModelEvent) e).getColumn();
                    delayedFireTableCellUpdated(firstRow, column);
                } else {
                    delayedFireTableRowsUpdated(firstRow, lastRow);
                }
	    }

	    public void treeNodesInserted(TreeModelEvent e) {
		delayedFireTableDataChanged();
	    }

	    public void treeNodesRemoved(TreeModelEvent e) {
		delayedFireTableDataChanged();
	    }

	    public void treeStructureChanged(TreeModelEvent e) {
                delayedFireTableStructureChanged();
	    }
	});
    }

    // Wrappers, implementing TableModel interface. 

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
	return treeTableModel.getColumnCount();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
	return treeTableModel.getColumnName(column);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
	return treeTableModel.getColumnClass(column);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
	return tree.getRowCount();
    }

    /**
     * Node for row.
     * 
     * @param row the row
     * 
     * @return the object
     */
    protected Object nodeForRow(int row) {
	TreePath treePath = tree.getPathForRow(row);
	return treePath.getLastPathComponent();         
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column) {
	return treeTableModel.getValueAt(nodeForRow(row), column);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int column) {
         return treeTableModel.isCellEditable(nodeForRow(row), column); 
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object value, int row, int column) {
	treeTableModel.setValueAt(value, nodeForRow(row), column);
    }

    /**
     * Delayed fire table data changed.
     */
    protected void delayedFireTableDataChanged() {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		fireTableDataChanged();
	    }
	});
    }

    /**
     * Delayed fire table cell updated.
     * 
     * @param row the row
     * @param column the column
     */
    protected void delayedFireTableCellUpdated(final int row, final int column) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		fireTableCellUpdated(row, column);
	    }
	});
    }

    /**
     * Delayed fire table rows updated.
     * 
     * @param first the first
     * @param last the last
     */
    protected void delayedFireTableRowsUpdated(final int first, final int last) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		fireTableRowsUpdated(first, last);
	    }
	});
    }

    /**
     * Delayed fire table structure changed.
     */
    protected void delayedFireTableStructureChanged() {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		fireTableStructureChanged();
	    }
	});
    }
}

