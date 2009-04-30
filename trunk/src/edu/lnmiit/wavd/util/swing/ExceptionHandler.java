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

import java.awt.Component;
import java.awt.Frame;

import java.io.PrintStream;

// TODO: Auto-generated Javadoc
/**
 * The Class ExceptionHandler.
 */
public class ExceptionHandler extends javax.swing.JDialog {
    
    /** The _parent. */
    private static Frame _parent = null;
    
    /** The _disabled. */
    private static boolean _disabled = false;
    
    /**
     * Instantiates a new exception handler.
     */
    public ExceptionHandler() {
        super(_parent, true);
        initComponents();
        getRootPane().setDefaultButton(okButton);
    }
    
    /**
     * Sets the parent component.
     * 
     * @param component the new parent component
     */
    public static void setParentComponent(Component component) {
        _parent = null;
        while (component != null && component.getParent() != null) 
            component = component.getParent();
        if (component != null && component instanceof Frame) 
            _parent = (Frame) component;
    }
    
    /**
     * Handle.
     * 
     * @param t the t
     */
    public void handle(Throwable t) {
        System.setProperty("sun.awt.exception.handler", "");
        t.printStackTrace();
        
        if (_disabled) 
            return;
        DocumentOutputStream dos = new DocumentOutputStream();
        t.printStackTrace(new PrintStream(dos));
        exceptionTextArea.setDocument(dos.getDocument());
        
        setVisible(true);
        
        if (!disableCheckBox.isSelected()) {
            System.setProperty("sun.awt.exception.handler", this.getClass().getName());
        } else {
            _disabled = true;
        }
    }
    
    /**
     * Inits the components.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane1 = new javax.swing.JScrollPane();
        exceptionTextArea = new javax.swing.JTextArea();
        disableCheckBox = new javax.swing.JCheckBox();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 300));
        exceptionTextArea.setBackground(new java.awt.Color(204, 204, 204));
        exceptionTextArea.setEditable(false);
        jScrollPane1.setViewportView(exceptionTextArea);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        disableCheckBox.setMnemonic('S');
        disableCheckBox.setText("Stop displaying exceptions");
        getContentPane().add(disableCheckBox, java.awt.BorderLayout.NORTH);

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        getContentPane().add(okButton, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    /**
     * Ok button action performed.
     * 
     * @param evt the evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String args[]) {
        System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                throw new RuntimeException("blah");
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The disable check box. */
    private javax.swing.JCheckBox disableCheckBox;
    
    /** The exception text area. */
    private javax.swing.JTextArea exceptionTextArea;
    
    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;
    
    /** The ok button. */
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    
}
