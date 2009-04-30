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
//
// $Log: MD4.java,v $
// Revision 1.2  1998/01/05 03:41:19  iang
// Added references only.
//
// Revision 1.1.1.1  1997/11/03 22:36:56  hopwood
// + Imported to CVS (tagged as 'start').
//
// Revision 0.1.0.0  1997/07/14  R. Naffah
// + original version
//
// $Endlog$
/*
 * Copyright (c) 1997 Systemics Ltd
 * on behalf of the Cryptix Development Team.  All rights reserved.
 */

package jcifs.util;

import java.security.MessageDigest;

// TODO: Auto-generated Javadoc
/**
 * The Class MD4.
 */
public class MD4 extends MessageDigest implements Cloneable {
    // MD4 specific object variables
    //...........................................................................

    /** The Constant BLOCK_LENGTH. */
    private static final int BLOCK_LENGTH = 64; // = 512 / 8;

    /** The context. */
    private int[] context = new int[4];

    /** The count. */
    private long count;

    /** The buffer. */
    private byte[] buffer = new byte[BLOCK_LENGTH];

    /** The X. */
    private int[] X = new int[16];

    // Constructors
    //...........................................................................

    /**
     * Instantiates a new m d4.
     */
    public MD4() {
        super("MD4");
        engineReset();
    }

    /**
     * Instantiates a new m d4.
     * 
     * @param md
     *            the md
     */
    private MD4(MD4 md) {
        this();
        context = (int[]) md.context.clone();
        buffer = (byte[]) md.buffer.clone();
        count = md.count;
    }

    // Cloneable method implementation
    //...........................................................................

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigest#clone()
     */
    public Object clone() {
        return new MD4(this);
    }

    // JCE methods
    //...........................................................................

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineReset()
     */
    public void engineReset() {
        // initial values of MD4 i.e. A, B, C, D
        // as per rfc-1320; they are low-order byte first
        context[0] = 0x67452301;
        context[1] = 0xEFCDAB89;
        context[2] = 0x98BADCFE;
        context[3] = 0x10325476;
        count = 0L;
        for (int i = 0; i < BLOCK_LENGTH; i++)
            buffer[i] = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineUpdate(byte)
     */
    public void engineUpdate(byte b) {
        // compute number of bytes still unhashed; ie. present in buffer
        int i = (int) (count % BLOCK_LENGTH);
        count++; // update number of bytes
        buffer[i] = b;
        if (i == BLOCK_LENGTH - 1)
            transform(buffer, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineUpdate(byte[], int, int)
     */
    public void engineUpdate(byte[] input, int offset, int len) {
        // make sure we don't exceed input's allocated size/length
        if (offset < 0 || len < 0 || (long) offset + len > input.length)
            throw new ArrayIndexOutOfBoundsException();

        // compute number of bytes still unhashed; ie. present in buffer
        int bufferNdx = (int) (count % BLOCK_LENGTH);
        count += len; // update number of bytes
        int partLen = BLOCK_LENGTH - bufferNdx;
        int i = 0;
        if (len >= partLen) {
            System.arraycopy(input, offset, buffer, bufferNdx, partLen);

            transform(buffer, 0);

            for (i = partLen; i + BLOCK_LENGTH - 1 < len; i += BLOCK_LENGTH)
                transform(input, offset + i);
            bufferNdx = 0;
        }
        // buffer remaining input
        if (i < len)
            System.arraycopy(input, offset + i, buffer, bufferNdx, len - i);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.MessageDigestSpi#engineDigest()
     */
    public byte[] engineDigest() {
        // pad output to 56 mod 64; as RFC1320 puts it: congruent to 448 mod 512
        int bufferNdx = (int) (count % BLOCK_LENGTH);
        int padLen = (bufferNdx < 56) ? (56 - bufferNdx) : (120 - bufferNdx);

        // padding is alwas binary 1 followed by binary 0s
        byte[] tail = new byte[padLen + 8];
        tail[0] = (byte) 0x80;

        // append length before final transform:
        // save number of bits, casting the long to an array of 8 bytes
        // save low-order byte first.
        for (int i = 0; i < 8; i++)
            tail[padLen + i] = (byte) ((count * 8) >>> (8 * i));

        engineUpdate(tail, 0, tail.length);

        byte[] result = new byte[16];
        // cast this MD4's context (array of 4 ints) into an array of 16 bytes.
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                result[i * 4 + j] = (byte) (context[i] >>> (8 * j));

        // reset the engine
        engineReset();
        return result;
    }

    // own methods
    //...........................................................................

    /**
     * Transform.
     * 
     * @param block
     *            the block
     * @param offset
     *            the offset
     */
    private void transform(byte[] block, int offset) {

        // encodes 64 bytes from input block into an array of 16 32-bit
        // entities. Use A as a temp var.
        for (int i = 0; i < 16; i++)
            X[i] = (block[offset++] & 0xFF) | (block[offset++] & 0xFF) << 8 | (block[offset++] & 0xFF) << 16
                    | (block[offset++] & 0xFF) << 24;

        int A = context[0];
        int B = context[1];
        int C = context[2];
        int D = context[3];

        A = FF(A, B, C, D, X[0], 3);
        D = FF(D, A, B, C, X[1], 7);
        C = FF(C, D, A, B, X[2], 11);
        B = FF(B, C, D, A, X[3], 19);
        A = FF(A, B, C, D, X[4], 3);
        D = FF(D, A, B, C, X[5], 7);
        C = FF(C, D, A, B, X[6], 11);
        B = FF(B, C, D, A, X[7], 19);
        A = FF(A, B, C, D, X[8], 3);
        D = FF(D, A, B, C, X[9], 7);
        C = FF(C, D, A, B, X[10], 11);
        B = FF(B, C, D, A, X[11], 19);
        A = FF(A, B, C, D, X[12], 3);
        D = FF(D, A, B, C, X[13], 7);
        C = FF(C, D, A, B, X[14], 11);
        B = FF(B, C, D, A, X[15], 19);

        A = GG(A, B, C, D, X[0], 3);
        D = GG(D, A, B, C, X[4], 5);
        C = GG(C, D, A, B, X[8], 9);
        B = GG(B, C, D, A, X[12], 13);
        A = GG(A, B, C, D, X[1], 3);
        D = GG(D, A, B, C, X[5], 5);
        C = GG(C, D, A, B, X[9], 9);
        B = GG(B, C, D, A, X[13], 13);
        A = GG(A, B, C, D, X[2], 3);
        D = GG(D, A, B, C, X[6], 5);
        C = GG(C, D, A, B, X[10], 9);
        B = GG(B, C, D, A, X[14], 13);
        A = GG(A, B, C, D, X[3], 3);
        D = GG(D, A, B, C, X[7], 5);
        C = GG(C, D, A, B, X[11], 9);
        B = GG(B, C, D, A, X[15], 13);

        A = HH(A, B, C, D, X[0], 3);
        D = HH(D, A, B, C, X[8], 9);
        C = HH(C, D, A, B, X[4], 11);
        B = HH(B, C, D, A, X[12], 15);
        A = HH(A, B, C, D, X[2], 3);
        D = HH(D, A, B, C, X[10], 9);
        C = HH(C, D, A, B, X[6], 11);
        B = HH(B, C, D, A, X[14], 15);
        A = HH(A, B, C, D, X[1], 3);
        D = HH(D, A, B, C, X[9], 9);
        C = HH(C, D, A, B, X[5], 11);
        B = HH(B, C, D, A, X[13], 15);
        A = HH(A, B, C, D, X[3], 3);
        D = HH(D, A, B, C, X[11], 9);
        C = HH(C, D, A, B, X[7], 11);
        B = HH(B, C, D, A, X[15], 15);

        context[0] += A;
        context[1] += B;
        context[2] += C;
        context[3] += D;
    }

    // The basic MD4 atomic functions.

    /**
     * FF.
     * 
     * @param a
     *            the a
     * @param b
     *            the b
     * @param c
     *            the c
     * @param d
     *            the d
     * @param x
     *            the x
     * @param s
     *            the s
     * 
     * @return the int
     */
    private int FF(int a, int b, int c, int d, int x, int s) {
        int t = a + ((b & c) | (~b & d)) + x;
        return t << s | t >>> (32 - s);
    }

    /**
     * GG.
     * 
     * @param a
     *            the a
     * @param b
     *            the b
     * @param c
     *            the c
     * @param d
     *            the d
     * @param x
     *            the x
     * @param s
     *            the s
     * 
     * @return the int
     */
    private int GG(int a, int b, int c, int d, int x, int s) {
        int t = a + ((b & (c | d)) | (c & d)) + x + 0x5A827999;
        return t << s | t >>> (32 - s);
    }

    /**
     * HH.
     * 
     * @param a
     *            the a
     * @param b
     *            the b
     * @param c
     *            the c
     * @param d
     *            the d
     * @param x
     *            the x
     * @param s
     *            the s
     * 
     * @return the int
     */
    private int HH(int a, int b, int c, int d, int x, int s) {
        int t = a + (b ^ c ^ d) + x + 0x6ED9EBA1;
        return t << s | t >>> (32 - s);
    }
}
