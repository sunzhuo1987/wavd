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

package edu.lnmiit.wavd.plugin;

import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

// TODO: Auto-generated Javadoc
/**
 * The Class ScriptableConversation.
 */
public class ScriptableConversation {

    /** The _request. */
    private Request _request;

    /** The _response. */
    private Response _response;

    /** The _origin. */
    private String _origin;

    /** The _cancelled. */
    private boolean _cancelled = false;

    /** The _analyse. */
    private boolean _analyse = true;

    /**
     * Instantiates a new scriptable conversation.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param origin
     *            the origin
     */
    public ScriptableConversation(Request request, Response response, String origin) {
        _request = request;
        _response = response;
        _origin = origin;
    }

    /**
     * Gets the request.
     * 
     * @return the request
     */
    public Request getRequest() {
        return new Request(_request); // protective copy
    }

    /**
     * Gets the response.
     * 
     * @return the response
     */
    public Response getResponse() {
        return new Response(_response); // protective copy
    }

    /**
     * Gets the origin.
     * 
     * @return the origin
     */
    public String getOrigin() {
        return _origin;
    }

    /**
     * Sets the cancelled.
     * 
     * @param cancelled
     *            the new cancelled
     */
    public void setCancelled(boolean cancelled) {
        _cancelled = cancelled;
    }

    /**
     * Checks if is cancelled.
     * 
     * @return true, if is cancelled
     */
    public boolean isCancelled() {
        return _cancelled;
    }

    /**
     * Sets the analyse.
     * 
     * @param analyse
     *            the new analyse
     */
    public void setAnalyse(boolean analyse) {
        _analyse = analyse;
    }

    /**
     * Should analyse.
     * 
     * @return true, if successful
     */
    public boolean shouldAnalyse() {
        return _analyse;
    }

}
