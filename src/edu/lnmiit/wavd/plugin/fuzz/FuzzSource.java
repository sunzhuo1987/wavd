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

package edu.lnmiit.wavd.plugin.fuzz;

// TODO: Auto-generated Javadoc
/**
 * The Interface FuzzSource.
 */
public interface FuzzSource {

    /**
     * Gets the description.
     * 
     * @return the description
     */
    String getDescription();

    /**
     * Size.
     * 
     * @return the int
     */
    int size();

    /**
     * Reset.
     */
    void reset();

    /**
     * Checks for next.
     * 
     * @return true, if successful
     */
    boolean hasNext();

    /**
     * Current.
     * 
     * @return the object
     */
    Object current();

    /**
     * Increment.
     */
    void increment();

    /**
     * New instance.
     * 
     * @return the fuzz source
     */
    FuzzSource newInstance();

}
