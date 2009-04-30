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
 * ConversationRenderer.java
 *
 * Created on 19 October 2004, 09:27
 */

package edu.lnmiit.wavd.ui.swing;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ConversationRenderer.
 */
public class ConversationRenderer extends JLabel implements ListCellRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 3583700653798989536L;
    /** The _conversation model. */
    private ConversationModel _conversationModel;

    /**
     * Instantiates a new conversation renderer.
     * 
     * @param conversationModel
     *            the conversation model
     */
    public ConversationRenderer(ConversationModel conversationModel) {
        _conversationModel = conversationModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
     * .JList, java.lang.Object, int, boolean, boolean)
     */
    public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        // Get the selected index. (The index param isn't
        // always valid, so just use the value.)
        ConversationID id = (ConversationID) value;

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (id == null) {
            setText("");
            return this;
        }
        if (_conversationModel == null) {
            setText(id.toString());
            return this;
        }
        StringBuffer text = new StringBuffer();
        text.append(id).append(" - ");
        text.append(_conversationModel.getRequestMethod(id)).append(" ");
        text.append(_conversationModel.getRequestUrl(id).getSHPP()).append("    ");
        text.append(_conversationModel.getResponseStatus(id));
        setText(text.toString());

        return this;
    }

}
