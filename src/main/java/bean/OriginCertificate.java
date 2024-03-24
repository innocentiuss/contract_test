package bean;

import lombok.Data;

@Data
public class OriginCertificate extends Certificate {
    private String signatureAlgo = "采用 ECC-256 算法的 X509 ECDSA 签名";
    private String issuerId = "非关键 密钥 ID：A5 CE 37 EA EB B0 75 0E 94 67 88 B4 45 FA D9 24 10 87 96 1F";
    private String subjectId = "非关键 密钥 ID：91 E9 A1 F5 3C 33 38 89 3B 6E 18 36 B4 72 2F AD 68 45 87 79";
}
