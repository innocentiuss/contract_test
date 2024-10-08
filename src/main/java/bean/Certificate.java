package bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import utils.EncryptUtils;
import utils.RandomUtils;

import java.security.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Certificate {
    private String version; // 版本
    private int serialNumber; // x序列号
    private String signature; // sign value
    private String issuerName; // 颁发者
    private Date invalidAfter; // 有效期
    private String holderName; // 主体名称
    private String publicKey;
    private int lastHeight;
    private byte opType;
    private String contractUrl;

    public Certificate(String version, int serialNumber, String issuer, Date validNotAfter, String holder,
                       String publicKey, int historyHeight, byte opType, String contractUrl, String sign) {
        this.version = version;
        this.serialNumber = serialNumber;
        this.issuerName = issuer;
        this.invalidAfter = validNotAfter;
        this.holderName = holder;
        this.publicKey = publicKey;
        this.lastHeight = historyHeight;
        this.opType = opType;
        this.contractUrl = contractUrl;
        this.signature = sign;
    }

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static Certificate getRandomCertificate(CertificateFormatType type) throws Exception{
        return getRandomCertificate(type, CertificateKeyType.ECC);
    }
    public static Certificate getRandomCertificate(CertificateFormatType type, CertificateKeyType keyType) throws Exception {
        return getRandomCertificate(type, keyType.getKeyPair());
    }

    public static Certificate getRSACertificate(CertificateFormatType type) throws Exception {
        return getRandomCertificate(type, CertificateKeyType.RSA);
    }

    public static Certificate getRandomCertificate(CertificateFormatType type, KeyPair keyPair) throws Exception{
        Certificate certificate = new Certificate(
                "v3.0",
                atomicInteger.getAndIncrement(),
                "CA_FC",
                RandomUtils.generateRandomDate(),
                RandomUtils.generateRandomString(20),
                keyPair.getPublic().toString(),
                -1,
                CertificateOpType.REGISTER.getCode(),
                RandomUtils.generateRandomString(40),
                "noSign"
        );
        // 序列化成byte[]
        byte[] certBytes = type.serializeCert(certificate);
        // 签名证书
        String sign = EncryptUtils.signCertificate(certBytes);
        certificate.setSignature(sign);
        return certificate;
    }
}
