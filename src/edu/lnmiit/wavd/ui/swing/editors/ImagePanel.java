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
 * HexEditor.java
 *
 * Created on November 4, 2003, 8:23 AM
 */

package edu.lnmiit.wavd.ui.swing.editors;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

// TODO: Auto-generated Javadoc
/**
 * The Class ImagePanel.
 */
public class ImagePanel extends javax.swing.JPanel implements ByteArrayEditor {

    /**
     * 
     */
    private static final long serialVersionUID = 5283319836792407989L;

    /** The _data. */
    private byte[] _data = new byte[0];

    /**
     * Instantiates a new image panel.
     */
    public ImagePanel() {
        initComponents();
        setName("Image");
    }

    /**
     * Gets the content types.
     * 
     * @return the content types
     */
    public String[] getContentTypes() {
        return new String[] { "image/.*" };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        return _data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setBytes(java.lang.String
     * , byte[])
     */
    public void setBytes(String contentType, byte[] bytes) {
        imageLabel.setIcon(null);
        if (bytes != null && bytes.length > 0) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                Image image = ImageIO.read(bais);
                if (image != null) {
                    imageLabel.setIcon(new ImageIcon(image));
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageLabel.setIcon(null);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#isModified()
     */
    public boolean isModified() {
        return false;
    }

    /**
     * Inits the components.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        imageScrollPane = new javax.swing.JScrollPane();
        imageLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        imageScrollPane.setViewportView(imageLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(imageScrollPane, gridBagConstraints);

    }// GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The image label. */
    private javax.swing.JLabel imageLabel;

    /** The image scroll pane. */
    private javax.swing.JScrollPane imageScrollPane;

    // End of variables declaration//GEN-END:variables

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        byte[] content = new byte[0];
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream("/usr/share/xfce/backdrops/Flower.jpg");
            byte[] buff = new byte[1024];
            int got = 0;
            while ((got = fis.read(buff)) > 0) {
                baos.write(buff, 0, got);
            }
            content = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        javax.swing.JFrame top = new javax.swing.JFrame("Image Panel");
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        ImagePanel ip = new ImagePanel();
        top.getContentPane().add(ip);
        top.setBounds(100, 100, 600, 400);
        try {
            ip.setBytes(null, content);
            ip.setEditable(false);
            top.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
