package bean;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import utils.CertSerializer;

@AllArgsConstructor
@Getter
public enum CertificateFormatType {
    ORIGIN("origin", "json") {
        @Override
        public byte[] serializeCert(Certificate certificate) {
            return JSON.toJSONBytes(certificate);
        }
    }, FLAT_BUFFERS("flat_buffers", "flat buffers") {
        @Override
        public byte[] serializeCert(Certificate certificate) {
            return CertSerializer.serializeFlatCert(certificate);
        }
    }, PROTOCOL_BUFFERS("protocol_buffers", "protobuf") {
        @Override
        public byte[] serializeCert(Certificate certificate) {
            return CertSerializer.serializeProtoCert(certificate);
        }
    },
    ASN1("asn.1", "der") {
        @Override
        public byte[] serializeCert(Certificate certificate) {
            try {
                return CertSerializer.serializeASN(certificate);
            } catch (Exception e) {
                return null;
            }
        }
    };
    private final String code;
    private final String desc;
    public abstract byte[] serializeCert(Certificate certificate);
}
