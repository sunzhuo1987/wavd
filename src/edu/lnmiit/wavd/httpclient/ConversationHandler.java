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

package edu.lnmiit.wavd.httpclient;

import java.io.IOException;

import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

// TODO: Auto-generated Javadoc
/**
 * The Interface ConversationHandler.
 */
public interface ConversationHandler {
    
    /**
     * Response received.
     * 
     * @param response the response
     */
    void responseReceived(Response response);
    
    /**
     * Request error.
     * 
     * @param request the request
     * @param ioe the ioe
     */
    void requestError(Request request, IOException ioe);
    
}
