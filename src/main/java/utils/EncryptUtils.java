package utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;

public class EncryptUtils {


    private static final KeyPairGenerator RSA_GENERATOR;
    private static final KeyPairGenerator ECC_GENERATOR;
    private static final KeyPairGenerator ECDSA_GENERATOR;

    static {
        Security.addProvider(new BouncyCastleProvider());
        try {
            RSA_GENERATOR = KeyPairGenerator.getInstance("RSA", "BC");
            RSA_GENERATOR.initialize(3072);
            ECC_GENERATOR = KeyPairGenerator.getInstance("EC", "BC");
            ECC_GENERATOR.initialize(256);
            ECDSA_GENERATOR = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECDSA_GENERATOR.initialize(256);
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

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        generateRSAKeyPair();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
