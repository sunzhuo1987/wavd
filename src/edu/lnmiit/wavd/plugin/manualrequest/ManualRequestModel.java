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

package edu.lnmiit.wavd.plugin.manualrequest;

import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.Cookie;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.plugin.AbstractPluginModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ManualRequestModel.
 */
public class ManualRequestModel extends AbstractPluginModel {

    /** The _model. */
    private FrameworkModel _model;

    /**
     * Instantiates a new manual request model.
     * 
     * @param model
     *            the model
     */
    public ManualRequestModel(FrameworkModel model) {
        _model = model;
    }

    /**
     * Gets the conversation model.
     * 
     * @return the conversation model
     */
    public ConversationModel getConversationModel() {
        return _model.getConversationModel();
    }

    /**
     * Gets the cookies for url.
     * 
     * @param url
     *            the url
     * 
     * @return the cookies for url
     */
    public Cookie[] getCookiesForUrl(HttpUrl url) {
        return _model.getCookiesForUrl(url);
    }

    /**
     * Adds the cookie.
     * 
     * @param cookie
     *            the cookie
     */
    public void addCookie(Cookie cookie) {
        _model.addCookie(cookie);
    }

}
