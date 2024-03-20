package tests;

import bean.Certificate;
import bean.CertificateKeyType;
import lombok.extern.slf4j.Slf4j;
import structure.CertificateValidator;
import structure.ProtoCertificateValidator;
import utils.EncryptUtils;
import utils.IOUtils;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CertEncryptTypeTest {
    private static List<Integer> CERT_SUM = Arrays.asList(1000, 2000, 3000, 4000, 5000, 6000);
    static final int maxElement = 6000;
    static final double falsePositiveProbability = 0.00001d;
    static String directory = "D:/test/certs1/";
    public static void main(String[] args) throws Exception{
        CertificateValidator protoValidator = new ProtoCertificateValidator(maxElement, falsePositiveProbability);

        List<CertificateValidator> validators = Arrays.asList(protoValidator);


        long start;
        long end;

        for (Integer sum : CERT_SUM) {
            for (CertificateValidator validator : validators) {
                // ========================================================
                // 证书添加start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    Certificate cert = Certificate.getRandomCertificate(validator.getType(), CertificateKeyType.ECC);
                    String filename = "ecc-" + sum + "-" + i;
                    IOUtils.writeBytesToFile(validator.serializeCert(cert), directory + filename);

                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.addCertificate(certificate);
                }
                end = System.currentTimeMillis();
                log.info("ecc {} 个 {} 证书add耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书添加end
                // =========================================================

                // =========================================================
                // 证书验证start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    String filename = "ecc-" + sum + "-" + i;
                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.verifyCertificate(certificate);
                }
                end = System.currentTimeMillis();
                log.info("ecc {} 个 {} 证书verify耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书验证end
                // ======================================================

                // ======================================================
                // 证书撤销start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    String filename = "ecc-" + sum + "-" + i;
                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.revokeCertificate(certificate);
                }
                end = System.currentTimeMillis();
                log.info("ecc {} 个 {} 证书revoke耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书撤销end
                // ========================================================
                validator.clearBloom();
                log.info("==================分割线==================");
            }
        }

        for (Integer sum : CERT_SUM) {
            for (CertificateValidator validator : validators) {

                // ========================================================
                // 证书添加start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    Certificate cert = Certificate.getRandomCertificate(validator.getType(), CertificateKeyType.RSA);
                    String filename = "rsa-" + sum + "-" + i;
                    IOUtils.writeBytesToFile(validator.serializeCert(cert), directory + filename);

                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.addCertificate(certificate);
                }
                end = System.currentTimeMillis();
                log.info("rsa {} 个 {} 证书add耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书添加end
                // =========================================================

                // =========================================================
                // 证书验证start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    String filename = "rsa-" + sum + "-" + i;
                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.verifyCertificate(certificate);
                }
                end = System.currentTimeMillis();
                log.info("rsa {} 个 {} 证书verify耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书验证end
                // ======================================================

                // ======================================================
                // 证书撤销start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    String filename = "rsa-" + sum + "-" + i;
                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.revokeCertificate(certificate);
                }
                end = System.currentTimeMillis();
                log.info("rsa {} 个 {} 证书revoke耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书撤销end
                // ========================================================
                validator.clearBloom();
                log.info("==================分割线==================");
            }
        }
    }
}
