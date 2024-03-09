package tests;


import bean.Certificate;
import bean.CertificateFormatType;
import lombok.extern.slf4j.Slf4j;
import structure.CertificateValidator;
import structure.FlatCertificateValidator;
import structure.PlainCertificateValidator;
import structure.ProtoCertificateValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CertMainTest {

    private static List<Integer> CERT_SUM = Arrays.asList(1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000);
    static final int size = 400000000;
    static final int hashes = 1000;
    public static void main(String[] args) throws Exception{
        CertificateValidator flatValidator = new FlatCertificateValidator(size, hashes);
        CertificateValidator protoValidator = new ProtoCertificateValidator(size, hashes);
        CertificateValidator plainValidator = new PlainCertificateValidator(size, hashes);

        List<CertificateValidator> validators = Arrays.asList(flatValidator, plainValidator, protoValidator);
        long start;
        long end;

        for (Integer sum : CERT_SUM) {
            List<Certificate> plainCertList = new ArrayList<>(sum);
            List<Certificate> protoCertList = new ArrayList<>(sum);
            List<Certificate> flatCertList = new ArrayList<>(sum);
            List<Certificate> invalidPlainCertList = new ArrayList<>(sum / 2);
            List<Certificate> invalidProtoCertList = new ArrayList<>(sum / 2);
            List<Certificate> invalidFlatCertList = new ArrayList<>(sum / 2);

            for (int i = 0; i < sum; i++) {
                plainCertList.add(Certificate.getRandomCertificate(CertificateFormatType.ORIGIN));
                protoCertList.add(Certificate.getRandomCertificate(CertificateFormatType.PROTOCOL_BUFFERS));
                flatCertList.add(Certificate.getRandomCertificate(CertificateFormatType.FLAT_BUFFERS));
            }
            for (int i = 0; i < sum / 2; i++) {
                invalidPlainCertList.add(Certificate.getRandomCertificate(CertificateFormatType.ORIGIN));
                invalidProtoCertList.add(Certificate.getRandomCertificate(CertificateFormatType.PROTOCOL_BUFFERS));
                invalidFlatCertList.add(Certificate.getRandomCertificate(CertificateFormatType.FLAT_BUFFERS));
            }

            for (CertificateValidator validator : validators) {
                List<Certificate> certificateList;
                List<Certificate> invalidCertList;
                if (validator.getType() == CertificateFormatType.FLAT_BUFFERS) {
                    certificateList = flatCertList;
                    invalidCertList = invalidFlatCertList;
                }
                else if (validator.getType() == CertificateFormatType.ORIGIN) {
                    certificateList = plainCertList;
                    invalidCertList = invalidPlainCertList;
                }
                else if (validator.getType() == CertificateFormatType.PROTOCOL_BUFFERS) {
                    certificateList = protoCertList;
                    invalidCertList = invalidProtoCertList;
                }
                else {
                    return;
                }

                // ========================================================
                // 证书添加start
                start = System.currentTimeMillis();
                for (Certificate cert : certificateList) {
                    validator.addCertificate(cert);
                }
                end = System.currentTimeMillis();
                log.info("{} 个 {} cert add time cost {} ms", sum, validator.getType().getDesc(), end - start);
                System.gc();
                // 证书添加end
                // =========================================================

                // =========================================================
                // 证书验证start
                start = System.currentTimeMillis();
                // 一半的有效
                for (int i = 0; i < certificateList.size() / 2; i++) {
                    validator.verifyCertificate(certificateList.get(i));
                }
                // 一半的无效
                for (Certificate invalid : invalidCertList) {
                    validator.verifyCertificate(invalid);
                }
                end = System.currentTimeMillis();
                log.info("{} 个 {} cert validate time cost {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书验证end
                // ======================================================

                // ======================================================
                // 证书撤销start
                start = System.currentTimeMillis();
                for (int i = 0; i < certificateList.size() / 2; i++) {
                    validator.revokeCertificate(certificateList.get(i));
                }
                // 一半的无效
                for (Certificate invalid : invalidCertList) {
                    validator.revokeCertificate(invalid);
                }
                end = System.currentTimeMillis();
                log.info("{} 个 {} cert revoke time cost {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书撤销end
                // ========================================================
                validator.clearBloom();
            }
        }
    }
}
