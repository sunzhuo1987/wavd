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
 * MyHTMLEditorKit.java
 *
 * Created on November 8, 2003, 12:37 PM
 */

package edu.lnmiit.wavd.ui.swing.editors;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.ViewFactory;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.BlockView;
import javax.swing.text.Position;
import javax.swing.text.BadLocationException;

import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics;

// TODO: Auto-generated Javadoc
/**
 * The Class MyHTMLEditorKit.
 */
public class MyHTMLEditorKit extends javax.swing.text.html.HTMLEditorKit {
    
    /** The Constant defaultFactory. */
    private static final ViewFactory defaultFactory = new MyHTMLFactory();
    
    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLEditorKit#getViewFactory()
     */
    public ViewFactory getViewFactory() {
	return defaultFactory;
    }
    
    /**
     * A factory for creating MyHTML objects.
     */
    private static class MyHTMLFactory extends HTMLEditorKit.HTMLFactory {
        
        /* (non-Javadoc)
         * @see javax.swing.text.html.HTMLEditorKit.HTMLFactory#create(javax.swing.text.Element)
         */
        public View create(Element elem) {
            Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
            if (o instanceof HTML.Tag) {
                HTML.Tag kind = (HTML.Tag) o;
                if (kind == HTML.Tag.FRAME || 
                    kind == HTML.Tag.FRAMESET || 
                    kind == HTML.Tag.OBJECT || 
                    kind == HTML.Tag.MAP ||
                    kind == HTML.Tag.IMG ||
                    kind == HTML.Tag.LINK ||
                    kind == HTML.Tag.SCRIPT ||
                    kind == HTML.Tag.APPLET) {
                    return new NoView(elem);
                }
            }
            return super.create(elem);
        }
    }
    
    /**
     * The Class NoView.
     */
    private static class NoView extends View {
        
        /**
         * Instantiates a new no view.
         * 
         * @param elem the elem
         */
        public NoView(Element elem) {
            super(elem);
            setSize(0.0f, 0.0f);
        }

        /* (non-Javadoc)
         * @see javax.swing.text.View#viewToModel(float, float, java.awt.Shape, javax.swing.text.Position.Bias[])
         */
        public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
            return 0;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.text.View#modelToView(int, java.awt.Shape, javax.swing.text.Position.Bias)
         */
        public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
            return new Rectangle(0, 0);
        }

        /* (non-Javadoc)
         * @see javax.swing.text.View#getPreferredSpan(int)
         */
        public float getPreferredSpan(int axis) {
            return 0.0f;
        }

        /* (non-Javadoc)
         * @see javax.swing.text.View#paint(java.awt.Graphics, java.awt.Shape)
         */
        public void paint(Graphics g, Shape allocation) {
        }
    }

}
