package structure;

import bean.Certificate;
import utils.CertSerializer;
import utils.HashUtils;

public class FlatCertificateValidator extends CertificateValidator{
    public FlatCertificateValidator(int bitSetSize, int numberOfHashFunctions) {
        super(bitSetSize, numberOfHashFunctions);
    }

    @Override
    String hashingCertificate(Certificate certificate) {
        byte[] bytes = CertSerializer.serializeFlatCert(certificate);
        return HashUtils.hashingBytes(bytes);
    }
}
