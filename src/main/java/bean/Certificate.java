package bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import utils.RandomUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Certificate {
    private String version;
    private int serialNumber;
    private String signatureAlgo;
    private String issuer;
    private int validNotAfter;
    private String holder;
    private String holderAlgo;
    private int blockHeight;
    private int blockPreHeight;
    private String publicKey;

    public static Certificate getRandomCertificate(String publicKey) {
        return new Certificate(
                "Version" + RandomUtils.generateRandomString(5),
                RandomUtils.randomInt(),
                "Algorithm" + RandomUtils.generateRandomString(5),
                "Issuer" + RandomUtils.generateRandomString(10),
                RandomUtils.randomInt(),
                "Holder" + RandomUtils.generateRandomString(5),
                "HolderAlgo" + RandomUtils.generateRandomString(5),
                RandomUtils.randomInt(10000),
                RandomUtils.randomInt(10000),
                publicKey
        );
    }
}
