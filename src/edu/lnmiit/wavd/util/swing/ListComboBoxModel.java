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
 * ListComboBoxModel.java
 *
 * Created on October 1, 2003, 11:15 PM
 */

package edu.lnmiit.wavd.util.swing;

import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

// TODO: Auto-generated Javadoc
/**
 * The Class ListComboBoxModel.
 */
public class ListComboBoxModel extends AbstractListModel implements ComboBoxModel {

    /**
     * 
     */
    private static final long serialVersionUID = 7326798886363501487L;

    /** The _list. */
    ListModel _list;

    /** The _selected. */
    Object _selected = null;

    /** The _logger. */
    Logger _logger = Logger.getLogger(this.getClass().getName());

    /**
     * Instantiates a new list combo box model.
     * 
     * @param list
     *            the list
     */
    public ListComboBoxModel(ListModel list) {
        _list = list;
        _list.addListDataListener(new MyListener());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        return _list.getElementAt(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    public Object getSelectedItem() {
        return _selected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return _list.getSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    public void setSelectedItem(Object anItem) {
        if (_selected == null && anItem == null)
            return;
        if (_selected == null && anItem != null || _selected != null && anItem == null || !_selected.equals(anItem)) {
            _selected = anItem;
            fireContentsChanged(this, -1, -1);
        }

    }

    /**
     * The listener interface for receiving my events. The class that is
     * interested in processing a my event implements this interface, and the
     * object created with that class is registered with a component using the
     * component's <code>addMyListener<code> method. When
     * the my event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see MyEvent
     */
    private class MyListener implements ListDataListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.ListDataListener#contentsChanged(javax.swing.event
         * .ListDataEvent)
         */
        public void contentsChanged(ListDataEvent e) {
            fireContentsChanged(ListComboBoxModel.this, e.getIndex0(), e.getIndex1());
            setSelectedItem(null);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.ListDataListener#intervalAdded(javax.swing.event
         * .ListDataEvent)
         */
        public void intervalAdded(ListDataEvent e) {
            fireIntervalAdded(ListComboBoxModel.this, e.getIndex0(), e.getIndex1());
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event
         * .ListDataEvent)
         */
        public void intervalRemoved(ListDataEvent e) {
            fireIntervalRemoved(ListComboBoxModel.this, e.getIndex0(), e.getIndex1());
            // we should notify listeners if the selected item has been removed
            if (_selected == null)
                return;
            int size = getSize();
            for (int i = 0; i < size; i++) {
                Object item = getElementAt(i);
                if (item != null && item.equals(_selected))
                    return;
            }
            // we haven't found it, it's been removed
            setSelectedItem(null);
        }

    }

    /**
     * The main method.
     * 
     * @param argList
     *            the arguments
     */
    public static void main(String[] argList) {
        javax.swing.JFrame top = new javax.swing.JFrame("ListComboBoxTest");
        final javax.swing.DefaultListModel dlm = new javax.swing.DefaultListModel();
        final ListComboBoxModel lcbm = new ListComboBoxModel(dlm);
        lcbm.addListDataListener(new ListDataListener() {
            public void intervalRemoved(ListDataEvent evt) {
                System.err.println("Interval Removed : " + evt);
            }

            public void intervalAdded(ListDataEvent evt) {
                System.err.println("Interval Added : " + evt);
            }

            public void contentsChanged(ListDataEvent evt) {
                System.err.println("ContentsChanged: " + evt);
            }
        });
        dlm.addElement("a");
        dlm.addElement("b");
        dlm.addElement("c");
        dlm.addElement("d");
        javax.swing.JComboBox jcb = new javax.swing.JComboBox(lcbm);
        jcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.err.println("Event : " + evt.paramString());
            }
        });
        top.getContentPane().setLayout(new java.awt.BorderLayout());
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
        top.getContentPane().add(jcb, java.awt.BorderLayout.NORTH);
        javax.swing.JButton clear = new javax.swing.JButton("CLEAR");
        top.getContentPane().add(clear, java.awt.BorderLayout.SOUTH);
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlm.clear();
                System.err.println("DLM size = " + dlm.size());
                System.err.println("Selected item = " + lcbm.getSelectedItem());
            }
        });
        javax.swing.JButton select = new javax.swing.JButton("SELECT");
        top.getContentPane().add(select, java.awt.BorderLayout.WEST);
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lcbm.setSelectedItem(lcbm.getElementAt(0));
                System.err.println("Selected " + lcbm.getSelectedItem());
            }
        });
        // top.setBounds(100,100,600,400);
        top.pack();
        top.setVisible(true);

    }

}
