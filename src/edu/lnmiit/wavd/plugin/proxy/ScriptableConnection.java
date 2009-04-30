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

package edu.lnmiit.wavd.plugin.proxy;

import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;

import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

// TODO: Auto-generated Javadoc
/**
 * The Class ScriptableConnection.
 */
public class ScriptableConnection {
    
    /** The _socket. */
    private Socket _socket = null;
    
    /** The _request. */
    private Request _request = null;
    
    /** The _response. */
    private Response _response = null;
    
    /**
     * Instantiates a new scriptable connection.
     * 
     * @param socket the socket
     */
    public ScriptableConnection(Socket socket) {
        _socket = socket;
    }
    
    /**
     * Gets the address.
     * 
     * @return the address
     */    
    public InetAddress getAddress() {
        return _socket.getInetAddress();
    }
    
    /**
     * Close connection.
     */
    public void closeConnection() {
        try {
            _socket.close();
        } catch (IOException ioe) {}
    }
    
    /**
     * Sets the request.
     * 
     * @param request the new request
     */    
    public void setRequest(Request request) {
        _request = request;
    }
    
    /**
     * Gets the request.
     * 
     * @return the request
     */    
    public Request getRequest() {
        return _request;
    }
    
    /**
     * Sets the response.
     * 
     * @param response the new response
     */    
    public void setResponse(Response response) {
        _response = response;
    }
    
    /**
     * Gets the response.
     * 
     * @return the response
     */    
    public Response getResponse() {
        return _response;
    }
    
}
