package tests;


import bean.Certificate;
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
                log.info("{} 个 {} 证书添加耗时 {} ms", sum, validator.getType().getDesc(), end - start);
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
                log.info("{} 个 {} 证书验证耗时 {} ms", sum, validator.getType().getDesc(), end - start);
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
                log.info("{} 个 {} 证书撤销耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书撤销end
                // ========================================================
                validator.clearBloom();
                log.info("==================分割线==================");
            }
        }
    }
}
