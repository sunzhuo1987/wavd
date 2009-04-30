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

package edu.lnmiit.wavd.plugin;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import edu.lnmiit.wavd.util.ReentrantReaderPreferenceReadWriteLock;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractPluginModel.
 */
public class AbstractPluginModel {

    /** The Constant PROPERTY_STATUS. */
    public final static String PROPERTY_STATUS = "Status";

    /** The Constant PROPERTY_RUNNING. */
    public final static String PROPERTY_RUNNING = "Running";

    /** The Constant PROPERTY_STOPPING. */
    public final static String PROPERTY_STOPPING = "Stopping";

    /** The Constant PROPERTY_MODIFIED. */
    public final static String PROPERTY_MODIFIED = "Modified";

    /** The Constant PROPERTY_BUSY. */
    public final static String PROPERTY_BUSY = "Busy";

    /** The _change support. */
    protected PropertyChangeSupport _changeSupport = new PropertyChangeSupport(this);

    /** The _rwl. */
    protected ReentrantReaderPreferenceReadWriteLock _rwl = new ReentrantReaderPreferenceReadWriteLock();

    /** The _status. */
    private String _status = "Stopped";

    /** The _running. */
    private boolean _running = false;

    /** The _stopping. */
    private boolean _stopping = false;

    /** The _modified. */
    private boolean _modified = false;

    /** The _busy. */
    private boolean _busy = false;

    /**
     * Instantiates a new abstract plugin model.
     */
    public AbstractPluginModel() {
    }

    /**
     * Sets the status.
     * 
     * @param status
     *            the new status
     */
    public void setStatus(String status) {
        if (!_status.equals(status)) {
            String old = _status;
            _status = status;
            _changeSupport.firePropertyChange(PROPERTY_STATUS, old, _status);
        }
    }

    /**
     * Gets the status.
     * 
     * @return the status
     */
    public String getStatus() {
        return _status;
    }

    /**
     * Sets the running.
     * 
     * @param running
     *            the new running
     */
    public void setRunning(boolean running) {
        if (_running != running) {
            _running = running;
            _changeSupport.firePropertyChange(PROPERTY_RUNNING, !_running, _running);
        }
    }

    /**
     * Checks if is running.
     * 
     * @return true, if is running
     */
    public boolean isRunning() {
        return _running;
    }

    /**
     * Sets the stopping.
     * 
     * @param stopping
     *            the new stopping
     */
    public void setStopping(boolean stopping) {
        if (_stopping != stopping) {
            _stopping = stopping;
            _changeSupport.firePropertyChange(PROPERTY_STOPPING, !_stopping, _stopping);
        }
    }

    /**
     * Checks if is stopping.
     * 
     * @return true, if is stopping
     */
    public boolean isStopping() {
        return _stopping;
    }

    /**
     * Sets the modified.
     * 
     * @param modified
     *            the new modified
     */
    public void setModified(boolean modified) {
        if (_modified != modified) {
            _modified = modified;
            _changeSupport.firePropertyChange(PROPERTY_MODIFIED, !_modified, _modified);
        }
    }

    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    public boolean isModified() {
        return _modified;
    }

    /**
     * Sets the busy.
     * 
     * @param busy
     *            the new busy
     */
    public void setBusy(boolean busy) {
        if (_busy != busy) {
            _busy = busy;
            _changeSupport.firePropertyChange(PROPERTY_BUSY, !_busy, _busy);
        }
    }

    /**
     * Checks if is busy.
     * 
     * @return true, if is busy
     */
    public boolean isBusy() {
        return _busy;
    }

    /**
     * Adds the property change listener.
     * 
     * @param listener
     *            the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Adds the property change listener.
     * 
     * @param propertyName
     *            the property name
     * @param listener
     *            the listener
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes the property change listener.
     * 
     * @param listener
     *            the listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Removes the property change listener.
     * 
     * @param propertyName
     *            the property name
     * @param listener
     *            the listener
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(propertyName, listener);
    }

}
