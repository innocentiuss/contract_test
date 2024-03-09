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
    private String certificateSignatureAlgorithm; // 签名算法
    private String thisCertificateSignatureValueFromSHA256_MURMUR32; // sign value
    private String certificateIssuerName; // 颁发者
    private Date thisCertificateWillBeInvalidAfterTimestamps; // 有效期
    private String thisCertificateHolderName; // 主体名称
    private String publicKeyForThisCertificate;
    private int lastTimeOperationFromBlockChainAndGetThisChainHistoryHeight;
    private byte certificateOperationType;
    private String smartContractUrl;

    public Certificate(String version, int serialNumber, String signatureAlgo, String issuer, Date validNotAfter, String holder,
                       String publicKey, int historyHeight, byte opType, String contractUrl, String sign) {
        this.certificateVersion = version;
        this.certificateSerialNumber = serialNumber;
        this.certificateSignatureAlgorithm = signatureAlgo;
        this.certificateIssuerName = issuer;
        this.thisCertificateWillBeInvalidAfterTimestamps = validNotAfter;
        this.thisCertificateHolderName = holder;
        this.publicKeyForThisCertificate = publicKey;
        this.lastTimeOperationFromBlockChainAndGetThisChainHistoryHeight = historyHeight;
        this.certificateOperationType = opType;
        this.smartContractUrl = contractUrl;
        this.thisCertificateSignatureValueFromSHA256_MURMUR32 = sign;
    }

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static Certificate getRandomCertificate(CertificateFormatType type) throws Exception{
        KeyPair keyPair = EncryptUtils.generateECCKeyPair();
        Certificate certificate = new Certificate(
                "v3.0.00000000000000000000000000",
                atomicInteger.getAndIncrement(),
                "ECC Algorithm with ECDSA sign algorithm",
                "CA_FCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC",
                RandomUtils.generateRandomDate(),
                RandomUtils.generateRandomString(200),
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
        certificate.setThisCertificateSignatureValueFromSHA256_MURMUR32(sign);
        return certificate;
    }

}
