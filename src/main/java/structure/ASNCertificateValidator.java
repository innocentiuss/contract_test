package structure;

import bean.Certificate;
import bean.CertificateFormatType;
import utils.CertSerializer;
import utils.HashUtils;

import java.util.Base64;

public class ASNCertificateValidator extends CertificateValidator{
    public ASNCertificateValidator(int expectedElements, double falsePositiveProbability) {
        super(expectedElements, falsePositiveProbability);
        super.type = CertificateFormatType.ASN1;
    }

    @Override
    String hashingCertificate(Certificate certificate) {
        byte[] raw = CertSerializer.serializeASN(certificate);
        byte[] bytes = Base64.getEncoder().encode(raw);
        return HashUtils.hashingBytes(bytes);
    }

    @Override
    public byte[] serializeCert(Certificate certificate) {
        return CertSerializer.serializeASN(certificate);
    }

    @Override
    public Certificate deserializeCert(byte[] bytes) throws Exception {
        return CertSerializer.deserializeASN(bytes);
    }
}
