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

package edu.lnmiit.wavd.httpclient;

import edu.lnmiit.wavd.model.HttpUrl;

// TODO: Auto-generated Javadoc
/**
 * The Interface Authenticator.
 */
public interface Authenticator {

    /**
     * Gets the credentials.
     * 
     * @param url
     *            the url
     * @param challenges
     *            the challenges
     * 
     * @return the credentials
     */
    String getCredentials(HttpUrl url, String[] challenges);

    /**
     * Gets the proxy credentials.
     * 
     * @param hostname
     *            the hostname
     * @param challenges
     *            the challenges
     * 
     * @return the proxy credentials
     */
    String getProxyCredentials(String hostname, String[] challenges);

}
