package encryptor;


import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

@Slf4j
public class EncryptPerformanceTest {

    private static final int GENERATING_COUNT_TARGET = 10;

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        EncryptService encryptService = new EncryptService(3072, 256);
        // obj invoke ---------------- ecc
        long start = System.currentTimeMillis();
        for (int attempts = 0; attempts < GENERATING_COUNT_TARGET; attempts++) {
            encryptService.generateECCKeyPair();
        }
        long end = System.currentTimeMillis();

        log.info("generating {} ecc pair from service at {} milliseconds", GENERATING_COUNT_TARGET, end - start);

        // static invoke
        start = System.currentTimeMillis();
        for (int attempts = 0; attempts < GENERATING_COUNT_TARGET; attempts++) {
            EncryptUtils.generateECCKeyPair();
        }
        end = System.currentTimeMillis();
        log.info("generating {} ecc pair from static method at {} milliseconds", GENERATING_COUNT_TARGET, end - start);

        // obj invoke ------------ rsa
        start = System.currentTimeMillis();
        for (int attempts = 0; attempts < GENERATING_COUNT_TARGET; attempts++) {
            encryptService.generateRSAKeyPair();
        }
        end = System.currentTimeMillis();
        log.info("generating {} ecc pair from static method at {} milliseconds", GENERATING_COUNT_TARGET, end - start);

        // static invoke
        start = System.currentTimeMillis();
        for (int attempts = 0; attempts < GENERATING_COUNT_TARGET; attempts++) {
            EncryptUtils.generateRSAKeyPair();
        }
        end = System.currentTimeMillis();
        log.info("generating {} ecc pair from static method at {} milliseconds", GENERATING_COUNT_TARGET, end - start);

        // obj invoke -------------- ecdsa
        start = System.currentTimeMillis();
        for (int attempts = 0; attempts < GENERATING_COUNT_TARGET; attempts++) {
            encryptService.generateECDSAPair();
        }
        end = System.currentTimeMillis();
        log.info("generating {} ecc pair from static method at {} milliseconds", GENERATING_COUNT_TARGET, end - start);

        // static invoke
        start = System.currentTimeMillis();
        for (int attempts = 0; attempts < GENERATING_COUNT_TARGET; attempts++) {
            EncryptUtils.generateECDSAPair();
        }
        end = System.currentTimeMillis();
        log.info("generating {} ecc pair from static method at {} milliseconds", GENERATING_COUNT_TARGET, end - start);
    }
}
