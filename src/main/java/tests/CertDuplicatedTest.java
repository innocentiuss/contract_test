package tests;

import bean.Certificate;
import utils.EncryptUtils;

import java.util.HashSet;
import java.util.Set;

public class CertDuplicatedTest {
    public static void main(String[] args) {
        Set<Certificate> set = new HashSet<>();
        // 随机生成一千万个证书，没有一个是重复的
        for (int i = 0; i < 10000000; i++) {
            set.add(Certificate.getRandomCertificate(EncryptUtils.generateECCKeyPair().getPublic().toString()));
        }
        System.out.println(set.size());
    }
}
