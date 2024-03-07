package tests;

import com.google.common.hash.Hashing;

public class HashingTest {
    public static void main(String[] args) {
        byte[] bytes = new byte[] {1,23,41,123,1,2,45,6};
        String hashCode = Hashing.murmur3_32_fixed().hashBytes(bytes).toString();
        String sha = Hashing.sha256().hashBytes(bytes).toString();
        System.out.println(hashCode);
        System.out.println(sha);
    }
}
