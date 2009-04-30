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

import java.io.OutputStream;
import java.io.PrintStream;

// TODO: Auto-generated Javadoc
/**
 * The Class Hexdump.
 */

public class Hexdump {

    /** The Constant NL. */
    private static final String NL = System.getProperty( "line.separator" );
    
    /** The Constant NL_LENGTH. */
    private static final int NL_LENGTH = NL.length();

    /** The Constant SPACE_CHARS. */
    private static final char[] SPACE_CHARS = {
        ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
        ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
        ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '
    };

    /** The Constant HEX_DIGITS. */
    public static final char[] HEX_DIGITS = { 
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'A', 'B',
        'C', 'D', 'E', 'F'
    };

/**
 * Hexdump.
 * 
 * @param ps the ps
 * @param src the src
 * @param srcIndex the src index
 * @param length the length
 */

    public static void hexdump( PrintStream ps, byte[] src, int srcIndex, int length ) {
        if( length == 0 ) {
            return;
        }

        int s = length % 16;
        int r = ( s == 0 ) ? length / 16 : length / 16 + 1;
        char[] c = new char[r * (74 + NL_LENGTH)];
        char[] d = new char[16];
        int i;
        int si = 0;
        int ci = 0;

        do {
            toHexChars( si, c, ci, 5 );
            ci += 5;
            c[ci++] = ':';
            do {
                if( si == length ) {
                    int n = 16 - s;
                    System.arraycopy( SPACE_CHARS, 0, c, ci, n * 3 );
                    ci += n * 3;
                    System.arraycopy( SPACE_CHARS, 0, d, s, n );
                    break;
                }
                c[ci++] = ' ';
                i = src[srcIndex + si] & 0xFF;
                toHexChars( i, c, ci, 2 );
                ci += 2; 
                if( i < 0 || Character.isISOControl( (char)i )) {
                    d[si % 16] = '.';
                } else {
                    d[si % 16] = (char)i;
                }
            } while(( ++si % 16 ) != 0 );
            c[ci++] = ' ';
            c[ci++] = ' ';
            c[ci++] = '|';
            System.arraycopy( d, 0, c, ci, 16 );
            ci += 16;
            c[ci++] = '|';
            NL.getChars( 0, NL_LENGTH, c, ci );
            ci += NL_LENGTH;
        } while( si < length );

        ps.println( c );
    }

/**
 * To hex string.
 * 
 * @param val the val
 * @param size the size
 * 
 * @return the string
 */ 
    public static String toHexString( int val, int size ) {
        char[] c = new char[size];
        toHexChars( val, c, 0, size );
        return new String( c );
    }
    
    /**
     * To hex string.
     * 
     * @param val the val
     * @param size the size
     * 
     * @return the string
     */
    public static String toHexString( long val, int size ) {
        char[] c = new char[size];
        toHexChars( val, c, 0, size );
        return new String( c );
    }
    
    /**
     * To hex string.
     * 
     * @param src the src
     * @param srcIndex the src index
     * @param size the size
     * 
     * @return the string
     */
    public static String toHexString( byte[] src, int srcIndex, int size ) {
        char[] c = new char[size];
        size = ( size % 2 == 0 ) ? size / 2 : size / 2 + 1;
        for( int i = 0, j = 0; i < size; i++ ) {
            c[j++] = HEX_DIGITS[(src[i] >> 4 ) & 0x0F];
            if( j == c.length ) {
                break;
            }
            c[j++] = HEX_DIGITS[src[i] & 0x0F];
        }
        return new String( c );
    }

/**
 * To hex chars.
 * 
 * @param val the val
 * @param dst the dst
 * @param dstIndex the dst index
 * @param size the size
 */ 
    public static void toHexChars( int val, char dst[], int dstIndex, int size ) {
        while( size > 0 ) {
            int i = dstIndex + size - 1;
            if( i < dst.length ) {
                dst[i] = HEX_DIGITS[val & 0x000F];
            }
            if( val != 0 ) {
                val >>>= 4;
            }
            size--;
        }
    }
    
    /**
     * To hex chars.
     * 
     * @param val the val
     * @param dst the dst
     * @param dstIndex the dst index
     * @param size the size
     */
    public static void toHexChars( long val, char dst[], int dstIndex, int size ) {
        while( size > 0 ) {
            dst[dstIndex + size - 1] = HEX_DIGITS[(int)( val & 0x000FL )];
            if( val != 0 ) {
                val >>>= 4;
            }
            size--;
        }
    }
}

