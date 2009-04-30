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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.lnmiit.wavd.model.Request;
import edu.lnmiit.wavd.model.Response;

// TODO: Auto-generated Javadoc
/**
 * The Class FetcherQueue.
 */
public class FetcherQueue {

    /** The _handler. */
    private ConversationHandler _handler;

    /** The _fetchers. */
    private Fetcher[] _fetchers;

    /** The _request delay. */
    private int _requestDelay;

    /** The _last request. */
    private long _lastRequest = 0;

    /** The _request queue. */
    private List _requestQueue = new ArrayList();

    /** The _running. */
    private boolean _running = true;

    /** The _pending. */
    private int _pending = 0;

    /**
     * Instantiates a new fetcher queue.
     * 
     * @param name
     *            the name
     * @param handler
     *            the handler
     * @param threads
     *            the threads
     * @param requestDelay
     *            the request delay
     */
    public FetcherQueue(String name, ConversationHandler handler, int threads, int requestDelay) {
        _handler = handler;
        _fetchers = new Fetcher[threads];
        _requestDelay = requestDelay;
        for (int i = 0; i < threads; i++) {
            _fetchers[i] = new Fetcher(name + "-" + i);
        }
        start();
    }

    /**
     * Stop.
     */
    public void stop() {
        _running = false;
    }

    /**
     * Start.
     */
    public void start() {
        _running = true;
        for (int i = 0; i < _fetchers.length; i++) {
            _fetchers[i].start();
        }

    }

    /**
     * Checks if is busy.
     * 
     * @return true, if is busy
     */
    public boolean isBusy() {
        return _pending > 0 || getRequestsQueued() > 0;
    }

    /**
     * Submit.
     * 
     * @param request
     *            the request
     */
    public void submit(Request request) {
        synchronized (_requestQueue) {
            _requestQueue.add(request);
            _requestQueue.notify();
        }
    }

    /**
     * Gets the requests queued.
     * 
     * @return the requests queued
     */
    public int getRequestsQueued() {
        synchronized (_requestQueue) {
            return _requestQueue.size();
        }
    }

    /**
     * Clear request queue.
     */
    public void clearRequestQueue() {
        synchronized (_requestQueue) {
            _requestQueue.clear();
        }
    }

    /**
     * Response received.
     * 
     * @param response
     *            the response
     */
    private void responseReceived(Response response) {
        _handler.responseReceived(response);
        _pending--;
    }

    /**
     * Request error.
     * 
     * @param request
     *            the request
     * @param ioe
     *            the ioe
     */
    private void requestError(Request request, IOException ioe) {
        _handler.requestError(request, ioe);
        _pending--;
    }

    /**
     * Gets the next request.
     * 
     * @return the next request
     */
    private Request getNextRequest() {
        synchronized (_requestQueue) {
            while (_requestQueue.size() == 0) {
                try {
                    _requestQueue.wait();
                } catch (InterruptedException ie) {
                    // check again
                }
            }
            if (_requestDelay > 0) {
                long currentTimeMillis = System.currentTimeMillis();
                while (currentTimeMillis < _lastRequest + _requestDelay) {
                    try {
                        Thread.sleep(_lastRequest + _requestDelay - currentTimeMillis);
                    } catch (InterruptedException ie) {
                    }
                    currentTimeMillis = System.currentTimeMillis();
                }
                _lastRequest = currentTimeMillis;
            }
            _pending++;
            return (Request) _requestQueue.remove(0);
        }
    }

    /**
     * The Class Fetcher.
     */
    private class Fetcher extends Thread {

        /**
         * Instantiates a new fetcher.
         * 
         * @param name
         *            the name
         */
        public Fetcher(String name) {
            super(name);
            setDaemon(true);
            setPriority(Thread.MIN_PRIORITY);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Thread#run()
         */
        public void run() {
            while (_running) {
                Request request = getNextRequest();
                try {
                    Response response = HTTPClientFactory.getInstance().fetchResponse(request);
                    response.flushContentStream();
                    responseReceived(response);
                } catch (IOException ioe) {
                    requestError(request, ioe);
                }
            }
        }
    }
}
