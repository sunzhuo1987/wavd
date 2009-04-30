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

import java.util.regex.Pattern;

import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.FilteredConversationModel;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.HttpUrl;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageFilteredConversationModel.
 */
public class ImageFilteredConversationModel extends FilteredConversationModel {

    /** The _model. */
    private ConversationModel _model;
    
    /** The pattern. */
    private Pattern pattern = Pattern.compile("^.*\\.(gif|jpg|png|axd\\?.*|style)$");
    
    /** The filter images. */
    private boolean filterImages = true;

    /**
     * Instantiates a new image filtered conversation model.
     * 
     * @param model the model
     * @param cmodel the cmodel
     */
    public ImageFilteredConversationModel(FrameworkModel model, ConversationModel cmodel) {
        super(model, cmodel);
        _model = cmodel;
    }

    /**
     * Sets the filter images.
     * 
     * @param filter the new filter images
     */
    public void setFilterImages(boolean filter) {
        if ( filter != filterImages) {
            filterImages = filter;
            updateConversations();
        }
    }

    /**
     * Gets the filter images.
     * 
     * @return the filter images
     */
    public boolean getFilterImages() {
        return filterImages;
    }

    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.model.FilteredConversationModel#shouldFilter(edu.lnmiit.wavd.model.ConversationID)
     */
    public boolean shouldFilter(ConversationID id) {
        if (! filterImages) {
            return false;
        } else {
            HttpUrl url = _model.getRequestUrl(id);
            boolean result = pattern.matcher(url.toString()).matches();
            System.out.println("Result for " + url + " : " + result);
            return result;
        }
    }

}
