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

import java.util.Date;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import edu.lnmiit.wavd.model.ConversationEvent;
import edu.lnmiit.wavd.model.ConversationID;
import edu.lnmiit.wavd.model.ConversationListener;
import edu.lnmiit.wavd.model.ConversationModel;
import edu.lnmiit.wavd.model.HttpUrl;
import edu.lnmiit.wavd.util.swing.ColumnDataModel;
import edu.lnmiit.wavd.util.swing.ExtensibleTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ConversationTableModel.
 */
public class ConversationTableModel extends ExtensibleTableModel {

    /**
     * 
     */
    private static final long serialVersionUID = 4511036328443304525L;

    /** The _model. */
    protected ConversationModel _model = null;

    /** The _listener. */
    private Listener _listener = new Listener();

    /** The _logger. */
    protected Logger _logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new conversation table model.
     * 
     * @param model
     *            the model
     */
    public ConversationTableModel(ConversationModel model) {
        _model = model;
        addStandardColumns();
        _model.addConversationListener(_listener);
    }

    /**
     * Adds the standard columns.
     */
    private void addStandardColumns() {
        ColumnDataModel cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null)
                    return null;
                return _model.getConversationDate((ConversationID) key);
            }

            public String getColumnName() {
                return "Date";
            }

            public Class getColumnClass() {
                return Date.class;
            }
        };
        addColumn(cdm);

        cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null)
                    return null;
                return _model.getRequestMethod((ConversationID) key);
            }

            public String getColumnName() {
                return "Method";
            }

            public Class getColumnClass() {
                return String.class;
            }
        };
        addColumn(cdm);

        cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null)
                    return null;
                HttpUrl url = _model.getRequestUrl((ConversationID) key);
                return url.getScheme() + "://" + url.getHost() + ":" + url.getPort();
            }

            public String getColumnName() {
                return "Host";
            }

            public Class getColumnClass() {
                return String.class;
            }
        };
        addColumn(cdm);

        cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null)
                    return null;
                HttpUrl url = _model.getRequestUrl((ConversationID) key);
                return url.getPath();
            }

            public String getColumnName() {
                return "Path";
            }

            public Class getColumnClass() {
                return String.class;
            }
        };
        addColumn(cdm);

        cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null)
                    return null;
                HttpUrl url = _model.getRequestUrl((ConversationID) key);
                return url.getParameters();
            }

            public String getColumnName() {
                return "Parameters";
            }

            public Class getColumnClass() {
                return String.class;
            }
        };
        addColumn(cdm);

        cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null)
                    return null;
                return _model.getResponseStatus((ConversationID) key);
            }

            public String getColumnName() {
                return "Status";
            }

            public Class getColumnClass() {
                return String.class;
            }
        };
        addColumn(cdm);

        cdm = new ColumnDataModel() {
            public Object getValue(Object key) {
                if (_model == null)
                    return null;
                return _model.getConversationOrigin((ConversationID) key);
            }

            public String getColumnName() {
                return "Origin";
            }

            public Class getColumnClass() {
                return String.class;
            }
        };
        addColumn(cdm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.util.swing.ExtensibleTableModel#getKeyAt(int)
     */
    public Object getKeyAt(int row) {
        return _model.getConversationAt(row);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.util.swing.ExtensibleTableModel#indexOfKey(java.lang.
     * Object)
     */
    public int indexOfKey(Object key) {
        return _model.getIndexOfConversation((ConversationID) key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.util.swing.ExtensibleTableModel#getRowCount()
     */
    public int getRowCount() {
        if (_model == null)
            return 0;
        return _model.getConversationCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.util.swing.ExtensibleTableModel#getColumnCount()
     */
    public int getColumnCount() {
        return super.getColumnCount() + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.util.swing.ExtensibleTableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column) {
        Object key = getKeyAt(row);
        if (column == 0)
            return key;
        return super.getValueAt(key, column - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.util.swing.ExtensibleTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        if (column == 0)
            return "ID";
        return super.getColumnName(column - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.util.swing.ExtensibleTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
        if (column == 0)
            return ConversationID.class;
        return super.getColumnClass(column - 1);
    }

    /**
     * Added conversation.
     * 
     * @param evt
     *            the evt
     */
    protected void addedConversation(ConversationEvent evt) {
        ConversationID id = evt.getConversationID();
        int row = indexOfKey(id);
        fireTableRowsInserted(row, row);
    }

    /**
     * Removed conversation.
     * 
     * @param evt
     *            the evt
     */
    protected void removedConversation(ConversationEvent evt) {
        fireTableDataChanged();
    }

    /**
     * Changed conversations.
     */
    protected void changedConversations() {
        fireTableDataChanged();
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
        public void conversationAdded(final ConversationEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                addedConversation(evt);
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            addedConversation(evt);
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception processing " + evt + ": " + e);
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
        public void conversationChanged(final ConversationEvent evt) {
            // we don't care. The values that we care about specifically
            // are set when the conversationAdded event is fired, and
            // do not change afterwards.
            // Other changes in user-supplied columns fire their own events
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.model.ConversationListener#conversationRemoved(edu
         * .lnmiit.wavd.model.ConversationEvent)
         */
        public void conversationRemoved(final ConversationEvent evt) {
            if (SwingUtilities.isEventDispatchThread()) {
                removedConversation(evt);
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            removedConversation(evt);
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception processing " + evt + ": " + e);
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
            if (SwingUtilities.isEventDispatchThread()) {
                changedConversations();
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            changedConversations();
                        }
                    });
                } catch (Exception e) {
                    _logger.warning("Exception: " + e);
                }
            }
        }

    }

}
