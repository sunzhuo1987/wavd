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

package edu.lnmiit.wavd.ui.swing;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

import edu.lnmiit.wavd.model.Preferences;

// TODO: Auto-generated Javadoc
/**
 * The Class DateRenderer.
 */
public class DateRenderer extends DefaultTableCellRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = -7367433148238420909L;
    /** The _sdf. */
    private SimpleDateFormat _sdf;

    /**
     * Instantiates a new date renderer.
     */
    public DateRenderer() {
        String format = Preferences.getPreference("WebScarab.DateFormat", "yyyy/MM/dd HH:mm:ss");
        _sdf = new SimpleDateFormat(format);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        if ((value != null) && (value instanceof Date)) {
            Date date = (Date) value;
            // value = DateUtil.rfc822Format(date);
            value = _sdf.format(date);
        }
        super.setValue(value);
    }

}
