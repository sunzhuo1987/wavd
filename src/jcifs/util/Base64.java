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

// TODO: Auto-generated Javadoc
/**
 * The Class Base64.
 */
public class Base64 {

    /** The Constant ALPHABET. */
    private static final String ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    /**
     * Encode.
     * 
     * @param bytes the bytes
     * 
     * @return the string
     */
    public static String encode(byte[] bytes) {
        int length = bytes.length;
        if (length == 0) return "";
        StringBuffer buffer =
                new StringBuffer((int) Math.ceil((double) length / 3d) * 4);
        int remainder = length % 3;
        length -= remainder;
        int block;
        int i = 0;
        while (i < length) {
            block = ((bytes[i++] & 0xff) << 16) | ((bytes[i++] & 0xff) << 8) |
                    (bytes[i++] & 0xff);
            buffer.append(ALPHABET.charAt(block >>> 18));
            buffer.append(ALPHABET.charAt((block >>> 12) & 0x3f));
            buffer.append(ALPHABET.charAt((block >>> 6) & 0x3f));
            buffer.append(ALPHABET.charAt(block & 0x3f));
        }
        if (remainder == 0) return buffer.toString();
        if (remainder == 1) {
            block = (bytes[i] & 0xff) << 4;
            buffer.append(ALPHABET.charAt(block >>> 6));
            buffer.append(ALPHABET.charAt(block & 0x3f));
            buffer.append("==");
            return buffer.toString();
        }
        block = (((bytes[i++] & 0xff) << 8) | ((bytes[i]) & 0xff)) << 2;
        buffer.append(ALPHABET.charAt(block >>> 12));
        buffer.append(ALPHABET.charAt((block >>> 6) & 0x3f));
        buffer.append(ALPHABET.charAt(block & 0x3f));
        buffer.append("=");
        return buffer.toString();
    }

    /**
     * Decode.
     * 
     * @param string the string
     * 
     * @return the byte[]
     */
    public static byte[] decode(String string) {
        int length = string.length();
        if (length == 0) return new byte[0];
        int pad = (string.charAt(length - 2) == '=') ? 2 :
                (string.charAt(length - 1) == '=') ? 1 : 0;
        int size = length * 3 / 4 - pad;
        byte[] buffer = new byte[size];
        int block;
        int i = 0;
        int index = 0;
        while (i < length) {
            block = (ALPHABET.indexOf(string.charAt(i++)) & 0xff) << 18 |
                    (ALPHABET.indexOf(string.charAt(i++)) & 0xff) << 12 |
                    (ALPHABET.indexOf(string.charAt(i++)) & 0xff) << 6 |
                    (ALPHABET.indexOf(string.charAt(i++)) & 0xff);
            buffer[index++] = (byte) (block >>> 16);
            if (index < size) buffer[index++] = (byte) ((block >>> 8) & 0xff);
            if (index < size) buffer[index++] = (byte) (block & 0xff);
        }
        return buffer;
    }

}
