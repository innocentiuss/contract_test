package utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class HashUtils {

    // sha256 + murmur32 hash拼接
    public static String hashingBytes(byte[] bytes) {
        return Hashing.sha256().hashBytes(bytes).toString() + Hashing.murmur3_32_fixed().hashBytes(bytes);
    }

    public static void main(String[] args) throws Exception {
        byte[] bytes = "zxczxchzjmlxcjzklxcjk".getBytes(StandardCharsets.UTF_8);
        byte[] shaBytes = Hashing.sha256().hashBytes(bytes).asBytes();
        byte[] murBytes = Hashing.murmur3_32_fixed().hashBytes(bytes).asBytes();
        byte[] res = IOUtils.concatBytes(shaBytes, murBytes);
        IOUtils.writeBytesToFile(res, "D:/test/hash.bin");
    }
}
