package tests;

import bean.Certificate;
import bean.CertificateFormatType;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import utils.CertSerializer;

@Slf4j
public class SerializerTest {
    public static void main(String[] args) throws Exception {
        Certificate certificate = Certificate.getRandomCertificate(CertificateFormatType.ORIGIN);
        byte[] proto = CertSerializer.serializeProtoCert(certificate);
        byte[] flats = CertSerializer.serializeFlatCert(certificate);
        byte[] json = JSON.toJSONBytes(certificate);

        System.out.println(proto.length);
        System.out.println(flats.length);
        System.out.println(json.length);

    }
}
