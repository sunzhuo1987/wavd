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
 * UrlTreeRenderer.java
 *
 * Created on August 8, 2004, 5:16 PM
 */

package edu.lnmiit.wavd.ui.swing;

import edu.lnmiit.wavd.model.HttpUrl;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.JLabel;
import javax.swing.tree.DefaultTreeCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class UrlTreeRenderer.
 */
public class UrlTreeRenderer extends DefaultTreeCellRenderer {
    
    /**
     * Instantiates a new url tree renderer.
     */
    public UrlTreeRenderer() {
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof HttpUrl && comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            HttpUrl url = (HttpUrl) value;
            if (url.getParameters() != null) {
                label.setText(url.getParameters());
            } else if (url.getPath().length()>1) {
                String path = url.getPath();
                int pos = path.lastIndexOf("/", path.length()-2);
                label.setText(path.substring(pos+1));
            }
        }
        return comp;
    }
    
}
