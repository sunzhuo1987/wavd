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

package jcifs;

import java.util.Properties;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import jcifs.util.LogStream;

// TODO: Auto-generated Javadoc
/**
 * The Class Config.
 */

public class Config {

    /** The prp. */

    private static Properties prp = new Properties();
    
    /** The log. */
    private static LogStream log;

    static {
        String filename;
        int level;
        FileInputStream in = null;

        log = LogStream.getInstance();

        try {
            filename = System.getProperty( "jcifs.properties" );
            if( filename != null && filename.length() > 1 ) {
                in = new FileInputStream( filename );
            }
            Config.load( in );
        } catch( IOException ioe ) {
            if( log.level > 0 )
                ioe.printStackTrace( log );
        }

        if(( level = Config.getInt( "jcifs.util.loglevel", -1 )) != -1 ) {
            LogStream.setLevel( level );
        }

        if( log.level > 2 ) {
            try {
                prp.store( log, "JCIFS PROPERTIES" );
            } catch( IOException ioe ) {
            }
        }
    }

    /**
     * Register smb url handler.
     */

    public static void registerSmbURLHandler() {
        String ver, pkgs;

        ver = System.getProperty( "java.version" );
        if( ver.startsWith( "1.1." ) || ver.startsWith( "1.2." )) {
             throw new RuntimeException( "jcifs-0.7.0b4+ requires Java 1.3 or above. You are running " + ver );
        }
        pkgs = System.getProperty( "java.protocol.handler.pkgs" );
        if( pkgs == null ) {
            System.setProperty( "java.protocol.handler.pkgs", "jcifs" );
        } else if( pkgs.indexOf( "jcifs" ) == -1 ) {
            pkgs += "|jcifs";
            System.setProperty( "java.protocol.handler.pkgs", pkgs );
        }
    }

    // supress javadoc constructor summary by removing 'protected'
    /**
     * Instantiates a new config.
     */
    Config() {}

    /**
     * Sets the properties.
     * 
     * @param prp the new properties
     */

    public static void setProperties( Properties prp ) {
        Config.prp = new Properties( prp );
        try {
            Config.prp.putAll( System.getProperties() );
        } catch( SecurityException se ) {
            if( log.level > 1 )
                log.println( "SecurityException: jcifs will ignore System properties" );
        }
    }

    /**
     * Load.
     * 
     * @param in the in
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */

    public static void load( InputStream in ) throws IOException {
        if( in != null ) {
            prp.load( in );
        }
        try {
            prp.putAll( System.getProperties() );
        } catch( SecurityException se ) {
            if( log.level > 1 )
                log.println( "SecurityException: jcifs will ignore System properties" );
        }
    }

    /**
     * Store.
     * 
     * @param out the out
     * @param header the header
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void store( OutputStream out, String header ) throws IOException {
        prp.store( out, header );
    }

    /**
     * List.
     * 
     * @param out the out
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */

    public static void list( PrintStream out ) throws IOException {
        prp.list( out );
    }

    /**
     * Sets the property.
     * 
     * @param key the key
     * @param value the value
     * 
     * @return the object
     */

    public static Object setProperty( String key, String value ) {
        return prp.setProperty( key, value );
    }

    /**
     * Gets the.
     * 
     * @param key the key
     * 
     * @return the object
     */

    public static Object get( String key ) {
        return prp.get( key );
    }

    /**
     * Gets the property.
     * 
     * @param key the key
     * @param def the def
     * 
     * @return the property
     */

    public static String getProperty( String key, String def ) {
        return prp.getProperty( key, def );
    }

    /**
     * Gets the property.
     * 
     * @param key the key
     * 
     * @return the property
     */

    public static String getProperty( String key ) {
        return prp.getProperty( key );
    }

    /**
     * Gets the int.
     * 
     * @param key the key
     * @param def the def
     * 
     * @return the int
     */

    public static int getInt( String key, int def ) {
        String s = prp.getProperty( key );
        if( s != null ) {
            try {
                def = Integer.parseInt( s );
            } catch( NumberFormatException nfe ) {
                if( log.level > 0 )
                    nfe.printStackTrace( log );
            }
        }
        return def;
    }

    /**
     * Gets the int.
     * 
     * @param key the key
     * 
     * @return the int
     */

    public static int getInt( String key ) {
        String s = prp.getProperty( key );
        int result = -1;
        if( s != null ) {
            try {
                result = Integer.parseInt( s );
            } catch( NumberFormatException nfe ) {
                if( log.level > 0 )
                    nfe.printStackTrace( log );
            }
        }
        return result;
    }

    /**
     * Gets the long.
     * 
     * @param key the key
     * @param def the def
     * 
     * @return the long
     */

    public static long getLong( String key, long def ) {
        String s = prp.getProperty( key );
        if( s != null ) {
            try {
                def = Long.parseLong( s );
            } catch( NumberFormatException nfe ) {
                if( log.level > 0 )
                    nfe.printStackTrace( log );
            }
        }
        return def;
    }

    /**
     * Gets the inet address.
     * 
     * @param key the key
     * @param def the def
     * 
     * @return the inet address
     */

    public static InetAddress getInetAddress( String key, InetAddress def ) {
        String addr = prp.getProperty( key );
        if( addr != null ) {
            try {
                def = InetAddress.getByName( addr );
            } catch( UnknownHostException uhe ) {
                if( log.level > 0 ) {
                    log.println( addr );
                    uhe.printStackTrace( log );
                }
            }
        }
        return def;
    }
    
    /**
     * Gets the local host.
     * 
     * @return the local host
     */
    public static InetAddress getLocalHost() {
        String addr = prp.getProperty( "jcifs.smb.client.laddr" );

        if (addr != null) {
            try {
                return InetAddress.getByName( addr );
            } catch( UnknownHostException uhe ) {
                if( log.level > 0 ) {
                    log.println( "Ignoring jcifs.smb.client.laddr address: " + addr );
                    uhe.printStackTrace( log );
                }
            }
        }

        return null;
    }

    /**
     * Gets the boolean.
     * 
     * @param key the key
     * @param def the def
     * 
     * @return the boolean
     */

    public static boolean getBoolean( String key, boolean def ) {
        String b = getProperty( key );
        if( b != null ) {
            def = b.toLowerCase().equals( "true" );
        }
        return def;
    }

    /**
     * Gets the inet address array.
     * 
     * @param key the key
     * @param delim the delim
     * @param def the def
     * 
     * @return the inet address array
     */

    public static InetAddress[] getInetAddressArray( String key, String delim, InetAddress[] def ) {
        String p = getProperty( key );
        if( p != null ) {
            StringTokenizer tok = new StringTokenizer( p, delim );
            int len = tok.countTokens();
            InetAddress[] arr = new InetAddress[len];
            for( int i = 0; i < len; i++ ) {
                String addr = tok.nextToken();
                try {
                    arr[i] = InetAddress.getByName( addr );
                } catch( UnknownHostException uhe ) {
                    if( log.level > 0 ) {
                        log.println( addr );
                        uhe.printStackTrace( log );
                    }
                    return def;
                }
            }
            return arr;
        }
        return def;
    }
}

