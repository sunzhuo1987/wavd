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
 * FuzzerPanel.java
 *
 * Created on 26 October 2004, 04:41
 */

package edu.lnmiit.wavd.plugin.fuzz.swing;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.plugin.AbstractPluginModel;
import edu.lnmiit.wavd.plugin.fuzz.FuzzFactory;
import edu.lnmiit.wavd.plugin.fuzz.FuzzSource;
import edu.lnmiit.wavd.plugin.fuzz.Fuzzer;
import edu.lnmiit.wavd.plugin.fuzz.FuzzerEvent;
import edu.lnmiit.wavd.plugin.fuzz.FuzzerListener;
import edu.lnmiit.wavd.plugin.fuzz.FuzzerModel;
import edu.lnmiit.wavd.plugin.fuzz.Parameter;
import edu.lnmiit.wavd.ui.swing.ColumnWidthTracker;
import edu.lnmiit.wavd.ui.swing.ConversationTableModel;
import edu.lnmiit.wavd.ui.swing.DateRenderer;
import edu.lnmiit.wavd.ui.swing.ShowConversationAction;
import edu.lnmiit.wavd.ui.swing.SwingPluginUI;
import edu.lnmiit.wavd.util.swing.ColumnDataModel;

// TODO: Auto-generated Javadoc
/**
 * The Class FuzzerPanel.
 */
public class FuzzerPanel extends javax.swing.JPanel implements SwingPluginUI {

    /**
     * 
     */
    private static final long serialVersionUID = -1490129926338658665L;

    /** The _fuzzer. */
    private Fuzzer _fuzzer;

    /** The _model. */
    private FuzzerModel _model;

    /** The _header table model. */
    private HeaderTableModel _headerTableModel;

    /** The _parameter table model. */
    private ParameterTableModel _parameterTableModel;

    /** The _fuzz factory. */
    private FuzzFactory _fuzzFactory;

    /** The _fuzz sources. */
    private DefaultComboBoxModel _fuzzSources;

    /** The _fuzz items. */
    private DefaultListModel _fuzzItems;

    /** The _show action. */
    private ShowConversationAction _showAction;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new fuzzer panel.
     * 
     * @param fuzzer
     *            the fuzzer
     */
    public FuzzerPanel(Fuzzer fuzzer) {
        _fuzzer = fuzzer;
        _model = fuzzer.getModel();
        initComponents();
        initFields();

        _fuzzFactory = _fuzzer.getFuzzFactory();
        configureTables();
        addTableListeners();
        configureFuzzDialog();

        Listener listener = new Listener();
        _model.addPropertyChangeListener(listener);
        _model.addModelListener(listener);
        _fuzzFactory.addPropertyChangeListener(listener);
    }

    /**
     * Configure tables.
     */
    private void configureTables() {
        _headerTableModel = new HeaderTableModel();
        headerTable.setModel(_headerTableModel);

        _parameterTableModel = new ParameterTableModel();
        paramTable.setModel(_parameterTableModel);
        DefaultComboBoxModel paramTypes = new DefaultComboBoxModel(Parameter.getParameterLocations());
        DefaultCellEditor dce = new DefaultCellEditor(new JComboBox(paramTypes));
        TableColumn col = paramTable.getColumnModel().getColumn(0);
        col.setCellEditor(dce);
        _fuzzSources = new DefaultComboBoxModel(_fuzzFactory.getSourceDescriptions());
        _fuzzSources.insertElementAt("", 0);
        dce = new DefaultCellEditor(new JComboBox(_fuzzSources));
        col = paramTable.getColumnModel().getColumn(5);
        col.setCellEditor(dce);
        paramTable.setRowHeight((int) dce.getComponent().getPreferredSize().getHeight());

        conversationTable.setModel(new ConversationTableModel(_model.getConversationModel()));
        ColumnWidthTracker.getTracker("ConversationTable").addTable(conversationTable);
        conversationTable.setDefaultRenderer(Date.class, new DateRenderer());
    }

    /**
     * Adds the table listeners.
     */
    private void addTableListeners() {
        _showAction = new ShowConversationAction(_model.getConversationModel());
        conversationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                _logger.info("Selection changed");
                if (e.getValueIsAdjusting())
                    return;
                int row = conversationTable.getSelectedRow();
                TableModel tm = conversationTable.getModel();
                if (row > -1) {
                    ConversationID id = (ConversationID) tm.getValueAt(row, 0); // UGLY
                                                                                // hack
                                                                                // !
                                                                                // FIXME
                                                                                // !
                                                                                // !
                                                                                // !
                                                                                // !
                    _showAction.putValue("CONVERSATION", id);
                    _logger.info("Conversation " + id + " selected");
                } else {
                    _showAction.putValue("CONVERSATION", null);
                }
            }
        });

        conversationTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                _logger.info("Mouse clicked in the table");
                int row = conversationTable.rowAtPoint(e.getPoint());
                conversationTable.getSelectionModel().setSelectionInterval(row, row);
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    ActionEvent evt = new ActionEvent(conversationTable, 0, (String) _showAction
                            .getValue(Action.ACTION_COMMAND_KEY));
                    if (_showAction.isEnabled())
                        _showAction.actionPerformed(evt);
                }
            }
        });
    }

    /**
     * Configure fuzz dialog.
     */
    private void configureFuzzDialog() {
        fuzzDialog.setBounds(200, 200, 600, 400);
        fuzzDialog.setResizable(false);
        _fuzzItems = new DefaultListModel();
        valueList.setModel(_fuzzItems);
        nameList.setModel(_fuzzSources);
        nameList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                _fuzzItems.clear();
                itemsLabel.setText("Items : ");
                Object value = nameList.getSelectedValue();
                if (value != null && !"".equals(value)) {
                    FuzzSource source = _fuzzFactory.getSource((String) value);
                    if (source != null) {
                        itemsLabel.setText("Items : " + source.size());
                        while (source.hasNext() && _fuzzItems.size() < 100) {
                            _fuzzItems.addElement(source.current());
                            source.increment();
                        }
                    }
                }
            }
        });
    }

    /**
     * Inits the fields.
     */
    private void initFields() {
        methodTextField.setText(_model.getFuzzMethod());
        urlTextField.setText(_model.getFuzzUrl());
        versionTextField.setText(_model.getFuzzVersion());
        statusLabel.setText(_model.getStatus());
        totalTextField.setText(Integer.toString(_model.getTotalRequests()));
        currentTextField.setText(Integer.toString(_model.getRequestIndex()));
    }

    /**
     * Update fields.
     * 
     * @param evt
     *            the evt
     */
    private void updateFields(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        Object value = evt.getNewValue();
        if (evt.getSource() == _fuzzFactory) {
            _fuzzSources.removeAllElements();
            _fuzzSources.addElement("");
            String[] names = _fuzzFactory.getSourceDescriptions();
            for (int i = 0; i < names.length; i++) {
                _fuzzSources.addElement(names[i]);
            }
        } else if (property.equals(FuzzerModel.PROPERTY_FUZZMETHOD) && !value.equals(methodTextField.getText())) {
            methodTextField.setText(value.toString());
        } else if (property.equals(FuzzerModel.PROPERTY_FUZZURL) && !value.toString().equals(urlTextField.getText())) {
            urlTextField.setText(value.toString());
        } else if (property.equals(FuzzerModel.PROPERTY_FUZZVERSION) && !value.equals(versionTextField.getText())) {
            versionTextField.setText(value.toString());
        } else if (property.equals(FuzzerModel.PROPERTY_REQUESTINDEX) && !value.equals(currentTextField.getText())) {
            currentTextField.setText(value.toString());
        } else if (property.equals(FuzzerModel.PROPERTY_TOTALREQUESTS) && !value.equals(totalTextField.getText())) {
            totalTextField.setText(value.toString());
        } else if (property.equals(AbstractPluginModel.PROPERTY_STATUS)) {
            statusLabel.setText(value.toString());
        }
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        fuzzDialog = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        nameList = new javax.swing.JList();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        valueList = new javax.swing.JList();
        itemsLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        descriptionTextField = new javax.swing.JTextField();
        regexTextField = new javax.swing.JTextField();
        fileNameTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        fuzzPanel = new javax.swing.JPanel();
        requestPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        methodTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        urlTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        versionTextField = new javax.swing.JTextField();
        headerPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        headerTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        addHeaderButton = new javax.swing.JButton();
        deleteHeaderButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        parameterPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        paramTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        addParameterButton = new javax.swing.JButton();
        deleteParameterButton = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        totalTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        currentTextField = new javax.swing.JTextField();
        actionPanel = new javax.swing.JPanel();
        sourcesButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        conversationTable = new javax.swing.JTable();
        statusLabel = new javax.swing.JLabel();

        fuzzDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        fuzzDialog.setTitle("Fuzz Sources");
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel8.setText("Fuzz Sources");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel8, gridBagConstraints);

        jScrollPane3.setMaximumSize(new java.awt.Dimension(100, 32767));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(100, 50));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(100, 131));
        nameList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(nameList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jScrollPane3, gridBagConstraints);

        jLabel10.setText("Items");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel4.add(jLabel10, gridBagConstraints);

        jScrollPane4.setViewportView(valueList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel4.add(jScrollPane4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        fuzzDialog.getContentPane().add(jPanel4, gridBagConstraints);

        itemsLabel.setText("Items : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        fuzzDialog.getContentPane().add(itemsLabel, gridBagConstraints);

        jLabel9.setText("Description : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        fuzzDialog.getContentPane().add(jLabel9, gridBagConstraints);

        jLabel11.setText("RegEx : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        fuzzDialog.getContentPane().add(jLabel11, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        fuzzDialog.getContentPane().add(descriptionTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        fuzzDialog.getContentPane().add(regexTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        fuzzDialog.getContentPane().add(fileNameTextField, gridBagConstraints);

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        fuzzDialog.getContentPane().add(browseButton, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jPanel3.add(addButton);

        deleteButton.setText("Remove");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jPanel3.add(deleteButton);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        jPanel3.add(closeButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        fuzzDialog.getContentPane().add(jPanel3, gridBagConstraints);

        jLabel4.setText("File : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        fuzzDialog.getContentPane().add(jLabel4, gridBagConstraints);

        setLayout(new java.awt.GridBagLayout());

        fuzzPanel.setLayout(new java.awt.GridBagLayout());

        requestPanel.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Method");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        requestPanel.add(jLabel3, gridBagConstraints);

        methodTextField.setText("GET");
        methodTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        methodTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                methodTextFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        requestPanel.add(methodTextField, gridBagConstraints);

        jLabel5.setText("URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        requestPanel.add(jLabel5, gridBagConstraints);

        urlTextField.setMinimumSize(new java.awt.Dimension(100, 19));
        urlTextField.setPreferredSize(new java.awt.Dimension(200, 19));
        urlTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlTextFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        requestPanel.add(urlTextField, gridBagConstraints);

        jLabel6.setText("Version");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        requestPanel.add(jLabel6, gridBagConstraints);

        versionTextField.setText("HTTP/1.1");
        versionTextField.setMinimumSize(new java.awt.Dimension(70, 19));
        versionTextField.setPreferredSize(new java.awt.Dimension(70, 19));
        versionTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                versionTextFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        requestPanel.add(versionTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        fuzzPanel.add(requestPanel, gridBagConstraints);

        headerPanel.setLayout(new java.awt.GridBagLayout());

        headerPanel.setMinimumSize(new java.awt.Dimension(22, 50));
        headerPanel.setPreferredSize(new java.awt.Dimension(527, 100));
        headerTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] {

        }));
        jScrollPane1.setViewportView(headerTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        headerPanel.add(jScrollPane1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridLayout(2, 1));

        addHeaderButton.setText("Add");
        addHeaderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addHeaderButtonActionPerformed(evt);
            }
        });

        jPanel1.add(addHeaderButton);

        deleteHeaderButton.setText("Delete");
        deleteHeaderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteHeaderButtonActionPerformed(evt);
            }
        });

        jPanel1.add(deleteHeaderButton);

        headerPanel.add(jPanel1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.2;
        fuzzPanel.add(headerPanel, gridBagConstraints);

        jLabel7.setText("Parameters");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        fuzzPanel.add(jLabel7, gridBagConstraints);

        parameterPanel.setLayout(new java.awt.GridBagLayout());

        paramTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] {

        }));
        jScrollPane2.setViewportView(paramTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        parameterPanel.add(jScrollPane2, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridLayout(2, 1));

        addParameterButton.setText("Add");
        addParameterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addParameterButtonActionPerformed(evt);
            }
        });

        jPanel2.add(addParameterButton);

        deleteParameterButton.setText("Delete");
        deleteParameterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteParameterButtonActionPerformed(evt);
            }
        });

        jPanel2.add(deleteParameterButton);

        parameterPanel.add(jPanel2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        fuzzPanel.add(parameterPanel, gridBagConstraints);

        statusPanel.setLayout(new java.awt.GridLayout(2, 2));

        jLabel1.setText("Total Requests : ");
        statusPanel.add(jLabel1);

        totalTextField.setColumns(5);
        totalTextField.setEditable(false);
        totalTextField.setMinimumSize(new java.awt.Dimension(50, 19));
        statusPanel.add(totalTextField);

        jLabel2.setText("Current Request : ");
        statusPanel.add(jLabel2);

        currentTextField.setColumns(5);
        currentTextField.setEditable(false);
        currentTextField.setMinimumSize(new java.awt.Dimension(50, 19));
        statusPanel.add(currentTextField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        fuzzPanel.add(statusPanel, gridBagConstraints);

        actionPanel.setLayout(new java.awt.GridLayout(1, 0));

        sourcesButton.setText("Sources");
        sourcesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourcesButtonActionPerformed(evt);
            }
        });

        actionPanel.add(sourcesButton);

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        actionPanel.add(startButton);

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        actionPanel.add(stopButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        fuzzPanel.add(actionPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(fuzzPanel, gridBagConstraints);

        conversationTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null },
                { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] {
                "Title 1", "Title 2", "Title 3", "Title 4" }));
        conversationTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane5.setViewportView(conversationTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.5;
        add(jScrollPane5, gridBagConstraints);

        statusLabel.setMaximumSize(new java.awt.Dimension(200, 15));
        statusLabel.setMinimumSize(new java.awt.Dimension(200, 15));
        statusLabel.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(statusLabel, gridBagConstraints);

    }

    // </editor-fold>//GEN-END:initComponents

    /**
     * Close button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_closeButtonActionPerformed
        fuzzDialog.setVisible(false);
    }// GEN-LAST:event_closeButtonActionPerformed

    /**
     * Browse button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_browseButtonActionPerformed
        JFileChooser jfc = new JFileChooser(fileNameTextField.getText());
        jfc.setDialogTitle("Select a file to load");
        int returnVal = jfc.showOpenDialog(fuzzDialog);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (file != null && !file.isDirectory()) {
                fileNameTextField.setText(file.toString());
            }
        }
    }// GEN-LAST:event_browseButtonActionPerformed

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
        String name = (String) nameList.getSelectedValue();
        if (name != null) {
            _fuzzFactory.removeSource(name);
        }
    }// GEN-LAST:event_deleteButtonActionPerformed

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
        String description = descriptionTextField.getText();
        String fileName = fileNameTextField.getText();
        String regex = regexTextField.getText();
        if (description.equals("")) {
            JOptionPane.showMessageDialog(null, new String[] { "Description cannot be empty", }, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!regex.equals("") && !fileName.equals("")) {
            JOptionPane.showMessageDialog(null,
                    new String[] { "Please enter EITHER a Regular Expression OR a File name" }, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (regex.equals("") && fileName.equals("")) {
            JOptionPane.showMessageDialog(null,
                    new String[] { "Please enter EITHER a Regular Expression OR a File name" }, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!fileName.equals("")) {
            try {
                File file = new File(fileName);
                if (file.isDirectory()) {
                    JOptionPane.showMessageDialog(null, new String[] { file.toString() + " is a directory", }, "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                _fuzzFactory.loadFuzzStrings(description, file);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null, new String[] { "Error loading fuzz strings!", ioe.getMessage() },
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        if (!regex.equals("")) {
            try {
                _fuzzFactory.addRegexSource(description, regex);
            } catch (PatternSyntaxException pse) {
                JOptionPane.showMessageDialog(null, new String[] { "Invalid regular expression!", pse.getMessage() },
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
    }// GEN-LAST:event_addButtonActionPerformed

    /**
     * Sources button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void sourcesButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_sourcesButtonActionPerformed
        fuzzDialog.setVisible(true);
    }// GEN-LAST:event_sourcesButtonActionPerformed

    /**
     * Version text field action performed.
     * 
     * @param evt
     *            the evt
     */
    private void versionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_versionTextFieldActionPerformed
        if (_model.getFuzzVersion().equals(versionTextField.getText()))
            return;
        _model.setFuzzVersion(versionTextField.getText());
    }// GEN-LAST:event_versionTextFieldActionPerformed

    /**
     * Method text field action performed.
     * 
     * @param evt
     *            the evt
     */
    private void methodTextFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_methodTextFieldActionPerformed
        if (_model.getFuzzMethod().equals(methodTextField.getText()))
            return;
        _model.setFuzzMethod(methodTextField.getText());
    }// GEN-LAST:event_methodTextFieldActionPerformed

    /**
     * Url text field action performed.
     * 
     * @param evt
     *            the evt
     */
    private void urlTextFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_urlTextFieldActionPerformed
        if (_model.getFuzzUrl().equals(urlTextField.getText()))
            return;
        _model.setFuzzUrl(urlTextField.getText());
    }// GEN-LAST:event_urlTextFieldActionPerformed

    /**
     * Stop button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_stopButtonActionPerformed
        _model.setBusyFuzzing(false);
    }// GEN-LAST:event_stopButtonActionPerformed

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
        methodTextFieldActionPerformed(evt);
        urlTextFieldActionPerformed(evt);
        versionTextFieldActionPerformed(evt);
        _fuzzer.startFuzzing();
    }// GEN-LAST:event_startButtonActionPerformed

    /**
     * Delete parameter button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void deleteParameterButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
                                                                                       // -
                                                                                       // FIRST
                                                                                       // :
        // event_deleteParameterButtonActionPerformed
        int row = paramTable.getSelectedRow();
        if (row == -1)
            return;
        _model.removeFuzzParameter(row);
    }// GEN-LAST:event_deleteParameterButtonActionPerformed

    /**
     * Adds the parameter button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void addParameterButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
                                                                                    // -
                                                                                    // FIRST
                                                                                    // :
        // event_addParameterButtonActionPerformed
        int row = paramTable.getSelectedRow();
        if (row == -1) {
            row = paramTable.getRowCount();
        }
        _model.addFuzzParameter(row, new Parameter(Parameter.LOCATION_QUERY, "v" + row, "String", "a" + row), null, 0);
    }// GEN-LAST:event_addParameterButtonActionPerformed

    /**
     * Delete header button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void deleteHeaderButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
                                                                                    // -
                                                                                    // FIRST
                                                                                    // :
        // event_deleteHeaderButtonActionPerformed
        int row = headerTable.getSelectedRow();
        if (row == -1)
            return;
        _model.removeFuzzHeader(row);
    }// GEN-LAST:event_deleteHeaderButtonActionPerformed

    /**
     * Adds the header button action performed.
     * 
     * @param evt
     *            the evt
     */
    private void addHeaderButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
        // -
        // FIRST
        // :
        // event_addHeaderButtonActionPerformed
        int row = headerTable.getSelectedRow();
        if (row == -1) {
            row = headerTable.getRowCount();
        }
        _model.addFuzzHeader(row, new NamedValue("Header", "Value"));
    }// GEN-LAST:event_addHeaderButtonActionPerformed

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getConversationActions()
     */
    public Action[] getConversationActions() {
        return new Action[] { new FuzzConversationAction() };
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getConversationColumns()
     */
    public ColumnDataModel[] getConversationColumns() {
        return new ColumnDataModel[0];
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
        return "Fuzzer";
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getUrlActions()
     */
    public Action[] getUrlActions() {
        return new Action[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.SwingPluginUI#getUrlColumns()
     */
    public ColumnDataModel[] getUrlColumns() {
        return new ColumnDataModel[0];
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The action panel. */
    private javax.swing.JPanel actionPanel;

    /** The add button. */
    private javax.swing.JButton addButton;

    /** The add header button. */
    private javax.swing.JButton addHeaderButton;

    /** The add parameter button. */
    private javax.swing.JButton addParameterButton;

    /** The browse button. */
    private javax.swing.JButton browseButton;

    /** The close button. */
    private javax.swing.JButton closeButton;

    /** The conversation table. */
    private javax.swing.JTable conversationTable;

    /** The current text field. */
    private javax.swing.JTextField currentTextField;

    /** The delete button. */
    private javax.swing.JButton deleteButton;

    /** The delete header button. */
    private javax.swing.JButton deleteHeaderButton;

    /** The delete parameter button. */
    private javax.swing.JButton deleteParameterButton;

    /** The description text field. */
    private javax.swing.JTextField descriptionTextField;

    /** The file name text field. */
    private javax.swing.JTextField fileNameTextField;

    /** The fuzz dialog. */
    private javax.swing.JDialog fuzzDialog;

    /** The fuzz panel. */
    private javax.swing.JPanel fuzzPanel;

    /** The header panel. */
    private javax.swing.JPanel headerPanel;

    /** The header table. */
    private javax.swing.JTable headerTable;

    /** The items label. */
    private javax.swing.JLabel itemsLabel;

    /** The j label1. */
    private javax.swing.JLabel jLabel1;

    /** The j label10. */
    private javax.swing.JLabel jLabel10;

    /** The j label11. */
    private javax.swing.JLabel jLabel11;

    /** The j label2. */
    private javax.swing.JLabel jLabel2;

    /** The j label3. */
    private javax.swing.JLabel jLabel3;

    /** The j label4. */
    private javax.swing.JLabel jLabel4;

    /** The j label5. */
    private javax.swing.JLabel jLabel5;

    /** The j label6. */
    private javax.swing.JLabel jLabel6;

    /** The j label7. */
    private javax.swing.JLabel jLabel7;

    /** The j label8. */
    private javax.swing.JLabel jLabel8;

    /** The j label9. */
    private javax.swing.JLabel jLabel9;

    /** The j panel1. */
    private javax.swing.JPanel jPanel1;

    /** The j panel2. */
    private javax.swing.JPanel jPanel2;

    /** The j panel3. */
    private javax.swing.JPanel jPanel3;

    /** The j panel4. */
    private javax.swing.JPanel jPanel4;

    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;

    /** The j scroll pane2. */
    private javax.swing.JScrollPane jScrollPane2;

    /** The j scroll pane3. */
    private javax.swing.JScrollPane jScrollPane3;

    /** The j scroll pane4. */
    private javax.swing.JScrollPane jScrollPane4;

    /** The j scroll pane5. */
    private javax.swing.JScrollPane jScrollPane5;

    /** The method text field. */
    private javax.swing.JTextField methodTextField;

    /** The name list. */
    private javax.swing.JList nameList;

    /** The param table. */
    private javax.swing.JTable paramTable;

    /** The parameter panel. */
    private javax.swing.JPanel parameterPanel;

    /** The regex text field. */
    private javax.swing.JTextField regexTextField;

    /** The request panel. */
    private javax.swing.JPanel requestPanel;

    /** The sources button. */
    private javax.swing.JButton sourcesButton;

    /** The start button. */
    private javax.swing.JButton startButton;

    /** The status label. */
    private javax.swing.JLabel statusLabel;

    /** The status panel. */
    private javax.swing.JPanel statusPanel;

    /** The stop button. */
    private javax.swing.JButton stopButton;

    /** The total text field. */
    private javax.swing.JTextField totalTextField;

    /** The url text field. */
    private javax.swing.JTextField urlTextField;

    /** The value list. */
    private javax.swing.JList valueList;

    /** The version text field. */
    private javax.swing.JTextField versionTextField;

    // End of variables declaration//GEN-END:variables

    /**
     * The Class Listener.
     */
    private class Listener implements PropertyChangeListener, FuzzerListener {

        /*
         * (non-Javadoc)
         * 
         * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
         * PropertyChangeEvent)
         */
        public void propertyChange(final PropertyChangeEvent evt) {
            Runnable runner = new Runnable() {
                public void run() {
                    updateFields(evt);
                }
            };
            runOnEDT(runner);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.plugin.fuzz.FuzzerListener#fuzzHeaderAdded(edu.lnmiit
         * .wavd.plugin.fuzz.FuzzerEvent)
         */
        public void fuzzHeaderAdded(final FuzzerEvent evt) {
            Runnable runner = new Runnable() {
                public void run() {
                    _headerTableModel.fireTableRowsInserted(evt.getRow(), evt.getRow());
                }
            };
            runOnEDT(runner);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.plugin.fuzz.FuzzerListener#fuzzHeaderChanged(edu.
         * lnmiit.wavd.plugin.fuzz.FuzzerEvent)
         */
        public void fuzzHeaderChanged(final FuzzerEvent evt) {
            Runnable runner = new Runnable() {
                public void run() {
                    _headerTableModel.fireTableRowsUpdated(evt.getRow(), evt.getRow());
                }
            };
            runOnEDT(runner);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.plugin.fuzz.FuzzerListener#fuzzHeaderRemoved(edu.
         * lnmiit.wavd.plugin.fuzz.FuzzerEvent)
         */
        public void fuzzHeaderRemoved(final FuzzerEvent evt) {
            Runnable runner = new Runnable() {
                public void run() {
                    _headerTableModel.fireTableRowsDeleted(evt.getRow(), evt.getRow());
                }
            };
            runOnEDT(runner);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.plugin.fuzz.FuzzerListener#fuzzParameterAdded(edu
         * .lnmiit.wavd.plugin.fuzz.FuzzerEvent)
         */
        public void fuzzParameterAdded(final FuzzerEvent evt) {
            Runnable runner = new Runnable() {
                public void run() {
                    _parameterTableModel.fireTableRowsInserted(evt.getRow(), evt.getRow());
                }
            };
            runOnEDT(runner);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.plugin.fuzz.FuzzerListener#fuzzParameterChanged(edu
         * .lnmiit.wavd.plugin.fuzz.FuzzerEvent)
         */
        public void fuzzParameterChanged(final FuzzerEvent evt) {
            Runnable runner = new Runnable() {
                public void run() {
                    _parameterTableModel.fireTableRowsUpdated(evt.getRow(), evt.getRow());
                }
            };
            runOnEDT(runner);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.plugin.fuzz.FuzzerListener#fuzzParameterRemoved(edu
         * .lnmiit.wavd.plugin.fuzz.FuzzerEvent)
         */
        public void fuzzParameterRemoved(final FuzzerEvent evt) {
            Runnable runner = new Runnable() {
                public void run() {
                    _parameterTableModel.fireTableRowsDeleted(evt.getRow(), evt.getRow());
                }
            };
            runOnEDT(runner);
        }

        /**
         * Run on edt.
         * 
         * @param runner
         *            the runner
         */
        private void runOnEDT(Runnable runner) {
            if (SwingUtilities.isEventDispatchThread()) {
                runner.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(runner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * The Class HeaderTableModel.
     */
    private class HeaderTableModel extends AbstractTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = -251004533507569271L;
        /** The _column names. */
        private String[] _columnNames = new String[] { "Header", "Value" };

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int columnIndex) {
            return _columnNames[columnIndex];
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return _columnNames.length;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return _model.getFuzzHeaderCount();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            NamedValue header = _model.getFuzzHeader(rowIndex);
            if (columnIndex == 0) {
                return header.getName();
            } else {
                return header.getValue();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable(int rowIndex, int ColumnIndex) {
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
         * int, int)
         */
        public void setValueAt(Object aValue, int rowIndex, int colIndex) {
            NamedValue header = _model.getFuzzHeader(rowIndex);
            switch (colIndex) {
            case 0:
                header = new NamedValue((String) aValue, header.getValue());
                break;
            case 1:
                header = new NamedValue(header.getName(), (String) aValue);
                break;
            }
            _model.setFuzzHeader(rowIndex, header);
        }

    }

    /**
     * The Class ParameterTableModel.
     */
    private class ParameterTableModel extends AbstractTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = 5385422370976993920L;
        /** The _column names. */
        private String[] _columnNames = new String[] { "Location", "Name", "Type", "Value", "Priority", "Fuzz Source" };

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int columnIndex) {
            return _columnNames[columnIndex];
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return _columnNames.length;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return _model.getFuzzParameterCount();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            Parameter param = _model.getFuzzParameter(rowIndex);
            if (param == null)
                return "null";
            switch (columnIndex) {
            case 0:
                return param.getLocation();
            case 1:
                return param.getName();
            case 2:
                return param.getType();
            case 3:
                return param.getValue();
            case 4:
                return new Integer(_model.getFuzzParameterPriority(rowIndex));
            case 5:
                FuzzSource source = _model.getParameterFuzzSource(rowIndex);
                if (source != null) {
                    return source.getDescription();
                } else {
                    return null;
                }
            }
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable(int rowIndex, int ColumnIndex) {
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
         * int, int)
         */
        public void setValueAt(Object aValue, int rowIndex, int colIndex) {
            Parameter parameter = _model.getFuzzParameter(rowIndex);
            Object defValue = parameter.getValue();
            int priority = _model.getFuzzParameterPriority(rowIndex);
            FuzzSource source = _model.getParameterFuzzSource(rowIndex);
            switch (colIndex) {
            case 0:
                parameter = new Parameter((String) aValue, parameter.getName(), parameter.getType(), defValue);
                break;
            case 1:
                parameter = new Parameter(parameter.getLocation(), (String) aValue, parameter.getType(), defValue);
                break;
            case 2:
                parameter = new Parameter(parameter.getLocation(), parameter.getName(), (String) aValue, defValue);
                break;
            case 3:
                parameter = new Parameter(parameter.getLocation(), parameter.getName(), parameter.getType(), aValue);
                break;
            case 4:
                priority = Integer.parseInt(aValue.toString());
                break;
            case 5:
                source = _fuzzFactory.getSource((String) aValue);
                break;
            }
            _model.setFuzzParameter(rowIndex, parameter, source, priority);
        }

    }

    /**
     * The Class FuzzConversationAction.
     */
    private class FuzzConversationAction extends AbstractAction {

        /**
	 * 
	 */
        private static final long serialVersionUID = 3569390666867032553L;

        /**
         * Instantiates a new fuzz conversation action.
         */
        public FuzzConversationAction() {
            putValue(NAME, "Use as fuzz template");
            putValue(SHORT_DESCRIPTION, "Loads this request into the Fuzzer");
            putValue("CONVERSATION", null);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        public void actionPerformed(ActionEvent e) {
            Object o = getValue("CONVERSATION");
            if (o == null || !(o instanceof ConversationID))
                return;
            ConversationID id = (ConversationID) o;
            _fuzzer.loadTemplateFromConversation(id);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.AbstractAction#putValue(java.lang.String,
         * java.lang.Object)
         */
        public void putValue(String key, Object value) {
            super.putValue(key, value);
            if (key != null && key.equals("CONVERSATION")) {
                if (value != null && value instanceof ConversationID) {
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
            }
        }

    }
}
