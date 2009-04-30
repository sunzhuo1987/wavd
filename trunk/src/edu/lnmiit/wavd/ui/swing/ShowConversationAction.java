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
 * ShowConversationAction.java
 *
 * Created on August 24, 2004, 11:07 PM
 */

package edu.lnmiit.wavd.ui.swing;

import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

import javax.swing.JFrame;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowConversationAction.
 */
public class ShowConversationAction extends AbstractAction {
    
    /** The _model. */
    private ConversationModel _model;
    
    /**
     * Instantiates a new show conversation action.
     * 
     * @param model the model
     */
    public ShowConversationAction(ConversationModel model) {
        _model = model;
        putValue(NAME, "Show conversation");
        putValue(SHORT_DESCRIPTION, "Opens a new window showing the request and response");
        putValue("CONVERSATION", null);
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object o = getValue("CONVERSATION");
        if (o == null || ! (o instanceof ConversationID)) return;
        ConversationID id = (ConversationID) o;
        ConversationPanel cp = new ConversationPanel(_model);
        cp.setSelectedConversation(id);
        JFrame frame = cp.inFrame();
        frame.setVisible(true);
        frame.toFront();
        frame.requestFocus();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.AbstractAction#putValue(java.lang.String, java.lang.Object)
     */
    public void putValue(String key, Object value) {
        super.putValue(key, value);
        if (key != null && key.equals("CONVERSATION")) {
            if (value != null && value instanceof ConversationID) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }
    
}
