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

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving framework events. The class that is
 * interested in processing a framework event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addFrameworkListener<code> method. When
 * the framework event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see FrameworkEvent
 */
public interface FrameworkListener extends java.util.EventListener {

    /**
     * Cookie added.
     * 
     * @param evt
     *            the evt
     */
    void cookieAdded(FrameworkEvent evt);

    /**
     * Cookie removed.
     * 
     * @param evt
     *            the evt
     */
    void cookieRemoved(FrameworkEvent evt);

    /**
     * Cookies changed.
     */
    void cookiesChanged();

    /**
     * Conversation property changed.
     * 
     * @param evt
     *            the evt
     */
    void conversationPropertyChanged(FrameworkEvent evt);

    /**
     * Url property changed.
     * 
     * @param evt
     *            the evt
     */
    void urlPropertyChanged(FrameworkEvent evt);

}
