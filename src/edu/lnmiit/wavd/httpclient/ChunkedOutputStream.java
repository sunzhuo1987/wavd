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

import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class ChunkedOutputStream.
 */
public class ChunkedOutputStream extends FilterOutputStream {
    
    /** The _trailer. */
    String[][] _trailer = null;
    
    /** The _write trailer. */
    boolean _writeTrailer = true;
    
    /**
     * Instantiates a new chunked output stream.
     * 
     * @param out the out
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public ChunkedOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    /**
     * Sets the trailer.
     * 
     * @param trailer the new trailer
     */
    public void setTrailer(String[][] trailer) {
        _trailer = trailer;
    }
    
    /**
     * Write trailer.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeTrailer() throws IOException {
        if (!_writeTrailer) return; // we've already written it
        out.write("0\r\n".getBytes());
        if (_trailer != null) {
            for (int i=0; i<_trailer.length; i++) {
                if (_trailer[i].length == 2) {
                    out.write((_trailer[i][0] + ": " + _trailer[i][1] + "\r\n").getBytes());
                }
            }
        }
        out.write("\r\n".getBytes());
        _writeTrailer = false;
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(int)
     */
    public void write(int b) throws IOException {
        out.write("1\r\n".getBytes());
        out.write(b);
        out.write("\r\n".getBytes());
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[])
     */
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[], int, int)
     */
    public void write(byte[] b, int off, int len) throws IOException {
        out.write((Integer.toString(len - off, 16) + "\r\n").getBytes());
        out.write(b, off, len);
        out.write("\r\n".getBytes());
    }
    
}
