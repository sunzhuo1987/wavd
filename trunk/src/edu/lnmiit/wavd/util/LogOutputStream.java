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
 * CopyInputStream.java
 *
 * Created on May 25, 2003, 10:59 AM
 */

package edu.lnmiit.wavd.util;

import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;

// TODO: Auto-generated Javadoc
/**
 * The Class LogOutputStream.
 */

public class LogOutputStream extends FilterOutputStream {
    
    /** The _os. */
    OutputStream _os;
    
    /** The _ps. */
    PrintStream _ps;
    
    /**
     * Instantiates a new log output stream.
     * 
     * @param os the os
     * @param ps the ps
     */
    public LogOutputStream(OutputStream os, PrintStream ps) {
        super(os);
        _os = os;
        _ps = ps;
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(int)
     */
    public void write(int b) throws IOException {
        _os.write(b);
        _ps.write(b);
    }
    
    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[], int, int)
     */
    public void write(byte b[], int off, int len) throws IOException {
        _os.write(b, off, len);
        _ps.write(b, off, len);
    }
    
}

