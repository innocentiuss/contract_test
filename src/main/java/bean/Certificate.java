package bean;

import lombok.Data;

@Data
public class Certificate {
    private String version;
    private int serialNumber;
    private String signatureAlgo;
    private String issuer;
    private Integer validNotAfter;
    private String holder;
    private String holderAlgo;
    private Integer blockHeight;
    private Integer blockPreHeight;
}
