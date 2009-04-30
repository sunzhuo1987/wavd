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
// import jcifs.netbios.NbtAddress;

/**
 * The Class Type2Message.
 */
public class Type2Message extends NtlmMessage {

    /** The Constant DEFAULT_FLAGS. */
    private static final int DEFAULT_FLAGS;

    /** The Constant DEFAULT_DOMAIN. */
    private static final String DEFAULT_DOMAIN;

    /** The Constant DEFAULT_TARGET_INFORMATION. */
    private static final byte[] DEFAULT_TARGET_INFORMATION;

    /** The challenge. */
    private byte[] challenge;

    /** The target. */
    private String target;

    /** The context. */
    private byte[] context;

    /** The target information. */
    private byte[] targetInformation;

    static {
        DEFAULT_FLAGS = NTLMSSP_NEGOTIATE_NTLM
                | (Config.getBoolean("jcifs.smb.client.useUnicode", true) ? NTLMSSP_NEGOTIATE_UNICODE
                        : NTLMSSP_NEGOTIATE_OEM);
        DEFAULT_DOMAIN = Config.getProperty("jcifs.smb.client.domain", null);
        byte[] domain = new byte[0];
        if (DEFAULT_DOMAIN != null) {
            try {
                domain = DEFAULT_DOMAIN.getBytes("UnicodeLittleUnmarked");
            } catch (IOException ex) {
            }
        }
        int domainLength = domain.length;
        byte[] server = new byte[0];
        // try {
        // String host = NbtAddress.getLocalHost().getHostName();
        String host = "localhost";
        if (host != null) {
            try {
                server = host.getBytes("UnicodeLittleUnmarked");
            } catch (IOException ex) {
            }
        }
        // } catch (UnknownHostException ex) { }
        int serverLength = server.length;
        byte[] targetInfo = new byte[(domainLength > 0 ? domainLength + 4 : 0)
                + (serverLength > 0 ? serverLength + 4 : 0) + 4];
        int offset = 0;
        if (domainLength > 0) {
            writeUShort(targetInfo, offset, 2);
            offset += 2;
            writeUShort(targetInfo, offset, domainLength);
            offset += 2;
            System.arraycopy(domain, 0, targetInfo, offset, domainLength);
            offset += domainLength;
        }
        if (serverLength > 0) {
            writeUShort(targetInfo, offset, 1);
            offset += 2;
            writeUShort(targetInfo, offset, serverLength);
            offset += 2;
            System.arraycopy(server, 0, targetInfo, offset, serverLength);
        }
        DEFAULT_TARGET_INFORMATION = targetInfo;
    }

    /**
     * Instantiates a new type2 message.
     */
    public Type2Message() {
        this(getDefaultFlags(), null, null);
    }

    /**
     * Instantiates a new type2 message.
     * 
     * @param type1
     *            the type1
     */
    public Type2Message(Type1Message type1) {
        this(type1, null, null);
    }

    /**
     * Instantiates a new type2 message.
     * 
     * @param type1
     *            the type1
     * @param challenge
     *            the challenge
     * @param target
     *            the target
     */
    public Type2Message(Type1Message type1, byte[] challenge, String target) {
        this(getDefaultFlags(type1), challenge, (type1 != null && target == null && type1
                .getFlag(NTLMSSP_REQUEST_TARGET)) ? getDefaultDomain() : target);
    }

    /**
     * Instantiates a new type2 message.
     * 
     * @param flags
     *            the flags
     * @param challenge
     *            the challenge
     * @param target
     *            the target
     */
    public Type2Message(int flags, byte[] challenge, String target) {
        setFlags(flags);
        setChallenge(challenge);
        setTarget(target);
        if (target != null)
            setTargetInformation(getDefaultTargetInformation());
    }

    /**
     * Instantiates a new type2 message.
     * 
     * @param material
     *            the material
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Type2Message(byte[] material) throws IOException {
        parse(material);
    }

    /**
     * Gets the challenge.
     * 
     * @return the challenge
     */
    public byte[] getChallenge() {
        return challenge;
    }

    /**
     * Sets the challenge.
     * 
     * @param challenge
     *            the new challenge
     */
    public void setChallenge(byte[] challenge) {
        this.challenge = challenge;
    }

    /**
     * Gets the target.
     * 
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target.
     * 
     * @param target
     *            the new target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Gets the target information.
     * 
     * @return the target information
     */
    public byte[] getTargetInformation() {
        return targetInformation;
    }

    /**
     * Sets the target information.
     * 
     * @param targetInformation
     *            the new target information
     */
    public void setTargetInformation(byte[] targetInformation) {
        this.targetInformation = targetInformation;
    }

    /**
     * Gets the context.
     * 
     * @return the context
     */
    public byte[] getContext() {
        return context;
    }

    /**
     * Sets the context.
     * 
     * @param context
     *            the new context
     */
    public void setContext(byte[] context) {
        this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcifs.ntlmssp.NtlmMessage#toByteArray()
     */
    public byte[] toByteArray() {
        try {
            String targetName = getTarget();
            byte[] challenge = getChallenge();
            byte[] context = getContext();
            byte[] targetInformation = getTargetInformation();
            int flags = getFlags();
            byte[] target = new byte[0];
            if ((flags & (NTLMSSP_TARGET_TYPE_DOMAIN | NTLMSSP_TARGET_TYPE_SERVER | NTLMSSP_TARGET_TYPE_SHARE)) != 0) {
                if (targetName != null && targetName.length() != 0) {
                    target = (flags & NTLMSSP_NEGOTIATE_UNICODE) != 0 ? targetName.getBytes("UnicodeLittleUnmarked")
                            : targetName.toUpperCase().getBytes(getOEMEncoding());
                } else {
                    flags &= (0xffffffff ^ (NTLMSSP_TARGET_TYPE_DOMAIN | NTLMSSP_TARGET_TYPE_SERVER | NTLMSSP_TARGET_TYPE_SHARE));
                }
            }
            if (targetInformation != null) {
                flags ^= NTLMSSP_NEGOTIATE_TARGET_INFO;
                // empty context is needed for padding when t.i. is supplied.
                if (context == null)
                    context = new byte[8];
            }
            int data = 32;
            if (context != null)
                data += 8;
            if (targetInformation != null)
                data += 8;
            byte[] type2 = new byte[data + target.length + (targetInformation != null ? targetInformation.length : 0)];
            System.arraycopy(NTLMSSP_SIGNATURE, 0, type2, 0, 8);
            writeULong(type2, 8, 2);
            writeSecurityBuffer(type2, 12, data, target);
            writeULong(type2, 20, flags);
            System.arraycopy(challenge != null ? challenge : new byte[8], 0, type2, 24, 8);
            if (context != null)
                System.arraycopy(context, 0, type2, 32, 8);
            if (targetInformation != null) {
                writeSecurityBuffer(type2, 40, data + target.length, targetInformation);
            }
            return type2;
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
        String target = getTarget();
        byte[] challenge = getChallenge();
        byte[] context = getContext();
        byte[] targetInformation = getTargetInformation();
        int flags = getFlags();
        StringBuffer buffer = new StringBuffer();
        if (target != null) {
            buffer.append("target: ").append(target);
        }
        if (challenge != null) {
            if (buffer.length() > 0)
                buffer.append("; ");
            buffer.append("challenge: ");
            buffer.append("0x");
            for (int i = 0; i < challenge.length; i++) {
                buffer.append(Integer.toHexString((challenge[i] >> 4) & 0x0f));
                buffer.append(Integer.toHexString(challenge[i] & 0x0f));
            }
        }
        if (context != null) {
            if (buffer.length() > 0)
                buffer.append("; ");
            buffer.append("context: ");
            buffer.append("0x");
            for (int i = 0; i < context.length; i++) {
                buffer.append(Integer.toHexString((context[i] >> 4) & 0x0f));
                buffer.append(Integer.toHexString(context[i] & 0x0f));
            }
        }
        if (targetInformation != null) {
            if (buffer.length() > 0)
                buffer.append("; ");
            buffer.append("targetInformation: ");
            buffer.append("0x");
            for (int i = 0; i < targetInformation.length; i++) {
                buffer.append(Integer.toHexString((targetInformation[i] >> 4) & 0x0f));
                buffer.append(Integer.toHexString(targetInformation[i] & 0x0f));
            }
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
     * Gets the default flags.
     * 
     * @param type1
     *            the type1
     * 
     * @return the default flags
     */
    public static int getDefaultFlags(Type1Message type1) {
        if (type1 == null)
            return DEFAULT_FLAGS;
        int flags = NTLMSSP_NEGOTIATE_NTLM;
        int type1Flags = type1.getFlags();
        flags |= ((type1Flags & NTLMSSP_NEGOTIATE_UNICODE) != 0) ? NTLMSSP_NEGOTIATE_UNICODE : NTLMSSP_NEGOTIATE_OEM;
        if ((type1Flags & NTLMSSP_REQUEST_TARGET) != 0) {
            String domain = getDefaultDomain();
            if (domain != null) {
                flags |= NTLMSSP_REQUEST_TARGET | NTLMSSP_TARGET_TYPE_DOMAIN;
            }
        }
        return flags;
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
     * Gets the default target information.
     * 
     * @return the default target information
     */
    public static byte[] getDefaultTargetInformation() {
        return DEFAULT_TARGET_INFORMATION;
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
        if (readULong(material, 8) != 2) {
            throw new IOException("Not a Type 2 message.");
        }
        int flags = readULong(material, 20);
        setFlags(flags);
        String target = null;
        byte[] bytes = readSecurityBuffer(material, 12);
        if (bytes.length != 0) {
            target = new String(bytes, ((flags & NTLMSSP_NEGOTIATE_UNICODE) != 0) ? "UnicodeLittleUnmarked"
                    : getOEMEncoding());
        }
        setTarget(target);
        for (int i = 24; i < 32; i++) {
            if (material[i] != 0) {
                byte[] challenge = new byte[8];
                System.arraycopy(material, 24, challenge, 0, 8);
                setChallenge(challenge);
                break;
            }
        }
        int offset = readULong(material, 16); // offset of targetname start
        if (offset == 32 || material.length == 32)
            return;
        for (int i = 32; i < 40; i++) {
            if (material[i] != 0) {
                byte[] context = new byte[8];
                System.arraycopy(material, 32, context, 0, 8);
                setContext(context);
                break;
            }
        }
        if (offset == 40 || material.length == 40)
            return;
        bytes = readSecurityBuffer(material, 40);
        if (bytes.length != 0)
            setTargetInformation(bytes);
    }

}
