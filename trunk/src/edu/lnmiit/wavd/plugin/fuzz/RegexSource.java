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

import java.util.regex.PatternSyntaxException;

import edu.lnmiit.wavd.util.RegexExpansion;

// TODO: Auto-generated Javadoc
/**
 * The Class RegexSource.
 */
public class RegexSource extends RegexExpansion implements FuzzSource {
    
    /** The description. */
    private String description;
    
    /**
     * Instantiates a new regex source.
     * 
     * @param description the description
     * @param regex the regex
     * 
     * @throws PatternSyntaxException the pattern syntax exception
     */
    public RegexSource(String description, String regex) throws PatternSyntaxException {
        super(regex);
        this.description = description;
    }
    
    /**
     * Instantiates a new regex source.
     * 
     * @param rs the rs
     */
    protected RegexSource(RegexSource rs) {
        super(rs);
        this.description = rs.description;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#current()
     */
    public Object current() {
        return super.get(super.getIndex());
    }

    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#getDescription()
     */
    public String getDescription() {
        return this.description;
    }

    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#increment()
     */
    public void increment() {
        super.next();
    }

    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#newInstance()
     */
    public FuzzSource newInstance() {
        return new RegexSource(this);
    }

    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.plugin.fuzz.FuzzSource#reset()
     */
    public void reset() {
        super.setIndex(0);
    }
    
}
