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
 * ReentrantReaderPreferenceReadWriteLock.java
 *
 * Created on September 8, 2004, 7:38 AM
 */

package edu.lnmiit.wavd.util;

import java.util.Iterator;

import EDU.oswego.cs.dl.util.concurrent.ReentrantWriterPreferenceReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.Sync;

// TODO: Auto-generated Javadoc
/**
 * The Class ReentrantReaderPreferenceReadWriteLock.
 */
public class ReentrantReaderPreferenceReadWriteLock extends ReentrantWriterPreferenceReadWriteLock {

    /** The _write lock. */
    private Sync _writeLock;

    /**
     * Instantiates a new reentrant reader preference read write lock.
     */
    public ReentrantReaderPreferenceReadWriteLock() {
        super();
        _writeLock = new LoggingLock(super.writeLock());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * EDU.oswego.cs.dl.util.concurrent.ReentrantWriterPreferenceReadWriteLock
     * #allowReader()
     */
    protected boolean allowReader() {
        return activeWriter_ == null || activeWriter_ == Thread.currentThread();
    }

    /**
     * Debug.
     */
    public void debug() {
        Iterator it = readers_.keySet().iterator();
        System.err.println("Readers:");
        while (it.hasNext()) {
            Object key = it.next();
            Object value = readers_.get(key);
            System.err.println(key + " : " + value);
        }
        System.err.println("Done");
        System.err.println("Writer thread:");
        System.err.println(activeWriter_.getName());
        System.err.println("Stack Trace:");
        Thread.dumpStack();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock#writeLock
     * ()
     */
    public EDU.oswego.cs.dl.util.concurrent.Sync writeLock() {
        return _writeLock;
    }

    /**
     * The Class LoggingLock.
     */
    private class LoggingLock implements Sync {

        /** The _sync. */
        private Sync _sync;

        /**
         * Instantiates a new logging lock.
         * 
         * @param sync
         *            the sync
         */
        public LoggingLock(Sync sync) {
            _sync = sync;
        }

        /*
         * (non-Javadoc)
         * 
         * @see EDU.oswego.cs.dl.util.concurrent.Sync#acquire()
         */
        public void acquire() throws InterruptedException {
            // System.err.println(Thread.currentThread().getName() +
            // " acquiring");
            while (!_sync.attempt(5000)) {
                debug();
            }
            // System.err.println(Thread.currentThread().getName() +
            // " acquired");
        }

        /*
         * (non-Javadoc)
         * 
         * @see EDU.oswego.cs.dl.util.concurrent.Sync#attempt(long)
         */
        public boolean attempt(long msecs) throws InterruptedException {
            // System.err.println(Thread.currentThread().getName() +
            // " attempting");
            try {
                boolean result = _sync.attempt(msecs);
                if (result) {
                    // System.err.println(Thread.currentThread().getName() +
                    // " successful");
                } else {
                    System.err.println(Thread.currentThread().getName() + "sync attempt unsuccessful");
                }
                return result;
            } catch (InterruptedException ie) {
                System.err.println(Thread.currentThread().getName() + " interrupted");
                throw ie;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see EDU.oswego.cs.dl.util.concurrent.Sync#release()
         */
        public void release() {
            // System.err.println(Thread.currentThread().getName() +
            // " releasing");
            _sync.release();
            // System.err.println(Thread.currentThread().getName() +
            // " released");
        }

    }
}
