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
 * ConversationTableModel.java
 *
 * Created on June 21, 2004, 6:05 PM
 */

package edu.lnmiit.wavd.ui.swing;

import edu.lnmiit.wavd.model.ConversationEvent;
import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationListener;
import edu.lnmiit.wavd.model.ConversationModel;

import javax.swing.AbstractListModel;
import javax.swing.SwingUtilities;

import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ConversationListModel.
 */
public class ConversationListModel extends AbstractListModel {
    
    /** The _model. */
    private ConversationModel _model = null;
    
    /** The _listener. */
    private Listener _listener = new Listener();
    
    /** The _size. */
    private int _size = 0;
    
    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /**
     * Instantiates a new conversation list model.
     * 
     * @param model the model
     */
    public ConversationListModel(ConversationModel model) {
        _model = model;
        _model.addConversationListener(_listener);
        fireContentsChanged(this, 0, getSize());
    }
    
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        return getConversationAt(index);
    }
    
    /**
     * Gets the index of conversation.
     * 
     * @param id the id
     * 
     * @return the index of conversation
     */
    public int getIndexOfConversation(ConversationID id) {
        return _model.getIndexOfConversation(id);
    }
    
    /**
     * Gets the conversation count.
     * 
     * @return the conversation count
     */
    public int getConversationCount() {
        return _model.getConversationCount();
    }
    
    /**
     * Gets the conversation at.
     * 
     * @param index the index
     * 
     * @return the conversation at
     */
    public ConversationID getConversationAt(int index) {
        return _model.getConversationAt(index);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        if (_model == null) return 0;
        _size = getConversationCount();
        return _size;
    }
    
    /**
     * Added conversation.
     * 
     * @param evt the evt
     */
    protected void addedConversation(ConversationEvent evt) {
        ConversationID id = evt.getConversationID();
        int row = getIndexOfConversation(id);
        if (row>-1) fireIntervalAdded(this, row, row);
    }
    
    /**
     * Changed conversation.
     * 
     * @param evt the evt
     */
    protected void changedConversation(ConversationEvent evt) {
        ConversationID id = evt.getConversationID();
        int row = getIndexOfConversation(id);
        if (row>-1) fireContentsChanged(this, row, row);
    }
    
    /**
     * Removed conversation.
     * 
     * @param evt the evt
     */
    protected void removedConversation(ConversationEvent evt) {
        ConversationID id = evt.getConversationID();
        int row = getIndexOfConversation(id);
        if (row>-1) fireIntervalRemoved(this, row, row);
    }
    
    /**
     * Conversations changed.
     */
    protected void conversationsChanged() {
        if (_size>0)
            fireIntervalRemoved(this, 0, _size);
        fireIntervalAdded(this, 0,  getSize());
    }
    
    /**
     * The Class Listener.
     */
    private class Listener implements ConversationListener {
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.model.ConversationListener#conversationAdded(edu.lnmiit.wavd.model.ConversationEvent)
         */
        public void conversationAdded(final ConversationEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                ConversationListModel.this.addedConversation(evt);
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            ConversationListModel.this.addedConversation(evt);
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception! " + e);
                }
            }
        }
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.model.ConversationListener#conversationChanged(edu.lnmiit.wavd.model.ConversationEvent)
         */
        public void conversationChanged(final ConversationEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                ConversationListModel.this.changedConversation(evt);
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            ConversationListModel.this.changedConversation(evt);
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception! " + e + " : " + e.getStackTrace()[0]);
                }
            }
        }
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.model.ConversationListener#conversationRemoved(edu.lnmiit.wavd.model.ConversationEvent)
         */
        public void conversationRemoved(final ConversationEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                ConversationListModel.this.removedConversation(evt);
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            ConversationListModel.this.removedConversation(evt);
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception! " + e);
                }
            }
        }
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.model.ConversationListener#conversationsChanged()
         */
        public void conversationsChanged() {
            if (SwingUtilities.isEventDispatchThread()) {
                ConversationListModel.this.conversationsChanged();
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            ConversationListModel.this.conversationsChanged();
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception! " + e + " : " + e.getStackTrace()[0]);
                    e.printStackTrace();
                }
            }
        }
        
    }
    
}
