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
 * FrameCache.java
 *
 * Created on May 13, 2004, 11:57 AM
 */

package edu.lnmiit.wavd.ui.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class FrameCache.
 */
public class FrameCache {

    /** The _cache. */
    private static Map _cache = new HashMap();

    /** The _listener. */
    private static WindowAdapter _listener = new WindowAdapter() {
        public void windowClosing(WindowEvent evt) {
            JFrame frame = (JFrame) evt.getSource();
            removeFrame(frame.getTitle());
        }
    };

    /**
     * Instantiates a new frame cache.
     */
    private FrameCache() {
    }

    /**
     * Gets the frame.
     * 
     * @param title
     *            the title
     * 
     * @return the frame
     */
    public static JFrame getFrame(String title) {
        synchronized (_cache) {
            return (JFrame) _cache.get(title);
        }
    }

    /**
     * Adds the frame.
     * 
     * @param title
     *            the title
     * @param frame
     *            the frame
     * 
     * @return the j frame
     */
    public static JFrame addFrame(String title, JFrame frame) {
        synchronized (_cache) {
            frame.addWindowListener(_listener);
            return (JFrame) _cache.put(title, frame);
        }
    }

    /**
     * Removes the frame.
     * 
     * @param title
     *            the title
     * 
     * @return the j frame
     */
    public static JFrame removeFrame(String title) {
        synchronized (_cache) {
            JFrame frame = (JFrame) _cache.remove(title);
            frame.removeWindowListener(_listener);
            return frame;
        }
    }

}
