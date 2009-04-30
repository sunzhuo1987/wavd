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

import java.util.List;
import java.util.ArrayList;

import javax.swing.JSplitPane;


import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import edu.lnmiit.wavd.model.Message;
import edu.lnmiit.wavd.model.MultiPartContent;
import edu.lnmiit.wavd.ui.swing.MessagePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiPartPanel.
 */
public class MultiPartPanel extends javax.swing.JPanel implements ByteArrayEditor {
    
    /** The _data. */
    private byte[] _data = null;
    
    /** The _modified. */
    private boolean _modified = false;
    
    /** The _editable. */
    private boolean _editable = false;
    
    /** The _boundary. */
    private byte[] _boundary = null;
    
    /** The _parts. */
    private List _parts = new ArrayList();
    
    /** The _content. */
    private MultiPartContent _content = null;
    
    /** The _parts list. */
    private PartsListModel _partsList = new PartsListModel();
    
    /** The _selected. */
    private int _selected = -1;
    
    /** The _mp. */
    private MessagePanel _mp = null;
    
    /**
     * Instantiates a new multi part panel.
     */
    public MultiPartPanel() {
        initComponents();
        setName("MultiPart");
        
        _mp = new MessagePanel(JSplitPane.HORIZONTAL_SPLIT);
        contentPanel.add(_mp);
        
        partList.setModel(_partsList);
        partList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        partList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	    public void valueChanged(ListSelectionEvent evt) {
                if (evt.getValueIsAdjusting()) return;
                if (_editable && _mp.isModified() && _selected > -1) {
                    _modified = true;
                    Message message = _mp.getMessage();
                    _content.set(_selected, message);
                    _partsList.fireContentsChanged(_selected, _selected);
                }
                _selected = partList.getSelectedIndex();
                Message message = _content.get(_selected);
                _mp.setMessage(message);
                invalidate();
                revalidate();
            }
        });
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        _mp.setEditable(editable);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setBytes(java.lang.String, byte[])
     */
    public void setBytes(String contentType, byte[] bytes) {
        int size = 0;
        if (_content != null) {
            size = _content.size();
        }
        _modified = false;
        _data = bytes;
        _content = new MultiPartContent(contentType, bytes);
        if (size>0) {
            _partsList.fireIntervalRemoved(0, size-1);
        }
        if (_content.size()>0) {
            _partsList.fireIntervalAdded(0, _content.size()-1);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#isModified()
     */
    public boolean isModified() {
        return (_editable && (_modified || _mp.isModified()));
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        if (_editable && isModified()) {
            if (_mp.isModified()) {
                _content.set(_selected, _mp.getMessage());
            }
            _data = _content.getBytes();
            _modified = false;
        }
        return _data;
    }
    
    /**
     * Inits the components.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        partList = new javax.swing.JList();
        contentPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(100, 22));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(100, 131));
        jScrollPane1.setViewportView(partList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        contentPanel.setLayout(new java.awt.BorderLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(contentPanel, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The content panel. */
    private javax.swing.JPanel contentPanel;
    
    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;
    
    /** The part list. */
    private javax.swing.JList partList;
    // End of variables declaration//GEN-END:variables
    
    /**
     * The Class PartsListModel.
     */
    private class PartsListModel extends AbstractListModel {
        
        /* (non-Javadoc)
         * @see javax.swing.ListModel#getElementAt(int)
         */
        public Object getElementAt(int index) {
            return _content.getPartName(index);
        }
        
        /* (non-Javadoc)
         * @see javax.swing.ListModel#getSize()
         */
        public int getSize() {
            if (_content == null) return 0;
            return _content.size();
        }
        
        /**
         * Fire interval added.
         * 
         * @param index0 the index0
         * @param index1 the index1
         */
        public void fireIntervalAdded(int index0, int index1) {
            super.fireIntervalAdded(PartsListModel.this, index0, index1);
        }
        
        /**
         * Fire interval removed.
         * 
         * @param index0 the index0
         * @param index1 the index1
         */
        public void fireIntervalRemoved(int index0, int index1) {
            super.fireIntervalRemoved(PartsListModel.this, index0, index1);
        }
        
        /**
         * Fire contents changed.
         * 
         * @param index0 the index0
         * @param index1 the index1
         */
        public void fireContentsChanged(int index0, int index1) {
            super.fireContentsChanged(PartsListModel.this, index0, index1);
        }
        
    }
    
}
