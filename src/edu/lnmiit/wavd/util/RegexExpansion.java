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

package edu.lnmiit.wavd.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

// TODO: Auto-generated Javadoc
/**
 * The Class RegexExpansion.
 */
public class RegexExpansion {

    /** The regex. */
    private String regex;

    /** The size. */
    private int size = 0;

    /** The index. */
    private int index = 0;

    /** The charsets. */
    private char[][] charsets;

    /**
     * Instantiates a new regex expansion.
     * 
     * @param regex
     *            the regex
     * 
     * @throws PatternSyntaxException
     *             the pattern syntax exception
     */
    public RegexExpansion(String regex) throws PatternSyntaxException {
        this.regex = regex;
        List charsets = new LinkedList();
        List chars = new LinkedList();

        boolean inClass = false;
        boolean quoted = false;
        String quantifier = null;
        char range = '\0';
        for (int index = 0; index < regex.length(); index++) {
            char ch = regex.charAt(index);
            if (!quoted && !inClass && (ch == '.' || ch == '*' || ch == '?')) {
                throw new PatternSyntaxException("No wildcards permitted", regex, index);
            }
            if (quantifier != null && ch != '}') {
                if (!Character.isDigit(ch))
                    throw new PatternSyntaxException("Illegal non-digit character in quantifier", regex, index);
                quantifier = quantifier + ch;
                continue;
            } else if (quoted) {
                chars.add(new Character(ch));
                quoted = false;
            } else
                switch (ch) {
                case '[':
                    inClass = true;
                    continue;
                case ']':
                    inClass = false;
                    break;
                case '\\':
                    quoted = true;
                    continue;
                case '{':
                    if (charsets.size() == 0)
                        throw new PatternSyntaxException("Illegal quantifier at start of regex", regex, index);
                    quantifier = "";
                    continue;
                case '}':
                    try {
                        int c = Integer.parseInt(quantifier);
                        if (c == 0)
                            throw new PatternSyntaxException("Cannot repeat 0 times", regex, index);
                        for (int i = 1; i < c; i++)
                            charsets.add(charsets.get(charsets.size() - 1));
                    } catch (NumberFormatException nfe) {
                        throw new PatternSyntaxException(nfe.getMessage(), regex, index);
                    }
                    quantifier = null;
                    continue;
                case '-':
                    if (inClass) {
                        range = ((Character) chars.get(chars.size() - 1)).charValue();
                        continue;
                    }
                default:
                    if (range != '\0') {
                        if (ch <= range)
                            throw new PatternSyntaxException("Illegal range definition", regex, index);
                        for (char q = ++range; q <= ch; q++)
                            chars.add(new Character(q));
                        range = '\0';
                    } else
                        chars.add(new Character(ch));
                }
            if (!inClass) {
                charsets.add(chars);
                chars = new LinkedList();
            }
        }
        this.charsets = new char[charsets.size()][];
        for (int i = 0; i < charsets.size(); i++) {
            chars = (List) charsets.get(i);
            char[] t = new char[chars.size()];
            for (int j = 0; j < chars.size(); j++) {
                t[j] = ((Character) chars.get(j)).charValue();
            }
            this.charsets[i] = t;
        }
        this.size = 1;
        for (int i = 0; i < this.charsets.length; i++) {
            this.size = this.size * this.charsets[i].length;
            if (size == 0)
                throw new PatternSyntaxException("Pattern expansion overflow at position " + i, regex, 0);
        }
    }

    /**
     * Instantiates a new regex expansion.
     * 
     * @param re
     *            the re
     */
    protected RegexExpansion(RegexExpansion re) {
        this.charsets = re.charsets;
        this.size = re.size;
        this.index = 0;
    }

    /**
     * Gets the regex.
     * 
     * @return the regex
     */
    public String getRegex() {
        return this.regex;
    }

    /**
     * Size.
     * 
     * @return the int
     */
    public int size() {
        return this.size;
    }

    /**
     * Sets the index.
     * 
     * @param index
     *            the new index
     */
    public void setIndex(int index) {
        if (index >= size)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index + " >= " + size);
        this.index = index;
    }

    /**
     * Gets the index.
     * 
     * @return the index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Checks for next.
     * 
     * @return true, if successful
     */
    public boolean hasNext() {
        return getIndex() < size();
    }

    /**
     * Next.
     * 
     * @return the string
     */
    public String next() {
        if (index >= size)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index + " >= " + size);
        return get(index++);
    }

    /**
     * Gets the.
     * 
     * @param index
     *            the index
     * 
     * @return the string
     */
    public String get(int index) {
        if (index >= size)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index + " >= " + size);
        StringBuffer buff = new StringBuffer(charsets.length);
        for (int i = charsets.length - 1; i >= 0; i--) {
            int mod = index % charsets[i].length;
            index = index / charsets[i].length;
            buff.insert(0, charsets[i][mod]);
        }
        return buff.toString();
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        RegexExpansion re = new RegexExpansion("[0-9A-F]{8}");
        System.out.println("Size " + re.size());
    }
}
