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
 * SwingPlugin.java
 *
 * Created on July 13, 2003, 8:08 PM
 */

package edu.lnmiit.wavd.ui.swing;


import edu.lnmiit.wavd.plugin.PluginUI;
import edu.lnmiit.wavd.util.swing.ColumnDataModel;

import javax.swing.JPanel;
import javax.swing.Action;

// TODO: Auto-generated Javadoc
/**
 * The Interface SwingPluginUI.
 */
public interface SwingPluginUI extends PluginUI {

    /**
     * Gets the panel.
     * 
     * @return the panel
     */
    JPanel getPanel();
    
    /**
     * Gets the url actions.
     * 
     * @return the url actions
     */
    Action[] getUrlActions();
    
    /**
     * Gets the url columns.
     * 
     * @return the url columns
     */
    ColumnDataModel[] getUrlColumns();
    
    /**
     * Gets the conversation actions.
     * 
     * @return the conversation actions
     */
    Action[] getConversationActions();
    
    /**
     * Gets the conversation columns.
     * 
     * @return the conversation columns
     */
    ColumnDataModel[] getConversationColumns();
    
    
}
