package utils;

import bean.Certificate;
import bean.FlatCertificate;
import bean.OpCertificate;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.protobuf.InvalidProtocolBufferException;

import java.nio.ByteBuffer;

public class CertSerializer {
    public static byte[] serializeFlatCert(Certificate certificate) {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int version = builder.createString(certificate.getVersion());
        int signatureAlgo = builder.createString(certificate.getSignatureAlgo());
        int issuer = builder.createString(certificate.getIssuer());
        int holder = builder.createString(certificate.getHolder());
        int holderAlgo = builder.createString(certificate.getHolderAlgo());
        int cert = FlatCertificate.createFlatCertificate(
                builder,
                version,
                certificate.getSerialNumber(),
                signatureAlgo,
                issuer,
                certificate.getValidNotAfter(),
                holder,
                holderAlgo,
                certificate.getBlockHeight(),
                certificate.getBlockPreHeight()
        );
        builder.finish(cert);
        return builder.sizedByteArray();
    }

    public static FlatCertificate halfDeserializeFlatCert(byte[] raw) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(raw);
        return FlatCertificate.getRootAsFlatCertificate(byteBuffer);
    }

    public static Certificate deserializeFlatCert(byte[] raw) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(raw);
        FlatCertificate flatCertificate = FlatCertificate.getRootAsFlatCertificate(byteBuffer);
        return new Certificate(
                flatCertificate.version(),
                flatCertificate.serialNumber(),
                flatCertificate.signatureAlgo(),
                flatCertificate.issuer(),
                flatCertificate.validNotAfter(),
                flatCertificate.holder(),
                flatCertificate.holderAlgo(),
                flatCertificate.blockHeight(),
                flatCertificate.blockPreHeight()
        );
    }

    public static byte[] serializeProtoCert(Certificate certificate) {
        OpCertificate.ProtoCertificate protoCertificate = OpCertificate.ProtoCertificate.newBuilder()
                .setVersion(certificate.getVersion())
                .setSerialNumber(certificate.getSerialNumber())
                .setSignatureAlgo(certificate.getSignatureAlgo())
                .setIssuer(certificate.getIssuer())
                .setValidNotAfter(certificate.getValidNotAfter())
                .setHolder(certificate.getHolder())
                .setHolderAlgo(certificate.getHolderAlgo())
                .setBlockHeight(certificate.getBlockHeight())
                .setBlockPreHeight(certificate.getBlockPreHeight())
                .build();
        return protoCertificate.toByteArray();
    }

    public static OpCertificate.ProtoCertificate halfDeserializeProtoCert(byte[] raw) throws InvalidProtocolBufferException {
        return OpCertificate.ProtoCertificate.parseFrom(raw);
    }

    public static Certificate deserializeProtoCert(byte[] raw) throws InvalidProtocolBufferException {
        OpCertificate.ProtoCertificate protoCertificate = OpCertificate.ProtoCertificate.parseFrom(raw);
        return new Certificate(
                protoCertificate.getVersion(),
                protoCertificate.getSerialNumber(),
                protoCertificate.getSignatureAlgo(),
                protoCertificate.getIssuer(),
                protoCertificate.getValidNotAfter(),
                protoCertificate.getHolder(),
                protoCertificate.getHolderAlgo(),
                protoCertificate.getBlockHeight(),
                protoCertificate.getBlockPreHeight()
        );
    }
}
