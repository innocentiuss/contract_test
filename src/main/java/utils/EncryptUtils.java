package utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class EncryptUtils {


    private static final KeyPairGenerator RSA_GENERATOR;
    private static final KeyPairGenerator ECC_GENERATOR;
    private static final KeyPairGenerator ECDSA_GENERATOR;

    private static final KeyPair CA_KEYPAIR;

    static {
        Security.addProvider(new BouncyCastleProvider());
        try {
            RSA_GENERATOR = KeyPairGenerator.getInstance("RSA", "BC");
            RSA_GENERATOR.initialize(3072);

            ECC_GENERATOR = KeyPairGenerator.getInstance("EC", "BC");
            ECC_GENERATOR.initialize(256);

            ECDSA_GENERATOR = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECDSA_GENERATOR.initialize(256);

            CA_KEYPAIR = ECDSA_GENERATOR.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generateRSAKeyPair()  {
        return RSA_GENERATOR.generateKeyPair();
    }

    public static KeyPair generateECCKeyPair() {
        return ECC_GENERATOR.generateKeyPair();
    }

    public static KeyPair generateECDSAPair() {
        return ECDSA_GENERATOR.generateKeyPair();
    }

    public static List<KeyPair> generateECCKeyPairs(int count) {
        List<KeyPair> keyPairs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            keyPairs.add(generateECCKeyPair());
        }
        return keyPairs;
    }

    public static List<KeyPair> generateRSAKeyPairs(int count) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 2000, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>());
        List<KeyPair> keyPairs = Collections.synchronizedList(new ArrayList<>(count));
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            threadPoolExecutor.execute(() -> {
                KeyPair keyPair = generateRSAKeyPair();
                keyPairs.add(keyPair);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
            return keyPairs;
        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            threadPoolExecutor.shutdown();
        }
    }


    public static void main(String[] args) {
        KeyPair keyPair = generateRSAKeyPair();
        System.out.println(keyPair.getPublic().toString());
        keyPair = generateECCKeyPair();
        System.out.println(keyPair.getPublic().toString());
    }

    public static String signCertificate(byte[] certBytes) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        // 签名操作器
        Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
        signature.initSign(CA_KEYPAIR.getPrivate());
        signature.update(certBytes);
        byte[] signedBytes = signature.sign();

        return Base64.toBase64String(signedBytes);
    }
}
