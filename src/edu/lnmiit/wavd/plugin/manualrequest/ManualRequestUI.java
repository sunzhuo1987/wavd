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
 * ManualRequestUI.java
 *
 * Created on August 8, 2004, 9:51 PM
 */

package edu.lnmiit.wavd.plugin.manualrequest;



import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;
import edu.lnmiit.wavd.plugin.PluginUI;

// TODO: Auto-generated Javadoc
/**
 * The Interface ManualRequestUI.
 */
public interface ManualRequestUI extends PluginUI {
    
    /**
     * Request changed.
     * 
     * @param request the request
     */
    void requestChanged(Request request);
    
    /**
     * Response changed.
     * 
     * @param response the response
     */
    void responseChanged(Response response);
    
}
