package utils;

import bean.Certificate;
import bean.FlatCertificate;
import bean.OpCertificate;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.protobuf.InvalidProtocolBufferException;

import java.nio.ByteBuffer;
import java.util.Date;

public class CertSerializer {
    public static byte[] serializeFlatCert(Certificate certificate) {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int version = builder.createString(certificate.getCertificateVersion());
        int signatureAlgo = builder.createString(certificate.getCertificateSignatureAlgorithm());
        int signatureValue = builder.createString(certificate.getThisCertificateSignatureValueFromSHA256_MURMUR32());
        int issuer = builder.createString(certificate.getCertificateIssuerName());
        int holder = builder.createString(certificate.getThisCertificateHolderName());
        int publicKey = builder.createString(certificate.getPublicKeyForThisCertificate());
        int url = builder.createString(certificate.getSmartContractUrl());
        int cert = FlatCertificate.createFlatCertificate(
                builder,
                version,
                certificate.getCertificateSerialNumber(),
                signatureAlgo,
                signatureValue,
                issuer,
                certificate.getThisCertificateWillBeInvalidAfterTimestamps().getTime(),
                holder,
                publicKey,
                certificate.getLastTimeOperationFromBlockChainAndGetThisChainHistoryHeight(),
                certificate.getCertificateOperationType(),
                url
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
                flatCertificate.signatureValue(),
                flatCertificate.issuer(),
                new Date(flatCertificate.validNotAfter()),
                flatCertificate.holder(),
                flatCertificate.publicKey(),
                flatCertificate.historyHeight(),
                (byte) flatCertificate.opType(),
                flatCertificate.contractUrl()
        );
    }

    public static byte[] serializeProtoCert(Certificate certificate) {
        OpCertificate.ProtoCertificate protoCertificate = OpCertificate.ProtoCertificate.newBuilder()
                .setVersion(certificate.getCertificateVersion())
                .setSerialNumber(certificate.getCertificateSerialNumber())
                .setSignatureAlgo(certificate.getCertificateSignatureAlgorithm())
                .setSignatureValue(certificate.getThisCertificateSignatureValueFromSHA256_MURMUR32())
                .setIssuer(certificate.getCertificateIssuerName())
                .setValidNotAfter(certificate.getThisCertificateWillBeInvalidAfterTimestamps().getTime())
                .setHolder(certificate.getThisCertificateHolderName())
                .setPublicKey(certificate.getPublicKeyForThisCertificate())
                .setHistoryHeight(certificate.getLastTimeOperationFromBlockChainAndGetThisChainHistoryHeight())
                .setBlockPreHeight(certificate.getCertificateOperationType())
                .setContractUrl(certificate.getSmartContractUrl())
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
                protoCertificate.getSignatureValue(),
                protoCertificate.getIssuer(),
                new Date(protoCertificate.getValidNotAfter()),
                protoCertificate.getHolder(),
                protoCertificate.getPublicKey(),
                protoCertificate.getHistoryHeight(),
                (byte) protoCertificate.getBlockPreHeight(),
                protoCertificate.getContractUrl()
        );
    }
}
