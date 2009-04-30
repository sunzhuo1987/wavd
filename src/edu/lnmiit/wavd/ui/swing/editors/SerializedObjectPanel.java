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
 * SerializedObjectPanel.java
 *
 * Created on 16 November 2003, 05:03
 */

package edu.lnmiit.wavd.ui.swing.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

// TODO: Auto-generated Javadoc
/**
 * The Class SerializedObjectPanel.
 */
public class SerializedObjectPanel extends ObjectPanel implements ByteArrayEditor {

    /**
     * 
     */
    private static final long serialVersionUID = -3440451430852832556L;

    /** The _data. */
    private byte[] _data = new byte[0];

    /** The _editable. */
    private boolean _editable = false;

    /** The _error. */
    private boolean _error = false;

    /**
     * Instantiates a new serialized object panel.
     */
    public SerializedObjectPanel() {
        setName("Serialized Object");
    }

    /**
     * Gets the content types.
     * 
     * @return the content types
     */
    public String[] getContentTypes() {
        return new String[] { "application/x-serialized-object" };
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.editors.ObjectPanel#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        super.setEditable(editable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setBytes(java.lang.String
     * , byte[])
     */
    public void setBytes(String type, byte[] bytes) {
        _data = bytes;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            setObject(o);
            _error = false;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "IOException deserializing the byte stream : " + ioe, "IOException",
                    JOptionPane.ERROR_MESSAGE);
            _error = true;
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "Class not found while deserializing the byte stream : " + cnfe,
                    "Class not found", JOptionPane.ERROR_MESSAGE);
            _error = true;
        }
        super.setEditable(_editable && !_error);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        if (isModified()) {
            try {
                Object o = getObject();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(o);
                oos.flush();
                baos.flush();
                _data = baos.toByteArray();
            } catch (IOException ioe) {
                System.err.println("Error serialising the object : " + ioe);
                return null;
            }
        }
        return _data;
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        edu.lnmiit.wavd.model.Response response = new edu.lnmiit.wavd.model.Response();
        try {
            new java.io.ByteArrayOutputStream();
            String filename = "c:/temp/2-response";
            if (args.length == 1) {
                filename = args[0];
            }
            java.io.FileInputStream fis = new java.io.FileInputStream(filename);
            response.read(fis);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        javax.swing.JFrame top = new javax.swing.JFrame("Serialized Object Panel");
        top.getContentPane().setLayout(new java.awt.BorderLayout());
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        javax.swing.JButton button = new javax.swing.JButton("GET");
        final SerializedObjectPanel sop = new SerializedObjectPanel();
        top.getContentPane().add(sop);
        top.getContentPane().add(button, java.awt.BorderLayout.SOUTH);
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println(new String(sop.getBytes()));
            }
        });
        top.setBounds(100, 100, 600, 400);
        top.setVisible(true);
        try {
            sop.setEditable(false);
            sop.setBytes(null, response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
