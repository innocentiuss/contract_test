package structure;

import bean.Certificate;
import bean.CertificateFormatType;
import utils.CertSerializer;
import utils.HashUtils;

public class FlatCertificateValidator extends CertificateValidator{
    public FlatCertificateValidator(int bitSetSize, int numberOfHashFunctions) {
        super(bitSetSize, numberOfHashFunctions);
        super.type = CertificateFormatType.FLAT_BUFFERS;
    }

    @Override
    String hashingCertificate(Certificate certificate) {
        byte[] bytes = CertSerializer.serializeFlatCert(certificate);
        return HashUtils.hashingBytes(bytes);
    }
}
