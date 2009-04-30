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
 * ByteViewer.java
 *
 * Created on November 4, 2003, 6:09 PM
 */

package edu.lnmiit.wavd.ui.swing.editors;

// TODO: Auto-generated Javadoc
/**
 * The Interface ByteArrayEditor.
 */
public interface ByteArrayEditor {
    
    /**
     * Gets the name.
     * 
     * @return the name
     */
    String getName();
    
    /**
     * Sets the editable.
     * 
     * @param editable the new editable
     */
    void setEditable(boolean editable);
    
    /**
     * Sets the bytes.
     * 
     * @param contentType the content type
     * @param bytes the bytes
     */
    void setBytes(String contentType, byte[] bytes);
    
    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    boolean isModified();
    
    /**
     * Gets the bytes.
     * 
     * @return the bytes
     */
    byte[] getBytes();

}
