package bean;

import utils.EncryptUtils;

import java.security.KeyPair;

public enum CertificateKeyType {
    ECC {
        @Override
        KeyPair getKeyPair() {
            return EncryptUtils.generateECCKeyPair();
        }
    }, RSA {
        @Override
        KeyPair getKeyPair() {
            return EncryptUtils.generateRSAKeyPair();
        }
    };
    abstract KeyPair getKeyPair();
}
