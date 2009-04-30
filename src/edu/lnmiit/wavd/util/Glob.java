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

import java.util.Stack;

// TODO: Auto-generated Javadoc
/**
 * The Class Glob.
 */
public class Glob {
    
    /**
     * Instantiates a new glob.
     */
    private Glob() {
    }
    
    /**
     * Glob to re.
     * 
     * @param glob the glob
     * 
     * @return the string
     */
    public static String globToRE(String glob) {
        final Object NEG = new Object();
        final Object GROUP = new Object();
        Stack state = new Stack();
        
        StringBuffer buf = new StringBuffer();
        boolean backslash = false;
        
        for(int i = 0; i < glob.length(); i++) {
            char c = glob.charAt(i);
            if(backslash) {
                buf.append('\\');
                buf.append(c);
                backslash = false;
                continue;
            }
            
            switch(c) {
                case '\\':
                    backslash = true;
                    break;
                case '?':
                    buf.append('.');
                    break;
                case '.':
                case '+':
                case '(':
                case ')':
                    buf.append('\\');
                    buf.append(c);
                    break;
                case '*':
                    buf.append(".*");
                    break;
                case '|':
                    if(backslash)
                        buf.append("\\|");
                    else
                        buf.append('|');
                    break;
                case '{':
                    buf.append('(');
                    if(i + 1 != glob.length() && glob.charAt(i + 1) == '!') {
                        buf.append('?');
                        state.push(NEG);
                    }
                    else
                        state.push(GROUP);
                    break;
                case ',':
                    if(!state.isEmpty() && state.peek() == GROUP)
                        buf.append('|');
                    else
                        buf.append(',');
                    break;
                case '}':
                    if(!state.isEmpty()) {
                        buf.append(")");
                        if(state.pop() == NEG)
                            buf.append(".*");
                    }
                    else
                        buf.append('}');
                    break;
                default:
                    buf.append(c);
            }
        }
        
        return buf.toString();
    }
    
}
