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

package edu.lnmiit.wavd.ui.swing;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.plugin.Hook;
import edu.lnmiit.wavd.plugin.Script;
import edu.lnmiit.wavd.plugin.ScriptListener;
import edu.lnmiit.wavd.plugin.ScriptManager;
import edu.lnmiit.wavd.util.swing.JTreeTable;
import edu.lnmiit.wavd.util.swing.treetable.AbstractTreeTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ScriptManagerFrame.
 */
public class ScriptManagerFrame extends javax.swing.JFrame implements ScriptListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1884106744504826998L;

    /** The _manager. */
    private ScriptManager _manager;

    /** The _hook tree. */
    private JTreeTable _hookTree;

    /** The _tree model. */
    private HookScriptTreeModel _treeModel;

    /**
     * Instantiates a new script manager frame.
     * 
     * @param manager
     *            the manager
     */
    public ScriptManagerFrame(ScriptManager manager) {
        _manager = manager;
        _treeModel = new HookScriptTreeModel();
        initComponents();
        _hookTree = new JTreeTable(_treeModel);
        hookScrollPane.getViewport().add(_hookTree);
        _hookTree.setModel(_treeModel);
        final JTree hookTree = _hookTree.getTree();
        hookTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        hookTree.setRootVisible(false);
        hookTree.setShowsRootHandles(true);
        hookTree.setCellRenderer(new HookTreeRenderer());
        hookTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath selection = hookTree.getSelectionPath();
                if (selection != null) {
                    Object o = selection.getLastPathComponent();
                    if (o instanceof Hook) {
                        showHook((Hook) o);
                        showScript(null);
                    } else if (o instanceof Script) {
                        showHook((Hook) selection.getParentPath().getLastPathComponent());
                        showScript((Script) o);
                    }
                } else {
                    showHook(null);
                    showScript(null);
                }
            }
        });
        _hookTree.getColumnModel().getColumn(1).setMaxWidth(50);
        _manager.addScriptListener(this);
    }

    /**
     * Show hook.
     * 
     * @param hook
     *            the hook
     */
    private void showHook(Hook hook) {
        if (hook != null) {
            descriptionTextArea.setText(hook.getDescription());
            descriptionTextArea.setCaretPosition(0);
            addButton.setEnabled(true);
        } else {
            descriptionTextArea.setText("");
            addButton.setEnabled(false);
        }
    }

    /**
     * Show script.
     * 
     * @param script
     *            the script
     */
    private void showScript(Script script) {
        if (script == null) {
            scriptTextField.setText("");
            scriptTextArea.setText("");
            removeButton.setEnabled(false);
        } else {
            scriptTextField.setText(script.getFile().getAbsolutePath());
            scriptTextArea.setText(script.getScript());
            scriptTextArea.setCaretPosition(0);
            removeButton.setEnabled(true);
        }
        saveButton.setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.ScriptListener#hooksChanged()
     */
    public void hooksChanged() {
        _treeModel.fireStructureChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.ScriptListener#hookStarted(java.lang.String,
     * edu.lnmiit.wavd.plugin.Hook)
     */
    public void hookStarted(String plugin, Hook hook) {
        // firePathChanged(new TreePath(new Object[] {plugin, hook}));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.plugin.ScriptListener#scriptStarted(java.lang.String,
     * edu.lnmiit.wavd.plugin.Hook, edu.lnmiit.wavd.plugin.Script)
     */
    public void scriptStarted(String plugin, Hook hook, Script script) {
        // firePathChanged(new TreePath(new Object[] {plugin, hook, script}));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.plugin.ScriptListener#scriptChanged(java.lang.String,
     * edu.lnmiit.wavd.plugin.Hook, edu.lnmiit.wavd.plugin.Script)
     */
    public void scriptChanged(final String plugin, final Hook hook, final Script script) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                _treeModel.firePathChanged(new TreePath(new Object[] { _treeModel.getRoot(), plugin, hook, script }));
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.ScriptListener#scriptEnded(java.lang.String,
     * edu.lnmiit.wavd.plugin.Hook, edu.lnmiit.wavd.plugin.Script)
     */
    public void scriptEnded(String plugin, Hook hook, Script script) {
        // firePathChanged(new TreePath(new Object[] {plugin, hook, script}));
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.ScriptListener#hookEnded(java.lang.String,
     * edu.lnmiit.wavd.plugin.Hook)
     */
    public void hookEnded(String plugin, Hook hook) {
        // firePathChanged(new TreePath(new Object[] {plugin, hook}));
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.ScriptListener#scriptError(java.lang.String,
     * edu.lnmiit.wavd.plugin.Hook, edu.lnmiit.wavd.plugin.Script,
     * java.lang.Throwable)
     */
    public void scriptError(final String plugin, final Hook hook, final Script script, final Throwable error) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, new String[] { "Error running script : ", plugin + hook.getName(),
                        script.getFile().getName(), error.getMessage() }, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.ScriptListener#scriptAdded(java.lang.String,
     * edu.lnmiit.wavd.plugin.Hook, edu.lnmiit.wavd.plugin.Script)
     */
    public void scriptAdded(String plugin, Hook hook, Script script) {
        _treeModel.fireTreeStructureChanged(new TreePath(new Object[] { _treeModel.getRoot(), plugin, hook }));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.plugin.ScriptListener#scriptRemoved(java.lang.String,
     * edu.lnmiit.wavd.plugin.Hook, edu.lnmiit.wavd.plugin.Script)
     */
    public void scriptRemoved(String plugin, Hook hook, Script script) {
        _treeModel.fireTreeStructureChanged(new TreePath(new Object[] { _treeModel.getRoot(), plugin, hook }));
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scriptToolBar = new javax.swing.JToolBar();
        newButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        hookScrollPane = new javax.swing.JScrollPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        scriptTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        scriptTextArea = new javax.swing.JTextArea();

        setTitle("Scripted Events");
        scriptToolBar.setFloatable(false);
        newButton.setText("New");
        newButton.setEnabled(false);
        scriptToolBar.add(newButton);

        addButton.setText("Add");
        addButton.setEnabled(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        scriptToolBar.add(addButton);

        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        scriptToolBar.add(removeButton);

        saveButton.setText("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        scriptToolBar.add(saveButton);

        saveAsButton.setText("Save As");
        saveAsButton.setEnabled(false);
        scriptToolBar.add(saveAsButton);

        getContentPane().add(scriptToolBar, java.awt.BorderLayout.NORTH);

        jSplitPane1.setResizeWeight(0.3);
        hookScrollPane.setMinimumSize(new java.awt.Dimension(200, 100));
        hookScrollPane.setVerifyInputWhenFocusTarget(false);
        jSplitPane1.setLeftComponent(hookScrollPane);

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(0.3);
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(300, 48));
        descriptionTextArea.setBackground(new java.awt.Color(204, 204, 204));
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setBorder(null);
        descriptionTextArea.setMinimumSize(new java.awt.Dimension(200, 45));
        descriptionTextArea.setPreferredSize(new java.awt.Dimension(400, 45));
        jScrollPane1.setViewportView(descriptionTextArea);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel3.setText("Hook description : ");
        jPanel2.add(jLabel3, java.awt.BorderLayout.NORTH);

        jSplitPane2.setLeftComponent(jPanel2);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Script : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel3.add(jLabel2, gridBagConstraints);

        scriptTextField.setBackground(new java.awt.Color(204, 204, 204));
        scriptTextField.setEditable(false);
        scriptTextField.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel3.add(scriptTextField, gridBagConstraints);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 200));
        scriptTextArea.setBackground(new java.awt.Color(204, 204, 204));
        scriptTextArea.setEditable(false);
        jScrollPane3.setViewportView(scriptTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(jScrollPane3, gridBagConstraints);

        jSplitPane2.setRightComponent(jPanel3);

        jSplitPane1.setRightComponent(jSplitPane2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 600) / 2, (screenSize.height - 400) / 2, 600, 400);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Save button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_saveButtonActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_saveButtonActionPerformed

    /**
     * Removes the button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_removeButtonActionPerformed
        TreePath path = _hookTree.getTree().getSelectionPath();
        if (path.getPathCount() == 4) {
            String plugin = (String) path.getPathComponent(1);
            Hook hook = (Hook) path.getPathComponent(2);
            Script script = (Script) path.getPathComponent(3);
            _manager.removeScript(plugin, hook, script);
        }
    }// GEN-LAST:event_removeButtonActionPerformed

    /**
     * Adds the button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
        // FIRST
        // :
        // event_addButtonActionPerformed
        TreePath path = _hookTree.getTree().getSelectionPath();
        String plugin = null;
        Hook hook = null;
        if (path.getPathCount() >= 3) {
            plugin = (String) path.getPathComponent(1);
            hook = (Hook) path.getPathComponent(2);
        } else {
            return;
        }
        JFileChooser jfc = new JFileChooser(Preferences.getPreference("ScriptManager.DefaultDir"));
        jfc.setDialogTitle("Load script");
        int returnVal = jfc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            try {
                Script script = new Script(file);
                _manager.addScript(plugin, hook, script);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, new String[] { "Error loading Script : ", e.getMessage() },
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        Preferences.setPreference("ScriptManager.DefaultDir", jfc.getCurrentDirectory().getAbsolutePath());
    }// GEN-LAST:event_addButtonActionPerformed

    /** The add button. */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;

    /** The description text area. */
    private javax.swing.JTextArea descriptionTextArea;

    /** The hook scroll pane. */
    private javax.swing.JScrollPane hookScrollPane;

    /** The j label2. */
    private javax.swing.JLabel jLabel2;

    /** The j label3. */
    private javax.swing.JLabel jLabel3;

    /** The j panel2. */
    private javax.swing.JPanel jPanel2;

    /** The j panel3. */
    private javax.swing.JPanel jPanel3;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    /** The j scroll pane3. */
    private javax.swing.JScrollPane jScrollPane3;

    /** The j split pane1. */
    private javax.swing.JSplitPane jSplitPane1;

    /** The j split pane2. */
    private javax.swing.JSplitPane jSplitPane2;

    /** The new button. */
    private javax.swing.JButton newButton;

    /** The remove button. */
    private javax.swing.JButton removeButton;

    /** The save as button. */
    private javax.swing.JButton saveAsButton;

    /** The save button. */
    private javax.swing.JButton saveButton;

    /** The script text area. */
    private javax.swing.JTextArea scriptTextArea;

    /** The script text field. */
    private javax.swing.JTextField scriptTextField;

    /** The script tool bar. */
    private javax.swing.JToolBar scriptToolBar;

    // End of variables declaration//GEN-END:variables

    /**
     * The Class HookScriptTreeModel.
     */
    private class HookScriptTreeModel extends AbstractTreeTableModel {

        /** The _root. */
        Object _root = new String("RooT");

        /*
         * (non-Javadoc)
         * 
         * @seeedu.lnmiit.wavd.util.swing.treetable.AbstractTreeTableModel#
         * getColumnClass(int)
         */
        public Class getColumnClass(int column) {
            if (column == 0)
                return super.getColumnClass(column);
            return Boolean.class;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 2;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getColumnName
         * (int)
         */
        public String getColumnName(int i) {
            if (i == 1)
                return "Enabled";
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.TreeTableModel#getValueAt(java
         * .lang.Object, int)
         */
        public Object getValueAt(Object node, int column) {
            if (column == 0)
                return node;
            if (node instanceof Script) {
                Script script = (Script) node;
                return new Boolean(script.isEnabled());
            }
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
         */
        public Object getChild(Object parent, int index) {
            if (parent == _root) {
                return _manager.getPlugin(index);
            } else if (parent instanceof String) {
                return _manager.getHook((String) parent, index);
            } else if (parent instanceof Hook) {
                return ((Hook) parent).getScript(index);
            } else
                return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
         */
        public int getChildCount(Object parent) {
            if (parent == _root) {
                return _manager.getPluginCount();
            } else if (parent instanceof String) {
                return _manager.getHookCount((String) parent);
            } else if (parent instanceof Hook) {
                return ((Hook) parent).getScriptCount();
            } else
                return 0;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.tree.TreeModel#getRoot()
         */
        public Object getRoot() {
            return _root;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
         */
        public boolean isLeaf(Object node) {
            if (node instanceof Script)
                return true;
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath
         * , java.lang.Object)
         */
        public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.AbstractTreeModel#fireStructureChanged()
         */
        public void fireStructureChanged() {
            super.fireStructureChanged();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.AbstractTreeModel#fireTreeStructureChanged
         * (javax.swing.tree.TreePath)
         */
        public void fireTreeStructureChanged(TreePath path) {
            super.fireTreeStructureChanged(path);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.AbstractTreeModel#firePathChanged(javax
         * .swing.tree.TreePath)
         */
        public void firePathChanged(TreePath path) {
            super.firePathChanged(path);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.util.swing.treetable.AbstractTreeTableModel#setValueAt
         * (java.lang.Object, java.lang.Object, int)
         */
        public void setValueAt(Object aValue, Object node, int column) {
            if (column == 1 && node instanceof Script) {
                ((Script) node).setEnabled(aValue == Boolean.TRUE);
            } else
                super.setValueAt(aValue, node, column);
        }

        /*
         * (non-Javadoc)
         * 
         * @seeedu.lnmiit.wavd.util.swing.treetable.AbstractTreeTableModel#
         * isCellEditable(java.lang.Object, int)
         */
        public boolean isCellEditable(Object node, int column) {
            if (node instanceof Script && column == 1)
                return true;
            return super.isCellEditable(node, column);
        }

    }

    /**
     * The Class HookTreeRenderer.
     */
    private class HookTreeRenderer extends DefaultTreeCellRenderer {

        /**
	 * 
	 */
        private static final long serialVersionUID = -7409599484697564375L;

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent
         * (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
         * boolean)
         */
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            if (value instanceof Hook && comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                Hook hook = (Hook) value;
                if (hook != null) {
                    label.setText(hook.getName());
                }
            } else if (value instanceof Script && comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                Script script = (Script) value;
                if (script != null) {
                    label.setText(script.getFile().getName());
                }
            }
            return comp;
        }

    }

}
