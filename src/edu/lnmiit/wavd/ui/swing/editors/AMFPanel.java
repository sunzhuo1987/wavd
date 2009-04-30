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
package edu.lnmiit.wavd.ui.swing.editors;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.openamf.AMFBody;
import org.openamf.AMFHeader;
import org.openamf.AMFMessage;
import org.openamf.io.AMFDeserializer;

import flashgateway.io.ASObject;

// TODO: Auto-generated Javadoc
/**
 * The Class AMFPanel.
 */
public class AMFPanel extends JPanel implements ByteArrayEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The bytes data. */
    private byte[] bytesData = null;

    /** The info text pane. */
    private JTextPane infoTextPane = null;

    /** The amf tabbed pane. */
    private JTabbedPane amfTabbedPane = null;

    /** The info scroll pane. */
    private JScrollPane infoScrollPane = null;

    /** The data pane. */
    private JPanel dataPane = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.owasp.webscarab.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        return bytesData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.owasp.webscarab.ui.swing.editors.ByteArrayEditor#isModified()
     */
    public boolean isModified() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.owasp.webscarab.ui.swing.editors.ByteArrayEditor#setBytes(java.lang
     * .String, byte[])
     */
    public void setBytes(String contentType, byte[] bytes) {
        // contentType is always application/x-amf
        this.bytesData = bytes;
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(this.bytesData));
        try {
            AMFDeserializer deserializer = new AMFDeserializer(dis);
            AMFMessage message = deserializer.getAMFMessage();

            // Info panel
            StringBuffer sb = new StringBuffer();
            sb.append("Version: " + message.getVersion() + "\n");
            sb.append("Headers:" + message.getHeaderCount() + "\n");
            for (int i = 0; i < message.getHeaderCount(); i++) {
                AMFHeader header = message.getHeader(i);
                sb.append(header.toString() + "\n\n");
            }
            sb.append("Bodies: " + message.getBodyCount() + "\n");

            for (int i = 0; i < message.getBodyCount(); i++) {
                AMFBody messagebody = message.getBody(i);
                sb.append("Body : " + (i + 1) + "\n");
                sb.append("   Service Name: " + messagebody.getServiceName() + "\n");
                sb.append("   Method Name: " + messagebody.getServiceMethodName() + "\n");
                sb.append("   Response: " + messagebody.getResponse() + "\n");
                sb.append("   Target: " + messagebody.getTarget() + "\n");
                sb.append("   Type: " + AMFBody.getObjectTypeDescription(messagebody.getType()) + "\n");

            }
            this.getInfoTextPane().setText(sb.toString());

            // Data panel
            ObjectPanel oPanel = new ObjectPanel();
            if (message.getBodyCount() > 1) {
                HashMap bodies = new HashMap();
                for (int i = 0; i < message.getBodyCount(); i++) {
                    bodies.put("Body :" + (i + 1), message.getBody(i).getValue());

                    oPanel.setObject(bodies);
                }
            } else {
                oPanel.setObject(message.getBody(0).getValue());
            }

            this.getDataPane().add(oPanel, BorderLayout.CENTER);

            // Recordsets
            // TODO:Recordsets are found as Custom Class...need to chceck it.
            for (int i = 0; i < message.getBodyCount(); i++) {
                // recordset is an ASObject too which is a HashMap
                if (message.getBody(i).getValue() instanceof flashgateway.io.ASObject) {
                    ASObject object = (ASObject) message.getBody(i).getValue();
                    // check if it's a RecordSet
                    if (object.containsKey("serverinfo") && object.get("serverinfo") instanceof ASObject) {
                        ASObject rs = (ASObject) object.get("serverinfo");

                        if (rs.containsKey("cursor") && rs.containsKey("initialdata") && rs.containsKey("id")
                                && rs.containsKey("servicename") && rs.containsKey("totalcount")
                                && rs.containsKey("version") && rs.containsKey("columnnames")) {

                            // Looks like a recordset
                            this.addRecordsetPanel(rs, i + 1);
                        }
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds the recordset panel.
     * 
     * @param rs
     *            the rs
     * @param number
     *            the number
     */
    private void addRecordsetPanel(ASObject rs, int number) {
        TableModel model = new RecordsetTableModel(rs);
        JTable rsTable = new JTable(model);
        JScrollPane rsScroll = new JScrollPane(rsTable);
        getAmfTabbedPane().addTab("Recordset " + number, rsScroll);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.owasp.webscarab.ui.swing.editors.ByteArrayEditor#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
        // This panel is to render. We can't edit data here.
    }

    /**
     * Instantiates a new aMF panel.
     */
    public AMFPanel() {
        super();
        setName("AMF");
        initialize();
    }

    /**
     * Gets the content types.
     * 
     * @return the content types
     */
    public String[] getContentTypes() {
        return new String[] { "application/x-amf" };
    }

    /**
     * Initialize.
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.weightx = 1.0;
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.add(getAmfTabbedPane(), gridBagConstraints1);
    }

    /**
     * Gets the info text pane.
     * 
     * @return the info text pane
     */
    private JTextPane getInfoTextPane() {
        if (infoTextPane == null) {
            infoTextPane = new JTextPane();
        }
        return infoTextPane;
    }

    /**
     * Gets the amf tabbed pane.
     * 
     * @return the amf tabbed pane
     */
    private JTabbedPane getAmfTabbedPane() {
        if (amfTabbedPane == null) {
            amfTabbedPane = new JTabbedPane();
            amfTabbedPane.addTab("Info", null, getInfoScrollPane(), null);
            amfTabbedPane.addTab("Data", null, getDataPane(), null);
        }
        return amfTabbedPane;
    }

    /**
     * Gets the info scroll pane.
     * 
     * @return the info scroll pane
     */
    private JScrollPane getInfoScrollPane() {
        if (infoScrollPane == null) {
            infoScrollPane = new JScrollPane();
            infoScrollPane.setViewportView(getInfoTextPane());
        }
        return infoScrollPane;
    }

    /**
     * Gets the data pane.
     * 
     * @return the data pane
     */
    private JPanel getDataPane() {
        if (dataPane == null) {
            dataPane = new JPanel();
            dataPane.setLayout(new BorderLayout());
        }
        return dataPane;
    }

    /**
     * The Class RecordsetTableModel.
     */
    private class RecordsetTableModel extends AbstractTableModel {

        /**
	 * 
	 */
        private static final long serialVersionUID = -7823558629830081682L;

        /** The data. */
        private ArrayList data = null;

        /** The col names. */
        private ArrayList colNames = null;

        /**
         * Instantiates a new recordset table model.
         * 
         * @param rs
         *            the rs
         */
        public RecordsetTableModel(ASObject rs) {
            if (rs.get("columnnames") instanceof ArrayList) {
                if (rs.get("initialdata") instanceof ArrayList) {
                    ArrayList rows = (ArrayList) rs.get("initialdata");

                    boolean allOk = true;
                    Iterator it = rows.iterator();
                    while (it.hasNext()) {
                        Object row = it.next();
                        if (row instanceof ArrayList) {
                            new Vector((ArrayList) row);

                        } else {
                            allOk = false;
                            break;
                        }
                    }
                    if (allOk) {
                        this.data = rows;
                        this.colNames = (ArrayList) rs.get("columnnames");
                    }
                }
            }

        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            if (this.colNames == null) {
                return 0;
            } else {
                return this.colNames.size();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int no) {
            if (colNames == null) {
                return null;
            } else {
                return this.colNames.get(no).toString();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            if (data == null) {
                return 0;
            } else {
                return data.size();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowNo, int colNo) {
            ArrayList row = (ArrayList) data.get(rowNo);
            return row.get(colNo);
        }

    }

}
