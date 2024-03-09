package utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.security.*;

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

    public static String signCertificate(byte[] certBytes) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        // 签名操作器
        Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
        signature.initSign(CA_KEYPAIR.getPrivate());
        signature.update(certBytes);
        byte[] signedBytes = signature.sign();

        return Base64.toBase64String(signedBytes);
    }
}
