package tests;

import bean.Certificate;
import bean.CertificateFormatType;
import com.alibaba.fastjson2.JSON;
import utils.CertSerializer;
import utils.IOUtils;

public class DiskSpaceTest {
    static String directory = "D:/test";
    public static void main(String[] args) throws Exception {
        // 要生成的证书个数
        int total = 1000;

        long start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            Certificate cert = Certificate.getRandomCertificate(CertificateFormatType.ASN1);
            byte[] proto = CertSerializer.serializeProtoCert(cert);
            byte[] json = JSON.toJSONBytes(cert);
            byte[] asn1= CertSerializer.serializeASN(cert);

            IOUtils.writeBytesToFile(json, directory + "/origin/" + i + ".bin");
            IOUtils.writeBytesToFile(proto, directory + "/proto/" + i + ".bin");
            IOUtils.writeBytesToFile(asn1, directory + "/asn/" + i + ".der");

        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
