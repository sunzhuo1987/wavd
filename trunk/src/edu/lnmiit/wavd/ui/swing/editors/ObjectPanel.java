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
 * ObjectEditorPanel.java
 *
 * Created on 15 November 2003, 02:56
 */

package edu.lnmiit.wavd.ui.swing.editors;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.CellEditor;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import edu.lnmiit.wavd.util.swing.JTreeTable;
import edu.lnmiit.wavd.util.swing.treetable.DefaultTreeTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ObjectPanel.
 */
public class ObjectPanel extends javax.swing.JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 5340750131699308220L;

    /** The _tt. */
    private JTreeTable _tt;

    /** The _ottm. */
    private ObjectTreeTableModel _ottm;

    /** The _editable. */
    private boolean _editable = false;

    /**
     * Instantiates a new object panel.
     */
    public ObjectPanel() {
        initComponents();
        setName("Object");
        _ottm = new ObjectTreeTableModel();
        _tt = new JTreeTable(_ottm) {
            /**
	     * 
	     */
            private static final long serialVersionUID = -8134123398869001161L;

            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 2) {
                    Object o = ((ObjectTreeNode) getValueAt(row, 0)).getUserObject();
                    if (o != null) {
                        return getDefaultRenderer(o.getClass());
                    }
                }
                return super.getCellRenderer(row, column);
            }

            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 2) {
                    Object o = ((ObjectTreeNode) getValueAt(row, 0)).getUserObject();
                    if (o != null) {
                        return getDefaultEditor(o.getClass());
                    }
                }
                return super.getCellEditor(row, column);
            }
        };
        _tt.setDefaultRenderer(Date.class, new DateRenderer());
        ArrayRenderer ar = new ArrayRenderer();
        _tt.setDefaultRenderer(byte[].class, ar);

        _tt.setDefaultEditor(Date.class, new DateEditor());
        _tt.setCellSelectionEnabled(true);
        ttScrollPane.setViewportView(_tt);
        setEditable(false);
    }

    /**
     * Sets the object.
     * 
     * @param object
     *            the new object
     */
    public void setObject(Object object) {
        _ottm.setObject(object);
    }

    /**
     * Stop editing.
     */
    private void stopEditing() {
        Component comp = _tt.getEditorComponent();
        if (comp != null && comp instanceof CellEditor) {
            ((CellEditor) comp).stopCellEditing();
        }
    }

    /**
     * Gets the object.
     * 
     * @return the object
     */
    public Object getObject() {
        if (_editable)
            stopEditing();
        return _ottm.getObject();
    }

    /**
     * Sets the editable.
     * 
     * @param editable
     *            the new editable
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        _ottm.setEditable(editable);
        insertButton.setVisible(editable);
        childButton.setVisible(editable);
        deleteButton.setVisible(editable);
        revalidate();
        repaint();
    }

    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    public boolean isModified() {
        if (_editable)
            stopEditing();
        return _editable && _ottm.isModified();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        ttScrollPane = new javax.swing.JScrollPane();
        insertButton = new javax.swing.JButton();
        childButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        ttScrollPane.setMinimumSize(new java.awt.Dimension(300, 200));
        ttScrollPane.setPreferredSize(new java.awt.Dimension(300, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(ttScrollPane, gridBagConstraints);

        insertButton.setText("Insert");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        add(insertButton, gridBagConstraints);

        childButton.setText("New Child");
        childButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                childButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(childButton, gridBagConstraints);

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        add(deleteButton, gridBagConstraints);

    }// GEN-END:initComponents

    /**
     * Child button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void childButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_childButtonActionPerformed
        javax.swing.tree.TreePath path = _tt.getTree().getSelectionPath();
        if (path == null) {
            JOptionPane.showMessageDialog(null, "Please select a row", "No selection", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ObjectTreeNode selected = (ObjectTreeNode) path.getLastPathComponent();
        Object userObject = (selected).getUserObject();
        if (userObject instanceof Map) {
            Object key = JOptionPane.showInputDialog("Please input a key value");
            if (key == null)
                return;
            Map map = (Map) userObject;
            if (map.containsKey(key)) {
                JOptionPane.showMessageDialog(null, "The Map already contains " + key, "Key exists",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            map.put(key, null);
            Iterator it = map.keySet().iterator();
            int position = 0;
            while (it.hasNext() && it.next() != key) {
                position++;
            }
            ObjectTreeNode newNode = new ObjectTreeNode(null);
            newNode.setParentKey(key);
            selected.insert(newNode, position);
            _ottm.nodesWereInserted(selected, new int[] { position });
        } else if (userObject instanceof List) {
            List list = (List) userObject;
            int position = list.size();
            list.add(position, null);
            ObjectTreeNode newNode = new ObjectTreeNode(null);
            newNode.setParentKey(new Integer(position));
            selected.insert(newNode, position);
            _ottm.nodesWereInserted(selected, new int[] { position });
        }
    }// GEN-LAST:event_childButtonActionPerformed

    /**
     * Insert button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_insertButtonActionPerformed
        javax.swing.tree.TreePath path = _tt.getTree().getSelectionPath();
        if (path == null) {
            JOptionPane.showMessageDialog(null, "Please select a row", "No selection", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ObjectTreeNode selected = (ObjectTreeNode) path.getLastPathComponent();
        (selected).getUserObject();
        ObjectTreeNode parent = (ObjectTreeNode) selected.getParent();
        if (parent == null) {
            JOptionPane.showMessageDialog(null, "It is not possible to insert a node at the root", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Object parentObject = parent.getUserObject();
        if (parentObject instanceof List) {
            List list = (List) parentObject;
            int position = ((Integer) selected.getParentKey()).intValue();
            list.add(position, null);
            ObjectTreeNode newNode = new ObjectTreeNode(null);
            newNode.setParentKey(new Integer(position));
            parent.insert(newNode, position);
            _ottm.nodesWereInserted(parent, new int[] { position });
            int[] changes = new int[parent.getChildCount() - (position + 1)];
            for (int i = position + 1; i < parent.getChildCount(); i++) {
                ObjectTreeNode sib = (ObjectTreeNode) parent.getChildAt(i);
                sib.setParentKey(new Integer(i));
                changes[i - (position + 1)] = i;
            }
            _ottm.nodesChanged(parent, changes);
        } else if (parentObject.getClass().isArray()) {
            JOptionPane.showMessageDialog(null, "Don't know how to insert a node into an Array yet", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "It is only possible to insert a node into a List", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_insertButtonActionPerformed

    /**
     * Delete button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_deleteButtonActionPerformed
        javax.swing.tree.TreePath path = _tt.getTree().getSelectionPath();
        if (path == null) {
            JOptionPane.showMessageDialog(null, "Please select a row", "No selection", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ObjectTreeNode selected = (ObjectTreeNode) path.getLastPathComponent();
        (selected).getUserObject();
        ObjectTreeNode parent = (ObjectTreeNode) selected.getParent();
        if (parent == null) {
            _ottm.setObject(null);
            return;
        }
        Object parentObject = parent.getUserObject();
        if (!(parentObject instanceof List || parentObject instanceof Map)) {
            JOptionPane.showMessageDialog(null, "We can only delete children of java.util.List and java.util.Map",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (parentObject instanceof Map) {
            Map map = (Map) parentObject;
            Object key = selected.getParentKey();
            int position = parent.getIndex(selected);
            try {
                map.remove(key);
            } catch (UnsupportedOperationException uoe) {
                JOptionPane.showMessageDialog(null, "Map returned an UnsupportedOperationException trying to remove "
                        + key, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            parent.remove(position);
            _ottm.nodesWereRemoved(parent, new int[] { position }, new Object[] { selected });
        } else if (parentObject instanceof List) {
            List list = (List) parentObject;
            int position = parent.getIndex(selected);
            try {
                list.remove(position);
            } catch (UnsupportedOperationException uoe) {
                JOptionPane.showMessageDialog(null, "List returned an UnsupportedOperationException trying to remove "
                        + position, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            parent.remove(position);
            _ottm.nodesWereRemoved(parent, new int[] { position }, new Object[] { selected });
        }
    }// GEN-LAST:event_deleteButtonActionPerformed

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        java.util.ArrayList a = new java.util.ArrayList();
        a.add(new String("the string"));
        a.add(new Integer(123));
        a.add(new Boolean(true));
        Map m = new java.util.TreeMap();
        m.put("a string", new String("value 1"));
        m.put("a boolean", new Boolean(false));
        m.put("a byte array", new byte[] { 0x00, 0x01 });
        a.add(m);
        a.add(new int[] { 1001, 1002, 1003 });
        a.add(new java.util.ArrayList());
        a.add(null);
        java.util.Set s = new java.util.HashSet();
        s.add(new Integer(7));
        s.add(new Boolean(true));
        s.add(new String("a new String"));
        a.add(s);
        a.add(new Date());
        a.add(new byte[] { 0, 1, 2, 3 });

        javax.swing.JFrame top = new javax.swing.JFrame("Object Panel");
        top.getContentPane().setLayout(new java.awt.BorderLayout());
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        javax.swing.JButton button = new javax.swing.JButton("GET");
        final ObjectPanel op = new ObjectPanel();
        top.getContentPane().add(op);
        top.getContentPane().add(button, java.awt.BorderLayout.SOUTH);
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println(op.getObject());
            }
        });
        top.setBounds(100, 100, 600, 400);
        top.setVisible(true);
        try {
            op.setEditable(false);
            op.setObject(a);
            // Thread.currentThread().sleep(3000);
            op.setEditable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The delete button. */
    private javax.swing.JButton deleteButton;

    /** The tt scroll pane. */
    private javax.swing.JScrollPane ttScrollPane;

    /** The child button. */
    private javax.swing.JButton childButton;

    /** The insert button. */
    private javax.swing.JButton insertButton;

    // End of variables declaration//GEN-END:variables

    /**
     * The Class ObjectTreeTableModel.
     */
    private class ObjectTreeTableModel extends DefaultTreeTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = 7328132359558195083L;

        /** The _modified. */
        private boolean _modified = false;

        /** The _editable. */
        private boolean _editable = false;

        /** The _column names. */
        private String[] _columnNames = new String[] { "Key", "Class", "Value" };

        /**
         * Instantiates a new object tree table model.
         */
        public ObjectTreeTableModel() {
            super(null, true);
        }

        /**
         * Instantiates a new object tree table model.
         * 
         * @param object
         *            the object
         */
        public ObjectTreeTableModel(Object object) {
            super(null, true); // we have to pass a parameter
            setObject(object); // but we override it immediately!
        }

        /**
         * Sets the editable.
         * 
         * @param editable
         *            the new editable
         */
        public void setEditable(boolean editable) {
            _editable = editable;
        }

        /**
         * Sets the object.
         * 
         * @param object
         *            the new object
         */
        public void setObject(Object object) {
            ObjectTreeNode root = createObjectTree(object);
            setRoot(root);
            nodeStructureChanged(root);
            _modified = false; // after we fire, since we have not changed
            // anything yet
        }

        /**
         * Checks if is modified.
         * 
         * @return true, if is modified
         */
        public boolean isModified() {
            return _modified;
        }

        /**
         * Gets the object.
         * 
         * @return the object
         */
        public Object getObject() {
            Object root = getRoot();
            return root == null ? null : ((ObjectTreeNode) root).getUserObject();
        }

        /**
         * Creates the object tree.
         * 
         * @param object
         *            the object
         * 
         * @return the object tree node
         */
        private ObjectTreeNode createObjectTree(Object object) {
            ObjectTreeNode otn = new ObjectTreeNode(object);
            if (object == null) {
                return otn;
            } else if (object instanceof Collection) {
                Collection collection = (Collection) object;
                Iterator it = collection.iterator();
                int count = 0;
                while (it.hasNext()) {
                    ObjectTreeNode child = createObjectTree(it.next());
                    child.setParentKey(new Integer(count++));
                    otn.add(child);
                }
            } else if (object instanceof Map) {
                Map map = (Map) object;
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    Object key = it.next();
                    ObjectTreeNode child = createObjectTree(map.get(key));
                    child.setParentKey(key);
                    otn.add(child);
                }
            } else if (object.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(object);
                for (int i = 0; i < length; i++) {
                    ObjectTreeNode child = new ObjectTreeNode(java.lang.reflect.Array.get(object, i));
                    child.setParentKey(new Integer(i));
                    otn.add(child);
                }
            }
            return otn;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.DefaultTreeTableModel#getColumnCount
         * ()
         */
        public int getColumnCount() {
            return 3;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.DefaultTreeTableModel#getColumnName
         * (int)
         */
        public String getColumnName(int column) {
            return _columnNames[column];
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.DefaultTreeTableModel#getValueAt
         * (java.lang.Object, int)
         */
        public Object getValueAt(Object node, int column) {
            ObjectTreeNode otn = (ObjectTreeNode) node;
            Object object = otn.getUserObject();
            ObjectTreeNode parent = (ObjectTreeNode) otn.getParent();
            Object parentObject = null;
            if (parent != null) {
                parentObject = parent.getUserObject();
            }
            switch (column) {
            case 0:
                return node;
            case 1:
                if (object == null) {
                    return "void";
                } else if (object.getClass().isArray()) {
                    return object.getClass().getComponentType() + "[]";
                } else if (parentObject != null && parentObject.getClass().isArray()) {
                    return parentObject.getClass().getComponentType().getName();
                } else {
                    return object.getClass().getName();
                }
            case 2:
                return describe(object);
            }
            return null;
        }

        /**
         * Describe.
         * 
         * @param object
         *            the object
         * 
         * @return the object
         */
        private Object describe(Object object) {
            if (object == null) {
                return "";
            } else if (object instanceof Map) {
                int size = ((Map) object).size();
                return size + " item" + (size != 1 ? "s" : "");
            } else if (object instanceof Collection) {
                int size = ((Collection) object).size();
                return size + " item" + (size != 1 ? "s" : "");
            } else if (object.getClass().isArray()) {
                int size = java.lang.reflect.Array.getLength(object);
                return size + " item" + (size != 1 ? "s" : "");
            }
            return object;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.DefaultTreeTableModel#isCellEditable
         * (java.lang.Object, int)
         */
        public boolean isCellEditable(Object node, int columnIndex) {
            // the tree must be editable so it gets mouse events
            // to expand and close nodes
            if (columnIndex == 0) {
                return true;
            } else if (columnIndex == 1 && _editable) {
                ObjectTreeNode parent = (ObjectTreeNode) ((ObjectTreeNode) node).getParent();
                if (parent != null) {
                    Object parentObject = parent.getUserObject();
                    if (parentObject.getClass().isArray()) {
                        return false;
                    }
                }
                return true;
            } else if (columnIndex == 2 && _editable) {
                Object object = ((ObjectTreeNode) node).getUserObject();
                if (object == null) {
                    return false;
                } else if (object instanceof Collection || object instanceof Map || object.getClass().isArray()) {
                    return false;
                } else {
                    ObjectTreeNode parent = (ObjectTreeNode) ((ObjectTreeNode) node).getParent();
                    if (parent != null) {
                        Object parentObject = parent.getUserObject();
                        if (parentObject instanceof List || parentObject instanceof Map
                                || parentObject.getClass().isArray()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        /**
         * New object of class.
         * 
         * @param theClass
         *            the the class
         * 
         * @return the object
         */
        private Object newObjectOfClass(String theClass) {
            try {
                Class aClass = Class.forName(theClass);
                java.lang.reflect.Constructor[] constructors = aClass.getConstructors();
                if (constructors.length == 0) {
                    System.err.println(theClass + " has no constructors");
                    return null;
                }
                for (int i = 0; i < constructors.length; i++) { // first look
                    // for void
                    // constructors
                    if (!java.lang.reflect.Modifier.isPublic(constructors[i].getModifiers())) {
                        continue;
                    }
                    Class[] params = constructors[i].getParameterTypes();
                    if (params.length == 0) {
                        return constructors[i].newInstance(new Object[0]);
                    }
                }
                for (int i = 0; i < constructors.length; i++) { // now look for
                    // single
                    // parameter
                    // constructors
                    if (!java.lang.reflect.Modifier.isPublic(constructors[i].getModifiers())) {
                        continue;
                    }
                    Class[] params = constructors[i].getParameterTypes();
                    if (params.length == 1) {
                        if (params[0] == boolean.class) {
                            return constructors[i].newInstance(new Object[] { new Boolean(false) });
                        } else if (params[0] == String.class && Number.class.isAssignableFrom(aClass)) {
                            return constructors[i].newInstance(new Object[] { new String("0") });
                        } else if (params[0] == char.class) {
                            return constructors[i].newInstance(new Object[] { new Character('a') });
                        }
                    }
                }
                StringBuffer buff = new StringBuffer();
                for (int i = 0; i < constructors.length; i++) {
                    Class[] params = constructors[i].getParameterTypes();
                    buff.append(java.lang.reflect.Modifier.toString(constructors[i].getModifiers()) + " " + theClass
                            + "(" + params[0].getName());
                    for (int j = 1; j < params.length; j++) {
                        buff.append(", " + params[j].getName());
                    }
                    buff.append(");\n");
                }
                System.err.println("Cannot instantiate a " + theClass);
                System.err
                        .println("Please special case this in newObjectOfClass() using one of the following constructors");
                System.err.print(buff);
            } catch (Exception e) {
                System.err.println("Error instantiating the new object : " + e);
            }
            return null;
        }

        /**
         * Convert value to class.
         * 
         * @param value
         *            the value
         * @param theClass
         *            the the class
         * 
         * @return the object
         */
        private Object convertValueToClass(Object value, Class theClass) {
            if (theClass == value.getClass()) { // The editor did any conversion
                // for us
                return value;
            } else if (value instanceof String) {
                String string = (String) value;
                if (Number.class.isAssignableFrom(theClass)) {
                    try {
                        if (theClass == Byte.class) {
                            return new Byte(string);
                        } else if (value == Double.class) {
                            return new Double(string);
                        } else if (theClass == Float.class) {
                            return new Float(string);
                        } else if (theClass == Integer.class) {
                            return new Integer(string);
                        } else if (theClass == Long.class) {
                            return new Long(string);
                        } else if (theClass == Short.class) {
                            return new Short(string);
                        } else if (theClass == java.math.BigDecimal.class) {
                            return new java.math.BigDecimal(string);
                        } else if (theClass == java.math.BigInteger.class) {
                            return new java.math.BigInteger(string);
                        }
                        return null;
                    } catch (NumberFormatException nfe) {
                        System.err.println("Can't parse '" + string + "' as a " + theClass);
                        return null;
                    }
                } else if (theClass == Boolean.class) {
                    return Boolean.valueOf(string);
                } else if (theClass == Character.class) {
                    if (string.length() > 0) {
                        return new Character(string.charAt(0));
                    } else {
                        return null;
                    }
                } else if (theClass == Date.class) {
                    try {
                        return DateFormat.getDateTimeInstance().parse(string);
                    } catch (java.text.ParseException pe) {
                        System.err.println("Couldn't parse a date from '" + string + "' : " + pe);
                        return null;
                    }
                }
            }
            System.err.println("Don't know how to convert a " + value.getClass().getName() + " to a "
                    + theClass.getName());
            System.err.println("Please update convertValueToClass() to support this if it is required");
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.DefaultTreeTableModel#setValueAt
         * (java.lang.Object, java.lang.Object, int)
         */
        public void setValueAt(Object aValue, Object node, int column) {
            ObjectTreeNode child = (ObjectTreeNode) node;
            Object childObject = child.getUserObject();
            ObjectTreeNode parent = (ObjectTreeNode) child.getParent();
            Object parentObject = null;
            int childPosition = -1;
            if (parent != null) {
                parentObject = parent.getUserObject();
                childPosition = parent.getIndex(child);
            }
            Object key = child.getParentKey();
            if (parentObject != null
                    && !(parentObject instanceof List || parentObject instanceof Map || parentObject.getClass()
                            .isArray())) {
                System.err.println("I only know how to edit children of List or Map, not "
                        + parentObject.getClass().getName());
                return;
            }
            if (column == 0) {
                System.err.println("Trying to edit the tree?!");
            } else if (column == 1) {
                String type = (String) aValue;
                if ((childObject == null && !type.equals("void"))
                        || (childObject != null && !type.equals(childObject.getClass().getName()))) {
                    if (type.equals("void")) {
                        childObject = null;
                    } else {
                        childObject = newObjectOfClass(type);
                        if (childObject == null) {
                            return;
                        }
                    }
                } else { // we are not changing anything
                    return;
                }
            } else if (column == 2) {
                if (childObject == null) { // this should not happen if
                    // isEditable is working
                    System.err.println("Please change the object type to a non-void class first!");
                    return;
                }
                childObject = convertValueToClass(aValue, childObject.getClass());
                if (childObject == null) {
                    return;
                }
            } else {
                throw new IndexOutOfBoundsException("Tried to edit column " + column);
            }
            child = createObjectTree(childObject);
            child.setParentKey(key);
            if (parent != null) { // we are not at the root
                if (parentObject instanceof List) {
                    List list = (List) parentObject;
                    list.set(childPosition, childObject);
                } else if (parentObject instanceof Map) {
                    Map map = (Map) parentObject;
                    map.put(key, childObject);
                } else if (parentObject.getClass().isArray()) {
                    java.lang.reflect.Array.set(parentObject, ((Integer) key).intValue(), childObject);
                } else {
                    System.err.println("Shouldn't get here! ParentObject is a " + parentObject.getClass());
                    return;
                }
                _modified = true;
                parent.remove(childPosition);
                parent.insert(child, childPosition);
                nodesChanged(parent, new int[] { childPosition });
            } else {
                _modified = true;
                setRoot(child);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.DefaultTreeModel#nodesWereInserted(javax.swing.tree
         * .TreeNode, int[])
         */
        public void nodesWereInserted(TreeNode node, int[] childIndices) {
            super.nodesWereInserted(node, childIndices);
            _modified = true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.DefaultTreeModel#nodesWereRemoved(javax.swing.tree
         * .TreeNode, int[], java.lang.Object[])
         */
        public void nodesWereRemoved(TreeNode node, int[] childIndices, Object[] removedChildren) {
            super.nodesWereRemoved(node, childIndices, removedChildren);
            _modified = true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.DefaultTreeModel#nodesChanged(javax.swing.tree.TreeNode
         * , int[])
         */
        public void nodesChanged(TreeNode node, int[] childIndices) {
            super.nodesChanged(node, childIndices);
            _modified = true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.DefaultTreeModel#nodeChanged(javax.swing.tree.TreeNode
         * )
         */
        public void nodeChanged(TreeNode node) {
            super.nodeChanged(node);
            _modified = true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.DefaultTreeModel#nodeStructureChanged(javax.swing
         * .tree.TreeNode)
         */
        public void nodeStructureChanged(TreeNode node) {
            super.nodeStructureChanged(node);
            _modified = true;
        }

    }

    /**
     * The Class ObjectTreeNode.
     */
    private class ObjectTreeNode extends DefaultMutableTreeNode {

        /**
	 * 
	 */
        private static final long serialVersionUID = 3610370384138109983L;
        /** The _key. */
        private Object _key = null;

        /**
         * Instantiates a new object tree node.
         * 
         * @param object
         *            the object
         */
        public ObjectTreeNode(Object object) {
            super(object);
            if (object instanceof Collection || object instanceof Map) {
                setAllowsChildren(true);
            } else if (object != null && object.getClass().isArray()) {
                setAllowsChildren(true);
            } else {
                setAllowsChildren(false);
            }
        }

        /**
         * Sets the parent key.
         * 
         * @param key
         *            the new parent key
         */
        public void setParentKey(Object key) {
            _key = key;
        }

        /**
         * Gets the parent key.
         * 
         * @return the parent key
         */
        public Object getParentKey() {
            return _key;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.tree.DefaultMutableTreeNode#toString()
         */
        public String toString() {
            // if we are the root, return "root", otherwise we return the key
            return getParent() == null ? "root" : (_key == null ? "null" : _key.toString());
        }
    }

    /**
     * The Class DateRenderer.
     */
    private class DateRenderer extends javax.swing.table.DefaultTableCellRenderer {

        /**
	 * 
	 */
        private static final long serialVersionUID = 2537487788429821771L;
        /** The _df. */
        private DateFormat _df;

        /**
         * Instantiates a new date renderer.
         */
        public DateRenderer() {
            super();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
         */
        public void setValue(Object value) {
            if (_df == null) {
                _df = DateFormat.getDateTimeInstance();
            }
            setText((value == null) ? "" : _df.format(value));
        }

    }

    /**
     * The Class DateEditor.
     */
    private class DateEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {

        /**
	 * 
	 */
        private static final long serialVersionUID = 9170064965165372166L;

        /** The _value. */
        private Object _value = null;

        /** The _text field. */
        private javax.swing.JTextField _textField;

        /** The _df. */
        private DateFormat _df;

        /** The click count to start. */
        private int clickCountToStart = 2;

        /**
         * Instantiates a new date editor.
         */
        public DateEditor() {
            _textField = new javax.swing.JTextField();
            _df = DateFormat.getDateTimeInstance();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
         */
        public boolean isCellEditable(java.util.EventObject anEvent) {
            if (anEvent instanceof java.awt.event.MouseEvent) {
                return ((java.awt.event.MouseEvent) anEvent).getClickCount() >= clickCountToStart;
            }
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.CellEditor#getCellEditorValue()
         */
        public Object getCellEditorValue() {
            try {
                return _df.parse(_textField.getText());
            } catch (java.text.ParseException pe) {
                System.err.println("Parse Error : " + pe);
                return _value;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax
         * .swing.JTable, java.lang.Object, boolean, int, int)
         */
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value,
                boolean isSelected, int row, int column) {
            _value = value;
            _textField.setText(_df.format(value));
            return _textField;
        }

    }

    /**
     * The Class ArrayRenderer.
     */
    private class ArrayRenderer extends javax.swing.table.DefaultTableCellRenderer {

        /**
	 * 
	 */
        private static final long serialVersionUID = -6202208830137850109L;

        /**
         * Instantiates a new array renderer.
         */
        public ArrayRenderer() {
            super();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
         */
        public void setValue(Object value) {
            if (value.getClass().isArray()) {
                StringBuffer buff = new StringBuffer();
                buff.append("{");
                int length = java.lang.reflect.Array.getLength(value);
                if (length > 0) {
                    buff.append(java.lang.reflect.Array.get(value, 0));
                }
                for (int i = 1; i < length; i++) {
                    buff.append(", ").append(java.lang.reflect.Array.get(value, i));
                }
                buff.append("}");
                setText(buff.toString());
            } else {
                setText((value == null) ? "" : value.toString());
            }
        }

    }

}
