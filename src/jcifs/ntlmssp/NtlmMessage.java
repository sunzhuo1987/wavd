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

package jcifs.ntlmssp;

import jcifs.Config;

// TODO: Auto-generated Javadoc
/**
 * The Class NtlmMessage.
 */
public abstract class NtlmMessage implements NtlmFlags {

    /** The Constant NTLMSSP_SIGNATURE. */
    protected static final byte[] NTLMSSP_SIGNATURE = new byte[] {
        (byte) 'N', (byte) 'T', (byte) 'L', (byte) 'M',
        (byte) 'S', (byte) 'S', (byte) 'P', (byte) 0
    };

    /** The Constant OEM_ENCODING. */
    private static final String OEM_ENCODING =
                Config.getProperty("jcifs.encoding",
                        System.getProperty("file.encoding"));

    /** The flags. */
    private int flags;

    /**
     * Gets the flags.
     * 
     * @return the flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Sets the flags.
     * 
     * @param flags the new flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * Gets the flag.
     * 
     * @param flag the flag
     * 
     * @return the flag
     */
    public boolean getFlag(int flag) {
        return (getFlags() & flag) != 0;
    }

    /**
     * Sets the flag.
     * 
     * @param flag the flag
     * @param value the value
     */
    public void setFlag(int flag, boolean value) {
        setFlags(value ? (getFlags() | flag) :
                (getFlags() & (0xffffffff ^ flag)));
    }

    /**
     * Read u long.
     * 
     * @param src the src
     * @param index the index
     * 
     * @return the int
     */
    static int readULong(byte[] src, int index) {
        return (src[index] & 0xff) |
                ((src[index + 1] & 0xff) << 8) |
                ((src[index + 2] & 0xff) << 16) |
                ((src[index + 3] & 0xff) << 24);
    }

    /**
     * Read u short.
     * 
     * @param src the src
     * @param index the index
     * 
     * @return the int
     */
    static int readUShort(byte[] src, int index) {
        return (src[index] & 0xff) | ((src[index + 1] & 0xff) << 8);
    }

    /**
     * Read security buffer.
     * 
     * @param src the src
     * @param index the index
     * 
     * @return the byte[]
     */
    static byte[] readSecurityBuffer(byte[] src, int index) {
        int length = readUShort(src, index);
        int offset = readULong(src, index + 4);
        byte[] buffer = new byte[length];
        System.arraycopy(src, offset, buffer, 0, length);
        return buffer;
    }

    /**
     * Write u long.
     * 
     * @param dest the dest
     * @param offset the offset
     * @param ulong the ulong
     */
    static void writeULong(byte[] dest, int offset, int ulong) {
        dest[offset] = (byte) (ulong & 0xff);
        dest[offset + 1] = (byte) (ulong >> 8 & 0xff);
        dest[offset + 2] = (byte) (ulong >> 16 & 0xff);
        dest[offset + 3] = (byte) (ulong >> 24 & 0xff);
    }

    /**
     * Write u short.
     * 
     * @param dest the dest
     * @param offset the offset
     * @param ushort the ushort
     */
    static void writeUShort(byte[] dest, int offset, int ushort) {
        dest[offset] = (byte) (ushort & 0xff);
        dest[offset + 1] = (byte) (ushort >> 8 & 0xff);
    }

    /**
     * Write security buffer.
     * 
     * @param dest the dest
     * @param offset the offset
     * @param bodyOffset the body offset
     * @param src the src
     */
    static void writeSecurityBuffer(byte[] dest, int offset, int bodyOffset,
            byte[] src) {
        int length = (src != null) ? src.length : 0;
        if (length == 0) return;
        writeUShort(dest, offset, length);
        writeUShort(dest, offset + 2, length);
        writeULong(dest, offset + 4, bodyOffset);
        System.arraycopy(src, 0, dest, bodyOffset, length);
    }

    /**
     * Gets the oEM encoding.
     * 
     * @return the oEM encoding
     */
    static String getOEMEncoding() {
        return OEM_ENCODING;
    }

    /**
     * To byte array.
     * 
     * @return the byte[]
     */
    public abstract byte[] toByteArray();

}
