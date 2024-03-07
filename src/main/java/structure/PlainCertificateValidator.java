package structure;

import bean.Certificate;
import com.alibaba.fastjson2.JSON;
import utils.HashUtils;

public class PlainCertificateValidator extends CertificateValidator {
    public PlainCertificateValidator(int bitSetSize, int numberOfHashFunctions) {
        super(bitSetSize, numberOfHashFunctions);
    }

    @Override
    String hashingCertificate(Certificate certificate) {
        byte[] jsonBytes = JSON.toJSONBytes(certificate);
        return HashUtils.hashingBytes(jsonBytes);
    }
}
