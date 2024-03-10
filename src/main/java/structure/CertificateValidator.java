package structure;

import bean.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import utils.RandomUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class CertificateValidator {
    protected CBF<String> validBloom;
    protected CBF<String> invalidBloom;

    private final Map<String, Integer> certHeight = new HashMap<>();
    @Getter
    protected CertificateFormatType type;

    public CertificateValidator(int bitSetSize, int numberOfHashFunctions) {
        this.validBloom = new AnotherCBF<>(bitSetSize, numberOfHashFunctions);
        this.invalidBloom = new AnotherCBF<>(bitSetSize, numberOfHashFunctions);
    }

    abstract String hashingCertificate(Certificate certificate);

    /**
     * 撤销证书
     * @param certificate Java证书对象
     * @return 新高度
     */
    public Integer revokeCertificate(Certificate certificate) {
        String oldHash = hashingCertificate(certificate);
        // 有效有
        if (validBloom.contains(oldHash)) {
            // 检查无效有
            if (invalidBloom.contains(oldHash)) {
                log.info("fake contains occurred! cert hash: {}. certificate: {}", oldHash, certificate);
                return null;
            }
            Integer oldHeight = certHeight.get(oldHash);
            certificate.setLastBlockChainHeight(oldHeight);
            certificate.setCertificateOperationType(CertificateOpType.REVOKE.getCode());

            String newHash = hashingCertificate(certificate);
            invalidBloom.add(newHash);

            int newHeight = RandomUtils.randomInt(10); // 获取证书高度
            certHeight.put(newHash, newHeight);
            validBloom.remove(oldHash);
            return newHeight;
        }
        return null;
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

    public void clearBloom() {
        validBloom.clear();
        invalidBloom.clear();
    }
}
