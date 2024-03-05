package encryptor;


import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class EncryptPerformanceTest {

//    private static int GENERATING_COUNT_TARGET = 10;
    private static List<Integer> GENERATING_COUNT_TARGETS = Arrays.asList(10, 50, 100, 200, 500, 1000, 2000, 5000, 10000);

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        EncryptService encryptService = new EncryptService(3072, 256);
        for (Integer generatingCountTarget : GENERATING_COUNT_TARGETS) {
            // obj invoke ---------------- ecc
            long start = System.currentTimeMillis();
            for (int attempts = 0; attempts < generatingCountTarget; attempts++) {
                encryptService.generateECCKeyPair();
            }
            long end = System.currentTimeMillis();

            log.info("generating {} ecc pair from service at {} milliseconds", generatingCountTarget, end - start);

            // static invoke
            start = System.currentTimeMillis();
            for (int attempts = 0; attempts < generatingCountTarget; attempts++) {
                EncryptUtils.generateECCKeyPair();
            }
            end = System.currentTimeMillis();
            log.info("generating {} ecc pair from static method at {} milliseconds", generatingCountTarget, end - start);

            // obj invoke ------------ rsa
            start = System.currentTimeMillis();
            for (int attempts = 0; attempts < generatingCountTarget; attempts++) {
                encryptService.generateRSAKeyPair();
            }
            end = System.currentTimeMillis();
            log.info("generating {} ecc pair from static method at {} milliseconds", generatingCountTarget, end - start);

            // static invoke
            start = System.currentTimeMillis();
            for (int attempts = 0; attempts < generatingCountTarget; attempts++) {
                EncryptUtils.generateRSAKeyPair();
            }
            end = System.currentTimeMillis();
            log.info("generating {} ecc pair from static method at {} milliseconds", generatingCountTarget, end - start);

            // obj invoke -------------- ecdsa
            start = System.currentTimeMillis();
            for (int attempts = 0; attempts < generatingCountTarget; attempts++) {
                encryptService.generateECDSAPair();
            }
            end = System.currentTimeMillis();
            log.info("generating {} ecc pair from static method at {} milliseconds", generatingCountTarget, end - start);

            // static invoke
            start = System.currentTimeMillis();
            for (int attempts = 0; attempts < generatingCountTarget; attempts++) {
                EncryptUtils.generateECDSAPair();
            }
            end = System.currentTimeMillis();
            log.info("generating {} ecc pair from static method at {} milliseconds", generatingCountTarget, end - start);
        }
    }
}
