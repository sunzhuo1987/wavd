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

package jcifs.smb;

import java.io.UnsupportedEncodingException;
import java.io.Serializable;
import java.security.Principal;
import java.util.Random;
import java.util.Arrays;
import jcifs.Config;
import jcifs.util.LogStream;
import jcifs.util.DES;
import jcifs.util.MD4;
import jcifs.util.HMACT64;

// TODO: Auto-generated Javadoc
/**
 * The Class NtlmPasswordAuthentication.
 */

public final class NtlmPasswordAuthentication implements Principal, Serializable {

    /** The Constant LM_COMPATIBILITY. */
    private static final int LM_COMPATIBILITY =
            Config.getInt("jcifs.smb.lmCompatibility", 0);

    /** The Constant OEM_ENCODING. */
    static final String OEM_ENCODING =
                Config.getProperty( "jcifs.encoding",
                        System.getProperty( "file.encoding" ));

    /** The Constant RANDOM. */
    private static final Random RANDOM = new Random();

    /** The log. */
    private static LogStream log = LogStream.getInstance();

    // KGS!@#$%
    /** The Constant S8. */
    private static final byte[] S8 = {
        (byte)0x4b, (byte)0x47, (byte)0x53, (byte)0x21,
        (byte)0x40, (byte)0x23, (byte)0x24, (byte)0x25
    };
    
    /**
     * E.
     * 
     * @param key the key
     * @param data the data
     * @param e the e
     */
    private static void E( byte[] key, byte[] data, byte[] e ) {
        byte[] key7 = new byte[7];
        byte[] e8 = new byte[8];

        for( int i = 0; i < key.length / 7; i++ ) {
            System.arraycopy( key, i * 7, key7, 0, 7 );
            DES des = new DES( key7 );
            des.encrypt( data, e8 );
            System.arraycopy( e8, 0, e, i * 8, 8 );
        }
    }

    /** The DEFAUL t_ domain. */
    static String DEFAULT_DOMAIN;
    
    /** The DEFAUL t_ username. */
    static String DEFAULT_USERNAME;
    
    /** The DEFAUL t_ password. */
    static String DEFAULT_PASSWORD;
    
    /** The Constant BLANK. */
    static final String BLANK = "";

    /**
     * Inits the defaults.
     */
    static void initDefaults() {
        if (DEFAULT_DOMAIN != null) return;
        DEFAULT_DOMAIN = Config.getProperty("jcifs.smb.client.domain", "?");
        DEFAULT_USERNAME = Config.getProperty("jcifs.smb.client.username", "GUEST");
        DEFAULT_PASSWORD = Config.getProperty("jcifs.smb.client.password", BLANK);
    }

/**
 * Gets the pre ntlm response.
 * 
 * @param password the password
 * @param challenge the challenge
 * 
 * @return the pre ntlm response
 */
    static public byte[] getPreNTLMResponse( String password, byte[] challenge ) {
        byte[] p14 = new byte[14];
        byte[] p21 = new byte[21];
        byte[] p24 = new byte[24];
        byte[] passwordBytes;
        try {
            passwordBytes = password.toUpperCase().getBytes( OEM_ENCODING );
        } catch( UnsupportedEncodingException uee ) {
            return null;
        }
        int passwordLength = passwordBytes.length;

        // Only encrypt the first 14 bytes of the password for Pre 0.12 NT LM
        if( passwordLength > 14) {
            passwordLength = 14;
        }
        System.arraycopy( passwordBytes, 0, p14, 0, passwordLength );
        E( p14, S8, p21);
        E( p21, challenge, p24);
        return p24;
    }

/**
 * Gets the nTLM response.
 * 
 * @param password the password
 * @param challenge the challenge
 * 
 * @return the nTLM response
 */
    static public byte[] getNTLMResponse( String password, byte[] challenge ) {
        byte[] uni = null;
        byte[] p21 = new byte[21];
        byte[] p24 = new byte[24];

        try {
            uni = password.getBytes( "UnicodeLittleUnmarked" );
        } catch( UnsupportedEncodingException uee ) {
            if( log.level > 0 )
                uee.printStackTrace( log );
        }
        MD4 md4 = new MD4();
        md4.update( uni );
        try {
            md4.digest(p21, 0, 16);
        } catch (Exception ex) {
            if( log.level > 0 )
                ex.printStackTrace( log );
        }
        E( p21, challenge, p24 );
        return p24;
    }

    /**
     * Gets the l mv2 response.
     * 
     * @param domain the domain
     * @param user the user
     * @param password the password
     * @param challenge the challenge
     * @param clientChallenge the client challenge
     * 
     * @return the l mv2 response
     */ 
    public static byte[] getLMv2Response(String domain, String user,
            String password, byte[] challenge, byte[] clientChallenge) {
        try {
            byte[] hash = new byte[16];
            byte[] response = new byte[24];
            MD4 md4 = new MD4();
            md4.update(password.getBytes("UnicodeLittleUnmarked"));
            HMACT64 hmac = new HMACT64(md4.digest());
            hmac.update(user.toUpperCase().getBytes("UnicodeLittleUnmarked"));
            hmac.update(domain.toUpperCase().getBytes("UnicodeLittleUnmarked"));
            hmac = new HMACT64(hmac.digest());
            hmac.update(challenge);
            hmac.update(clientChallenge);
            hmac.digest(response, 0, 16);
            System.arraycopy(clientChallenge, 0, response, 16, 8);
            return response;
        } catch (Exception ex) {
            if( log.level > 0 )
                ex.printStackTrace( log );
            return null;
        }
    }

    /** The Constant NULL. */
    static final NtlmPasswordAuthentication NULL =
                new NtlmPasswordAuthentication( "", "", "" );
    
    /** The Constant GUEST. */
    static final NtlmPasswordAuthentication GUEST =
                new NtlmPasswordAuthentication( "?", "GUEST", "" );
    
    /** The Constant DEFAULT. */
    static final NtlmPasswordAuthentication DEFAULT =
                new NtlmPasswordAuthentication( null );

    /** The domain. */
    String domain;
    
    /** The username. */
    String username;
    
    /** The password. */
    String password;
    
    /** The ansi hash. */
    byte[] ansiHash;
    
    /** The unicode hash. */
    byte[] unicodeHash;
    
    /** The hashes external. */
    boolean hashesExternal = false;
    
    /** The client challenge. */
    byte[] clientChallenge = null;
    
    /** The challenge. */
    byte[] challenge = null;

/**
 * Instantiates a new ntlm password authentication.
 * 
 * @param userInfo the user info
 */

    public NtlmPasswordAuthentication( String userInfo ) {
        domain = username = password = null;

        if( userInfo != null ) {
            int i, u, end;
            char c;

            end = userInfo.length();
            for( i = 0, u = 0; i < end; i++ ) {
                c = userInfo.charAt( i );
                if( c == ';' ) {
                    domain = userInfo.substring( 0, i );
                    u = i + 1;
                } else if( c == ':' ) {
                    password = userInfo.substring( i + 1 );
                    break;
                }
            }
            username = userInfo.substring( u, i );
        }

        initDefaults();

        if( domain == null ) this.domain = DEFAULT_DOMAIN;
        if( username == null ) this.username = DEFAULT_USERNAME;
        if( password == null ) this.password = DEFAULT_PASSWORD;
    }

/**
 * Instantiates a new ntlm password authentication.
 * 
 * @param domain the domain
 * @param username the username
 * @param password the password
 */
    public NtlmPasswordAuthentication( String domain, String username, String password ) {
        this.domain = domain;
        this.username = username;
        this.password = password;

        initDefaults();

        if( domain == null ) this.domain = DEFAULT_DOMAIN;
        if( username == null ) this.username = DEFAULT_USERNAME;
        if( password == null ) this.password = DEFAULT_PASSWORD;
    }

/**
 * Instantiates a new ntlm password authentication.
 * 
 * @param domain the domain
 * @param username the username
 * @param challenge the challenge
 * @param ansiHash the ansi hash
 * @param unicodeHash the unicode hash
 */
    public NtlmPasswordAuthentication( String domain, String username,
                    byte[] challenge, byte[] ansiHash, byte[] unicodeHash ) {
        if( domain == null || username == null ||
                                    ansiHash == null || unicodeHash == null ) {
            throw new IllegalArgumentException( "External credentials cannot be null" );
        }
        this.domain = domain;
        this.username = username;
        this.password = null;
        this.challenge = challenge;
        this.ansiHash = ansiHash;
        this.unicodeHash = unicodeHash;
        hashesExternal = true;
    }

/**
 * Gets the domain.
 * 
 * @return the domain
 */
    public String getDomain() {
        return domain;
    }

/**
 * Gets the username.
 * 
 * @return the username
 */
    public String getUsername() {
        return username;
    }

/**
 * Gets the password.
 * 
 * @return the password
 */
    public String getPassword() {
        return password;
    }

/* (non-Javadoc)
 * @see java.security.Principal#getName()
 */
    public String getName() {
        boolean d = domain.length() > 0 && domain.equals( "?" ) == false;
        return d ? domain + "\\" + username : username;
    }

/**
 * Gets the ansi hash.
 * 
 * @param challenge the challenge
 * 
 * @return the ansi hash
 */
    public byte[] getAnsiHash( byte[] challenge ) {
        if( hashesExternal ) {
            return ansiHash;
        }
        switch (LM_COMPATIBILITY) {
        case 0:
        case 1:
            return getPreNTLMResponse( password, challenge );
        case 2:
            return getNTLMResponse( password, challenge );
        case 3:
        case 4:
        case 5:
            if( clientChallenge == null ) {
                clientChallenge = new byte[8];
                RANDOM.nextBytes( clientChallenge );
            }
            return getLMv2Response(domain, username, password, challenge,
                    clientChallenge);
        default:
            return getPreNTLMResponse( password, challenge );
        }
    }

/**
 * Gets the unicode hash.
 * 
 * @param challenge the challenge
 * 
 * @return the unicode hash
 */
    public byte[] getUnicodeHash( byte[] challenge ) {
        if( hashesExternal ) {
            return unicodeHash;
        }
        switch (LM_COMPATIBILITY) {
        case 0:
        case 1:
        case 2:
            return getNTLMResponse( password, challenge );
        case 3:
        case 4:
        case 5:
            /*
            if( clientChallenge == null ) {
                clientChallenge = new byte[8];
                RANDOM.nextBytes( clientChallenge );
            }
            return getNTLMv2Response(domain, username, password, null,
                    challenge, clientChallenge);
            */
            return new byte[0];
        default:
            return getNTLMResponse( password, challenge );
        }
    }

    /**
     * Gets the user session key.
     * 
     * @param challenge the challenge
     * 
     * @return the user session key
     */
    public byte[] getUserSessionKey(byte[] challenge) {
        if (hashesExternal) return null;
        byte[] key = new byte[16];
        try {
            getUserSessionKey(challenge, key, 0); 
        } catch (Exception ex) {
            if( log.level > 0 )
                ex.printStackTrace( log );
        }
        return key; 
    }

    /**
     * Gets the user session key.
     * 
     * @param challenge the challenge
     * @param dest the dest
     * @param offset the offset
     * 
     * @return the user session key
     * 
     * @throws Exception the exception
     */
    void getUserSessionKey(byte[] challenge, byte[] dest, int offset)
            throws Exception {
        if (hashesExternal) return;
        MD4 md4 = new MD4();
        md4.update(password.getBytes("UnicodeLittleUnmarked")); 
        switch (LM_COMPATIBILITY) {
        case 0:
        case 1:
        case 2:
            md4.update(md4.digest()); 
            md4.digest(dest, offset, 16); 
            break; 
        case 3:
        case 4:
        case 5:
            if( clientChallenge == null ) {
                clientChallenge = new byte[8];
                RANDOM.nextBytes( clientChallenge );
            }

            HMACT64 hmac = new HMACT64(md4.digest());
            hmac.update(username.toUpperCase().getBytes(
                    "UnicodeLittleUnmarked"));
            hmac.update(domain.toUpperCase().getBytes(
                    "UnicodeLittleUnmarked"));
            byte[] ntlmv2Hash = hmac.digest();
            hmac = new HMACT64(ntlmv2Hash);
            hmac.update(challenge);
            hmac.update(clientChallenge); 
            HMACT64 userKey = new HMACT64(ntlmv2Hash); 
            userKey.update(hmac.digest()); 
            userKey.digest(dest, offset, 16); 
            break; 
        default: 
            md4.update(md4.digest()); 
            md4.digest(dest, offset, 16); 
            break; 
        } 
    } 

/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
    public boolean equals( Object obj ) {
        if( obj instanceof NtlmPasswordAuthentication ) {
            NtlmPasswordAuthentication ntlm = (NtlmPasswordAuthentication)obj;
            if( ntlm.domain.toUpperCase().equals( domain.toUpperCase() ) &&
                        ntlm.username.toUpperCase().equals( username.toUpperCase() )) {
                if( hashesExternal && ntlm.hashesExternal ) {
                    return Arrays.equals( ansiHash, ntlm.ansiHash ) &&
                                Arrays.equals( unicodeHash, ntlm.unicodeHash );
                    /* This still isn't quite right. If one npa object does not have external
                     * hashes and the other does then they will not be considered equal even
                     * though they may be.
                     */
                } else if( !hashesExternal && password.equals( ntlm.password )) {
                    return true;
                }
            }
        }
        return false;
    }


/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
    public int hashCode() {
        return getName().toUpperCase().hashCode();
    }

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
    public String toString() {
        return getName();
    }
}

