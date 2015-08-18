package com.onepiece.kuga.crypto;

import java.security.MessageDigest;

public class Hash {
    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
    };

    /**
     * md5 algorithm
     *
     * @param input input
     * @return String
     */
    public static String md5(String input) {
        return toHex(md5(input.getBytes()));
    }

    /**
     * md5 algorithm
     *
     * @param input input
     * @return byte[]
     */
    public static byte[] md5(byte[] input) {
        return calc(input, "md5");
    }

    /**
     * sha1 algorithm
     *
     * @param input input
     * @return String
     */
    public static String sha1(String input) {
        return toHex(sha1(input.getBytes()));
    }

    /**
     * sha1 algorithm
     *
     * @param input input
     * @return byte[]
     */
    public static byte[] sha1(byte[] input) {
        return calc(input, "sha1");
    }

    /**
     * Calculate hash
     *
     * @param input input
     * @param algorithm algorithm
     * @return String
     */
    private static String calc(String input, String algorithm) {
        return toHex(calc(input.getBytes(), algorithm));
    }

    /**
     * Calculate hash
     *
     * @param input input
     * @param algorithm algorithm
     * @return bytes
     */
    private static byte[] calc(byte[] input, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(input);
            return messageDigest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return String the formatted bytes.
     */
    public static String toHex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(2 * bytes.length);

        // to hex
        for (byte b : bytes) {
            buf.append(HEX_DIGITS[(b >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[b & 0x0f]);
        }

        return buf.toString();
    }
}


