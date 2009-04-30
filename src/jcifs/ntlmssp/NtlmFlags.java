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

// TODO: Auto-generated Javadoc
/**
 * The Interface NtlmFlags.
 */
public interface NtlmFlags {

    /** The Constant NTLMSSP_NEGOTIATE_UNICODE. */
    public static final int NTLMSSP_NEGOTIATE_UNICODE = 0x00000001;

    /** The Constant NTLMSSP_NEGOTIATE_OEM. */
    public static final int NTLMSSP_NEGOTIATE_OEM = 0x00000002;

    /** The Constant NTLMSSP_REQUEST_TARGET. */
    public static final int NTLMSSP_REQUEST_TARGET = 0x00000004;

    /** The Constant NTLMSSP_NEGOTIATE_SIGN. */
    public static final int NTLMSSP_NEGOTIATE_SIGN = 0x00000010;

    /** The Constant NTLMSSP_NEGOTIATE_SEAL. */
    public static final int NTLMSSP_NEGOTIATE_SEAL = 0x00000020;

    /** The Constant NTLMSSP_NEGOTIATE_DATAGRAM_STYLE. */
    public static final int NTLMSSP_NEGOTIATE_DATAGRAM_STYLE = 0x00000040;

    /** The Constant NTLMSSP_NEGOTIATE_LM_KEY. */
    public static final int NTLMSSP_NEGOTIATE_LM_KEY = 0x00000080;

    /** The Constant NTLMSSP_NEGOTIATE_NETWARE. */
    public static final int NTLMSSP_NEGOTIATE_NETWARE = 0x00000100;

    /** The Constant NTLMSSP_NEGOTIATE_NTLM. */
    public static final int NTLMSSP_NEGOTIATE_NTLM = 0x00000200;

    /** The Constant NTLMSSP_NEGOTIATE_OEM_DOMAIN_SUPPLIED. */
    public static final int NTLMSSP_NEGOTIATE_OEM_DOMAIN_SUPPLIED =
            0x00001000;

    /** The Constant NTLMSSP_NEGOTIATE_OEM_WORKSTATION_SUPPLIED. */
    public static final int NTLMSSP_NEGOTIATE_OEM_WORKSTATION_SUPPLIED =
            0x00002000;

    /** The Constant NTLMSSP_NEGOTIATE_LOCAL_CALL. */
    public static final int NTLMSSP_NEGOTIATE_LOCAL_CALL = 0x00004000;

    /** The Constant NTLMSSP_NEGOTIATE_ALWAYS_SIGN. */
    public static final int NTLMSSP_NEGOTIATE_ALWAYS_SIGN = 0x00008000;

    /** The Constant NTLMSSP_TARGET_TYPE_DOMAIN. */
    public static final int NTLMSSP_TARGET_TYPE_DOMAIN = 0x00010000;

    /** The Constant NTLMSSP_TARGET_TYPE_SERVER. */
    public static final int NTLMSSP_TARGET_TYPE_SERVER = 0x00020000;

    /** The Constant NTLMSSP_TARGET_TYPE_SHARE. */
    public static final int NTLMSSP_TARGET_TYPE_SHARE = 0x00040000;

    /** The Constant NTLMSSP_NEGOTIATE_NTLM2. */ 
    public static final int NTLMSSP_NEGOTIATE_NTLM2 = 0x00080000;

    /** The Constant NTLMSSP_REQUEST_INIT_RESPONSE. */
    public static final int NTLMSSP_REQUEST_INIT_RESPONSE = 0x00100000;

    /** The Constant NTLMSSP_REQUEST_ACCEPT_RESPONSE. */
    public static final int NTLMSSP_REQUEST_ACCEPT_RESPONSE = 0x00200000;

    /** The Constant NTLMSSP_REQUEST_NON_NT_SESSION_KEY. */
    public static final int NTLMSSP_REQUEST_NON_NT_SESSION_KEY = 0x00400000;

    /** The Constant NTLMSSP_NEGOTIATE_TARGET_INFO. */
    public static final int NTLMSSP_NEGOTIATE_TARGET_INFO = 0x00800000;

    /** The Constant NTLMSSP_NEGOTIATE_128. */
    public static final int NTLMSSP_NEGOTIATE_128 = 0x20000000;

    /** The Constant NTLMSSP_NEGOTIATE_KEY_EXCH. */
    public static final int NTLMSSP_NEGOTIATE_KEY_EXCH = 0x40000000;

    /** The Constant NTLMSSP_NEGOTIATE_56. */
    public static final int NTLMSSP_NEGOTIATE_56 = 0x80000000;

}
