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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

// TODO: Auto-generated Javadoc
/**
 * The Class RegexSearcher.
 */
public class RegexSearcher {
    
    /**
     * Instantiates a new regex searcher.
     * 
     * @param comp the comp
     * @param painter the painter
     */
    public RegexSearcher(JTextComponent comp, Highlighter.HighlightPainter painter) {
        this.comp = comp;
        this.painter = painter;
    }
    
    // Search for a word and return the offset of the
    // first occurrence. Highlights are added for all
    // occurrences found.
    /**
     * Search.
     * 
     * @param pattern the pattern
     * @param start the start
     * @param caseSensitive the case sensitive
     * 
     * @return the int
     * 
     * @throws PatternSyntaxException the pattern syntax exception
     */
    public int search(String pattern, int start, boolean caseSensitive) throws PatternSyntaxException {
        int firstOffset = -1;
        Highlighter highlighter = comp.getHighlighter();
        
        // Remove any existing highlights for last word
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];
            if (h.getPainter() == this.painter) {
                highlighter.removeHighlight(h);
            }
        }
        
        if (pattern == null || pattern.equals("")) {
            return -1;
        }
        
        // Look for the word we are given - insensitive search
        String content = null;
        try {
            Document d = comp.getDocument();
            content = d.getText(0, d.getLength());
        } catch (BadLocationException e) {
            // Cannot happen
            return -1;
        }
        
        int flags = Pattern.DOTALL | Pattern.MULTILINE;
        if (!caseSensitive) flags |= Pattern.CASE_INSENSITIVE;
        Pattern p = Pattern.compile(pattern, flags);
        int offset = 0;
        Matcher m = p.matcher(content);
        while (m.find()) {
            for (int i=(m.groupCount()>0?1:0); i<=m.groupCount(); i++) {
                String match = m.group(i);
                if (firstOffset == -1 && m.start(i)>start) firstOffset = m.start(i);
                try {
                    highlighter.addHighlight(m.start(i), m.end(i), painter);
                } catch (BadLocationException e) {}
            }
        }
        
        return firstOffset;
    }
    
    /** The comp. */
    protected JTextComponent comp;
    
    /** The painter. */
    protected Highlighter.HighlightPainter painter;
    
}
