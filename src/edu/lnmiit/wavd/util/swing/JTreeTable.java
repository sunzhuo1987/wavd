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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import edu.lnmiit.wavd.util.swing.treetable.AbstractCellEditor;
import edu.lnmiit.wavd.util.swing.treetable.TreeTableModel;
import edu.lnmiit.wavd.util.swing.treetable.TreeTableModelAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class JTreeTable.
 */
public class JTreeTable extends JTable {

    /**
     * 
     */
    private static final long serialVersionUID = -5987217728473366154L;
    /** The tree. */
    protected TreeTableCellRenderer tree;

    /**
     * Instantiates a new j tree table.
     * 
     * @param treeTableModel
     *            the tree table model
     */
    public JTreeTable(TreeTableModel treeTableModel) {
        super();

        setModel(treeTableModel);

        // No grid.
        setShowGrid(false);

        // No intercell spacing
        setIntercellSpacing(new Dimension(0, 0));

        // And update the height of the trees row to match that of
        // the table.
        if (tree.getRowHeight() < 1) {
            // Metal looks better like this.
            setRowHeight(18);
        }
    }

    /**
     * Sets the model.
     * 
     * @param treeTableModel
     *            the new model
     */
    public void setModel(TreeTableModel treeTableModel) {
        // Create the tree. It will be used as a renderer and editor.
        tree = new TreeTableCellRenderer(treeTableModel);

        // Install a tableModel representing the visible rows in the tree.
        super.setModel(new TreeTableModelAdapter(treeTableModel, tree));

        // Force the JTable and JTree to share their row selection models.
        ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
        tree.setSelectionModel(selectionWrapper);
        setSelectionModel(selectionWrapper.getListSelectionModel());

        // Install the tree editor renderer and editor.
        setDefaultRenderer(TreeTableModel.class, tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JTable#updateUI()
     */
    public void updateUI() {
        super.updateUI();
        if (tree != null) {
            tree.updateUI();
        }
        // Use the tree's default foreground and background colors in the
        // table.
        LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
    }

    /*
     * Workaround for BasicTableUI anomaly. Make sure the UI never tries to
     * paint the editor. The UI currently uses different techniques to paint the
     * renderers and editors and overriding setBounds() below is not the right
     * thing to do for an editor. Returning -1 for the editing row in this case,
     * ensures the editor is never painted.
     */
    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JTable#getEditingRow()
     */
    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JTable#setRowHeight(int)
     */
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        if (tree != null && tree.getRowHeight() != rowHeight) {
            tree.setRowHeight(getRowHeight());
        }
    }

    /**
     * Gets the tree.
     * 
     * @return the tree
     */
    public JTree getTree() {
        return tree;
    }

    /**
     * The Class TreeTableCellRenderer.
     */
    public class TreeTableCellRenderer extends JTree implements TableCellRenderer {

        /**
	 * 
	 */
        private static final long serialVersionUID = 2212260051607386351L;
        /** The visible row. */
        protected int visibleRow;

        /**
         * Instantiates a new tree table cell renderer.
         * 
         * @param model
         *            the model
         */
        public TreeTableCellRenderer(TreeModel model) {
            super(model);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.JTree#updateUI()
         */
        public void updateUI() {
            super.updateUI();
            // Make the tree's cell renderer use the table's cell selection
            // colors.
            TreeCellRenderer tcr = getCellRenderer();
            if (tcr instanceof DefaultTreeCellRenderer) {
                DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
                // For 1.1 uncomment this, 1.2 has a bug that will cause an
                // exception to be thrown if the border selection color is
                // null.
                // dtcr.setBorderSelectionColor(null);
                dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
                dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.JTree#setRowHeight(int)
         */
        public void setRowHeight(int rowHeight) {
            if (rowHeight > 0) {
                super.setRowHeight(rowHeight);
                if (JTreeTable.this != null && JTreeTable.this.getRowHeight() != rowHeight) {
                    JTreeTable.this.setRowHeight(getRowHeight());
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.Component#setBounds(int, int, int, int)
         */
        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, JTreeTable.this.getHeight());
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.JComponent#paint(java.awt.Graphics)
         */
        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.table.TableCellRenderer#getTableCellRendererComponent
         * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            if (isSelected)
                setBackground(table.getSelectionBackground());
            else
                setBackground(table.getBackground());

            visibleRow = row;
            return this;
        }
    }

    /**
     * The Class TreeTableCellEditor.
     */
    public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax
         * .swing.JTable, java.lang.Object, boolean, int, int)
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            return tree;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.AbstractCellEditor#isCellEditable
         * (java.util.EventObject)
         */
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                for (int counter = getColumnCount() - 1; counter >= 0; counter--) {
                    if (getColumnClass(counter) == TreeTableModel.class) {
                        MouseEvent me = (MouseEvent) e;
                        MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX()
                                - getCellRect(0, counter, true).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
                        tree.dispatchEvent(newME);
                        break;
                    }
                }
            }
            return false;
        }
    }

    /**
     * The Class ListToTreeSelectionModelWrapper.
     */
    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = -7356963001773182977L;
        /** The updating list selection model. */
        protected boolean updatingListSelectionModel;

        /**
         * Instantiates a new list to tree selection model wrapper.
         */
        public ListToTreeSelectionModelWrapper() {
            super();
            getListSelectionModel().addListSelectionListener(createListSelectionListener());
        }

        /**
         * Gets the list selection model.
         * 
         * @return the list selection model
         */
        ListSelectionModel getListSelectionModel() {
            return listSelectionModel;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.tree.DefaultTreeSelectionModel#resetRowSelection()
         */
        public void resetRowSelection() {
            if (!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    super.resetRowSelection();
                } finally {
                    updatingListSelectionModel = false;
                }
            }
            // Notice how we don't message super if
            // updatingListSelectionModel is true. If
            // updatingListSelectionModel is true, it implies the
            // ListSelectionModel has already been updated and the
            // paths are the only thing that needs to be updated.
        }

        /**
         * Creates the list selection listener.
         * 
         * @return the list selection listener
         */
        protected ListSelectionListener createListSelectionListener() {
            return new ListSelectionHandler();
        }

        /**
         * Update selected paths from selected rows.
         */
        protected void updateSelectedPathsFromSelectedRows() {
            if (!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    // This is way expensive, ListSelectionModel needs an
                    // enumerator for iterating.
                    int min = listSelectionModel.getMinSelectionIndex();
                    int max = listSelectionModel.getMaxSelectionIndex();

                    clearSelection();
                    if (min != -1 && max != -1) {
                        for (int counter = min; counter <= max; counter++) {
                            if (listSelectionModel.isSelectedIndex(counter)) {
                                TreePath selPath = tree.getPathForRow(counter);

                                if (selPath != null) {
                                    addSelectionPath(selPath);
                                }
                            }
                        }
                    }
                } finally {
                    updatingListSelectionModel = false;
                }
            }
        }

        /**
         * The Class ListSelectionHandler.
         */
        class ListSelectionHandler implements ListSelectionListener {

            /*
             * (non-Javadoc)
             * 
             * @see
             * javax.swing.event.ListSelectionListener#valueChanged(javax.swing
             * .event.ListSelectionEvent)
             */
            public void valueChanged(ListSelectionEvent e) {
                updateSelectedPathsFromSelectedRows();
            }
        }
    }
}
