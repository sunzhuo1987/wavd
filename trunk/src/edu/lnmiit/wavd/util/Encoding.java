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
 * Copyright (c) 2002,2003 Free Software Foundation
 * developed under the custody of the
 * Open Web Application Security Project
 * (http://www.owasp.org)
 *
 * This file is part of the OWASP common library (OCL).
 * OCL is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * OCL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * The valid license text for this file can be retrieved with
 * the call:   java -cp owasp.jar org.owasp.LICENSE
 *
 * If you are not able to view the LICENSE that way, which should
 * always be possible within a valid and working OCL release,
 * please write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * to get a copy of the GNU General Public License or to report a
 * possible license violation.
 */
package edu.lnmiit.wavd.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

// TODO: Auto-generated Javadoc
/**
 * The Class Encoding.
 */
public final class Encoding {

    /**
     * Instantiates a new encoding.
     */
    private Encoding() {
    }

    /** The _base64en. */
    private static byte[] _base64en;

    /** The _base64de. */
    private static byte[] _base64de;

    /** The Constant B64INV. */
    private static final byte B64INV = (byte) 0x80;

    /** Initializes the base64 de-/encoding maps according to RFC 1341, 5.2 */
    static {
        _base64en = new byte[65];
        _base64de = new byte[256];
        Arrays.fill(_base64de, B64INV);
        for (byte i = 0; i < 26; i++) {
            _base64en[i] = (byte) (65 + i);
            _base64en[26 + i] = (byte) (97 + i);
            _base64de[65 + i] = i;
            _base64de[97 + i] = (byte) (26 + i);
        }
        for (byte i = 48; i < 58; i++) {
            _base64en[4 + i] = i;
            _base64de[i] = (byte) (4 + i);
        }
        _base64en[62] = 43;
        _base64en[63] = 47;
        _base64en[64] = 61;
        _base64de[43] = 62;
        _base64de[47] = 63;
        _base64de[61] = 0; // sic!
    }

    /**
     * Base64encode.
     * 
     * @param code
     *            the code
     * 
     * @return the string
     */
    public static String base64encode(byte[] code) {
        if (null == code)
            return null;
        if (0 == code.length)
            return new String();
        int len = code.length;
        // remainder of the encoding process
        int rem = len % 3;
        // size of the destination byte array
        byte[] dst = new byte[4 + (((len - 1) / 3) << 2) + (len / 57)];
        // actual column of the destination string;
        // RFC 1341 requires a linefeed every 58 data bytes
        int column = 0;
        // position within source
        int spos = 0;
        // position within destination
        int dpos = 0;
        // adjust length for loop (remainder is treated separately)
        len -= 2;
        // using a while loop here since spos may be needed for the remainder
        while (spos < len) {
            byte b0 = code[spos];
            byte b1 = code[spos + 1];
            byte b2 = code[spos + 2];
            dst[dpos++] = _base64en[0x3f & (b0 >>> 2)];
            dst[dpos++] = _base64en[(0x30 & (b0 << 4)) + (0x0f & (b1 >>> 4))];
            dst[dpos++] = _base64en[(0x3c & (b1 << 2)) + (0x03 & (b2 >>> 6))];
            dst[dpos++] = _base64en[0x3f & b2];
            spos += 3;
            column += 3;
            if (57 == column) {
                dst[dpos++] = 10;
                column = 0;
            }
        }
        // there may be a remainder to be processed
        if (0 != rem) {
            byte b0 = code[spos];
            dst[dpos++] = _base64en[0x3f & (b0 >>> 2)];
            if (1 == rem) {
                // one-byte remainder
                dst[dpos++] = _base64en[0x30 & (b0 << 4)];
                dst[dpos++] = 61;
            } else {
                // two-byte remainder
                byte b1 = code[spos + 1];
                dst[dpos++] = _base64en[(0x30 & (b0 << 4)) + (0x0f & (b1 >>> 4))];
                dst[dpos++] = _base64en[0x3c & (b1 << 2)];
            }
            dst[dpos++] = 61;
        }
        // using any default encoding is possible, since the base64 char subset
        // is
        // identically represented in all ISO encodings, including US-ASCII
        return new String(dst);
    }

    /**
     * Base64decode.
     * 
     * @param coded
     *            the coded
     * 
     * @return the byte[]
     */
    public static byte[] base64decode(String coded) {
        if (null == coded)
            return null;
        byte[] src = coded.getBytes();
        int len = src.length;
        int dlen = len - (len / 77);
        dlen = (dlen >>> 2) + (dlen >>> 1);
        int rem = 0;
        if (61 == src[len - 1])
            rem++;
        if (61 == src[len - 2])
            rem++;
        dlen -= rem;
        byte[] dst = new byte[dlen];

        int pos = 0;
        int dpos = 0;
        int col = 0;
        // adjust for remainder
        len -= 4;

        while (pos < len) {
            byte b0 = _base64de[src[pos++]];
            byte b1 = _base64de[src[pos++]];
            byte b2 = _base64de[src[pos++]];
            byte b3 = _base64de[src[pos++]];

            if (B64INV == b0 || B64INV == b1 || B64INV == b2 || B64INV == b3)
                throw new RuntimeException("Invalid character at or around position " + pos);

            dst[dpos++] = (byte) ((b0 << 2) | ((b1 >>> 4) & 0x03));
            dst[dpos++] = (byte) ((b1 << 4) | ((b2 >>> 2) & 0x0f));
            dst[dpos++] = (byte) ((b2 << 6) | (b3 & 0x3f));
            col += 4;
            // skip linefeed which is only allowed here; if at that pos is any
            // other char then the input is goofed and we throw an
            // exception
            if (76 == col) {
                if (10 != src[pos++])
                    throw new RuntimeException("No linefeed found at position " + (pos - 1));
                col = 0;
            }
        }

        // process the remainder
        byte b0 = _base64de[src[pos++]];
        byte b1 = _base64de[src[pos++]];
        byte b2 = _base64de[src[pos++]];
        byte b3 = _base64de[src[pos++]];
        if (B64INV == b0 || B64INV == b1 || B64INV == b2 || B64INV == b3)
            throw new RuntimeException("Invalid character at or around position " + pos);

        dst[dpos++] = (byte) ((b0 << 2) | ((b1 >>> 4) & 0x03));
        if (2 == rem)
            return dst;
        dst[dpos++] = (byte) ((b1 << 4) | ((b2 >>> 2) & 0x0f));
        if (1 == rem)
            return dst;
        dst[dpos++] = (byte) ((b2 << 6) | (b3 & 0x3f));

        return dst;
    }

    /**
     * To hex string.
     * 
     * @param b
     *            the b
     * 
     * @return the string
     */
    public static String toHexString(byte[] b) {
        if (null == b)
            return null;
        int len = b.length;
        byte[] hex = new byte[len << 1];
        for (int i = 0, j = 0; i < len; i++, j += 2) {
            hex[j] = (byte) ((b[i] & 0xF0) >> 4);
            hex[j] += 10 > hex[j] ? 48 : 87;
            hex[j + 1] = (byte) (b[i] & 0x0F);
            hex[j + 1] += 10 > hex[j + 1] ? 48 : 87;
        }
        return new String(hex);
    }

    /**
     * Hash m d5.
     * 
     * @param str
     *            the str
     * 
     * @return the string
     */
    public static String hashMD5(String str) {
        return hashMD5(str.getBytes());
    }

    /**
     * Hash m d5.
     * 
     * @param bytes
     *            the bytes
     * 
     * @return the string
     */
    public static String hashMD5(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // it's got to be there
        }
        return toHexString(md.digest());
    }

    /**
     * Hash sha.
     * 
     * @param str
     *            the str
     * 
     * @return the string
     */
    public static String hashSHA(String str) {
        byte[] b = str.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
            md.update(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // it's got to be there
        }
        return toHexString(md.digest());
    }

    /**
     * Rot13.
     * 
     * @param input
     *            the input
     * 
     * @return the string
     */
    public static synchronized String rot13(String input) {
        StringBuffer output = new StringBuffer();
        if (input != null) {
            for (int i = 0; i < input.length(); i++) {
                char inChar = input.charAt(i);
                if ((inChar >= 'A') & (inChar <= 'Z')) {
                    inChar += 13;
                    if (inChar > 'Z') {
                        inChar -= 26;
                    }
                }
                if ((inChar >= 'a') & (inChar <= 'z')) {
                    inChar += 13;
                    if (inChar > 'z') {
                        inChar -= 26;
                    }
                }
                output.append(inChar);
            }
        }
        return output.toString();
    }

    /**
     * Url decode.
     * 
     * @param str
     *            the str
     * 
     * @return the string
     */
    public static String urlDecode(String str) {
        try {
            return (URLDecoder.decode(str, "utf-8"));
        } catch (Exception e) {
            return ("Decoding error");
        }
    }

    /**
     * Url encode.
     * 
     * @param str
     *            the str
     * 
     * @return the string
     */
    public static String urlEncode(String str) {
        try {
            return (URLEncoder.encode(str, "utf-8"));
        } catch (Exception e) {
            return ("Encoding error");
        }
    }

} // class Base64

