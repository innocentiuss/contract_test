package generator;

import lombok.extern.slf4j.Slf4j;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;

import java.util.Arrays;


@Slf4j
public class ContractClassGenerator {
    public static void main(String[] args) {
        String[] params = Arrays.asList(
                "-a", "src/main/resources/Cert_CA.abi",
                "-b", "src/main/resources/Cert_CA.bin",
                "-p", "",
                "-o", "src/main/java/generated/asd"
        ).toArray(new String[0]);
        SolidityFunctionWrapperGenerator.main(params);
    }
}
