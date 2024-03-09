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
    private String signatureAlgo; // 签名算法
    private String signatureValue; // sign value
    private String issuer; // 颁发者
    private Date validNotAfter; // 有效期
    private String holder; // 主体名称
    private String publicKey;
    private int historyHeight;
    private byte opType;
    private String contractUrl;

    public Certificate(String version, int serialNumber, String signatureAlgo, String issuer, Date validNotAfter, String holder,
                       String publicKey, int historyHeight, byte opType, String contractUrl, String sign) {
        this.version = version;
        this.serialNumber = serialNumber;
        this.signatureAlgo = signatureAlgo;
        this.issuer = issuer;
        this.validNotAfter = validNotAfter;
        this.holder = holder;
        this.publicKey = publicKey;
        this.historyHeight = historyHeight;
        this.opType = opType;
        this.contractUrl = contractUrl;
        this.signatureValue = sign;
    }

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static Certificate getRandomCertificate(CertificateFormatType type) throws Exception{
        KeyPair keyPair = EncryptUtils.generateECCKeyPair();
        Certificate certificate = new Certificate(
                "v3.0",
                atomicInteger.getAndIncrement(),
                "ecc+ecdsa",
                "CA_fc",
                RandomUtils.generateRandomDate(),
                RandomUtils.generateRandomString(50),
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
