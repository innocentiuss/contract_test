package utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashUtils {

    // sha256 + murmur32 hash拼接
    public static String hashingBytes(byte[] bytes) {
        return Hashing.sha256().hashBytes(bytes).toString() + Hashing.murmur3_32_fixed().hashBytes(bytes);
    }
}
