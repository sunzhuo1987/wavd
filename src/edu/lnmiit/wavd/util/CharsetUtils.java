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
package edu.lnmiit.wavd.util;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

// TODO: Auto-generated Javadoc
/**
 * The Class CharsetUtils.
 */
public class CharsetUtils {

    /**
     * Gets the charset.
     * 
     * @param bytes
     *            the bytes
     * 
     * @return the charset
     */
    public static String getCharset(byte[] bytes) {
        nsDetector det = new nsDetector(nsPSMDetector.ALL);
        CharsetListener listener = new CharsetListener();
        det.Init(listener);

        boolean isAscii = det.isAscii(bytes, bytes.length);
        // DoIt if non-ascii and not done yet.
        if (!isAscii)
            det.DoIt(bytes, bytes.length, false);
        det.DataEnd();
        if (isAscii)
            return "ASCII";

        return listener.getCharset();
    }

    /**
     * The listener interface for receiving charset events. The class that is
     * interested in processing a charset event implements this interface, and
     * the object created with that class is registered with a component using
     * the component's <code>addCharsetListener<code> method. When
     * the charset event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see CharsetEvent
     */
    private static class CharsetListener implements nsICharsetDetectionObserver {

        /** The charset. */
        private String charset = null;

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.mozilla.intl.chardet.nsICharsetDetectionObserver#Notify(java.
         * lang.String)
         */
        public void Notify(String charset) {
            this.charset = charset;
        }

        /**
         * Gets the charset.
         * 
         * @return the charset
         */
        public String getCharset() {
            return this.charset;
        }

    }

}
