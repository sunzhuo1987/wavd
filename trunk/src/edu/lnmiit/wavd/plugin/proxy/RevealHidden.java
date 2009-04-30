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
 * RevealHidden.java
 *
 * Created on July 13, 2003, 7:39 PM
 */

package edu.lnmiit.wavd.plugin.proxy;

// import org.owasp.util.StringUtil;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.lnmiit.wavd.httpclient.HTTPClient;
import edu.lnmiit.wavd.model.Preferences;
import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

// TODO: Auto-generated Javadoc
/**
 * The Class RevealHidden.
 */
public class RevealHidden extends ProxyPlugin {

    /** The _enabled. */
    private boolean _enabled = false;

    /**
     * Instantiates a new reveal hidden.
     */
    public RevealHidden() {
        parseProperties();
    }

    /**
     * Parses the properties.
     */
    public void parseProperties() {
        String prop = "RevealHidden.enabled";
        String value = Preferences.getPreference(prop, "false");
        _enabled = ("true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value));
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.lnmiit.wavd.plugin.proxy.ProxyPlugin#getPluginName()
     */
    public String getPluginName() {
        return new String("Reveal Hidden");
    }

    /**
     * Sets the enabled.
     * 
     * @param bool
     *            the new enabled
     */
    public void setEnabled(boolean bool) {
        _enabled = bool;
        String prop = "RevealHidden.enabled";
        Preferences.setPreference(prop, Boolean.toString(bool));
    }

    /**
     * Gets the enabled.
     * 
     * @return the enabled
     */
    public boolean getEnabled() {
        return _enabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.lnmiit.wavd.plugin.proxy.ProxyPlugin#getProxyPlugin(edu.lnmiit.wavd
     * .httpclient.HTTPClient)
     */
    public HTTPClient getProxyPlugin(HTTPClient in) {
        return new Plugin(in);
    }

    /**
     * The Class Plugin.
     */
    private class Plugin implements HTTPClient {

        /** The _in. */
        private HTTPClient _in;

        /**
         * Instantiates a new plugin.
         * 
         * @param in
         *            the in
         */
        public Plugin(HTTPClient in) {
            _in = in;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * edu.lnmiit.wavd.httpclient.HTTPClient#fetchResponse(edu.lnmiit.wavd
         * .model.Request)
         */
        public Response fetchResponse(Request request) throws IOException {
            Response response = _in.fetchResponse(request);
            if (_enabled) {
                String ct = response.getHeader("Content-Type");
                if (ct != null && ct.matches("text/.*")) {
                    byte[] content = response.getContent();
                    if (content != null) {
                        response.setContent(revealHidden(content));
                        response.addHeader("X-RevealHidden", "possibly modified");
                    }
                }
            }
            return response;
        }

        /**
         * Reveal hidden.
         * 
         * @param content
         *            the content
         * 
         * @return the byte[]
         */
        private byte[] revealHidden(byte[] content) {
            /*
             * We split this pattern into two parts, one before "hidden" and one
             * after Then it is simple to concatenate part 1 + "text" + part 2
             * to get an "unhidden" input tag
             */
            Pattern inputPattern = Pattern.compile("(<input.+?type\\s*=\\s*[\"']{0,1})hidden([\"']{0,1}.+?>)",
                    Pattern.CASE_INSENSITIVE);
            Matcher inputMatcher = inputPattern.matcher(new String(content));
            StringBuffer outbuf = new StringBuffer();

            /* matched hidden input parameter */
            while (inputMatcher.find()) {
                String input = inputMatcher.group();
                String name = "noname";

                // extract hidden field name
                Pattern namePattern = Pattern.compile("name=[\"']{0,1}(\\w+)[\"']{0,1}", Pattern.CASE_INSENSITIVE);
                Matcher nameMatcher = namePattern.matcher(input);
                if (nameMatcher.find() && nameMatcher.groupCount() == 1) {
                    name = nameMatcher.group(1);
                }

                // make hidden field a text field - there MUST be 2 groups
                // Note: this way we don't have to care about which quotes are
                // being used
                input = inputMatcher.group(1) + "text" + inputMatcher.group(2);

                /* insert [hidden] <fieldname> before the field itself */
                inputMatcher.appendReplacement(outbuf,
                        "<STRONG style=\"background-color: white;\"> [hidden field name =\"" + name + "\"]:</STRONG> "
                                + input + "<BR/>");
            }
            inputMatcher.appendTail(outbuf);
            return outbuf.toString().getBytes();
        }

    }

}
