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
 * MultiLineCellRenderer.java
 *
 * Created on 09 December 2004, 11:26
 */

package edu.lnmiit.wavd.util.swing;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiLineCellRenderer.
 */
public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer, ListCellRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 9134634545982038001L;

    /**
     * Instantiates a new multi line cell renderer.
     */
    public MultiLineCellRenderer() {
        setOpaque(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
     * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setFont(table.getFont());
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column)) {
                setForeground(UIManager.getColor("Table.focusCellForeground"));
                setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            setBorder(new EmptyBorder(1, 2, 1, 2));
        }
        setText((value == null) ? "" : value.toString());

        // FIXME : this is not the entire solution, but is good enough for the
        // moment
        // fails when resizing to smaller than the text width, if we are using
        // line wrapping
        int height_wanted = (int) getPreferredSize().getHeight();
        if (height_wanted > table.getRowHeight(row))
            table.setRowHeight(row, height_wanted);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
     * .JList, java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        if (isSelected) {
            setForeground(list.getSelectionForeground());
            setBackground(list.getSelectionBackground());
        } else {
            setForeground(list.getForeground());
            setBackground(list.getBackground());
        }
        setFont(list.getFont());
        if (cellHasFocus) {
            setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
            /*
             * if (list.isCellEditable(row, column)) { setForeground(
             * UIManager.getColor("List.focusCellForeground") ); setBackground(
             * UIManager.getColor("List.focusCellBackground") ); }
             */
        } else {
            // setBorder(new EmptyBorder(1, 2, 1, 2));
            setBorder(new javax.swing.border.LineBorder(java.awt.Color.LIGHT_GRAY));
        }
        setText((value == null) ? "" : value.toString());

        /*
         * // FIXME : this is not the entire solution, but is good enough for
         * the moment // fails when resizing to smaller than the text width, if
         * we are using line wrapping int height_wanted =
         * (int)getPreferredSize().getHeight(); if (height_wanted >
         * list.getRowHeight(row)) table.setRowHeight(row, height_wanted);
         */

        return this;
    }

}
