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
 * The Class FuzzerEvent.
 */
public class FuzzerEvent {

    /** The Constant FUZZHEADER_ADDED. */
    public final static int FUZZHEADER_ADDED = 101;

    /** The Constant FUZZHEADER_CHANGED. */
    public final static int FUZZHEADER_CHANGED = 102;

    /** The Constant FUZZHEADER_REMOVED. */
    public final static int FUZZHEADER_REMOVED = 103;

    /** The Constant FUZZPARAMETER_ADDED. */
    public final static int FUZZPARAMETER_ADDED = 104;

    /** The Constant FUZZPARAMETER_CHANGED. */
    public final static int FUZZPARAMETER_CHANGED = 105;

    /** The Constant FUZZPARAMETER_REMOVED. */
    public final static int FUZZPARAMETER_REMOVED = 106;

    /** The _type. */
    private int _type;

    /** The _row. */
    private int _row;

    /**
     * Instantiates a new fuzzer event.
     * 
     * @param source
     *            the source
     * @param eventType
     *            the event type
     * @param row
     *            the row
     */
    public FuzzerEvent(Object source, int eventType, int row) {
        _type = eventType;
        _row = row;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public int getType() {
        return _type;
    }

    /**
     * Gets the row.
     * 
     * @return the row
     */
    public int getRow() {
        return _row;
    }

}
