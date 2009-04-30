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

import javax.swing.SwingUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class SwingWorker.
 */
public abstract class SwingWorker {

    /** The value. */
    private Object value; // see getValue(), setValue()

    /**
     * The Class ThreadVar.
     */
    private static class ThreadVar {

        /** The thread. */
        private Thread thread;

        /**
         * Instantiates a new thread var.
         * 
         * @param t
         *            the t
         */
        ThreadVar(Thread t) {
            thread = t;
        }

        /**
         * Gets the.
         * 
         * @return the thread
         */
        synchronized Thread get() {
            return thread;
        }

        /**
         * Clear.
         */
        synchronized void clear() {
            thread = null;
        }
    }

    /** The thread var. */
    private ThreadVar threadVar;

    /**
     * Gets the value.
     * 
     * @return the value
     */
    protected synchronized Object getValue() {
        return value;
    }

    /**
     * Sets the value.
     * 
     * @param x
     *            the new value
     */
    private synchronized void setValue(Object x) {
        value = x;
    }

    /**
     * Construct.
     * 
     * @return the object
     */
    public abstract Object construct();

    /**
     * Finished.
     */
    public void finished() {
    }

    /**
     * Interrupt.
     */
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }

    /**
     * Gets the.
     * 
     * @return the object
     */
    public Object get() {
        while (true) {
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }

    /**
     * Instantiates a new swing worker.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
            public void run() {
                finished();
            }
        };

        Runnable doConstruct = new Runnable() {
            public void run() {
                try {
                    setValue(construct());
                } finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    /**
     * Start.
     */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}
