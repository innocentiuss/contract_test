package structure;

import bean.Certificate;
import lombok.extern.slf4j.Slf4j;
import utils.EncryptUtils;
import utils.HashUtils;

@Slf4j
public abstract class CertificateValidator {
    protected CountingBloomFilter<String> validBloom;
    protected CountingBloomFilter<String> invalidBloom;

    public CertificateValidator(int bitSetSize, int numberOfHashFunctions) {
        this.validBloom = new CountingBloomFilter<>(bitSetSize, numberOfHashFunctions);
        this.invalidBloom = new CountingBloomFilter<>(bitSetSize, numberOfHashFunctions);
    }

    abstract String hashingCertificate(Certificate certificate);
    public void revokeCertificate(Certificate certificate) {
        String hash = hashingCertificate(certificate);
        if (validBloom.contains(hash)) {
            if (invalidBloom.contains(hash)) {
                log.info("fake contains occurred! cert hash: {}. certificate: {}", hash, certificate);
            }
            validBloom.remove(hash);
        }
    }
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
    public void addCertificate(String publicKey) {
        Certificate cert = Certificate.getRandomCertificate(publicKey);
        String hash = hashingCertificate(cert);
        validBloom.add(hash);
    }

    public void addCertificate() {
        String publicKey = EncryptUtils.generateECCKeyPair().getPublic().toString();
        Certificate cert = Certificate.getRandomCertificate(publicKey);
        String hash = hashingCertificate(cert);
        validBloom.add(hash);
    }
}
