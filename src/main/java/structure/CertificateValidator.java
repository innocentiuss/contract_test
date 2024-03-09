package structure;

import bean.Certificate;
import bean.CertificateFormatType;
import bean.CertificateOpType;
import lombok.extern.slf4j.Slf4j;
import utils.EncryptUtils;
import utils.HashUtils;
import utils.RandomUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class CertificateValidator {
    protected CountingBloomFilter<String> validBloom;
    protected CountingBloomFilter<String> invalidBloom;

    private final Map<String, Integer> certHeight = new HashMap<>();

    public CertificateValidator(int bitSetSize, int numberOfHashFunctions) {
        this.validBloom = new CountingBloomFilter<>(bitSetSize, numberOfHashFunctions);
        this.invalidBloom = new CountingBloomFilter<>(bitSetSize, numberOfHashFunctions);
    }

    abstract String hashingCertificate(Certificate certificate);

    /**
     * 撤销证书
     * @param certificate Java证书对象
     * @return 新高度
     */
    public Integer revokeCertificate(Certificate certificate) {
        String oldHash = hashingCertificate(certificate);
        if (validBloom.contains(oldHash)) {
            if (invalidBloom.contains(oldHash)) {
                log.info("fake contains occurred! cert hash: {}. certificate: {}", oldHash, certificate);
            }
            validBloom.remove(oldHash);
        }
        Integer oldHeight = certHeight.get(oldHash);
        certificate.setHistoryHeight(oldHeight);
        certificate.setOpType(CertificateOpType.REVOKE.getCode());

        String newHash = hashingCertificate(certificate);
        invalidBloom.add(newHash);

        int newHeight = RandomUtils.randomInt(10); // 获取证书高度
        certHeight.put(newHash, newHeight);
        return newHeight;
    }

    /**
     * 验证证书
     * @param certificate Java证书对象
     * @return 是否存在
     */
    public Boolean verifyCertificate(Certificate certificate) {
        String hash = hashingCertificate(certificate);
        if (validBloom.contains(hash)) {
            if (invalidBloom.contains(hash)) {
                log.info("unknown whether cert is valid. cert hash: {}. certificate: {}", hash, certificate);
                return null;
            }
            return true;
        }
        return false;
    }

    /**
     * 添加证书
     * @param certificate Java证书对象
     * @return 证书哈希所在区块高度
     */
    public Integer addCertificate(Certificate certificate) {
        String hash = hashingCertificate(certificate);
        validBloom.add(hash);
        int height = RandomUtils.randomInt(10);
        certHeight.put(hash, height);
        return height;
    }
}
