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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import EDU.oswego.cs.dl.util.concurrent.Sync;
import edu.lnmiit.wavd.util.ReentrantReaderPreferenceReadWriteLock;

// TODO: Auto-generated Javadoc
/**
 * The Class FilteredConversationModel.
 */
public abstract class FilteredConversationModel extends AbstractConversationModel {

    /** The _model. */
    private ConversationModel _model;

    /** The _rwl. */
    private ReentrantReaderPreferenceReadWriteLock _rwl = new ReentrantReaderPreferenceReadWriteLock();

    // contains conversations that should be visible
    /** The _conversations. */
    private List _conversations = new ArrayList();

    /** The _logger. */
    // private Logger _logger = Logger.getLogger(getClass().toString());
    /**
     * Instantiates a new filtered conversation model.
     * 
     * @param model
     *            the model
     * @param cmodel
     *            the cmodel
     */
    public FilteredConversationModel(FrameworkModel model, ConversationModel cmodel) {
        super(model);
        _model = cmodel;
        _model.addConversationListener(new Listener());
        updateConversations();
    }

    /**
     * Update conversations.
     */
    protected void updateConversations() {
        try {
            _rwl.writeLock().acquire();
            _conversations.clear();
            int count = _model.getConversationCount();
            for (int i = 0; i < count; i++) {
                ConversationID id = _model.getConversationAt(i);
                if (!shouldFilter(id)) {
                    _conversations.add(id);
                }
            }
            _rwl.readLock().acquire();
            _rwl.writeLock().release();
            fireConversationsChanged();
            _rwl.readLock().release();
        } catch (InterruptedException ie) {
            // _logger.warning("Interrupted waiting for the read lock! " +
            // ie.getMessage());
        }
    }

    /**
     * Should filter.
     * 
     * @param id
     *            the id
     * 
     * @return true, if successful
     */
    public abstract boolean shouldFilter(ConversationID id);

    /**
     * Checks if is filtered.
     * 
     * @param id
     *            the id
     * 
     * @return true, if is filtered
     */
    protected boolean isFiltered(ConversationID id) {
        try {
            _rwl.readLock().acquire();
            return _conversations.indexOf(id) == -1;
        } catch (InterruptedException ie) {
            // _logger.warning("Interrupted waiting for the read lock! " +
            // ie.getMessage());
            return false;
        } finally {
            _rwl.readLock().release();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.AbstractConversationModel#getConversationAt(int)
     */
    public ConversationID getConversationAt(int index) {
        try {
            _rwl.readLock().acquire();
            return (ConversationID) _conversations.get(index);
        } catch (InterruptedException ie) {
            // _logger.warning("Interrupted waiting for the read lock! " +
            // ie.getMessage());
            return null;
        } finally {
            _rwl.readLock().release();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.AbstractConversationModel#getConversationCount()
     */
    public int getConversationCount() {
        try {
            _rwl.readLock().acquire();
            return _conversations.size();
        } catch (InterruptedException ie) {
            // _logger.warning("Interrupted waiting for the read lock! " +
            // ie.getMessage());
            return 0;
        } finally {
            _rwl.readLock().release();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.AbstractConversationModel#getIndexOfConversation
     * (edu.lnmiit.wavd.model.ConversationID)
     */
    public int getIndexOfConversation(ConversationID id) {
        try {
            _rwl.readLock().acquire();
            return Collections.binarySearch(_conversations, id);
        } catch (InterruptedException ie) {
            // _logger.warning("Interrupted waiting for the read lock! " +
            // ie.getMessage());
            return -1;
        } finally {
            _rwl.readLock().release();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.AbstractConversationModel#readLock()
     */
    public Sync readLock() {
        return _rwl.readLock();
    }

    /**
     * The Class Listener.
     */
    private class Listener implements ConversationListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.ConversationListener#conversationAdded(edu.
         * lnmiit.wavd.model.ConversationEvent)
         */
        public void conversationAdded(ConversationEvent evt) {
            ConversationID id = evt.getConversationID();
            if (!shouldFilter(id)) {
                try {
                    _rwl.writeLock().acquire();
                    int index = getIndexOfConversation(id);
                    if (index < 0) {
                        index = -index - 1;
                        _conversations.add(index, id);
                    }
                    _rwl.readLock().acquire();
                    _rwl.writeLock().release();
                    fireConversationAdded(id, index);
                    _rwl.readLock().release();
                } catch (InterruptedException ie) {
                    // _logger.warning("Interrupted waiting for the read lock! "
                    // + ie.getMessage());
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.ConversationListener#conversationChanged(edu
         * .lnmiit.wavd.model.ConversationEvent)
         */
        public void conversationChanged(ConversationEvent evt) {
            ConversationID id = evt.getConversationID();
            int index = getIndexOfConversation(id);
            if (shouldFilter(id)) {
                if (index > -1) {
                    try {
                        _rwl.writeLock().acquire();
                        _conversations.remove(index);
                        _rwl.readLock().acquire();
                        _rwl.writeLock().release();
                        fireConversationRemoved(id, index);
                        _rwl.readLock().release();
                    } catch (InterruptedException ie) {
                        // _logger.warning(
                        // "Interrupted waiting for the read lock! " +
                        // ie.getMessage());
                    }
                }
            } else {
                if (index < 0) {
                    index = -index - 1;
                    try {
                        _rwl.writeLock().acquire();
                        _conversations.add(index, id);
                        _rwl.readLock().acquire();
                        _rwl.writeLock().release();
                        fireConversationAdded(id, index);
                        _rwl.readLock().release();
                    } catch (InterruptedException ie) {
                        // _logger.warning(
                        // "Interrupted waiting for the read lock! " +
                        // ie.getMessage());
                    }
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.ConversationListener#conversationRemoved(edu
         * .lnmiit.wavd.model.ConversationEvent)
         */
        public void conversationRemoved(ConversationEvent evt) {
            ConversationID id = evt.getConversationID();
            int index = getIndexOfConversation(id);
            if (index > -1) {
                try {
                    _rwl.writeLock().acquire();
                    _conversations.remove(index);
                    _rwl.readLock().acquire();
                    _rwl.writeLock().release();
                    fireConversationRemoved(id, index);
                    _rwl.readLock().release();
                } catch (InterruptedException ie) {
                    // _logger.warning("Interrupted waiting for the read lock! "
                    // + ie.getMessage());
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.ConversationListener#conversationsChanged()
         */
        public void conversationsChanged() {
            updateConversations();
        }

    }

}
