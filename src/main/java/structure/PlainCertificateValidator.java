package structure;

import bean.Certificate;
import bean.CertificateFormatType;
import com.alibaba.fastjson2.JSON;
import utils.HashUtils;

public class PlainCertificateValidator extends CertificateValidator {
    public PlainCertificateValidator(int expectedElements, double falsePositiveProbability) {
        super(expectedElements, falsePositiveProbability);
        super.type = CertificateFormatType.ORIGIN;
    }

    @Override
    String hashingCertificate(Certificate certificate) {
        byte[] jsonBytes = JSON.toJSONBytes(certificate);
        return HashUtils.hashingBytes(jsonBytes);
    }
}
