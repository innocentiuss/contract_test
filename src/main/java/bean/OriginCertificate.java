package bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import utils.EncryptUtils;
import utils.RandomUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
public class OriginCertificate extends Certificate {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private String signatureAlgo = "采用 ECC-256 算法的 X509 ECDSA 签名";
    private String issuerId = "非关键 密钥 ID：A5 CE 37 EA EB B0 75 0E 94 67 88 B4 45 FA D9 24 10 87 96 1F";
    private String subjectId = "非关键 密钥 ID：91 E9 A1 F5 3C 33 38 89 3B 6E 18 36 B4 72 2F AD 68 45 87 79";
    public OriginCertificate(String version, int serialNumber, String issuer, Date validNotAfter, String holder,
                       String publicKey, int historyHeight, byte opType, String contractUrl, String sign) {
        super(version, serialNumber, issuer, validNotAfter, holder,
                publicKey, historyHeight, opType, contractUrl, sign);
    }

    public static OriginCertificate getRandomOriginCertificate(CertificateFormatType type) throws Exception{
        OriginCertificate certificate = new OriginCertificate(
                "v3.0",
                atomicInteger.getAndIncrement(),
                "CA_FC",
                RandomUtils.generateRandomDate(),
                RandomUtils.generateRandomString(15),
                CertificateKeyType.ECC.getKeyPair().getPublic().toString(),
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
