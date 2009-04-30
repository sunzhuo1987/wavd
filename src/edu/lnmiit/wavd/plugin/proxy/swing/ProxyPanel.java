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
 * ProxyPanel.java
 *
 * Created on July 25, 2003, 11:07 PM
 */

package edu.lnmiit.wavd.plugin.proxy.swing;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.plugin.proxy.Proxy;
import edu.lnmiit.wavd.plugin.proxy.ProxyUI;
import edu.lnmiit.wavd.ui.swing.SwingPluginUI;
import edu.lnmiit.wavd.util.W32WinInet;
import edu.lnmiit.wavd.util.swing.ColumnDataModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ProxyPanel.
 */
public class ProxyPanel extends javax.swing.JPanel implements SwingPluginUI, ProxyUI {

    /**
     * 
     */
    private static final long serialVersionUID = 4626990388221498646L;

    /** The _proxy. */
    private Proxy _proxy;

    /** The _ltm. */
    private ListenerTableModel _ltm;

    /** The _mtm. */
    private MessageTableModel _mtm;

    /** The _plugins. */
    private ArrayList _plugins;

    /** The _plugin array. */
    private ProxyPluginUI[] _pluginArray = new ProxyPluginUI[0];

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new proxy panel.
     * 
     * @param proxy
     *            the proxy
     */
    public ProxyPanel(Proxy proxy) {
        initComponents();

        _proxy = proxy;

        _ltm = new ListenerTableModel(_proxy);
        listenerTable.setModel(_ltm);
        listenerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        _mtm = new MessageTableModel();
        messageTable.setModel(_mtm);

        int[][] columnWidths = { { 200, 200, 200 }, { 50, 50, 50 }, { 50, 50, 50 }, { 250, 250, 250 }, };

        javax.swing.table.TableColumnModel columnModel = messageTable.getColumnModel();
        for (int i = 0; i < columnWidths.length; i++) {
            columnModel.getColumn(i).setMinWidth(columnWidths[i][0]);
            columnModel.getColumn(i).setMaxWidth(columnWidths[i][1]);
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i][2]);
        }

        networkComboBox.setModel(new DefaultComboBoxModel(_proxy.getSimulators()));
        networkComboBox.setSelectedItem("Unlimited");

        String[] keys = _proxy.getProxies();
        for (int i = 0; i < keys.length; i++)
            _ltm.proxyAdded(keys[i]);

        proxy.setUI(this);

        if (!W32WinInet.isAvailable()) {
            primaryLabel.setEnabled(false);
            primaryCheckBox.setEnabled(false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getPanel()
     */
    public javax.swing.JPanel getPanel() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.PluginUI#getPluginName()
     */
    public String getPluginName() {
        return new String("Proxy");
    }

    /**
     * Adds the plugin.
     * 
     * @param plugin
     *            the plugin
     */
    public void addPlugin(ProxyPluginUI plugin) {
        if (_plugins == null) {
            _plugins = new ArrayList();
        }
        _plugins.add(plugin);
        _pluginArray = (ProxyPluginUI[]) _plugins.toArray(_pluginArray);
        mainTabbedPane.add(plugin.getPanel(), plugin.getPluginName());
        if (plugin instanceof ManualEditPanel)
            mainTabbedPane.setSelectedIndex(mainTabbedPane.getTabCount() - 1);
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        mainTabbedPane = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        stopButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listenerTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        addressTextField = new javax.swing.JTextField();
        portTextField = new javax.swing.JTextField();
        baseTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        networkComboBox = new javax.swing.JComboBox();
        primaryLabel = new javax.swing.JLabel();
        primaryCheckBox = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageTable = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.9);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(stopButton, gridBagConstraints);

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(startButton, gridBagConstraints);

        listenerTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] {

        }));
        jScrollPane1.setViewportView(listenerTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("Port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("Base URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel3, gridBagConstraints);

        addressTextField.setToolTipText("Blank or \"*\" means all interfaces");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(addressTextField, gridBagConstraints);

        portTextField.setToolTipText("integer between 0 and 65536");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(portTextField, gridBagConstraints);

        baseTextField.setToolTipText("Blank for a conventional proxy, or http://host:port for a reverse proxy.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(baseTextField, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setText("Speed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel5, gridBagConstraints);

        networkComboBox.setFont(new java.awt.Font("Dialog", 0, 12));
        networkComboBox.setMinimumSize(new java.awt.Dimension(32, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(networkComboBox, gridBagConstraints);

        primaryLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        primaryLabel.setText("Primary?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel1.add(primaryLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel1.add(primaryCheckBox, gridBagConstraints);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        messageTable.setBackground(new java.awt.Color(204, 204, 204));
        messageTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Time", "ID", "Method", "URL", "Status" }));
        messageTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        messageTable.setShowHorizontalLines(false);
        messageTable.setShowVerticalLines(false);
        jScrollPane2.setViewportView(messageTable);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel3);

        mainTabbedPane.addTab("Listeners", jSplitPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(mainTabbedPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Start button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_startButtonActionPerformed
        try {
            String address = addressTextField.getText();
            int port = Integer.parseInt(portTextField.getText().trim());
            HttpUrl base = null;
            if (!baseTextField.getText().equals("")) {
                base = new HttpUrl(baseTextField.getText());
            }
            String simulator = (String) networkComboBox.getSelectedItem();
            boolean primary = primaryCheckBox.isSelected();
            _proxy.addListener(address, port, base, simulator, primary);
            addressTextField.setText("");
            portTextField.setText("");
            baseTextField.setText("");
        } catch (Exception e) {
            _logger.severe("Error starting the listener : " + e);
            JOptionPane.showMessageDialog(null, new String[] { "Error starting proxy listener: ", e.toString() },
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_startButtonActionPerformed

    /**
     * Stop button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        _proxy.getProxies();
        int row = listenerTable.getSelectedRow();
        if (row < 0)
            return;
        String key = _ltm.getKey(row);
        String address = _proxy.getAddress(key);
        String port = Integer.toString(_proxy.getPort(key));
        HttpUrl base = _proxy.getBase(key);
        String simulator = _proxy.getSimulator(key);
        boolean primary = _proxy.isPrimaryProxy(key);
        if (!_proxy.removeListener(key)) {
            _logger.severe("Failed to stop " + key);
        } else {
            addressTextField.setText(address);
            portTextField.setText(port);
            baseTextField.setText(base == null ? "" : base.toString());
            if (simulator != null) {
                networkComboBox.setSelectedItem(simulator);
            } else {
                networkComboBox.setSelectedItem("Unlimited");
            }
            primaryCheckBox.setSelected(primary);
        }
    }// GEN-LAST:event_stopButtonActionPerformed

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getConversationActions()
     */
    public javax.swing.Action[] getConversationActions() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getUrlActions()
     */
    public javax.swing.Action[] getUrlActions() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyUI#proxyAdded(java.lang.String)
     */
    public void proxyAdded(final String key) {
        if (SwingUtilities.isEventDispatchThread()) {
            _ltm.proxyAdded(key);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    proxyAdded(key);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyUI#proxyRemoved(java.lang.String)
     */
    public void proxyRemoved(final String key) {
        if (SwingUtilities.isEventDispatchThread()) {
            _ltm.proxyRemoved(key);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    proxyRemoved(key);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyUI#proxyStarted(java.lang.String)
     */
    public void proxyStarted(String key) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyUI#proxyStopped(java.lang.String)
     */
    public void proxyStopped(String key) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.plugin.proxy.ProxyUI#requested(edu.lnmiit.wavd.model.
     * ConversationID, java.lang.String, edu.lnmiit.wavd.model.HttpUrl)
     */
    public void requested(final ConversationID id, final String method, final HttpUrl url) {
        if (SwingUtilities.isEventDispatchThread()) {
            _mtm.addRow(id, method, url);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    requested(id, method, url);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeedu.lnmiit.wavd.plugin.proxy.ProxyUI#received(edu.lnmiit.wavd.model.
     * ConversationID, java.lang.String)
     */
    public void received(final ConversationID id, final String status) {
        if (SwingUtilities.isEventDispatchThread()) {
            _mtm.updateRow(id, status);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    received(id, status);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeedu.lnmiit.wavd.plugin.proxy.ProxyUI#aborted(edu.lnmiit.wavd.model.
     * ConversationID, java.lang.String)
     */
    public void aborted(final ConversationID id, final String reason) {
        if (SwingUtilities.isEventDispatchThread()) {
            _mtm.updateRow(id, reason);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    aborted(id, reason);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    public void setEnabled(final boolean enabled) {
        if (SwingUtilities.isEventDispatchThread()) {
            startButton.setEnabled(enabled);
            stopButton.setEnabled(enabled);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setEnabled(enabled);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getConversationColumns()
     */
    public ColumnDataModel[] getConversationColumns() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getUrlColumns()
     */
    public ColumnDataModel[] getUrlColumns() {
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The address text field. */
    private javax.swing.JTextField addressTextField;

    /** The base text field. */
    private javax.swing.JTextField baseTextField;

    /** The j label1. */
    private javax.swing.JLabel jLabel1;

    /** The j label2. */
    private javax.swing.JLabel jLabel2;

    /** The j label3. */
    private javax.swing.JLabel jLabel3;

    /** The j label5. */
    private javax.swing.JLabel jLabel5;

    /** The j panel1. */
    private javax.swing.JPanel jPanel1;

    /** The j panel2. */
    private javax.swing.JPanel jPanel2;

    /** The j panel3. */
    private javax.swing.JPanel jPanel3;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    /** The j scroll pane2. */
    private javax.swing.JScrollPane jScrollPane2;

    /** The j split pane1. */
    private javax.swing.JSplitPane jSplitPane1;

    /** The listener table. */
    private javax.swing.JTable listenerTable;

    /** The main tabbed pane. */
    private javax.swing.JTabbedPane mainTabbedPane;

    /** The message table. */
    private javax.swing.JTable messageTable;

    /** The network combo box. */
    private javax.swing.JComboBox networkComboBox;

    /** The port text field. */
    private javax.swing.JTextField portTextField;

    /** The primary check box. */
    private javax.swing.JCheckBox primaryCheckBox;

    /** The primary label. */
    private javax.swing.JLabel primaryLabel;

    /** The start button. */
    private javax.swing.JButton startButton;

    /** The stop button. */
    private javax.swing.JButton stopButton;

    // End of variables declaration//GEN-END:variables

    /**
     * The Class MessageTableModel.
     */
    private class MessageTableModel extends AbstractTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = -1488247422224756561L;

        /** The _rows. */
        private ArrayList _rows = new ArrayList();

        /** The _timer. */
        private Timer _timer = new Timer(true);

        /** The _columns. */
        private String[] _columns = new String[] { "Time", "ID", "Method", "URL", "Status" };

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int columnIndex) {
            return _columns[columnIndex];
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return _columns.length;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return _rows.size();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object[] row = (Object[]) _rows.get(rowIndex);
            return row[columnIndex];
        }

        /**
         * Adds the row.
         * 
         * @param id
         *            the id
         * @param method
         *            the method
         * @param url
         *            the url
         */
        public void addRow(ConversationID id, String method, HttpUrl url) {
            Object[] row = new Object[] { new Date(), id, method, url, null };
            _rows.add(row);
            fireTableRowsInserted(_rows.size() - 1, _rows.size() - 1);
        }

        /**
         * Update row.
         * 
         * @param id
         *            the id
         * @param status
         *            the status
         */
        public void updateRow(final ConversationID id, String status) {
            for (int i = 0; i < _rows.size(); i++) {
                Object[] row = (Object[]) _rows.get(i);
                if (row[1].equals(id)) {
                    row[4] = status;
                    fireTableCellUpdated(i, 4);
                    _timer.schedule(new TimerTask() {
                        public void run() {
                            removeRow(id);
                        }
                    }, 5000);
                    return;
                }
            }
        }

        /**
         * Removes the row.
         * 
         * @param id
         *            the id
         */
        public void removeRow(final ConversationID id) {
            if (SwingUtilities.isEventDispatchThread()) {
                for (int i = 0; i < _rows.size(); i++) {
                    Object[] row = (Object[]) _rows.get(i);
                    if (row[1].equals(id)) {
                        _rows.remove(i);
                        fireTableRowsDeleted(i, i);
                        return;
                    }
                }
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        removeRow(id);
                    }
                });
            }
        }

    }

}
