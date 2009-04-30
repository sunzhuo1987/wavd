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

import jcifs.Config;

// TODO: Auto-generated Javadoc
/**
 * The Class Type1Message.
 */
public class Type1Message extends NtlmMessage {

    /** The Constant LM_COMPATIBILITY. */
    private static final int LM_COMPATIBILITY;

    /** The Constant DEFAULT_FLAGS. */
    private static final int DEFAULT_FLAGS;

    /** The Constant DEFAULT_DOMAIN. */
    private static final String DEFAULT_DOMAIN;

    /** The Constant DEFAULT_WORKSTATION. */
    private static final String DEFAULT_WORKSTATION;

    /** The supplied domain. */
    private String suppliedDomain;

    /** The supplied workstation. */
    private String suppliedWorkstation;

    static {
        LM_COMPATIBILITY = Config.getInt("jcifs.smb.lmCompatibility", 0);
        DEFAULT_FLAGS = NTLMSSP_NEGOTIATE_NTLM
                | (Config.getBoolean("jcifs.smb.client.useUnicode", true) ? NTLMSSP_NEGOTIATE_UNICODE
                        : NTLMSSP_NEGOTIATE_OEM) | (LM_COMPATIBILITY > 2 ? NTLMSSP_REQUEST_TARGET : 0);

        DEFAULT_DOMAIN = Config.getProperty("jcifs.smb.client.domain", null);
        String defaultWorkstation = null;
        // try {
        // defaultWorkstation = NbtAddress.getLocalHost().getHostName();
        defaultWorkstation = "localhost";
        // } catch (UnknownHostException ex) { }
        DEFAULT_WORKSTATION = defaultWorkstation;
    }

    /**
     * Instantiates a new type1 message.
     */
    public Type1Message() {
        this(getDefaultFlags(), getDefaultDomain(), getDefaultWorkstation());
    }

    /**
     * Instantiates a new type1 message.
     * 
     * @param flags
     *            the flags
     * @param suppliedDomain
     *            the supplied domain
     * @param suppliedWorkstation
     *            the supplied workstation
     */
    public Type1Message(int flags, String suppliedDomain, String suppliedWorkstation) {
        setFlags(flags);
        setSuppliedDomain(suppliedDomain);
        setSuppliedWorkstation(suppliedWorkstation);
    }

    /**
     * Instantiates a new type1 message.
     * 
     * @param material
     *            the material
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Type1Message(byte[] material) throws IOException {
        parse(material);
    }

    /**
     * Gets the supplied domain.
     * 
     * @return the supplied domain
     */
    public String getSuppliedDomain() {
        return suppliedDomain;
    }

    /**
     * Sets the supplied domain.
     * 
     * @param suppliedDomain
     *            the new supplied domain
     */
    public void setSuppliedDomain(String suppliedDomain) {
        this.suppliedDomain = suppliedDomain;
    }

    /**
     * Gets the supplied workstation.
     * 
     * @return the supplied workstation
     */
    public String getSuppliedWorkstation() {
        return suppliedWorkstation;
    }

    /**
     * Sets the supplied workstation.
     * 
     * @param suppliedWorkstation
     *            the new supplied workstation
     */
    public void setSuppliedWorkstation(String suppliedWorkstation) {
        this.suppliedWorkstation = suppliedWorkstation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcifs.ntlmssp.NtlmMessage#toByteArray()
     */
    public byte[] toByteArray() {
        try {
            String suppliedDomain = getSuppliedDomain();
            String suppliedWorkstation = getSuppliedWorkstation();
            int flags = getFlags();
            boolean hostInfo = false;
            byte[] domain = new byte[0];
            if (suppliedDomain != null && suppliedDomain.length() != 0) {
                hostInfo = true;
                flags |= NTLMSSP_NEGOTIATE_OEM_DOMAIN_SUPPLIED;
                domain = suppliedDomain.toUpperCase().getBytes(getOEMEncoding());
            } else {
                flags &= (NTLMSSP_NEGOTIATE_OEM_DOMAIN_SUPPLIED ^ 0xffffffff);
            }
            byte[] workstation = new byte[0];
            if (suppliedWorkstation != null && suppliedWorkstation.length() != 0) {
                hostInfo = true;
                flags |= NTLMSSP_NEGOTIATE_OEM_WORKSTATION_SUPPLIED;
                workstation = suppliedWorkstation.toUpperCase().getBytes(getOEMEncoding());
            } else {
                flags &= (NTLMSSP_NEGOTIATE_OEM_WORKSTATION_SUPPLIED ^ 0xffffffff);
            }
            byte[] type1 = new byte[hostInfo ? (32 + domain.length + workstation.length) : 16];
            System.arraycopy(NTLMSSP_SIGNATURE, 0, type1, 0, 8);
            writeULong(type1, 8, 1);
            writeULong(type1, 12, flags);
            if (hostInfo) {
                writeSecurityBuffer(type1, 16, 32, domain);
                writeSecurityBuffer(type1, 24, 32 + domain.length, workstation);
            }
            return type1;
        } catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String suppliedDomain = getSuppliedDomain();
        String suppliedWorkstation = getSuppliedWorkstation();
        int flags = getFlags();
        StringBuffer buffer = new StringBuffer();
        if (suppliedDomain != null) {
            buffer.append("suppliedDomain: ").append(suppliedDomain);
        }
        if (suppliedWorkstation != null) {
            if (buffer.length() > 0)
                buffer.append("; ");
            buffer.append("suppliedWorkstation: ").append(suppliedWorkstation);
        }
        if (flags != 0) {
            if (buffer.length() > 0)
                buffer.append("; ");
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
     * Gets the default domain.
     * 
     * @return the default domain
     */
    public static String getDefaultDomain() {
        return DEFAULT_DOMAIN;
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
     * @param material
     *            the material
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void parse(byte[] material) throws IOException {
        for (int i = 0; i < 8; i++) {
            if (material[i] != NTLMSSP_SIGNATURE[i]) {
                throw new IOException("Not an NTLMSSP message.");
            }
        }
        if (readULong(material, 8) != 1) {
            throw new IOException("Not a Type 1 message.");
        }
        int flags = readULong(material, 12);
        String suppliedDomain = null;
        if ((flags & NTLMSSP_NEGOTIATE_OEM_DOMAIN_SUPPLIED) != 0) {
            byte[] domain = readSecurityBuffer(material, 16);
            suppliedDomain = new String(domain, getOEMEncoding());
        }
        String suppliedWorkstation = null;
        if ((flags & NTLMSSP_NEGOTIATE_OEM_WORKSTATION_SUPPLIED) != 0) {
            byte[] workstation = readSecurityBuffer(material, 24);
            suppliedWorkstation = new String(workstation, getOEMEncoding());
        }
        setFlags(flags);
        setSuppliedDomain(suppliedDomain);
        setSuppliedWorkstation(suppliedWorkstation);
    }

}
