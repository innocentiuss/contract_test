package structure;

import bean.Certificate;

public abstract class CertificateValidator {
    protected CountingBloomFilter<String> validBloom;
    protected CountingBloomFilter<String> invalidBloom;

    public CertificateValidator(int bitSetSize, int numberOfHashFunctions) {
        this.validBloom = new CountingBloomFilter<>(bitSetSize, numberOfHashFunctions);
        this.invalidBloom = new CountingBloomFilter<>(bitSetSize, numberOfHashFunctions);
    }

    abstract String hashingCertificate(Certificate certificate);
    public void revokeCertificate(Certificate certificate) {

    }
    public boolean verifyCertificate(Certificate certificate) {
        return false;
    }
    public void addCertificate(Certificate certificate) {

    }
}
