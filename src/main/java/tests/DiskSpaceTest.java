package tests;

import bean.Certificate;
import bean.CertificateFormatType;
import bean.OriginCertificate;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import utils.CertSerializer;
import utils.IOUtils;

public class DiskSpaceTest {
    static String directory = "D:/test/test0324";
    public static void main(String[] args) throws Exception {
        // 要生成的证书个数
        int total = 1000;

        CBORFactory cborFactory = new CBORFactory();
        ObjectMapper cborMapper = new ObjectMapper(cborFactory);
        long start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            Certificate cert = Certificate.getRandomCertificate(CertificateFormatType.ASN1);
            OriginCertificate originCertificate = OriginCertificate.getRandomOriginCertificate(CertificateFormatType.ASN1);
            byte[] proto = CertSerializer.serializeProtoCert(cert);
            byte[] flat = CertSerializer.serializeFlatCert(cert);
            byte[] asn1= CertSerializer.serializeOriginASN(originCertificate);
            byte[] cbor = cborMapper.writeValueAsBytes(cert);

            IOUtils.writeBytesToFile(flat, directory + "/flat/" + i + ".bin");
            IOUtils.writeBytesToFile(proto, directory + "/proto/" + i + ".bin");
            IOUtils.writeBytesToFile(asn1, directory + "/asn/" + i + ".der");
            IOUtils.writeBytesToFile(cbor, directory + "/cbor/" + i + ".cbor");

        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
