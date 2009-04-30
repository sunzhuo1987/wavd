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
 * SummaryPanel.java
 *
 * Created on December 16, 2003, 10:35 AM
 */

package edu.lnmiit.wavd.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.FilteredUrlModel;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.util.swing.ColumnDataModel;
import edu.lnmiit.wavd.util.swing.JTreeTable;
import edu.lnmiit.wavd.util.swing.TableSorter;

// TODO: Auto-generated Javadoc
/**
 * The Class SummaryPanel.
 */
public class SummaryPanel extends JPanel {
    
    /** The _model. */
    private FrameworkModel _model;
    
    /** The _conversation model. */
    private UrlFilteredConversationModel _conversationModel;
    
    /** The _url model. */
    private FilteredUrlModel _urlModel;
    
    /** The _url tree table. */
    private JTreeTable _urlTreeTable;
    
    /** The _url actions. */
    private ArrayList _urlActions = new ArrayList();
    
    /** The _tree url. */
    private HttpUrl _treeURL = null;
    
    /** The _conversation table sorter. */
    private TableSorter _conversationTableSorter;
    
    /** The _conversation table model. */
    private ConversationTableModel _conversationTableModel;
    
    /** The _url tree table model. */
    private UrlTreeTableModelAdapter _urlTreeTableModel;
    
    /** The _conversation actions. */
    private ArrayList _conversationActions = new ArrayList();
    
    /** The _url columns. */
    private Map _urlColumns = new HashMap();
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /**
     * Instantiates a new summary panel.
     * 
     * @param model the model
     */
    public SummaryPanel(FrameworkModel model) {
        _model = model;
        _conversationModel = new UrlFilteredConversationModel(_model, _model.getConversationModel());
        // FIXME this is the wrong place for this, I think?
        _urlModel = new FilteredUrlModel(model.getUrlModel()) {
            protected boolean shouldFilter(HttpUrl url) {
                return _model.getUrlProperty(url, "METHODS") == null;
            }
        };
        initComponents();
        
        initTree();
        addTreeListeners();
        
        initTable();
        addTableListeners();
        addConversationActions(new Action[] {
            new ShowConversationAction(_conversationModel)
        });
    }
    
    /**
     * Inits the tree.
     */
    private void initTree() {
        _urlTreeTableModel = new UrlTreeTableModelAdapter(_urlModel);
        _urlTreeTable = new JTreeTable(_urlTreeTableModel);
        _urlTreeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ColumnWidthTracker.getTracker("UrlTree").addTable(_urlTreeTable);
        
        ColumnDataModel cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null) return null;
                return _model.getUrlProperty((HttpUrl) key, "METHODS");
            }
            public String getColumnName() { return "Methods"; }
            public Class getColumnClass() { return String.class; }
        };
        _urlColumns.put("METHODS", cdm);
        _urlTreeTableModel.addColumn(cdm);
        
        cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null) return null;
                return _model.getUrlProperty((HttpUrl) key, "STATUS");
            }
            public String getColumnName() { return "Status"; }
            public Class getColumnClass() { return String.class; }
        };
        _urlColumns.put("STATUS", cdm);
        _urlTreeTableModel.addColumn(cdm);
        
        JTree urlTree = _urlTreeTable.getTree();
        urlTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        urlTree.setRootVisible(false);
        urlTree.setShowsRootHandles(true);
        urlTree.setCellRenderer(new UrlTreeRenderer());
        
        treeScrollPane.setViewportView(_urlTreeTable);
    }
    
    /**
     * Adds the tree listeners.
     */
    private void addTreeListeners() {
        // Listen for when the selection changes.
        // We use this to set the selected URLInfo for each action, and
        // to filter the conversation list
        final JTree urlTree = _urlTreeTable.getTree();
        urlTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath selection = urlTree.getSelectionPath();
                _treeURL = null;
                if (selection != null) {
                    Object o = selection.getLastPathComponent();
                    if (o instanceof HttpUrl) {
                        _treeURL = (HttpUrl) o;
                    }
                }
                if (treeCheckBox.isSelected()) {
                    _conversationModel.setUrl(_treeURL);
                }
                synchronized (_urlActions) {
                    for (int i=0; i<_urlActions.size(); i++) {
                        AbstractAction action = (AbstractAction) _urlActions.get(i);
                        action.putValue("URL", _treeURL);
                    }
                }
            }
        });
        _urlTreeTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger() && _urlActions.size() > 0) {
                    int row = _urlTreeTable.rowAtPoint(e.getPoint());
                    _urlTreeTable.getSelectionModel().setSelectionInterval(row,row);
                    urlPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    /**
     * Adds the url actions.
     * 
     * @param actions the actions
     */
    public void addUrlActions(Action[] actions) {
        if (actions == null) return;
        for (int i=0; i<actions.length; i++) {
            _urlActions.add(actions[i]);
        }
        for (int i=0; i<actions.length; i++) {
            urlPopupMenu.add(new JMenuItem(actions[i]));
        }
    }
    
    /**
     * Adds the url columns.
     * 
     * @param columns the columns
     */
    public void addUrlColumns(ColumnDataModel[] columns) {
        if (columns == null) return;
        for (int i=0; i<columns.length; i++) {
            _urlTreeTableModel.addColumn(columns[i]);
        }
    }
    
    /**
     * Inits the table.
     */
    private void initTable() {
        _conversationTableModel = new ConversationTableModel(_conversationModel);
        ColumnWidthTracker.getTracker("ConversationTable").addTable(conversationTable);
        
        _conversationTableSorter = new TableSorter(_conversationTableModel, conversationTable.getTableHeader());
        conversationTable.setModel(_conversationTableSorter);
        
        conversationTable.setDefaultRenderer(Date.class, new DateRenderer());
    }
    
    /**
     * Adds the table listeners.
     */
    private void addTableListeners() {
        // This listener updates the registered actions with the selected Conversation
        conversationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                int row = conversationTable.getSelectedRow();
                TableModel tm = conversationTable.getModel();
                ConversationID id = null;
                if (row >-1)
                    id = (ConversationID) tm.getValueAt(row, 0); // UGLY hack! FIXME!!!!
                synchronized (_conversationActions) {
                    for (int i=0; i<_conversationActions.size(); i++) {
                        Action action = (Action) _conversationActions.get(i);
                        action.putValue("CONVERSATION", id);
                    }
                }
            }
        });
        
        conversationTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
            private void maybeShowPopup(MouseEvent e) {
                int row = conversationTable.rowAtPoint(e.getPoint());
                conversationTable.getSelectionModel().setSelectionInterval(row,row);
                if (e.isPopupTrigger() && _conversationActions.size() > 0) {
                    conversationPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    if (_conversationActions.size()>0) {
                        Action action = (Action) _conversationActions.get(0);
                        ActionEvent evt = new ActionEvent(conversationTable, 0, (String) action.getValue(Action.ACTION_COMMAND_KEY));
                        if (action.isEnabled()) {
                            action.actionPerformed(evt);
                        }
                    }
                }
            }
        });
        
    }
    
    /**
     * Adds the conversation actions.
     * 
     * @param actions the actions
     */
    public void addConversationActions(Action[] actions) {
        if (actions == null) return;
        for (int i=0; i<actions.length; i++) {
            _conversationActions.add(actions[i]);
        }
        for (int i=0; i<actions.length; i++) {
            conversationPopupMenu.add(new JMenuItem(actions[i]));
        }
    }
    
    /**
     * Adds the conversation columns.
     * 
     * @param columns the columns
     */
    public void addConversationColumns(ColumnDataModel[] columns) {
        if (columns == null) return;
        for (int i=0; i<columns.length; i++) {
            _conversationTableModel.addColumn(columns[i]);
        }
        _conversationTableSorter.setSortingStatus(0, TableSorter.DESCENDING);
    }
    
    /**
     * Inits the components.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        urlPopupMenu = new javax.swing.JPopupMenu();
        conversationPopupMenu = new javax.swing.JPopupMenu();
        summarySplitPane = new javax.swing.JSplitPane();
        urlPanel = new javax.swing.JPanel();
        treeCheckBox = new javax.swing.JCheckBox();
        treeScrollPane = new javax.swing.JScrollPane();
        conversationScrollPane = new javax.swing.JScrollPane();
        conversationTable = new javax.swing.JTable();

        urlPopupMenu.setLabel("URL Actions");
        conversationPopupMenu.setLabel("Conversation Actions");

        setLayout(new java.awt.BorderLayout());

        summarySplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        summarySplitPane.setResizeWeight(0.5);
        summarySplitPane.setOneTouchExpandable(true);
        urlPanel.setLayout(new java.awt.GridBagLayout());

        urlPanel.setMinimumSize(new java.awt.Dimension(283, 100));
        urlPanel.setPreferredSize(new java.awt.Dimension(264, 100));
        treeCheckBox.setText("Tree Selection filters conversation list");
        treeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                treeCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        urlPanel.add(treeCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        urlPanel.add(treeScrollPane, gridBagConstraints);

        summarySplitPane.setLeftComponent(urlPanel);

        conversationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        conversationTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        conversationScrollPane.setViewportView(conversationTable);

        summarySplitPane.setRightComponent(conversationScrollPane);

        add(summarySplitPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    /**
     * Tree check box action performed.
     * 
     * @param evt the evt
     */
    private void treeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_treeCheckBoxActionPerformed
        if (treeCheckBox.isSelected() && _treeURL != null) {
            _conversationModel.setUrl(_treeURL);
        } else {
            _conversationModel.setUrl(null);
        }
    }//GEN-LAST:event_treeCheckBoxActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The conversation popup menu. */
    private javax.swing.JPopupMenu conversationPopupMenu;
    
    /** The conversation scroll pane. */
    private javax.swing.JScrollPane conversationScrollPane;
    
    /** The conversation table. */
    private javax.swing.JTable conversationTable;
    
    /** The summary split pane. */
    private javax.swing.JSplitPane summarySplitPane;
    
    /** The tree check box. */
    private javax.swing.JCheckBox treeCheckBox;
    
    /** The tree scroll pane. */
    private javax.swing.JScrollPane treeScrollPane;
    
    /** The url panel. */
    private javax.swing.JPanel urlPanel;
    
    /** The url popup menu. */
    private javax.swing.JPopupMenu urlPopupMenu;
    // End of variables declaration//GEN-END:variables
    
    
}
