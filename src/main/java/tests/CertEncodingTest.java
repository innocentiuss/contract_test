package tests;


import bean.Certificate;
import lombok.extern.slf4j.Slf4j;
import structure.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class CertEncodingTest {

    private static List<Integer> CERT_SUM = Arrays.asList(1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500, 7000, 7500, 8000, 8500, 9000, 9500, 10000);
    static int expectedElements;
    static {
        expectedElements = CERT_SUM.stream().max(Comparator.comparingInt(a -> a)).orElse(-1);
    }
    static final double falsePositiveProbability = 0.00001d;
    public static void main(String[] args) throws Exception{
        CertificateValidator protoValidator = new ProtoCertificateValidator(expectedElements, falsePositiveProbability);
        CertificateValidator plainValidator = new PlainCertificateValidator(expectedElements, falsePositiveProbability);
        ASNCertificateValidator asnCertificateValidator = new ASNCertificateValidator(expectedElements, falsePositiveProbability);
        CERT_SUM = new ArrayList<>();
        for (int i = 10; i <= 10000; i += 10) {
            CERT_SUM.add(i);
        }

        List<CertificateValidator> validators = Arrays.asList(plainValidator, protoValidator, asnCertificateValidator);
        long start;
        long end;

        for (Integer sum : CERT_SUM) {
            for (CertificateValidator validator : validators) {

                List<Certificate> validList = new ArrayList<>(sum);
                List<Certificate> compareList = new ArrayList<>(sum / 2);
                for (int i = 0; i < sum / 2; i++) {
                    compareList.add(Certificate.getRandomCertificate(validator.getType()));
                }

                // ========================================================
                // 证书添加start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    Certificate cert = Certificate.getRandomCertificate(validator.getType());
                    validator.addCertificate(cert);
                    validList.add(cert);
                }
                end = System.currentTimeMillis();
                log.info("{} 个 {} 证书add耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书添加end
                // =========================================================

                // =========================================================
                // 证书验证start
                start = System.currentTimeMillis();
                // 一半的有效
                for (int i = 0; i < validList.size() / 2; i++) {
                    validator.verifyCertificate(validList.get(i));
                }
                // 一半的无效
                for (Certificate invalid : compareList) {
                    validator.verifyCertificate(invalid);
                }
                end = System.currentTimeMillis();
                log.info("{} 个 {} 证书verify耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书验证end
                // ======================================================

                // ======================================================
                // 证书撤销start
                start = System.currentTimeMillis();
                for (int i = 0; i < validList.size() / 2; i++) {
                    validator.revokeCertificate(validList.get(i));
                }
                // 一半的无效
                for (Certificate invalid : compareList) {
                    validator.revokeCertificate(invalid);
                }
                end = System.currentTimeMillis();
                log.info("{} 个 {} 证书revoke耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书撤销end
                // ========================================================
                validator.clearBloom();
                log.info("==================分割线==================");
            }
        }
    }
}
