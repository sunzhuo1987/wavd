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

package edu.lnmiit.wavd.plugin.fuzz;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import EDU.oswego.cs.dl.util.concurrent.Sync;
import edu.lnmiit.wavd.model.AbstractConversationModel;
import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.FrameworkModel;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.model.NamedValue;
import edu.lnmiit.wavd.plugin.AbstractPluginModel;
import edu.lnmiit.wavd.util.ReentrantReaderPreferenceReadWriteLock;

// TODO: Auto-generated Javadoc
/**
 * The Class FuzzerModel.
 */
public class FuzzerModel extends AbstractPluginModel {

    /** The Constant PROPERTY_FUZZMETHOD. */
    public final static String PROPERTY_FUZZMETHOD = "FuzzMethod";

    /** The Constant PROPERTY_FUZZURL. */
    public final static String PROPERTY_FUZZURL = "FuzzUrl";

    /** The Constant PROPERTY_FUZZVERSION. */
    public final static String PROPERTY_FUZZVERSION = "FuzzVersion";

    /** The Constant PROPERTY_REQUESTINDEX. */
    public final static String PROPERTY_REQUESTINDEX = "RequestIndex";

    /** The Constant PROPERTY_TOTALREQUESTS. */
    public final static String PROPERTY_TOTALREQUESTS = "TotalRequests";

    /** The Constant PROPERTY_BUSYFUZZING. */
    public final static String PROPERTY_BUSYFUZZING = "BusyFuzzing";

    /** The _model. */
    private FrameworkModel _model = null;

    /** The _logger. */
    private Logger _logger = Logger.getLogger(getClass().getName());

    /** The _listener list. */
    private EventListenerList _listenerList = new EventListenerList();

    /** The _rwl. */
    private ReentrantReaderPreferenceReadWriteLock _rwl = new ReentrantReaderPreferenceReadWriteLock();

    /** The _conversation model. */
    private FuzzConversationModel _conversationModel;

    /** The _fuzz method. */
    private String _fuzzMethod = "GET";

    /** The _fuzz url. */
    private String _fuzzUrl = "http://localhost:8080/test";

    /** The _fuzz version. */
    private String _fuzzVersion = "HTTP/1.0";

    /** The _fuzz headers. */
    private List _fuzzHeaders = new ArrayList();

    /** The _fuzz parameters. */
    private List _fuzzParameters = new ArrayList();

    /** The _fuzz sources. */
    private List _fuzzSources = new ArrayList();

    /** The _parameter priorities. */
    private List _parameterPriorities = new ArrayList();

    /** The _max priority. */
    private int _maxPriority = 0;

    /** The _request index. */
    private int _requestIndex = 0;

    /** The _total requests. */
    private int _totalRequests = 0;

    /** The _busy fuzzing. */
    private boolean _busyFuzzing = false;

    /**
     * Instantiates a new fuzzer model.
     * 
     * @param model
     *            the model
     */
    public FuzzerModel(FrameworkModel model) {
        _model = model;
        _conversationModel = new FuzzConversationModel(model);
    }

    /**
     * Gets the conversation model.
     * 
     * @return the conversation model
     */
    public ConversationModel getConversationModel() {
        return _conversationModel;
    }

    /**
     * Adds the conversation.
     * 
     * @param id
     *            the id
     */
    public void addConversation(ConversationID id) {
        _conversationModel.addConversation(id);
    }

    /**
     * Sets the fuzz method.
     * 
     * @param method
     *            the new fuzz method
     */
    public void setFuzzMethod(String method) {
        Object old = _fuzzMethod;
        _fuzzMethod = method;
        if (old == null || _fuzzMethod != old)
            _changeSupport.firePropertyChange(PROPERTY_FUZZMETHOD, old, _fuzzMethod);
        resetFuzzer();
    }

    /**
     * Gets the fuzz method.
     * 
     * @return the fuzz method
     */
    public String getFuzzMethod() {
        return _fuzzMethod;
    }

    /**
     * Sets the fuzz url.
     * 
     * @param url
     *            the new fuzz url
     */
    public void setFuzzUrl(String url) {
        Object old = _fuzzUrl;
        _fuzzUrl = url;
        if (old == null || _fuzzUrl != old)
            _changeSupport.firePropertyChange(PROPERTY_FUZZURL, old, _fuzzUrl);
        resetFuzzer();
    }

    /**
     * Gets the fuzz url.
     * 
     * @return the fuzz url
     */
    public String getFuzzUrl() {
        return _fuzzUrl;
    }

    /**
     * Sets the fuzz version.
     * 
     * @param version
     *            the new fuzz version
     */
    public void setFuzzVersion(String version) {
        Object old = _fuzzVersion;
        _fuzzVersion = version;
        if (old == null || _fuzzVersion != old)
            _changeSupport.firePropertyChange(PROPERTY_FUZZVERSION, old, _fuzzVersion);
        resetFuzzer();
    }

    /**
     * Gets the fuzz version.
     * 
     * @return the fuzz version
     */
    public String getFuzzVersion() {
        return _fuzzVersion;
    }

    /**
     * Sets the busy fuzzing.
     * 
     * @param busy
     *            the new busy fuzzing
     */
    public void setBusyFuzzing(boolean busy) {
        boolean old = _busyFuzzing;
        _busyFuzzing = busy;
        if (_busyFuzzing != old)
            _changeSupport.firePropertyChange(PROPERTY_BUSYFUZZING, old, _busyFuzzing);
    }

    /**
     * Checks if is busy fuzzing.
     * 
     * @return true, if is busy fuzzing
     */
    public boolean isBusyFuzzing() {
        return _busyFuzzing;
    }

    /**
     * Gets the fuzz header count.
     * 
     * @return the fuzz header count
     */
    public int getFuzzHeaderCount() {
        return _fuzzHeaders.size();
    }

    /**
     * Adds the fuzz header.
     * 
     * @param index
     *            the index
     * @param header
     *            the header
     */
    public void addFuzzHeader(int index, NamedValue header) {
        _fuzzHeaders.add(index, header);
        fireFuzzHeaderAdded(index);
        resetFuzzer();
    }

    /**
     * Sets the fuzz header.
     * 
     * @param index
     *            the index
     * @param header
     *            the header
     */
    public void setFuzzHeader(int index, NamedValue header) {
        _fuzzHeaders.set(index, header);
        fireFuzzHeaderChanged(index);
        resetFuzzer();
    }

    /**
     * Removes the fuzz header.
     * 
     * @param index
     *            the index
     */
    public void removeFuzzHeader(int index) {
        _fuzzHeaders.remove(index);
        fireFuzzHeaderRemoved(index);
        resetFuzzer();
    }

    /**
     * Gets the fuzz header.
     * 
     * @param position
     *            the position
     * 
     * @return the fuzz header
     */
    public NamedValue getFuzzHeader(int position) {
        return (NamedValue) _fuzzHeaders.get(position);
    }

    /**
     * Gets the fuzz parameter count.
     * 
     * @return the fuzz parameter count
     */
    public int getFuzzParameterCount() {
        return _fuzzParameters.size();
    }

    /**
     * Adds the fuzz parameter.
     * 
     * @param index
     *            the index
     * @param parameter
     *            the parameter
     * @param fuzzSource
     *            the fuzz source
     * @param priority
     *            the priority
     */
    public void addFuzzParameter(int index, Parameter parameter, FuzzSource fuzzSource, int priority) {
        _logger.info("Adding a parameter at index " + index);
        _fuzzParameters.add(index, parameter);
        _fuzzSources.add(index, fuzzSource);
        _parameterPriorities.add(index, new Integer(priority));
        fireFuzzParameterAdded(index);
        resetFuzzer();
    }

    /**
     * Sets the fuzz parameter.
     * 
     * @param index
     *            the index
     * @param parameter
     *            the parameter
     * @param fuzzSource
     *            the fuzz source
     * @param priority
     *            the priority
     */
    public void setFuzzParameter(int index, Parameter parameter, FuzzSource fuzzSource, int priority) {
        _logger.info("Setting a parameter at index " + index + ", source is " + fuzzSource);
        _fuzzParameters.set(index, parameter);
        _fuzzSources.set(index, fuzzSource);
        _parameterPriorities.set(index, new Integer(priority));
        fireFuzzParameterChanged(index);
        resetFuzzer();
    }

    /**
     * Removes the fuzz parameter.
     * 
     * @param index
     *            the index
     */
    public void removeFuzzParameter(int index) {
        _logger.info("Removing parameter at index " + index);
        _fuzzParameters.remove(index);
        _fuzzSources.remove(index);
        _parameterPriorities.remove(index);
        fireFuzzParameterRemoved(index);
        resetFuzzer();
    }

    /**
     * Gets the fuzz parameter.
     * 
     * @param index
     *            the index
     * 
     * @return the fuzz parameter
     */
    public Parameter getFuzzParameter(int index) {
        return (Parameter) _fuzzParameters.get(index);
    }

    /**
     * Gets the parameter fuzz source.
     * 
     * @param index
     *            the index
     * 
     * @return the parameter fuzz source
     */
    public FuzzSource getParameterFuzzSource(int index) {
        return (FuzzSource) _fuzzSources.get(index);
    }

    /**
     * Gets the fuzz parameter priority.
     * 
     * @param index
     *            the index
     * 
     * @return the fuzz parameter priority
     */
    public int getFuzzParameterPriority(int index) {
        Integer p = (Integer) _parameterPriorities.get(index);
        if (p == null)
            return 0;
        return p.intValue();
    }

    /**
     * Gets the fuzz parameter value.
     * 
     * @param index
     *            the index
     * 
     * @return the fuzz parameter value
     */
    public Object getFuzzParameterValue(int index) {
        FuzzSource fuzzSource = getParameterFuzzSource(index);
        if (fuzzSource != null) {
            return fuzzSource.current();
        } else {
            return ((Parameter) _fuzzParameters.get(index)).getValue();
        }
    }

    /**
     * Reset fuzzer.
     */
    public void resetFuzzer() {
        Map sizes = new HashMap();
        _maxPriority = 0;
        int count = getFuzzParameterCount();
        for (int i = 0; i < count; i++) {
            FuzzSource source = getParameterFuzzSource(i);
            if (source != null) {
                source.reset();
                Integer priority = new Integer(getFuzzParameterPriority(i));
                _maxPriority = Math.max(priority.intValue(), _maxPriority);
                int size = source.size();
                Integer s = (Integer) sizes.get(priority);
                if (s == null) {
                    sizes.put(priority, new Integer(size));
                } else {
                    sizes.put(priority, new Integer(Math.min(s.intValue(), size)));
                }
            }
        }
        int totalsize = 1;
        Iterator it = sizes.values().iterator();
        while (it.hasNext()) {
            Integer size = (Integer) it.next();
            totalsize = totalsize * size.intValue();
        }
        setRequestIndex(0);
        setTotalRequests(totalsize);
        _conversationModel.clear();
    }

    /**
     * Increment fuzzer.
     * 
     * @return true, if successful
     */
    public boolean incrementFuzzer() {
        boolean success = false;
        int count = getFuzzParameterCount();
        for (int priority = 0; priority <= _maxPriority; priority++) {
            // make sure we can increment ALL the sources at the current
            // priority
            // set success = true if so
            for (int param = 0; param < count; param++) {
                FuzzSource source = getParameterFuzzSource(param);
                if (source == null)
                    continue; // nothing to do for this param
                int paramPriority = getFuzzParameterPriority(param);
                if (paramPriority == priority) { // we need to increment this
                    // one
                    if (source.hasNext()) {
                        source.increment();
                        success = true;
                    } else {
                        success = false;
                        break;
                    }
                }
            }
            if (success) {
                setRequestIndex(getRequestIndex() + 1);
                return true;
            } else {
                // no success, reset all parameters <= current priority, we'll
                // go around again, and increment the next priority level
                for (int param = 0; param < count; param++) {
                    FuzzSource source = getParameterFuzzSource(param);
                    if (source == null)
                        continue; // nothing to do for this param
                    int paramPriority = getFuzzParameterPriority(param);
                    if (paramPriority <= priority) {
                        source.reset();
                    }
                }
            }
        }
        // we have gone through all the permutations, no more to do
        setRequestIndex(getTotalRequests());
        return false;
    }

    /**
     * Sets the request index.
     * 
     * @param index
     *            the new request index
     */
    private void setRequestIndex(int index) {
        int old = _requestIndex;
        _requestIndex = index;
        if (_requestIndex != old)
            _changeSupport.firePropertyChange(PROPERTY_REQUESTINDEX, old, _requestIndex);
    }

    /**
     * Gets the request index.
     * 
     * @return the request index
     */
    public int getRequestIndex() {
        return _requestIndex;
    }

    /**
     * Sets the total requests.
     * 
     * @param count
     *            the new total requests
     */
    private void setTotalRequests(int count) {
        int old = _totalRequests;
        _totalRequests = count;
        if (_totalRequests != old)
            _changeSupport.firePropertyChange(PROPERTY_TOTALREQUESTS, old, _totalRequests);
    }

    /**
     * Gets the total requests.
     * 
     * @return the total requests
     */
    public int getTotalRequests() {
        return _totalRequests;
    }

    /**
     * Adds the signature.
     * 
     * @param signature
     *            the signature
     */
    public void addSignature(Signature signature) {
        HttpUrl url = signature.getUrl();
        _model.addUrlProperty(url, "SIGNATURE", signature.toString());
    }

    /**
     * Gets the signature count.
     * 
     * @param url
     *            the url
     * 
     * @return the signature count
     */
    public int getSignatureCount(HttpUrl url) {
        String[] signatures = _model.getUrlProperties(url, "SIGNATURE");
        if (signatures == null)
            return 0;
        return signatures.length;
    }

    /**
     * Gets the signature.
     * 
     * @param url
     *            the url
     * @param index
     *            the index
     * 
     * @return the signature
     */
    public Signature getSignature(HttpUrl url, int index) {
        String[] signatures = _model.getUrlProperties(url, "SIGNATURE");
        if (signatures == null)
            return null;
        try {
            return new Signature(signatures[index]);
        } catch (MalformedURLException mue) {
            _logger.severe("Malformed URL reading a signature! " + mue.getMessage());
            return null;
        }
    }

    /**
     * Adds the checksum.
     * 
     * @param url
     *            the url
     * @param checksum
     *            the checksum
     */
    public void addChecksum(HttpUrl url, String checksum) {
        _model.addUrlProperty(url, "CHECKSUM", checksum);
    }

    /**
     * Gets the checksum count.
     * 
     * @param url
     *            the url
     * 
     * @return the checksum count
     */
    public int getChecksumCount(HttpUrl url) {
        String[] checksums = _model.getUrlProperties(url, "CHECKSUM");
        if (checksums == null)
            return 0;
        return checksums.length;
    }

    /**
     * Gets the checksum.
     * 
     * @param url
     *            the url
     * @param index
     *            the index
     * 
     * @return the checksum
     */
    public String getChecksum(HttpUrl url, int index) {
        String[] checksums = _model.getUrlProperties(url, "CHECKSUM");
        if (checksums == null)
            return null;
        return checksums[index];
    }

    /**
     * Adds the model listener.
     * 
     * @param listener
     *            the listener
     */
    public void addModelListener(FuzzerListener listener) {
        _listenerList.add(FuzzerListener.class, listener);
    }

    /**
     * Removes the model listener.
     * 
     * @param listener
     *            the listener
     */
    public void removeModelListener(FuzzerListener listener) {
        _listenerList.remove(FuzzerListener.class, listener);
    }

    /**
     * Fire fuzz header added.
     * 
     * @param index
     *            the index
     */
    protected void fireFuzzHeaderAdded(int index) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FuzzerEvent evt = new FuzzerEvent(this, FuzzerEvent.FUZZHEADER_ADDED, index);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FuzzerListener.class) {
                try {
                    ((FuzzerListener) listeners[i + 1]).fuzzHeaderAdded(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire fuzz header changed.
     * 
     * @param index
     *            the index
     */
    protected void fireFuzzHeaderChanged(int index) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FuzzerEvent evt = new FuzzerEvent(this, FuzzerEvent.FUZZHEADER_CHANGED, index);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FuzzerListener.class) {
                try {
                    ((FuzzerListener) listeners[i + 1]).fuzzHeaderChanged(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire fuzz header removed.
     * 
     * @param index
     *            the index
     */
    protected void fireFuzzHeaderRemoved(int index) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FuzzerEvent evt = new FuzzerEvent(this, FuzzerEvent.FUZZHEADER_REMOVED, index);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FuzzerListener.class) {
                try {
                    ((FuzzerListener) listeners[i + 1]).fuzzHeaderRemoved(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire fuzz parameter added.
     * 
     * @param index
     *            the index
     */
    protected void fireFuzzParameterAdded(int index) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FuzzerEvent evt = new FuzzerEvent(this, FuzzerEvent.FUZZPARAMETER_ADDED, index);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FuzzerListener.class) {
                try {
                    ((FuzzerListener) listeners[i + 1]).fuzzParameterAdded(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire fuzz parameter changed.
     * 
     * @param index
     *            the index
     */
    protected void fireFuzzParameterChanged(int index) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FuzzerEvent evt = new FuzzerEvent(this, FuzzerEvent.FUZZPARAMETER_CHANGED, index);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FuzzerListener.class) {
                try {
                    ((FuzzerListener) listeners[i + 1]).fuzzParameterChanged(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * Fire fuzz parameter removed.
     * 
     * @param index
     *            the index
     */
    protected void fireFuzzParameterRemoved(int index) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        FuzzerEvent evt = new FuzzerEvent(this, FuzzerEvent.FUZZPARAMETER_REMOVED, index);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FuzzerListener.class) {
                try {
                    ((FuzzerListener) listeners[i + 1]).fuzzParameterRemoved(evt);
                } catch (Exception e) {
                    _logger.severe("Unhandled exception: " + e);
                }
            }
        }
    }

    /**
     * The Class FuzzConversationModel.
     */
    private class FuzzConversationModel extends AbstractConversationModel {

        /** The _conversations. */
        private List _conversations = new ArrayList();

        /**
         * Instantiates a new fuzz conversation model.
         * 
         * @param model
         *            the model
         */
        public FuzzConversationModel(FrameworkModel model) {
            super(model);
        }

        /**
         * Adds the conversation.
         * 
         * @param id
         *            the id
         */
        void addConversation(ConversationID id) {
            try {
                _rwl.writeLock().acquire();
                int index = _conversations.size();
                _conversations.add(id);
                _rwl.readLock().acquire();
                _rwl.writeLock().release();
                fireConversationAdded(id, index);
                _rwl.readLock().release();
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
            }
        }

        /**
         * Clear.
         */
        void clear() {
            try {
                _rwl.writeLock().acquire();
                _conversations.size();
                _conversations.clear();
                _rwl.readLock().acquire();
                _rwl.writeLock().release();
                fireConversationsChanged();
                _rwl.readLock().release();
            } catch (InterruptedException ie) {
                _logger.severe("Interrupted! " + ie);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractConversationModel#getConversationAt
         * (int)
         */
        public ConversationID getConversationAt(int index) {
            return (ConversationID) _conversations.get(index);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractConversationModel#getIndexOfConversation
         * (edu.lnmiit.wavd.model.ConversationID)
         */
        public int getIndexOfConversation(ConversationID id) {
            return _conversations.indexOf(id);
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.model.AbstractConversationModel#readLock()
         */
        public Sync readLock() {
            return _rwl.readLock();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.AbstractConversationModel#getConversationCount
         * ()
         */
        public int getConversationCount() {
            return _conversations.size();
        }

    }
}
