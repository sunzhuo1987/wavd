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
 * ChunkedInputStream.java
 *
 * Created on May 25, 2003, 11:00 AM
 */

package edu.lnmiit.wavd.httpclient;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.io.InputStream;
import java.io.FilterInputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class ChunkedInputStream.
 */
public class ChunkedInputStream extends FilterInputStream {
    
    /** The chunk. */
    byte[] chunk = null;
    
    /** The start. */
    int start = 0;
    
    /** The size. */
    int size = 0;
    
    /** The _trailer. */
    String[][] _trailer = null;
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(this.getClass().getName());
    
    /**
     * Instantiates a new chunked input stream.
     * 
     * @param in the in
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public ChunkedInputStream(InputStream in) throws IOException {
        super(in);
        readChunk();
    }
    
    /**
     * Gets the trailer.
     * 
     * @return the trailer
     */
    public String[][] getTrailer() {
        return _trailer;
    }
    
    /**
     * Read chunk.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void readChunk() throws IOException {
        String line = readLine().trim();
        try {
            size = Integer.parseInt(line.trim(),16);
            _logger.finest("Expecting a chunk of " + size + " bytes");
            chunk = new byte[size];
            int read = 0;
            while (read < size) {
                int got = in.read(chunk,read, Math.min(1024,size-read));
                _logger.finest("read " + got + " bytes");
                if (got>0) {
                    read = read + got;
                } else if (read == 0) {
                    _logger.info("read 0 bytes from the input stream! Huh!?");
                } else {
                    _logger.info("No more bytes to read from the stream, read " + read + " of " + size);
                    continue;
                }
            }
            _logger.finest("Got " + size + " bytes");
            if (size == 0) { // read the trailer and the CRLF
                readTrailer();
            } else {
                readLine(); // read the trailing line feed after the chunk body, but before the next chunk size
            }
            start = 0;
        } catch (NumberFormatException nfe) {
            _logger.severe("Error parsing chunk size from '" + line + "' : " + nfe);
        }
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#read()
     */
    public int read() throws IOException {
        if (size == 0) {
            return -1;
        }
        if (start == size) {
            readChunk();
        }
        if (size == 0) {
            return -1;
        }
        return chunk[start++];
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#read(byte[])
     */
    public int read(byte[] b) throws IOException {
        return read(b,0,b.length);
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#read(byte[], int, int)
     */
    public int read(byte[] b, int off, int len) throws IOException {
        if (size == 0) {
            return -1;
        }
        if (start == size) {
            readChunk();
        }
        if (size == 0) {
            return -1;
        }
        if (len - off < available()) {
        } else {
            len = available();
        }
        System.arraycopy(chunk, start, b, off, len);
        start += len;
        return len;
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#available()
     */
    public int available() throws IOException {
        return size - start;
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterInputStream#markSupported()
     */
    public boolean markSupported() {
        return false;
    }
    
    /**
     * Read line.
     * 
     * @return the string
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String readLine() throws IOException {
        String line = new String();
        int i;
        byte[] b={(byte)0x00};
        i = in.read();
        while (i > -1 && i != 10 && i != 13) {
            // Convert the int to a byte
            // we use an array because we can't concat a single byte :-(
            b[0] = (byte)(i & 0xFF);
            String input = new String(b,0,1);
            line = line.concat(input);
            i = in.read();
        }
        if (i == 13) { // 10 is unix LF, but DOS does 13+10, so read the 10 if we got 13
            i = in.read();
        }
        _logger.finest("Read '" + line + "'");
        return line;
    }
    
    /**
     * Read trailer.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void readTrailer() throws IOException {
        String line = readLine();
        ArrayList trailer = new ArrayList();
        while (!line.equals("")) {
            String[] pair = line.split(": *",2);
            if (pair.length == 2) {
                trailer.add(pair);
            }
            line = readLine();
        }
        if (trailer.size()>0) {
            _trailer = new String[trailer.size()][2];
            for (int i=0; i<trailer.size(); i++) {
                String[] pair = (String[]) trailer.get(i);
                _trailer[i][0] = pair[0];
                _trailer[i][1] = pair[1];
            }
        }
    }
}
