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

package jcifs.util;

import java.io.PrintStream;

// TODO: Auto-generated Javadoc
/**
 * The Class LogStream.
 */

public class LogStream extends PrintStream {

    /** The inst. */
    private static LogStream inst;

    /** The level. */
    public static int level = 1;

    /**
     * Instantiates a new log stream.
     * 
     * @param stream the stream
     */
    public LogStream( PrintStream stream ) {
        super( stream );
    }

    /**
     * Sets the level.
     * 
     * @param level the new level
     */
    public static void setLevel( int level ) {
        LogStream.level = level;
    }
    
    /**
     * Sets the instance.
     * 
     * @param stream the new instance
     */
    public static void setInstance( PrintStream stream ) {
        inst = new LogStream( stream );
    }
    
    /**
     * Gets the single instance of LogStream.
     * 
     * @return single instance of LogStream
     */
    public static LogStream getInstance() {
        if( inst == null ) {
            setInstance( System.err );
        }
        return inst;
    }
}

