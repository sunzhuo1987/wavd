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

package jcifs.util;

import java.security.MessageDigest;

// TODO: Auto-generated Javadoc
/**
 * The Class HMACT64.
 */
public class HMACT64 extends MessageDigest implements Cloneable {

    /** The Constant BLOCK_LENGTH. */
    private static final int BLOCK_LENGTH = 64;

    /** The Constant IPAD. */
    private static final byte IPAD = (byte) 0x36;

    /** The Constant OPAD. */
    private static final byte OPAD = (byte) 0x5c;

    /** The md5. */
    private MessageDigest md5;

    /** The ipad. */
    private byte[] ipad = new byte[BLOCK_LENGTH];

    /** The opad. */
    private byte[] opad = new byte[BLOCK_LENGTH];

    /**
     * Instantiates a new hMAC t64.
     * 
     * @param key
     *            the key
     */
    public HMACT64(byte[] key) {
        super("HMACT64");
        int length = Math.min(key.length, BLOCK_LENGTH);
        for (int i = 0; i < length; i++) {
            ipad[i] = (byte) (key[i] ^ IPAD);
            opad[i] = (byte) (key[i] ^ OPAD);
        }
        for (int i = length; i < BLOCK_LENGTH; i++) {
            ipad[i] = IPAD;
            opad[i] = OPAD;
        }
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }
        engineReset();
    }

    /**
     * Instantiates a new hMAC t64.
     * 
     * @param hmac
     *            the hmac
     * 
     * @throws CloneNotSupportedException
     *             the clone not supported exception
     */
    private HMACT64(HMACT64 hmac) throws CloneNotSupportedException {
        super("HMACT64");
        this.ipad = hmac.ipad;
        this.opad = hmac.opad;
        this.md5 = (MessageDigest) hmac.md5.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigest#clone()
     */
    public Object clone() {
        try {
            return new HMACT64(this);
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineDigest()
     */
    protected byte[] engineDigest() {
        byte[] digest = md5.digest();
        md5.update(opad);
        return md5.digest(digest);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineDigest(byte[], int, int)
     */
    protected int engineDigest(byte[] buf, int offset, int len) {
        byte[] digest = md5.digest();
        md5.update(opad);
        md5.update(digest);
        try {
            return md5.digest(buf, offset, len);
        } catch (Exception ex) {
            throw new IllegalStateException();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineGetDigestLength()
     */
    protected int engineGetDigestLength() {
        return md5.getDigestLength();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineReset()
     */
    protected void engineReset() {
        md5.reset();
        md5.update(ipad);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineUpdate(byte)
     */
    protected void engineUpdate(byte b) {
        md5.update(b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineUpdate(byte[], int, int)
     */
    protected void engineUpdate(byte[] input, int offset, int len) {
        md5.update(input, offset, len);
    }

}
