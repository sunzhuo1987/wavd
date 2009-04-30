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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Fuzz objects.
 */
public class FuzzFactory {

    /** The Constant SOURCES. */
    public static final String SOURCES = "Sources";

    /** The _sources. */
    private Map _sources = new TreeMap();

    /** The _change support. */
    private PropertyChangeSupport _changeSupport = new PropertyChangeSupport(this);

    /**
     * Instantiates a new fuzz factory.
     */
    public FuzzFactory() {
    }

    /**
     * Gets the source descriptions.
     * 
     * @return the source descriptions
     */
    public String[] getSourceDescriptions() {
        return (String[]) _sources.keySet().toArray(new String[_sources.size()]);
    }

    /**
     * Adds the source.
     * 
     * @param source
     *            the source
     */
    public void addSource(FuzzSource source) {
        _sources.put(source.getDescription(), source);
        _changeSupport.firePropertyChange(SOURCES, null, null);
    }

    /**
     * Load fuzz strings.
     * 
     * @param description
     *            the description
     * @param inputStream
     *            the input stream
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void loadFuzzStrings(String description, InputStream inputStream) throws IOException {
        addSource(new FileSource(description, new InputStreamReader(inputStream)));
    }

    /**
     * Load fuzz strings.
     * 
     * @param description
     *            the description
     * @param file
     *            the file
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void loadFuzzStrings(String description, File file) throws IOException {
        addSource(new FileSource(description, new FileReader(file)));
    }

    /**
     * Removes the source.
     * 
     * @param name
     *            the name
     * 
     * @return true, if successful
     */
    public boolean removeSource(String name) {
        boolean success = (_sources.remove(name) != null);
        _changeSupport.firePropertyChange(SOURCES, null, null);
        return success;
    }

    /**
     * Adds the regex source.
     * 
     * @param description
     *            the description
     * @param regex
     *            the regex
     * 
     * @throws PatternSyntaxException
     *             the pattern syntax exception
     */
    public void addRegexSource(String description, String regex) throws PatternSyntaxException {
        addSource(new RegexSource(description, regex));
    }

    /**
     * Gets the source.
     * 
     * @param name
     *            the name
     * 
     * @return the source
     */
    public FuzzSource getSource(String name) {
        FuzzSource source = (FuzzSource) _sources.get(name);
        if (source == null) {
            return null;
        } else {
            return source.newInstance();
        }
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
     * Removes the property change listener.
     * 
     * @param listener
     *            the listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * The Class FileSource.
     */
    private class FileSource implements FuzzSource {

        /** The _description. */
        private String _description;

        /** The _items. */
        private String[] _items;

        /** The _index. */
        private int _index = 0;

        /**
         * Instantiates a new file source.
         * 
         * @param description
         *            the description
         * @param reader
         *            the reader
         * 
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        public FileSource(String description, Reader reader) throws IOException {
            _description = description;
            BufferedReader br = new BufferedReader(reader);
            String line;
            List items = new LinkedList();
            while ((line = br.readLine()) != null) {
                items.add(line);
            }
            br.close();
            _items = (String[]) items.toArray(new String[items.size()]);
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#getDescription()
         */
        public String getDescription() {
            return _description;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#increment()
         */
        public void increment() {
            _index++;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#hasNext()
         */
        public boolean hasNext() {
            return _index < _items.length - 1;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#reset()
         */
        public void reset() {
            _index = 0;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#size()
         */
        public int size() {
            return _items.length;
        }

        /**
         * Gets the items.
         * 
         * @return the items
         */
        protected String[] getItems() {
            return _items;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#current()
         */
        public Object current() {
            return _items[_index];
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#newInstance()
         */
        public FuzzSource newInstance() {
            return new ArraySource(_description, _items);
        }

    }

    /**
     * The Class ArraySource.
     */
    private class ArraySource implements FuzzSource {

        /** The _description. */
        private String _description;

        /** The _items. */
        private String[] _items;

        /** The _index. */
        private int _index = 0;

        /**
         * Instantiates a new array source.
         * 
         * @param description
         *            the description
         * @param items
         *            the items
         */
        public ArraySource(String description, String[] items) {
            _description = description;
            _items = items;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#getDescription()
         */
        public String getDescription() {
            return _description;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#size()
         */
        public int size() {
            return _items.length;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#reset()
         */
        public void reset() {
            _index = 0;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#increment()
         */
        public void increment() {
            _index++;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#hasNext()
         */
        public boolean hasNext() {
            return _index < _items.length - 1;
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#current()
         */
        public Object current() {
            return _items[_index];
        }

        /*
         * (non-Javadoc)
         * 
         * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#newInstance()
         */
        public FuzzSource newInstance() {
            return new ArraySource(_description, _items);
        }

    }

}
