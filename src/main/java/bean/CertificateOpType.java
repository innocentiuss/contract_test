package bean;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum CertificateOpType {

    REGISTER((byte) 0, "注册"), REVOKE((byte) 1, "撤销");

    private final byte code;
    private final String desc;
}
