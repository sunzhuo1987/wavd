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

import java.util.EventListener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving conversation events. The class that is
 * interested in processing a conversation event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addConversationListener<code> method. When
 * the conversation event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ConversationEvent
 */
public interface ConversationListener extends EventListener {

    /**
     * Conversation added.
     * 
     * @param evt
     *            the evt
     */
    void conversationAdded(ConversationEvent evt);

    /**
     * Conversation changed.
     * 
     * @param evt
     *            the evt
     */
    void conversationChanged(ConversationEvent evt);

    /**
     * Conversation removed.
     * 
     * @param evt
     *            the evt
     */
    void conversationRemoved(ConversationEvent evt);

    /**
     * Conversations changed.
     */
    void conversationsChanged();

}
