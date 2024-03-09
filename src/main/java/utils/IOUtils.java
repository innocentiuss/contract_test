package utils;

import java.io.File;
import java.io.FileOutputStream;

public class IOUtils {

    public static void writeBytesToFile(byte[] bytes, String fileName) throws Exception {
        File file = new File(fileName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(bytes);
        }
    }
}
