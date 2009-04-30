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

package edu.lnmiit.wavd.model;

import java.util.EventObject;

// TODO: Auto-generated Javadoc
/**
 * The Class ConversationEvent.
 */
public class ConversationEvent extends EventObject {

    /**
     * 
     */
    private static final long serialVersionUID = -703071094507504012L;

    /** The _id. */
    private ConversationID _id;

    /** The _position. */
    private int _position;

    /**
     * Instantiates a new conversation event.
     * 
     * @param source
     *            the source
     * @param id
     *            the id
     * @param position
     *            the position
     */
    public ConversationEvent(Object source, ConversationID id, int position) {
        super(source);
        _id = id;
        _position = position;
    }

    /**
     * Gets the conversation id.
     * 
     * @return the conversation id
     */
    public ConversationID getConversationID() {
        return _id;
    }

    /**
     * Gets the position.
     * 
     * @return the position
     */
    public int getPosition() {
        return _position;
    }

}
