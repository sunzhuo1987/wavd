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

import java.util.EventListener;

import edu.lnmiit.wavd.model.HttpUrl;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving fuzzer events.
 * The class that is interested in processing a fuzzer
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFuzzerListener<code> method. When
 * the fuzzer event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see FuzzerEvent
 */
public interface FuzzerListener extends EventListener {
    
    /**
     * Fuzz header added.
     * 
     * @param evt the evt
     */
    void fuzzHeaderAdded(FuzzerEvent evt);
    
    /**
     * Fuzz header changed.
     * 
     * @param evt the evt
     */
    void fuzzHeaderChanged(FuzzerEvent evt);
    
    /**
     * Fuzz header removed.
     * 
     * @param evt the evt
     */
    void fuzzHeaderRemoved(FuzzerEvent evt);
    
    /**
     * Fuzz parameter added.
     * 
     * @param evt the evt
     */
    void fuzzParameterAdded(FuzzerEvent evt);
    
    /**
     * Fuzz parameter changed.
     * 
     * @param evt the evt
     */
    void fuzzParameterChanged(FuzzerEvent evt);
    
    /**
     * Fuzz parameter removed.
     * 
     * @param evt the evt
     */
    void fuzzParameterRemoved(FuzzerEvent evt);
    
}
