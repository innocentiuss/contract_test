package utils;

import bean.*;
import com.alibaba.fastjson2.JSON;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bouncycastle.asn1.*;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Date;

public class CertSerializer {
    public static byte[] serializeFlatCert(Certificate certificate) {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int version = builder.createString(certificate.getVersion());
        int signatureValue = builder.createString(certificate.getSignature());
        int issuer = builder.createString(certificate.getIssuerName());
        int holder = builder.createString(certificate.getHolderName());
        int publicKey = builder.createString(certificate.getPublicKey());
        int url = builder.createString(certificate.getContractUrl());
        int cert = FlatCertificate.createFlatCertificate(
                builder,
                version,
                certificate.getSerialNumber(),
                signatureValue,
                issuer,
                certificate.getInvalidAfter().getTime(),
                holder,
                publicKey,
                certificate.getLastHeight(),
                certificate.getOpType(),
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
    public static byte[] serializeOriginASN(OriginCertificate certificate) {
        try {
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new DERUTF8String(certificate.getVersion()));
            v.add(new DERUTF8String(String.valueOf(certificate.getSerialNumber())));
            v.add(new DERUTF8String(certificate.getIssuerName()));
            v.add(new DERUTF8String(String.valueOf(certificate.getInvalidAfter().getTime())));
            v.add(new DERUTF8String(certificate.getHolderName()));
            v.add(new DERUTF8String(certificate.getPublicKey()));
            v.add(new DERUTF8String(String.valueOf(certificate.getLastHeight())));
            v.add(new DERUTF8String(String.valueOf(certificate.getOpType())));
            v.add(new DERUTF8String(certificate.getContractUrl()));
            v.add(new DERUTF8String(certificate.getSignature()));
            v.add(new DERUTF8String(certificate.getSubjectId()));
            v.add(new DERUTF8String(certificate.getIssuerId()));
            v.add(new DERUTF8String(certificate.getSignatureAlgo()));
            DERSequence derSequence = new DERSequence(v);
            return Base64.getEncoder().encode(derSequence.getEncoded());
        } catch (Exception e) {
            return new byte[0];
        }
    }
    public static OriginCertificate deserializeOriginASN(byte[] data) {
        byte[] decodedData = Base64.getDecoder().decode(data);
        try (ASN1InputStream asn1InputStream = new ASN1InputStream(decodedData)) {

            // 将字节数组转换为ASN.1对象
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            ASN1Encodable[] asn1Array = DERSequence.getInstance(asn1Primitive).toArray();

            OriginCertificate certificate = new OriginCertificate();
            certificate.setVersion(((DERUTF8String) asn1Array[0]).getString());
            certificate.setSerialNumber((int) Long.parseLong(((DERUTF8String) asn1Array[1]).getString()));
            certificate.setIssuerName(((DERUTF8String) asn1Array[2]).getString());
            certificate.setInvalidAfter(new Date(Long.parseLong(((DERUTF8String) asn1Array[3]).getString())));
            certificate.setHolderName(((DERUTF8String) asn1Array[4]).getString());
            certificate.setPublicKey(((DERUTF8String) asn1Array[5]).getString());
            certificate.setLastHeight(Integer.parseInt(((DERUTF8String) asn1Array[6]).getString()));
            certificate.setOpType((byte) Integer.parseInt(((DERUTF8String) asn1Array[7]).getString()));
            certificate.setContractUrl(((DERUTF8String) asn1Array[8]).getString());
            certificate.setSignature(((DERUTF8String) asn1Array[9]).getString());
            certificate.setIssuerId(((DERUTF8String) asn1Array[10]).getString());
            certificate.setSignatureAlgo(((DERUTF8String) asn1Array[11]).getString());
            certificate.setSubjectId(((DERUTF8String) asn1Array[12]).getString());
            return certificate;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] serializeASN(Certificate certificate) {
        try {
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new DERUTF8String(certificate.getVersion()));
            v.add(new DERUTF8String(String.valueOf(certificate.getSerialNumber())));
            v.add(new DERUTF8String(certificate.getIssuerName()));
            v.add(new DERUTF8String(String.valueOf(certificate.getInvalidAfter().getTime())));
            v.add(new DERUTF8String(certificate.getHolderName()));
            v.add(new DERUTF8String(certificate.getPublicKey()));
            v.add(new DERUTF8String(String.valueOf(certificate.getLastHeight())));
            v.add(new DERUTF8String(String.valueOf(certificate.getOpType())));
            v.add(new DERUTF8String(certificate.getContractUrl()));
            v.add(new DERUTF8String(certificate.getSignature()));
            DERSequence derSequence = new DERSequence(v);
            return Base64.getEncoder().encode(derSequence.getEncoded());
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static Certificate deserializeASN(byte[] data) {
        byte[] decodedData = Base64.getDecoder().decode(data);
        try (ASN1InputStream asn1InputStream = new ASN1InputStream(decodedData)) {

            // 将字节数组转换为ASN.1对象
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            ASN1Encodable[] asn1Array = DERSequence.getInstance(asn1Primitive).toArray();

            Certificate certificate = new Certificate();
            certificate.setVersion(((DERUTF8String) asn1Array[0]).getString());
            certificate.setSerialNumber((int) Long.parseLong(((DERUTF8String) asn1Array[1]).getString()));
            certificate.setIssuerName(((DERUTF8String) asn1Array[2]).getString());
            certificate.setInvalidAfter(new Date(Long.parseLong(((DERUTF8String) asn1Array[3]).getString())));
            certificate.setHolderName(((DERUTF8String) asn1Array[4]).getString());
            certificate.setPublicKey(((DERUTF8String) asn1Array[5]).getString());
            certificate.setLastHeight(Integer.parseInt(((DERUTF8String) asn1Array[6]).getString()));
            certificate.setOpType((byte) Integer.parseInt(((DERUTF8String) asn1Array[7]).getString()));
            certificate.setContractUrl(((DERUTF8String) asn1Array[8]).getString());
            certificate.setSignature(((DERUTF8String) asn1Array[9]).getString());
            return certificate;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Certificate certificate = Certificate.getRandomCertificate(CertificateFormatType.ASN1);
        System.out.println(JSON.toJSONString(certificate));
        byte[] bytes = serializeFlatCert(certificate);
        Certificate certificate1 = deserializeFlatCert(bytes);
        System.out.println(JSON.toJSONString(certificate1));
    }

    public static byte[] serializeProtoCert(Certificate certificate) {
        OpCertificate.ProtoCertificate protoCertificate = OpCertificate.ProtoCertificate.newBuilder()
                .setVersion(certificate.getVersion())
                .setSerialNumber(certificate.getSerialNumber())
                .setSignatureValue(certificate.getSignature())
                .setIssuer(certificate.getIssuerName())
                .setValidNotAfter(certificate.getInvalidAfter().getTime())
                .setHolder(certificate.getHolderName())
                .setPublicKey(certificate.getPublicKey())
                .setHistoryHeight(certificate.getLastHeight())
                .setBlockPreHeight(certificate.getOpType())
                .setContractUrl(certificate.getContractUrl())
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
