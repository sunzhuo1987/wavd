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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

// TODO: Auto-generated Javadoc
/**
 * The Class LogInputStream.
 */

public class LogInputStream extends FilterInputStream {

    /** The _ps. */
    private PrintStream _ps;

    /**
     * Instantiates a new log input stream.
     * 
     * @param is
     *            the is
     * @param ps
     *            the ps
     */
    public LogInputStream(InputStream is, PrintStream ps) {
        super(is);
        if (is == null) {
            throw new NullPointerException("InputStream may not be null!");
        }
        _ps = ps;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.FilterInputStream#read()
     */
    public int read() throws IOException {
        int b = super.read();
        if (b > -1) {
            _ps.write(b);
            _ps.flush();
        } else {
            // we close to signal downstream readers that the inputstream has
            // closed
            _ps.close();
        }
        return b;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.FilterInputStream#read(byte[], int, int)
     */
    public int read(byte[] b, int off, int len) throws IOException {
        int num = super.read(b, off, len);
        if (num > 0) {
            _ps.write(b, off, num);
            _ps.flush();
        } else {
            _ps.close();
        }
        return num;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.FilterInputStream#markSupported()
     */
    public boolean markSupported() {
        return false;
    }
}
