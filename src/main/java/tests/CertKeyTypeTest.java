package tests;

import bean.Certificate;
import lombok.extern.slf4j.Slf4j;
import structure.CertificateValidator;
import structure.ProtoCertificateValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CertKeyTypeTest {
    private static List<Integer> CERT_SUM = Arrays.asList(100, 150, 200, 250, 300);
    static final int maxElement = 300;
    static final double falsePositiveProbability = 0.00001d;
    public static void main(String[] args) throws Exception{
        CertificateValidator protoValidator = new ProtoCertificateValidator(maxElement, falsePositiveProbability);
        CERT_SUM = new ArrayList<>();
        for (int i = 10; i <= 10000; i += 10) {
            CERT_SUM.add(i);
        }

        List<CertificateValidator> validators = Arrays.asList(protoValidator);
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

        for (Integer sum : CERT_SUM) {
            for (CertificateValidator validator : validators) {

                List<Certificate> validList = new ArrayList<>(sum);
                List<Certificate> compareList = new ArrayList<>(sum / 2);
                for (int i = 0; i < sum / 2; i++) {
                    compareList.add(Certificate.getRSACertificate(validator.getType()));
                }

                // ========================================================
                // 证书添加start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    Certificate cert = Certificate.getRSACertificate(validator.getType());
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
