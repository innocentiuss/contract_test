package tests;


import bean.Certificate;
import bean.CertificateFormatType;
import lombok.extern.slf4j.Slf4j;
import structure.*;
import utils.IOUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class CertFormatTypeTest {

    private static List<Integer> CERT_SUM = Arrays.asList(1000, 2000, 3000, 4000, 5000, 6000);
    static int expectedElements;

    static {
        expectedElements = CERT_SUM.stream().max(Comparator.comparingInt(a -> a)).orElse(-1);
    }

    static final double falsePositiveProbability = 0.00001d;
    static String directory = "D:/test/certs/";

    public static void main(String[] args) throws Exception {
        CertificateValidator protoValidator = new ProtoCertificateValidator(expectedElements, falsePositiveProbability);
        CertificateValidator plainValidator = new PlainCertificateValidator(expectedElements, falsePositiveProbability);
        CertificateValidator asnCertificateValidator = new ASNCertificateValidator(expectedElements, falsePositiveProbability);
        CertificateValidator flatValidator = new FlatCertificateValidator(expectedElements, falsePositiveProbability);

        List<CertificateValidator> validators = Arrays.asList(asnCertificateValidator, protoValidator, flatValidator, plainValidator);
        long start;
        long end;

        for (CertificateValidator validator : validators) {
            for (Integer sum : CERT_SUM) {
                // ========================================================
                // 证书添加start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    Certificate cert = Certificate.getRandomCertificate(CertificateFormatType.ORIGIN);
                    String filename = validator.getType().getCode() + "-" + sum + "-" + i;
                    IOUtils.writeBytesToFile(validator.serializeCert(cert), directory + filename);

                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.addCertificate(certificate);
                }
                end = System.currentTimeMillis();
                log.info("{} 个 {} 证书add耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书添加end
                // =========================================================

                // =========================================================
                // 证书验证start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    String filename = validator.getType().getCode() + "-" + sum + "-" + i;
                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.verifyCertificate(certificate);
                }
                end = System.currentTimeMillis();
                log.info("{} 个 {} 证书verify耗时 {} ms", sum, validator.getType().getDesc(), end - start);
                // 证书验证end
                // ======================================================

                // ======================================================
                // 证书撤销start
                start = System.currentTimeMillis();
                for (int i = 0; i < sum; i++) {
                    String filename = validator.getType().getCode() + "-" + sum + "-" + i;
                    byte[] bytes = IOUtils.readBytesFromFile(directory + filename);
                    Certificate certificate = validator.deserializeCert(bytes);
                    validator.revokeCertificate(certificate);
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
