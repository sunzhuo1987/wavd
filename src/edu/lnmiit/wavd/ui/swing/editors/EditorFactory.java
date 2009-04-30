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

package edu.lnmiit.wavd.ui.swing.editors;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Editor objects.
 */
public class EditorFactory {

    /** The _editors. */
    private static Map _editors = null;

    /** The NONE. */
    private static ByteArrayEditor[] NONE = new ByteArrayEditor[0];

    /** The _logger. */
    private static Logger _logger = Logger.getLogger("org.owasp.webscarab.ui.swing.editors.EditorFactory");

    /**
     * Instantiates a new editor factory.
     */
    private EditorFactory() {
    }

    /**
     * Register editors.
     */
    private static void registerEditors() {
        _editors = new LinkedHashMap(); // this helps to maintainthe order of
        // the editors
        registerEditor("multipart/form-data; .*", "org.owasp.webscarab.ui.swing.editors.MultiPartPanel");
        registerEditor("application/x-serialized-object", "org.owasp.webscarab.ui.swing.editors.SerializedObjectPanel");
        registerEditor("image/.*", "org.owasp.webscarab.ui.swing.editors.ImagePanel");
        registerEditor("application/x-www-form-urlencoded", "org.owasp.webscarab.ui.swing.editors.UrlEncodedPanel");
        registerEditor("text/html.*", "org.owasp.webscarab.ui.swing.editors.HTMLPanel");
        registerEditor("text/html.*", "org.owasp.webscarab.ui.swing.editors.XMLPanel");
        registerEditor("text/xml.*", "org.owasp.webscarab.ui.swing.editors.XMLPanel");
        registerEditor("text/.*", "org.owasp.webscarab.ui.swing.editors.TextPanel");
        registerEditor("application/x-javascript", "org.owasp.webscarab.ui.swing.editors.TextPanel");
        registerEditor("application/x-www-form-urlencoded", "org.owasp.webscarab.ui.swing.editors.TextPanel");
        registerEditor("application/x-amf", "org.owasp.webscarab.ui.swing.editors.AMFPanel");
        registerEditor(".*", "org.owasp.webscarab.ui.swing.editors.HexPanel");
        // registerEditor(".*",
        // "org.owasp.webscarab.ui.swing.editors.CompressedTextPanel");
    }

    /**
     * Register editor.
     * 
     * @param contentType
     *            the content type
     * @param editorClass
     *            the editor class
     */
    public static void registerEditor(String contentType, String editorClass) {
        List list = (List) _editors.get(contentType);
        if (list == null) {
            list = new ArrayList();
            _editors.put(contentType, list);
        }
        if (list.indexOf(editorClass) < 0)
            list.add(editorClass);
    }

    /**
     * Gets the editors.
     * 
     * @param contentType
     *            the content type
     * 
     * @return the editors
     */
    public static ByteArrayEditor[] getEditors(String contentType) {
        if (contentType == null)
            return new ByteArrayEditor[] { new HexPanel() };
        if (_editors == null)
            registerEditors();
        Iterator it = _editors.keySet().iterator();
        List editors = new ArrayList();
        while (it.hasNext()) {
            String type = (String) it.next();
            if (contentType.matches(type)) {
                List list = (List) _editors.get(type);
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    String className = (String) it2.next();
                    try {
                        Object ed = Class.forName(className).newInstance();
                        if (ed instanceof ByteArrayEditor && ed instanceof Component) {
                            editors.add(ed);
                        } else {
                            _logger.warning("Editor " + className + " must implement ByteArrayEditor and Component");
                        }
                    } catch (Exception e) {
                        _logger.warning("Exception trying to instantiate " + className + " : " + e);
                    }
                }
            }
        }
        return (ByteArrayEditor[]) editors.toArray(NONE);
    }

}
