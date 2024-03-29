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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiLineTreeCellRenderer.
 */
public class MultiLineTreeCellRenderer extends JPanel implements TreeCellRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 4440573854167074653L;

    /** The _icon. */
    protected JLabel _icon;

    /** The _text. */
    protected TreeTextArea _text;

    /**
     * Instantiates a new multi line tree cell renderer.
     */
    public MultiLineTreeCellRenderer() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        _icon = new JLabel() {
            /**
	     * 
	     */
            private static final long serialVersionUID = -3046508578357797549L;

            public void setBackground(Color color) {
                if (color instanceof ColorUIResource)
                    color = null;
                super.setBackground(color);
            }
        };
        add(_icon);
        add(Box.createHorizontalStrut(4));
        add(_text = new TreeTextArea());
        _text.setFont(new Font("Monospaced", 0, 12));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.
     * swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        _text.setText(stringValue);
        _text.setSelect(isSelected);
        _text.setFocus(hasFocus);
        if (leaf) {
            _icon.setIcon(UIManager.getIcon("Tree.leafIcon"));
        } else if (expanded) {
            _icon.setIcon(UIManager.getIcon("Tree.openIcon"));
        } else {
            _icon.setIcon(UIManager.getIcon("Tree.closedIcon"));
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        Dimension iconD = _icon.getPreferredSize();
        Dimension textD = _text.getPreferredSize();
        int height = iconD.height < textD.height ? textD.height : iconD.height;
        return new Dimension(iconD.width + textD.width, height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setBackground(java.awt.Color)
     */
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
            color = null;
        super.setBackground(color);
    }

    /**
     * The Class TreeTextArea.
     */
    class TreeTextArea extends JTextArea {

        /**
	 * 
	 */
        private static final long serialVersionUID = 876925864921923073L;
        /** The preferred size. */
        Dimension preferredSize;

        /**
         * Instantiates a new tree text area.
         */
        TreeTextArea() {
            setLineWrap(false);
            setWrapStyleWord(false);
            setOpaque(true);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.JComponent#setBackground(java.awt.Color)
         */
        public void setBackground(Color color) {
            if (color instanceof ColorUIResource)
                color = null;
            super.setBackground(color);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.text.JTextComponent#setText(java.lang.String)
         */
        public void setText(String str) {
            BufferedImage bufferedImage = new BufferedImage(2, 2, BufferedImage.TYPE_4BYTE_ABGR_PRE);
            Graphics2D g2d = (bufferedImage.createGraphics());
            FontRenderContext frc = g2d.getFontRenderContext();
            Font font = getFont();

            BufferedReader br = new BufferedReader(new StringReader(str));
            String line;
            double maxWidth = 0, maxHeight = 0;
            int lines = 0;
            try {
                while ((line = br.readLine()) != null) {
                    if (!line.equals("")) {
                        TextLayout tl = new TextLayout(line, font, frc);
                        maxWidth = Math.max(maxWidth, tl.getBounds().getWidth());
                        maxHeight = Math.max(maxHeight, tl.getBounds().getHeight());
                    }
                    lines++;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            lines = (lines < 1) ? 1 : lines;
            int height = (int) (maxHeight * lines * 1.35); // interline space?
            int width = (int) (maxWidth + 200); // ?
            setPreferredSize(new Dimension(width, height));
            super.setText(str);
        }

        /**
         * Sets the select.
         * 
         * @param isSelected
         *            the new select
         */
        void setSelect(boolean isSelected) {
            Color bColor;
            if (isSelected) {
                bColor = UIManager.getColor("Tree.selectionBackground");
            } else {
                bColor = UIManager.getColor("Tree.textBackground");
            }
            super.setBackground(bColor);
        }

        /**
         * Sets the focus.
         * 
         * @param hasFocus
         *            the new focus
         */
        void setFocus(boolean hasFocus) {
            if (hasFocus) {
                Color lineColor = UIManager.getColor("Tree.selectionBorderColor");
                setBorder(BorderFactory.createLineBorder(lineColor));
            } else {
                setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        }
    }
}
