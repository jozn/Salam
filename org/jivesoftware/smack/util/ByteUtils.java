package org.jivesoftware.smack.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class ByteUtils {
    private static MessageDigest md5Digest;

    public static synchronized byte[] md5(byte[] data) {
        byte[] digest;
        synchronized (ByteUtils.class) {
            if (md5Digest == null) {
                try {
                    md5Digest = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException nsae) {
                    throw new IllegalStateException(nsae);
                }
            }
            md5Digest.update(data);
            digest = md5Digest.digest();
        }
        return digest;
    }

    public static byte[] concact(byte[] arrayOne, byte[] arrayTwo) {
        byte[] res = new byte[(arrayOne.length + arrayTwo.length)];
        System.arraycopy(arrayOne, 0, res, 0, arrayOne.length);
        System.arraycopy(arrayTwo, 0, res, arrayOne.length, arrayTwo.length);
        return res;
    }
}
