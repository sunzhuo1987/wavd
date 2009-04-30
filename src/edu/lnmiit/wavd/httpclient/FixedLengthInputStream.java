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
 * FixedLengthInputStream.java
 *
 * Created on May 12, 2003, 11:10 PM
 */

package edu.lnmiit.wavd.httpclient;

import java.io.IOException;
import java.io.InputStream;

import java.io.FilterInputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class FixedLengthInputStream.
 */
public class FixedLengthInputStream extends FilterInputStream {
    
    /** The max. */
    private int max;
    
    /** The read. */
    private int read = 0;
    
    /** The mark. */
    private int mark = 0;
    
    /** The closed. */
    private boolean closed = false;
    
    /**
     * Instantiates a new fixed length input stream.
     * 
     * @param is the is
     * @param max the max
     */
    public FixedLengthInputStream(InputStream is, int max) {
        super(is);
        this.max=max;
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#available()
     */
    public int available() throws IOException {
        if (closed) {
            throw new IOException("available called on closed stream");
        }
        int canread = max - read;
        int available = super.available();
        if (canread > available) {
            available = canread;
        }
        return available;
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#close()
     */
    public void close() {
        closed = true;
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#mark(int)
     */
    public void mark(int readlimit) {
        super.mark(readlimit);
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#markSupported()
     */
    public boolean markSupported() {
        return super.markSupported();
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#read()
     */
    public int read() throws IOException {
        if (closed) {
            throw new IOException("read called on closed stream");
        }
        int canread = max - read;
        if (canread < 1) {
            canread = 0;
        }
        if (canread>0) {
            int b = super.read();
            if (b > -1) {
                read++;
            }
            return b;
        } else {
            return -1;
        }
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#read(byte[])
     */
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#read(byte[], int, int)
     */
    public int read(byte[] b, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("read called on closed stream");
        }
        int canread = Math.min(len, max - read);
        if (canread>0) {
            int bytesRead = super.read(b,off,canread);
            if (bytesRead > -1) {
                read = read + bytesRead;
            }
            return bytesRead;
        } else {
            return -1;
        }
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#skip(long)
     */
    public long skip(long n) throws IOException {
        if (closed) {
            throw new IOException("skip called on closed stream");
        }
        int canread = max - read;
        if (n > canread) {
            n = canread;
        }
        if (n>0) {
            n = super.skip(n);
            read = read + (int) n;
        }
        return n;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.getClass().getName() + " on a " + super.in.getClass().getName() + " (" + read + " of " + max + ")";
    }
}
