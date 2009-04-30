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

import java.util.Date;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import EDU.oswego.cs.dl.util.concurrent.Sync;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractConversationModel.
 */
public abstract class AbstractConversationModel implements ConversationModel {

    /** The _model. */
    private FrameworkModel _model;

    /** The _listener list. */
    private EventListenerList _listenerList = new EventListenerList();

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new abstract conversation model.
     * 
     * @param model
     *            the model
     */
    public AbstractConversationModel(FrameworkModel model) {
        _model = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.ConversationModel#getConversationCount()
     */
    public abstract int getConversationCount();

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.ConversationModel#getConversationAt(int)
     */
    public abstract ConversationID getConversationAt(int index);

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#getIndexOfConversation(edu.lnmiit
     * .wavd.model.ConversationID)
     */
    public abstract int getIndexOfConversation(ConversationID id);

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.model.ConversationModel#readLock()
     */
    public abstract Sync readLock();

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#getConversationOrigin(edu.lnmiit
     * .wavd.model.ConversationID)
     */
    public String getConversationOrigin(ConversationID id) {
        return _model.getConversationOrigin(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#getConversationDate(edu.lnmiit
     * .wavd.model.ConversationID)
     */
    public Date getConversationDate(ConversationID id) {
        return _model.getConversationDate(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#getRequestMethod(edu.lnmiit.wavd
     * .model.ConversationID)
     */
    public String getRequestMethod(ConversationID id) {
        return _model.getRequestMethod(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#getResponseStatus(edu.lnmiit.
     * wavd.model.ConversationID)
     */
    public String getResponseStatus(ConversationID id) {
        return _model.getResponseStatus(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#getRequestUrl(edu.lnmiit.wavd
     * .model.ConversationID)
     */
    public HttpUrl getRequestUrl(ConversationID id) {
        return _model.getRequestUrl(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#getRequest(edu.lnmiit.wavd.model
     * .ConversationID)
     */
    public Request getRequest(ConversationID id) {
        return _model.getRequest(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#getResponse(edu.lnmiit.wavd.model
     * .ConversationID)
     */
    public Response getResponse(ConversationID id) {
        return _model.getResponse(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#removeConversationListener(edu
     * .lnmiit.wavd.model.ConversationListener)
     */
    public void removeConversationListener(ConversationListener listener) {
        synchronized (_listenerList) {
            _listenerList.remove(ConversationListener.class, listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.model.ConversationModel#addConversationListener(edu.lnmiit
     * .wavd.model.ConversationListener)
     */
    public void addConversationListener(ConversationListener listener) {
        synchronized (_listenerList) {
            _listenerList.add(ConversationListener.class, listener);
        }
    }

    /**
     * Fire conversation added.
     * 
     * @param id
     *            the id
     * @param position
     *            the position
     */
    protected void fireConversationAdded(ConversationID id, int position) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        ConversationEvent evt = new ConversationEvent(this, id, position);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ConversationListener.class) {
                try {
                    ((ConversationListener) listeners[i + 1]).conversationAdded(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire conversation removed.
     * 
     * @param id
     *            the id
     * @param position
     *            the position
     */
    protected void fireConversationRemoved(ConversationID id, int position) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        ConversationEvent evt = new ConversationEvent(this, id, position);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ConversationListener.class) {
                try {
                    ((ConversationListener) listeners[i + 1]).conversationRemoved(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire conversation changed.
     * 
     * @param id
     *            the id
     * @param position
     *            the position
     */
    protected void fireConversationChanged(ConversationID id, int position) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        ConversationEvent evt = new ConversationEvent(this, id, position);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ConversationListener.class) {
                try {
                    ((ConversationListener) listeners[i + 1]).conversationChanged(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire conversations changed.
     */
    protected void fireConversationsChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ConversationListener.class) {
                try {
                    ((ConversationListener) listeners[i + 1]).conversationsChanged();
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

}
