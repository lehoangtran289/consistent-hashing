package com.hashing;

import org.apache.commons.codec.digest.MurmurHash3;

import java.nio.charset.StandardCharsets;

/**
 * MurmurHash3, industrial-standard non-cryptographic hash function.
 */
public class MurmurHash implements HashFunction {
    private static final int DEFAULT_SEED = 104729;

    private final int seed;

    public MurmurHash() {
        this(DEFAULT_SEED);
    }

    public MurmurHash(int seed) {
        this.seed = seed;
    }

    @Override
    public long hash(String key) {
        byte[] data = key.getBytes(StandardCharsets.UTF_8);
        return MurmurHash3.hash128x64(data, 0, data.length, seed)[0];
    }
}
