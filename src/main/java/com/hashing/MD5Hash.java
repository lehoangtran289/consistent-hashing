package com.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MD5Hash implements HashFunction {
    /**
     * MessageDigest is stateful, so one instance per thread rather than one shared instance.
     */
    private static final ThreadLocal<MessageDigest> DIGEST = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available", e);
        }
    });

    @Override
    public long hash(String key) {
        MessageDigest instance = DIGEST.get();
        instance.reset();
        instance.update(key.getBytes(UTF_8));
        byte[] digest = instance.digest();

        // fold 8 bytes: a 32-bit space collides at ~65k virtual nodes
        long h = 0;
        for (int i = 0; i < 8; i++) {
            h <<= 8;
            h |= (digest[i]) & 0xFF;
        }
        return h;
    }
}
