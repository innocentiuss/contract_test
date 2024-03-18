package structure;

import bean.Certificate;
import bean.CertificateFormatType;
import com.google.protobuf.InvalidProtocolBufferException;
import utils.CertSerializer;
import utils.HashUtils;

public class ProtoCertificateValidator extends CertificateValidator {

    public ProtoCertificateValidator(int expectedElements, double falsePositiveProbability) {
        super(expectedElements, falsePositiveProbability);
        super.type = CertificateFormatType.PROTOCOL_BUFFERS;
    }

    @Override
    String hashingCertificate(Certificate certificate) {
        byte[] bytes = CertSerializer.serializeProtoCert(certificate);
        return HashUtils.hashingBytes(bytes);
    }

    @Override
    public byte[] serializeCert(Certificate certificate) {
        return CertSerializer.serializeProtoCert(certificate);
    }

    @Override
    public Certificate deserializeCert(byte[] bytes) throws Exception {
        return CertSerializer.deserializeProtoCert(bytes);
    }
}
