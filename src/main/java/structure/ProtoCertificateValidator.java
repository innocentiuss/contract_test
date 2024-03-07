package structure;

import bean.Certificate;
import utils.CertSerializer;
import utils.HashUtils;

public class ProtoCertificateValidator extends CertificateValidator {

    public ProtoCertificateValidator(int bitSetSize, int numberOfHashFunctions) {
        super(bitSetSize, numberOfHashFunctions);
    }

    @Override
    String hashingCertificate(Certificate certificate) {
        byte[] bytes = CertSerializer.serializeProtoCert(certificate);
        return HashUtils.hashingBytes(bytes);
    }
}
