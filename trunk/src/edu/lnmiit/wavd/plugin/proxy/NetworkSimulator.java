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
 * NetworkSimulator.java
 *
 * Created on May 18, 2004, 9:18 PM
 */

package edu.lnmiit.wavd.plugin.proxy;

import java.util.Timer;
import java.util.TimerTask;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class NetworkSimulator.
 */
public class NetworkSimulator {
    
    /** The _timer. */
    private static Timer _timer = new Timer(true);
    
    /** The HZ. */
    private static int HZ = 10;
    
    /** The _shared. */
    private boolean _shared = false;
    
    /** The _read lock. */
    private Object _readLock = null;
    
    /** The _write lock. */
    private Object _writeLock = null;
    
    /** The _shared lock. */
    private Object _sharedLock = null;
    
    /** The _name. */
    private String _name;
    
    /** The _read bandwidth. */
    private int _readBandwidth = 0;
    
    /** The _write bandwidth. */
    private int _writeBandwidth = 0;
    
    /** The _shared bandwidth. */
    private int _sharedBandwidth = 0;
    
    /** The _read available. */
    private int _readAvailable = 0;
    
    /** The _write available. */
    private int _writeAvailable = 0;
    
    /** The _shared available. */
    private int _sharedAvailable = 0;
    
    /** The _latency. */
    private int _latency;
    
    /**
     * Instantiates a new network simulator.
     * 
     * @param name the name
     * @param latency the latency
     * @param readBandwidth the read bandwidth
     * @param writeBandwidth the write bandwidth
     */
    public NetworkSimulator(String name, int latency, int readBandwidth, int writeBandwidth) {
        _readLock = new Object();
        _writeLock = new Object();
        
        _name = name;
        _shared = false;
        _latency = latency;
        _readBandwidth = readBandwidth;
        _writeBandwidth = writeBandwidth;
        _timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                refreshBandwidth();
            }
        }, 0, 1000 / HZ);
    }
    
    /**
     * Instantiates a new network simulator.
     * 
     * @param name the name
     * @param latency the latency
     * @param sharedBandwidth the shared bandwidth
     */
    public NetworkSimulator(String name, int latency, int sharedBandwidth) {
        _sharedLock = new Object();
        
        _name = name;
        _shared = true;
        _latency = latency;
        _sharedBandwidth = sharedBandwidth;
        _timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                refreshBandwidth();
            }
        }, 0, 1000 / HZ);
    }
    
    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return _name;
    }
    
    /**
     * Refresh bandwidth.
     */
    private void refreshBandwidth() {
        if (_shared) {
            synchronized(_sharedLock) {
                _sharedAvailable = _sharedBandwidth / HZ;
                _sharedLock.notifyAll();
            }
        } else {
            synchronized(_readLock) {
                _readAvailable = _readBandwidth / HZ;
                _readLock.notifyAll();
            }
            synchronized(_writeLock) {
                _writeAvailable = _writeBandwidth / HZ;
                _writeLock.notifyAll();
            }
        }
    }
    
    /**
     * Reserve shared.
     * 
     * @param requested the requested
     * 
     * @return the int
     */
    private int reserveShared(int requested) {
        synchronized(_sharedLock) {
            if (requested == 0) {
                try {
                    _sharedLock.wait();
                } catch (InterruptedException ie) {}
                return 0;
            }
            while(_sharedAvailable == 0) {
                try {
                    _sharedLock.wait();
                } catch (InterruptedException ie) {}
            }
            int allocation = Math.min(requested, _sharedAvailable);
            _sharedAvailable -= allocation;
            return allocation;
        }
    }
    
    /**
     * Reserve read.
     * 
     * @param requested the requested
     * 
     * @return the int
     */
    private int reserveRead(int requested) {
        if (_shared) {
            return reserveShared(requested);
        }
        synchronized(_readLock) {
            if (requested == 0) {
                try {
                    _readLock.wait();
                } catch (InterruptedException ie) {}
                return 0;
            }
            while(_readAvailable == 0) {
                try {
                    _readLock.wait();
                } catch (InterruptedException ie) {}
            }
            int allocation = Math.min(requested, _readAvailable);
            _readAvailable -= allocation;
            return allocation;
        }
    }
    
    /**
     * Reserve write.
     * 
     * @param requested the requested
     * 
     * @return the int
     */
    private int reserveWrite(int requested) {
        if (_shared) {
            return reserveShared(requested);
        }
        synchronized(_writeLock) {
            if (requested == 0) {
                try {
                    _writeLock.wait();
                } catch (InterruptedException ie) {}
                return 0;
            }
            while(_writeAvailable == 0) {
                try {
                    _writeLock.wait();
                } catch (InterruptedException ie) {}
            }
            int allocation = Math.min(requested, _writeAvailable);
            _writeAvailable -= allocation;
            return allocation;
        }
    }
    
    /**
     * Wrap input stream.
     * 
     * @param in the in
     * 
     * @return the input stream
     */
    public InputStream wrapInputStream(InputStream in) {
        return new NetworkSimulator.ThrottledInputStream(in);
    }
    
    /**
     * Wrap output stream.
     * 
     * @param out the out
     * 
     * @return the output stream
     */
    public OutputStream wrapOutputStream(OutputStream out) {
        return new NetworkSimulator.ThrottledOutputStream(out);
    }
    
    /**
     * The Class ThrottledInputStream.
     */
    private class ThrottledInputStream extends FilterInputStream {
        
        /**
         * Instantiates a new throttled input stream.
         * 
         * @param in the in
         */
        public ThrottledInputStream(InputStream in) {
            super(in);
        }
        
        /* (non-Javadoc)
         * @see java.io.FilterInputStream#read()
         */
        public int read() throws IOException {
            int got = in.read();
            if (got < 0) return got;
            while (reserveRead(1)==0);
            return got;
        }
        
        /* (non-Javadoc)
         * @see java.io.FilterInputStream#read(byte[], int, int)
         */
        public int read(byte[] buff, int off, int len) throws IOException {
            int allocation = 0;
            int got = in.read(buff, off, len);
            while (allocation < got) {
                allocation = allocation + reserveRead(got - allocation);
            }
            return got;
        }
        
    }
    
    /**
     * The Class ThrottledOutputStream.
     */
    private class ThrottledOutputStream extends FilterOutputStream {
        
        /**
         * Instantiates a new throttled output stream.
         * 
         * @param out the out
         */
        public ThrottledOutputStream(OutputStream out) {
            super(out);
        }
        
        /**
         * Sleep.
         * 
         * @param period the period
         */
        private void sleep(long period) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException ie) {}
        }
        
        /* (non-Javadoc)
         * @see java.io.FilterOutputStream#write(int)
         */
        public void write(int b) throws IOException {
            long finish = System.currentTimeMillis() + _latency;
            while (reserveWrite(1)==0);
            out.write(b);
            long now = System.currentTimeMillis();
            if (now < finish) sleep(finish - now);
        }
        
        /* (non-Javadoc)
         * @see java.io.FilterOutputStream#write(byte[], int, int)
         */
        public void write(byte[] buff, int off, int len) throws IOException {
            long finish = System.currentTimeMillis() + _latency + (len/(_shared?_sharedBandwidth:_writeBandwidth));
            int allocation;
            while (len > 0) {
                allocation = reserveWrite(len);
                if (allocation > 0) {
                    out.write(buff, off, allocation);
                    off += allocation;
                    len -= allocation;
                }
            }
            long now = System.currentTimeMillis();
            if (now < finish) sleep(finish - now);
        }
        
    }
    
}
