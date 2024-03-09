package utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;

@Deprecated
public class EncryptService {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    private final KeyPairGenerator rsaGenerator;
    private final KeyPairGenerator eccGenerator;
    private final KeyPairGenerator ecdsaGenerator;

    public EncryptService(int rsaLength, int eccLength) throws NoSuchAlgorithmException, NoSuchProviderException {
        rsaGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        rsaGenerator.initialize(rsaLength);
        eccGenerator = KeyPairGenerator.getInstance("EC", "BC");
        eccGenerator.initialize(eccLength);
        ecdsaGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
        ecdsaGenerator.initialize(eccLength);
    }

    public KeyPair generateRSAKeyPair() {
        return rsaGenerator.generateKeyPair();
    }
    public KeyPair generateECCKeyPair() {
        return eccGenerator.generateKeyPair();
    }

    public KeyPair generateECDSAPair() {return ecdsaGenerator.generateKeyPair();}
}
