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
    private String certificateVersion; // 版本
    private int certificateSerialNumber; // x序列号
    private String certSignatureAlgorithm; // 签名算法
    private String signatureValue; // sign value
    private String certIssuerName; // 颁发者
    private Date invalidAfterTimestamps; // 有效期
    private String certHolderName; // 主体名称
    private String publicKey;
    private int lastBlockChainHeight;
    private byte certificateOperationType;
    private String smartContractUrl;

    public Certificate(String version, int serialNumber, String signatureAlgo, String issuer, Date validNotAfter, String holder,
                       String publicKey, int historyHeight, byte opType, String contractUrl, String sign) {
        this.certificateVersion = version;
        this.certificateSerialNumber = serialNumber;
        this.certSignatureAlgorithm = signatureAlgo;
        this.certIssuerName = issuer;
        this.invalidAfterTimestamps = validNotAfter;
        this.certHolderName = holder;
        this.publicKey = publicKey;
        this.lastBlockChainHeight = historyHeight;
        this.certificateOperationType = opType;
        this.smartContractUrl = contractUrl;
        this.signatureValue = sign;
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
                "ECDSA",
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
        certificate.setSignatureValue(sign);
        return certificate;
    }
}
