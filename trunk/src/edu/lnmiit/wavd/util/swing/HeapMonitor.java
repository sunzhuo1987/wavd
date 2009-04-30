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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// TODO: Auto-generated Javadoc
/**
 * The Class HeapMonitor.
 */
public class HeapMonitor extends JButton implements ActionListener {

    /** The free. */
    private long free;

    /** The max. */
    private long max;

    /** The Constant MB. */
    private static final long MB = 1024l * 1024l;

    /** The timer. */
    private Timer timer;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2794077275357170746L;

    /**
     * Instantiates a new heap monitor.
     */
    public HeapMonitor() {
        max = Runtime.getRuntime().maxMemory();
        update();

        timer = new Timer(15000, this);
        timer.start();

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gc();
            }
        });
    }

    /**
     * Gc.
     */
    private void gc() {
        new Thread("Garbage collector") {
            public void run() {
                System.out.println("Running gc");
                System.gc();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        update();
                    }
                });
            }
        }.start();
    }

    /**
     * Update.
     */
    private void update() {
        free = max + Runtime.getRuntime().freeMemory() - Runtime.getRuntime().totalMemory();
        String label = "Used " + toMB(max - free) + " of " + toMB(max) + "MB";
        setOpaque(true);
        double ratio = (double) (max - free) / (double) max;
        if (ratio > 0.9f) {
            setBackground(Color.RED);
        } else if (ratio > 0.75f) {
            setBackground(Color.YELLOW);
        } else {
            setBackground(Color.GREEN);
        }
        setText(label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        update();
    }

    /**
     * To mb.
     * 
     * @param bytes
     *            the bytes
     * 
     * @return the string
     */
    public static String toMB(long bytes) {
        String s = Double.toString((double) bytes / MB);
        int dot = s.indexOf(".");
        if (dot > 0 && dot < s.length() - 2)
            s = s.substring(0, dot + 3);
        return s;
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     * 
     * @throws Exception
     *             the exception
     */
    public static void main(String[] args) throws Exception {
        javax.swing.JFrame frame = new javax.swing.JFrame("Heapspace");
        frame.getContentPane().add(new HeapMonitor());
        frame.pack();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        // do something to allocate memory
        byte[][] bytes = new byte[1024][];
        for (int i = 0; i < 1024; i++) {
            bytes[i] = new byte[65535];
            java.util.Arrays.fill(bytes[i], (byte) 1);
            Thread.sleep(10);
        }
        System.exit(0);
    }

}
