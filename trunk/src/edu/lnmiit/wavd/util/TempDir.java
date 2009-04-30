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

package edu.lnmiit.wavd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Random;
import java.security.AccessController;
import java.security.AccessControlException;
import sun.security.action.GetPropertyAction;

// TODO: Auto-generated Javadoc
/**
 * The Class TempDir.
 */
public class TempDir {
    
    /**
     * Instantiates a new temp dir.
     */
    private TempDir() {
    }
    
    /* -- Temporary files -- */
    
    /** The Constant tmpFileLock. */
    private static final Object tmpFileLock = new Object();
    
    /** The counter. */
    private static int counter = -1; /* Protected by tmpFileLock */
    
    /**
     * Generate file.
     * 
     * @param prefix the prefix
     * @param suffix the suffix
     * @param dir the dir
     * 
     * @return the file
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File generateFile(String prefix, String suffix, File dir)
    throws IOException {
        if (counter == -1) {
            counter = new Random().nextInt() & 0xffff;
        }
        counter++;
        return new File(dir, prefix + Integer.toString(counter) + suffix);
    }
    
    /** The tmpdir. */
    private static String tmpdir; /* Protected by tmpFileLock */
    
    /**
     * Gets the temp dir.
     * 
     * @return the temp dir
     */
    private static String getTempDir() {
        if (tmpdir == null) {
            GetPropertyAction a = new GetPropertyAction("java.io.tmpdir");
            tmpdir = ((String) AccessController.doPrivileged(a));
        }
        return tmpdir;
    }
    
    /**
     * Check and create.
     * 
     * @param file the file
     * @param sm the sm
     * 
     * @return true, if successful
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static boolean checkAndCreate(File file, SecurityManager sm)
    throws IOException {
        if (sm != null) {
            try {
                sm.checkWrite(file.getPath());
            } catch (AccessControlException x) {
                /* Throwing the original AccessControlException could disclose
                   the location of the default temporary directory, so we
                   re-throw a more innocuous SecurityException */
                throw new SecurityException("Unable to create temporary file");
            }
        }
        if (file.exists()) return false;
        return file.mkdirs();
    }
    
    /**
     * Creates the temp dir.
     * 
     * @param prefix the prefix
     * @param suffix the suffix
     * @param directory the directory
     * 
     * @return the file
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static File createTempDir(String prefix, String suffix,
            File directory)
            throws IOException {
        if (prefix == null) throw new NullPointerException();
        if (prefix.length() < 3)
            throw new IllegalArgumentException("Prefix string too short");
        String s = (suffix == null) ? ".tmp" : suffix;
        synchronized (tmpFileLock) {
            if (directory == null) {
                directory = new File(getTempDir());
            }
            SecurityManager sm = System.getSecurityManager();
            File f;
            do {
                f = generateFile(prefix, s, directory);
            } while (!checkAndCreate(f, sm));
            return f;
        }
    }
    
    /**
     * Recursive delete.
     * 
     * @param dir the dir
     * 
     * @return true, if successful
     */
    public static boolean recursiveDelete(File dir) {
        String[] ls = dir.list();
        
        for (int i = 0; i < ls.length; i++) {
            File file = new File(dir, ls[i]);
            if (file.isDirectory()) {
                if (!recursiveDelete(file)) return false;
            } else {
                if (!file.delete()) return false;
            }
        }
        return dir.delete();
    }
    
    /**
     * Recursive copy.
     * 
     * @param source the source
     * @param dest the dest
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void recursiveCopy(File source, File dest) throws IOException {
        if (dest.exists())
            throw new IOException("Copy to existing directory " + dest);
        if (!source.isDirectory())
            throw new IOException("Source is not a directory " + source);
        if (!dest.mkdirs()) 
            throw new IOException("Could not create destination directory " + dest);
        
        File[] ls = source.listFiles();
        
        for (int i = 0; i < ls.length; i++) {
            if (ls[i].isDirectory()) {
                recursiveCopy(ls[i], new File(dest, ls[i].getName()));
            } else {
                File newDest = new File(dest, ls[i].getName());
                FileInputStream fis = new FileInputStream(ls[i]);
                FileOutputStream fos = new FileOutputStream(newDest);
                byte[] buff = new byte[1024];
                int got;
                while ((got=fis.read(buff))>0) {
                    fos.write(buff, 0, got);
                }
                fis.close();
                fos.close();
            }
        }
    }
    
    
}
