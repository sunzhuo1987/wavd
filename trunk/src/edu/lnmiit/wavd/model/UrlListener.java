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
 * The listener interface for receiving url events. The class that is interested
 * in processing a url event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addUrlListener<code> method. When
 * the url event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see UrlEvent
 */
public interface UrlListener extends EventListener {

    /**
     * Url added.
     * 
     * @param evt
     *            the evt
     */
    void urlAdded(UrlEvent evt);

    /**
     * Url changed.
     * 
     * @param evt
     *            the evt
     */
    void urlChanged(UrlEvent evt);

    /**
     * Url removed.
     * 
     * @param evt
     *            the evt
     */
    void urlRemoved(UrlEvent evt);

    /**
     * Urls changed.
     */
    void urlsChanged();

}
