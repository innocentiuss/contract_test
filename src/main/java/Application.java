import com.google.common.hash.BloomFilter;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import tests.CertEncryptTypeTest;
import tests.CertFormatTypeTest;

public class Application {
    public static void main(String[] args) throws Exception {
        CertFormatTypeTest.main(args);
        System.out.println("---------------------第一轮测试完成--------------------------");
        CertEncryptTypeTest.main(args);
    }
}
