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

package edu.lnmiit.wavd.ui.swing;

import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.FilteredConversationModel;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.HttpUrl;

// TODO: Auto-generated Javadoc
/**
 * The Class UrlFilteredConversationModel.
 */
public class UrlFilteredConversationModel extends FilteredConversationModel {
    
    /** The _model. */
    private ConversationModel _model;
    
    /** The _url. */
    private HttpUrl _url = null;
    
    /**
     * Instantiates a new url filtered conversation model.
     * 
     * @param model the model
     * @param cmodel the cmodel
     */
    public UrlFilteredConversationModel(FrameworkModel model, ConversationModel cmodel) {
        super(model, cmodel);
        _model = cmodel;
    }
    
    /**
     * Sets the url.
     * 
     * @param url the new url
     */
    public void setUrl(HttpUrl url) {
        if (url == _url) {
            return;
        } else if (_url == null && url != null || _url != null && url == null || !_url.equals(url)) {
            _url = url;
            updateConversations();
        }
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.FilteredConversationModel#shouldFilter(edu.lnmiit.wavd.model.ConversationID)
     */
    public boolean shouldFilter(ConversationID id) {
        if (_url == null) {
            return false;
        } else {
            return ! _url.equals(_model.getRequestUrl(id));
        }
    }
    
}
