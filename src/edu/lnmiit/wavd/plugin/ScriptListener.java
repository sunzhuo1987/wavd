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

package edu.lnmiit.wavd.plugin;

import java.util.EventListener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving script events. The class that is
 * interested in processing a script event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addScriptListener<code> method. When
 * the script event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ScriptEvent
 */
public interface ScriptListener extends EventListener {

    /**
     * Hooks changed.
     */
    void hooksChanged();

    /**
     * Hook started.
     * 
     * @param plugin
     *            the plugin
     * @param hook
     *            the hook
     */
    void hookStarted(String plugin, Hook hook);

    /**
     * Hook ended.
     * 
     * @param plugin
     *            the plugin
     * @param hook
     *            the hook
     */
    void hookEnded(String plugin, Hook hook);

    /**
     * Script added.
     * 
     * @param plugin
     *            the plugin
     * @param hook
     *            the hook
     * @param script
     *            the script
     */
    void scriptAdded(String plugin, Hook hook, Script script);

    /**
     * Script removed.
     * 
     * @param plugin
     *            the plugin
     * @param hook
     *            the hook
     * @param script
     *            the script
     */
    void scriptRemoved(String plugin, Hook hook, Script script);

    /**
     * Script started.
     * 
     * @param plugin
     *            the plugin
     * @param hook
     *            the hook
     * @param script
     *            the script
     */
    void scriptStarted(String plugin, Hook hook, Script script);

    /**
     * Script ended.
     * 
     * @param plugin
     *            the plugin
     * @param hook
     *            the hook
     * @param script
     *            the script
     */
    void scriptEnded(String plugin, Hook hook, Script script);

    /**
     * Script changed.
     * 
     * @param plugin
     *            the plugin
     * @param hook
     *            the hook
     * @param script
     *            the script
     */
    void scriptChanged(String plugin, Hook hook, Script script);

    /**
     * Script error.
     * 
     * @param plugin
     *            the plugin
     * @param hook
     *            the hook
     * @param script
     *            the script
     * @param error
     *            the error
     */
    void scriptError(String plugin, Hook hook, Script script, Throwable error);

}
