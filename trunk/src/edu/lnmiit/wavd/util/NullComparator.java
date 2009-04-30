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

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class NullComparator.
 */
public class NullComparator implements Comparator {
    
    /**
     * Instantiates a new null comparator.
     */
    public NullComparator() {
    }
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(T, T)
     */
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) return 0;
        if (o1 == null && o2 != null) return 1;
        if (o1 != null && o2 == null) return -1;
        if (o1 instanceof Comparable) return ((Comparable)o1).compareTo(o2);
        throw new ClassCastException("Incomparable objects " + o1.getClass().getName() + " and " + o2.getClass().getName());
    }
    
}
