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

import java.io.IOException;

import java.net.UnknownHostException;

import java.security.SecureRandom;

import jcifs.Config;

// import jcifs.netbios.NbtAddress;

import jcifs.smb.NtlmPasswordAuthentication;

// TODO: Auto-generated Javadoc
/**
 * The Class Type3Message.
 */
public class Type3Message extends NtlmMessage {

    /** The Constant DEFAULT_FLAGS. */
    private static final int DEFAULT_FLAGS;

    /** The Constant DEFAULT_DOMAIN. */
    private static final String DEFAULT_DOMAIN;

    /** The Constant DEFAULT_USER. */
    private static final String DEFAULT_USER;

    /** The Constant DEFAULT_PASSWORD. */
    private static final String DEFAULT_PASSWORD;

    /** The Constant DEFAULT_WORKSTATION. */
    private static final String DEFAULT_WORKSTATION;

    /** The Constant LM_COMPATIBILITY. */
    private static final int LM_COMPATIBILITY;

    /** The Constant RANDOM. */
    private static final SecureRandom RANDOM = new SecureRandom();

    /** The lm response. */
    private byte[] lmResponse;

    /** The nt response. */
    private byte[] ntResponse;

    /** The domain. */
    private String domain;

    /** The user. */
    private String user;

    /** The workstation. */
    private String workstation;

    /** The session key. */
    private byte[] sessionKey;

    static {
        DEFAULT_FLAGS = NTLMSSP_NEGOTIATE_NTLM |
                (Config.getBoolean("jcifs.smb.client.useUnicode", true) ?
                        NTLMSSP_NEGOTIATE_UNICODE : NTLMSSP_NEGOTIATE_OEM);
        DEFAULT_DOMAIN = Config.getProperty("jcifs.smb.client.domain", null);
        DEFAULT_USER = Config.getProperty("jcifs.smb.client.username", null);
        DEFAULT_PASSWORD = Config.getProperty("jcifs.smb.client.password",
                null);
        String defaultWorkstation = null;
//        try {
//            defaultWorkstation = NbtAddress.getLocalHost().getHostName();
//        } catch (UnknownHostException ex) { }
//        DEFAULT_WORKSTATION = defaultWorkstation;
        DEFAULT_WORKSTATION = "localhost";
        LM_COMPATIBILITY = Config.getInt("jcifs.smb.lmCompatibility", 0);
    }

    /**
     * Instantiates a new type3 message.
     */
    public Type3Message() {
        setFlags(getDefaultFlags());
        setDomain(getDefaultDomain());
        setUser(getDefaultUser());
        setWorkstation(getDefaultWorkstation());
    }

    /**
     * Instantiates a new type3 message.
     * 
     * @param type2 the type2
     */
    public Type3Message(Type2Message type2) {
        setFlags(getDefaultFlags(type2));
        setWorkstation(getDefaultWorkstation());
        String domain = getDefaultDomain();
        setDomain(domain);
        String user = getDefaultUser();
        setUser(user);
        String password = getDefaultPassword();
        switch (LM_COMPATIBILITY) {
        case 0:
        case 1:
            setLMResponse(getLMResponse(type2, password));
            setNTResponse(getNTResponse(type2, password));
            break;
        case 2:
            byte[] nt = getNTResponse(type2, password);
            setLMResponse(nt);
            setNTResponse(nt);
            break;
        case 3:
        case 4:
        case 5:
            byte[] clientChallenge = new byte[8];
            RANDOM.nextBytes(clientChallenge);
            setLMResponse(getLMv2Response(type2, domain, user, password,
                    clientChallenge));
            /*
            setNTResponse(getNTLMv2Response(type2, domain, user, password,
                    clientChallenge));
            */
            break;
        default:
            setLMResponse(getLMResponse(type2, password));
            setNTResponse(getNTResponse(type2, password));
        }
    }

    /**
     * Instantiates a new type3 message.
     * 
     * @param type2 the type2
     * @param password the password
     * @param domain the domain
     * @param user the user
     * @param workstation the workstation
     */
    public Type3Message(Type2Message type2, String password, String domain,
            String user, String workstation) {
        setFlags(getDefaultFlags(type2));
        setDomain(domain);
        setUser(user);
        setWorkstation(workstation);
        switch (LM_COMPATIBILITY) {
        case 0:
        case 1:
            setLMResponse(getLMResponse(type2, password));
            setNTResponse(getNTResponse(type2, password));
            break;
        case 2:
            byte[] nt = getNTResponse(type2, password);
            setLMResponse(nt);
            setNTResponse(nt);
            break;
        case 3:
        case 4:
        case 5:
            byte[] clientChallenge = new byte[8];
            RANDOM.nextBytes(clientChallenge);
            setLMResponse(getLMv2Response(type2, domain, user, password,
                    clientChallenge));
            /*
            setNTResponse(getNTLMv2Response(type2, domain, user, password,
                    clientChallenge));
            */
            break;
        default:
            setLMResponse(getLMResponse(type2, password));
            setNTResponse(getNTResponse(type2, password));
        }
    }

    /**
     * Instantiates a new type3 message.
     * 
     * @param flags the flags
     * @param lmResponse the lm response
     * @param ntResponse the nt response
     * @param domain the domain
     * @param user the user
     * @param workstation the workstation
     */
    public Type3Message(int flags, byte[] lmResponse, byte[] ntResponse,
            String domain, String user, String workstation) {
        setFlags(flags);
        setLMResponse(lmResponse);
        setNTResponse(ntResponse);
        setDomain(domain);
        setUser(user);
        setWorkstation(workstation);
    }

    /**
     * Instantiates a new type3 message.
     * 
     * @param material the material
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Type3Message(byte[] material) throws IOException {
        parse(material);
    }

    /**
     * Gets the lM response.
     * 
     * @return the lM response
     */
    public byte[] getLMResponse() {
        return lmResponse;
    }

    /**
     * Sets the lM response.
     * 
     * @param lmResponse the new lM response
     */
    public void setLMResponse(byte[] lmResponse) {
        this.lmResponse = lmResponse;
    }

    /**
     * Gets the nT response.
     * 
     * @return the nT response
     */
    public byte[] getNTResponse() {
        return ntResponse;
    }

    /**
     * Sets the nT response.
     * 
     * @param ntResponse the new nT response
     */
    public void setNTResponse(byte[] ntResponse) {
        this.ntResponse = ntResponse;
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
     * Sets the domain.
     * 
     * @param domain the new domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Gets the user.
     * 
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the user.
     * 
     * @param user the new user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the workstation.
     * 
     * @return the workstation
     */
    public String getWorkstation() {
        return workstation;
    }

    /**
     * Sets the workstation.
     * 
     * @param workstation the new workstation
     */
    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    /**
     * Gets the session key.
     * 
     * @return the session key
     */
    public byte[] getSessionKey() {
        return sessionKey;
    }

    /**
     * Sets the session key.
     * 
     * @param sessionKey the new session key
     */
    public void setSessionKey(byte[] sessionKey) {
        this.sessionKey = sessionKey;
    }

    /* (non-Javadoc)
     * @see jcifs.ntlmssp.NtlmMessage#toByteArray()
     */
    public byte[] toByteArray() {
        try {
            int flags = getFlags();
            boolean unicode = (flags & NTLMSSP_NEGOTIATE_UNICODE) != 0;
            String oem = unicode ? null : getOEMEncoding();
            String domainName = getDomain();
            byte[] domain = null;
            if (domainName != null && domainName.length() != 0) {
                domain = unicode ?
                        domainName.toUpperCase().getBytes("UnicodeLittleUnmarked") :
                                domainName.toUpperCase().getBytes(oem);
            }
            int domainLength = (domain != null) ? domain.length : 0;
            String userName = getUser();
            byte[] user = null;
            if (userName != null && userName.length() != 0) {
                user = unicode ? userName.getBytes("UnicodeLittleUnmarked") :
                        userName.toUpperCase().getBytes(oem);
            }
            int userLength = (user != null) ? user.length : 0;
            String workstationName = getWorkstation();
            byte[] workstation = null;
            if (workstationName != null && workstationName.length() != 0) {
                workstation = unicode ?
                        workstationName.getBytes("UnicodeLittleUnmarked") :
                                workstationName.toUpperCase().getBytes(oem);
            }
            int workstationLength = (workstation != null) ?
                    workstation.length : 0;
            byte[] lmResponse = getLMResponse();
            int lmLength = (lmResponse != null) ? lmResponse.length : 0;
            byte[] ntResponse = getNTResponse();
            int ntLength = (ntResponse != null) ? ntResponse.length : 0;
            byte[] sessionKey = getSessionKey();
            int keyLength = (sessionKey != null) ? sessionKey.length : 0;
            byte[] type3 = new byte[64 + domainLength + userLength +
                    workstationLength + lmLength + ntLength + keyLength];
            System.arraycopy(NTLMSSP_SIGNATURE, 0, type3, 0, 8);
            writeULong(type3, 8, 3);
            int offset = 64;
            writeSecurityBuffer(type3, 12, offset, lmResponse);
            offset += lmLength;
            writeSecurityBuffer(type3, 20, offset, ntResponse);
            offset += ntLength;
            writeSecurityBuffer(type3, 28, offset, domain);
            offset += domainLength;
            writeSecurityBuffer(type3, 36, offset, user);
            offset += userLength;
            writeSecurityBuffer(type3, 44, offset, workstation);
            offset += workstationLength;
            writeSecurityBuffer(type3, 52, offset, sessionKey);
            writeULong(type3, 60, flags);
            return type3;
        } catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String user = getUser();
        String domain = getDomain();
        String workstation = getWorkstation();
        byte[] lmResponse = getLMResponse();
        byte[] ntResponse = getNTResponse();
        byte[] sessionKey = getSessionKey();
        int flags = getFlags();
        StringBuffer buffer = new StringBuffer();
        if (domain != null) {
            buffer.append("domain: ").append(domain);
        }
        if (user != null) {
            if (buffer.length() > 0) buffer.append("; ");
            buffer.append("user: ").append(user);
        }
        if (workstation != null) {
            if (buffer.length() > 0) buffer.append("; ");
            buffer.append("workstation: ").append(workstation);
        }
        if (lmResponse != null) {
            if (buffer.length() > 0) buffer.append("; ");
            buffer.append("lmResponse: ");
            buffer.append("0x");
            for (int i = 0; i < lmResponse.length; i++) {
                buffer.append(Integer.toHexString((lmResponse[i] >> 4) & 0x0f));
                buffer.append(Integer.toHexString(lmResponse[i] & 0x0f));
            }
        }
        if (ntResponse != null) {
            if (buffer.length() > 0) buffer.append("; ");
            buffer.append("ntResponse: ");
            buffer.append("0x");
            for (int i = 0; i < ntResponse.length; i++) {
                buffer.append(Integer.toHexString((ntResponse[i] >> 4) & 0x0f));
                buffer.append(Integer.toHexString(ntResponse[i] & 0x0f));
            }
        }
        if (sessionKey != null) {
            if (buffer.length() > 0) buffer.append("; ");
            buffer.append("sessionKey: ");
            buffer.append("0x");
            for (int i = 0; i < sessionKey.length; i++) {
                buffer.append(Integer.toHexString((sessionKey[i] >> 4) & 0x0f));
                buffer.append(Integer.toHexString(sessionKey[i] & 0x0f));
            }
        }
        if (flags != 0) {
            if (buffer.length() > 0) buffer.append("; ");
            buffer.append("flags: ");
            buffer.append("0x");
            buffer.append(Integer.toHexString((flags >> 28) & 0x0f));
            buffer.append(Integer.toHexString((flags >> 24) & 0x0f));
            buffer.append(Integer.toHexString((flags >> 20) & 0x0f));
            buffer.append(Integer.toHexString((flags >> 16) & 0x0f));
            buffer.append(Integer.toHexString((flags >> 12) & 0x0f));
            buffer.append(Integer.toHexString((flags >> 8) & 0x0f));
            buffer.append(Integer.toHexString((flags >> 4) & 0x0f));
            buffer.append(Integer.toHexString(flags & 0x0f));
        }
        return buffer.toString();
    }

    /**
     * Gets the default flags.
     * 
     * @return the default flags
     */
    public static int getDefaultFlags() {
        return DEFAULT_FLAGS;
    }

    /**
     * Gets the default flags.
     * 
     * @param type2 the type2
     * 
     * @return the default flags
     */
    public static int getDefaultFlags(Type2Message type2) {
        if (type2 == null) return DEFAULT_FLAGS;
        int flags = NTLMSSP_NEGOTIATE_NTLM;
        flags |= ((type2.getFlags() & NTLMSSP_NEGOTIATE_UNICODE) != 0) ?
                NTLMSSP_NEGOTIATE_UNICODE : NTLMSSP_NEGOTIATE_OEM;
        return flags;
    }

    /**
     * Gets the lM response.
     * 
     * @param type2 the type2
     * @param password the password
     * 
     * @return the lM response
     */
    public static byte[] getLMResponse(Type2Message type2, String password) {
        if (type2 == null || password == null) return null;
        return NtlmPasswordAuthentication.getPreNTLMResponse(password,
                type2.getChallenge());
    }

    /**
     * Gets the l mv2 response.
     * 
     * @param type2 the type2
     * @param domain the domain
     * @param user the user
     * @param password the password
     * @param clientChallenge the client challenge
     * 
     * @return the l mv2 response
     */
    public static byte[] getLMv2Response(Type2Message type2,
            String domain, String user, String password,
                    byte[] clientChallenge) {
        if (type2 == null || domain == null || user == null ||
                password == null || clientChallenge == null) {
            return null;
        }
        return NtlmPasswordAuthentication.getLMv2Response(domain, user,
                password, type2.getChallenge(), clientChallenge);
    }

    /**
     * Gets the nT response.
     * 
     * @param type2 the type2
     * @param password the password
     * 
     * @return the nT response
     */
    public static byte[] getNTResponse(Type2Message type2, String password) {
        if (type2 == null || password == null) return null;
        return NtlmPasswordAuthentication.getNTLMResponse(password,
                type2.getChallenge());
    }

    /**
     * Gets the default domain.
     * 
     * @return the default domain
     */
    public static String getDefaultDomain() {
        return DEFAULT_DOMAIN;
    }

    /**
     * Gets the default user.
     * 
     * @return the default user
     */
    public static String getDefaultUser() {
        return DEFAULT_USER;
    }

    /**
     * Gets the default password.
     * 
     * @return the default password
     */
    public static String getDefaultPassword() {
        return DEFAULT_PASSWORD;
    }

    /**
     * Gets the default workstation.
     * 
     * @return the default workstation
     */
    public static String getDefaultWorkstation() {
        return DEFAULT_WORKSTATION;
    }

    /**
     * Parses the.
     * 
     * @param material the material
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void parse(byte[] material) throws IOException {
        for (int i = 0; i < 8; i++) {
            if (material[i] != NTLMSSP_SIGNATURE[i]) {
                throw new IOException("Not an NTLMSSP message.");
            }
        }
        if (readULong(material, 8) != 3) {
            throw new IOException("Not a Type 3 message.");
        }
        byte[] lmResponse = readSecurityBuffer(material, 12);
        int lmResponseOffset = readULong(material, 16);
        byte[] ntResponse = readSecurityBuffer(material, 20);
        int ntResponseOffset = readULong(material, 24);
        byte[] domain = readSecurityBuffer(material, 28);
        int domainOffset = readULong(material, 32);
        byte[] user = readSecurityBuffer(material, 36);
        int userOffset = readULong(material, 40);
        byte[] workstation = readSecurityBuffer(material, 44);
        int workstationOffset = readULong(material, 48);
        int flags;
        String charset;
        if (lmResponseOffset == 52 || ntResponseOffset == 52 ||
                domainOffset == 52 || userOffset == 52 ||
                        workstationOffset == 52) {
            flags = NTLMSSP_NEGOTIATE_NTLM | NTLMSSP_NEGOTIATE_OEM;
            charset = getOEMEncoding();
        } else {
            setSessionKey(readSecurityBuffer(material, 52));
            flags = readULong(material, 60);
            charset = ((flags & NTLMSSP_NEGOTIATE_UNICODE) != 0) ?
                "UnicodeLittleUnmarked" : getOEMEncoding();
        }
        setFlags(flags);
        setLMResponse(lmResponse);
        /* NTLMv2 issues w/cross-domain authentication; leave
         * NT empty if NTLMv2 was sent by the client. NTLM response
         * will always be 24 bytes; NTLMv2 response will always be
         * longer. - Kevin Tapperson
         */
        if (ntResponse.length == 24) setNTResponse(ntResponse);
        setDomain(new String(domain, charset));
        setUser(new String(user, charset));
        setWorkstation(new String(workstation, charset));
    }
}
